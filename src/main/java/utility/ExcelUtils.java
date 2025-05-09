package utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
	
	/**
	 * Utility class to handle Excel operations
	 */
	
	    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);
	    private Workbook workbook;
	    private Sheet sheet;
	    
	    /**
	     * Constructor to initialize ExcelUtils with file path and sheet name
	     * @param filePath Path to Excel file
	     * @param sheetName Name of the sheet
	     */
	    public ExcelUtils(String filePath, String sheetName) {
	        try {
	            FileInputStream fis = new FileInputStream(filePath);
	            workbook = new XSSFWorkbook(fis);
	            sheet = workbook.getSheet(sheetName);
	            
	            if (sheet == null) {
	                logger.error("Sheet '{}' not found in {}", sheetName, filePath);
	                throw new RuntimeException("Sheet not found: " + sheetName);
	            }
	            
	            logger.info("Excel file loaded: {}, Sheet: {}", filePath, sheetName);
	        } catch (IOException e) {
	            logger.error("Failed to load Excel file: {}", e.getMessage());
	            throw new RuntimeException("Failed to load Excel file: " + filePath, e);
	        }
	    }
	    
	    /**
	     * Gets cell data as string
	     * @param rowNum Row number (0-based)
	     * @param colNum Column number (0-based)
	     * @return Cell value as string
	     */
	    public String getCellData(int rowNum, int colNum) {
	        try {
	            Cell cell = sheet.getRow(rowNum).getCell(colNum);
	            return getCellValueAsString(cell);
	        } catch (Exception e) {
	            logger.error("Failed to get cell data at [{}, {}]: {}", rowNum, colNum, e.getMessage());
	            return "";
	        }
	    }
	    
	    /**
	     * Gets all data from the sheet as list of maps
	     * Each map represents a row with column headers as keys
	     * @return List of maps containing row data
	     */
	    public List<Map<String, String>> getDataAsListOfMaps() {
	        List<Map<String, String>> data = new ArrayList<>();
	        int rowCount = sheet.getLastRowNum();
	        
	        // Get header row for column names
	        Row headerRow = sheet.getRow(0);
	        int colCount = headerRow.getLastCellNum();
	        
	        // Process each row
	        for (int i = 1; i <= rowCount; i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;
	            
	            Map<String, String> rowMap = new HashMap<>();
	            for (int j = 0; j < colCount; j++) {
	                String columnName = getCellValueAsString(headerRow.getCell(j));
	                String cellValue = (row.getCell(j) != null) ? getCellValueAsString(row.getCell(j)) : "";
	                rowMap.put(columnName, cellValue);
	            }
	            data.add(rowMap);
	        }
	        
	        logger.info("Retrieved {} rows of data from Excel", data.size());
	        return data;
	    }
	    
	    /**
	     * Gets data from specific column
	     * @param columnName Name of the column (from header row)
	     * @return List of values in the column
	     */
	    public List<String> getColumnData(String columnName) {
	        List<String> columnData = new ArrayList<>();
	        int rowCount = sheet.getLastRowNum();
	        
	        // Find column index
	        Row headerRow = sheet.getRow(0);
	        int columnIndex = -1;
	        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
	            if (getCellValueAsString(headerRow.getCell(i)).equals(columnName)) {
	                columnIndex = i;
	                break;
	            }
	        }
	        
	        if (columnIndex == -1) {
	            logger.error("Column '{}' not found", columnName);
	            throw new RuntimeException("Column not found: " + columnName);
	        }
	        
	        // Get data from column
	        for (int i = 1; i <= rowCount; i++) {
	            Row row = sheet.getRow(i);
	            if (row != null && row.getCell(columnIndex) != null) {
	                columnData.add(getCellValueAsString(row.getCell(columnIndex)));
	            }
	        }
	        
	        logger.info("Retrieved {} values from column '{}'", columnData.size(), columnName);
	        return columnData;
	    }
	    
	    /**
	     * Helper method to convert cell value to string based on cell type
	     * @param cell Cell to convert
	     * @return Cell value as string
	     */
	    private String getCellValueAsString(Cell cell) {
	        if (cell == null) {
	            return "";
	        }
	        
	        switch (cell.getCellType()) {
	            case STRING:
	                return cell.getStringCellValue();
	            case NUMERIC:
	                if (DateUtil.isCellDateFormatted(cell)) {
	                    return cell.getLocalDateTimeCellValue().toString();
	                } else {
	                    // Convert to string and remove trailing zeros
	                    return String.valueOf(cell.getNumericCellValue()).replaceAll("\\.0+$", "");
	                }
	            case BOOLEAN:
	                return String.valueOf(cell.getBooleanCellValue());
	            case FORMULA:
	                return cell.getCellFormula();
	            case BLANK:
	                return "";
	            default:
	                return "";
	        }
	    }
	    
	    /**
	     * Closes the workbook
	     */
	    public void closeWorkbook() {
	        try {
	            if (workbook != null) {
	                workbook.close();
	                logger.info("Workbook closed successfully");
	            }
	        } catch (IOException e) {
	            logger.error("Failed to close workbook: {}", e.getMessage());
	        }
	    }
	

}
