package com.zepto.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    
    private static Workbook workbook;
    private static Sheet sheet;
    
    public static void setExcelFile(String filePath, String sheetName) throws IOException {
        File file = new File(filePath);
        
        if (!file.exists()) {
            throw new IOException("Excel file not found at path: " + filePath + 
                "\nPlease ensure the file exists in the testdata folder.");
        }
        
        FileInputStream fis = new FileInputStream(file);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in Excel file");
        }
    }
    
    public static String getCellData(int rowNum, int colNum) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                return "";
            }
            
            Cell cell = row.getCell(colNum);
            if (cell == null) {
                return "";
            }
            
            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell);
        } catch (Exception e) {
            System.err.println("Error reading cell data: " + e.getMessage());
            return "";
        }
    }
    
    public static int getRowCount() {
        return sheet.getLastRowNum() + 1;
    }
    
    public static int getColumnCount() {
        Row row = sheet.getRow(0);
        return row != null ? row.getLastCellNum() : 0;
    }
    
    public static Object[][] getTestData(String filePath, String sheetName) {
        try {
            // Normalize file path for Windows
            filePath = filePath.replace("/", File.separator);
            
            // Validate file exists
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("ERROR: Excel file not found at: " + file.getAbsolutePath());
                System.err.println("Current working directory: " + System.getProperty("user.dir"));
                System.err.println("Please create the testdata folder and add testdata.xlsx file");
                System.err.println("Expected location: " + System.getProperty("user.dir") + File.separator + "testdata" + File.separator + "testdata.xlsx");
                return new Object[0][0];
            }
            
            setExcelFile(filePath, sheetName);
            int totalRows = getRowCount();
            int colCount = getColumnCount();
            
            // First pass: count non-empty rows
            java.util.List<Object[]> dataList = new java.util.ArrayList<>();
            
            for (int i = 1; i < totalRows; i++) {
                Object[] rowData = new Object[colCount];
                boolean isRowEmpty = true;
                
                for (int j = 0; j < colCount; j++) {
                    String cellData = getCellData(i, j);
                    rowData[j] = cellData;
                    
                    // Check if cell has data
                    if (cellData != null && !cellData.trim().isEmpty()) {
                        isRowEmpty = false;
                    }
                }
                
                // Only add non-empty rows
                if (!isRowEmpty) {
                    dataList.add(rowData);
                }
            }
            
            // Convert list to array
            Object[][] data = new Object[dataList.size()][colCount];
            for (int i = 0; i < dataList.size(); i++) {
                data[i] = dataList.get(i);
            }
            
            closeExcel();
            System.out.println("✓ Successfully loaded " + data.length + " data rows from sheet: " + sheetName);
            return data;
        } catch (IOException e) {
            System.err.println("Error loading test data: " + e.getMessage());
            return new Object[0][0];
        }
    }
    
    public static void closeExcel() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing Excel file: " + e.getMessage());
        }
    }
}