package com.zepto.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateExcelHelper {
    
    public static void main(String[] args) {
        String projectDir = System.getProperty("user.dir");
        String testdataDir = projectDir + File.separator + "testdata";
        String excelFilePath = testdataDir + File.separator + "testdata.xlsx";
        
        System.out.println("==========================================");
        System.out.println("Excel File Creator for Zepto Automation");
        System.out.println("==========================================");
        System.out.println("Project Directory: " + projectDir);
        System.out.println("Creating testdata folder at: " + testdataDir);
        
        // Create testdata directory if it doesn't exist
        File directory = new File(testdataDir);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("✓ testdata folder created successfully!");
            } else {
                System.err.println("✗ Failed to create testdata folder");
                return;
            }
        } else {
            System.out.println("✓ testdata folder already exists");
        }
        
        // Create Excel file
        try {
            createExcelFile(excelFilePath);
        } catch (IOException e) {
            System.err.println("✗ Error creating Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createExcelFile(String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        // Create bold font for headers
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        
        // Sheet 1: LoginData
        System.out.println("\nCreating Sheet 1: LoginData");
        Sheet loginSheet = workbook.createSheet("LoginData");
        
        // Create header row
        Row loginHeaderRow = loginSheet.createRow(0);
        Cell loginHeaderCell = loginHeaderRow.createCell(0);
        loginHeaderCell.setCellValue("MobileNumber");
        loginHeaderCell.setCellStyle(headerStyle);
        
        // Add sample data rows (only 1 mobile number as per user's requirement)
        String[] sampleMobileNumbers = {
            "9876543210"
        };
        
        for (int i = 0; i < sampleMobileNumbers.length; i++) {
            Row row = loginSheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(sampleMobileNumbers[i]);
        }
        
        // Auto-size column
        loginSheet.autoSizeColumn(0);
        
        System.out.println("✓ LoginData sheet created with " + sampleMobileNumbers.length + " sample mobile number");
        System.out.println("  Note: Replace this with your actual mobile number!");
        
        // Sheet 2: ProductData
        System.out.println("\nCreating Sheet 2: ProductData");
        Sheet productSheet = workbook.createSheet("ProductData");
        
        // Create header row
        Row productHeaderRow = productSheet.createRow(0);
        Cell productHeaderCell = productHeaderRow.createCell(0);
        productHeaderCell.setCellValue("ProductName");
        productHeaderCell.setCellStyle(headerStyle);
        
        // Add sample product data (only 4 products as per user's requirement)
        String[] sampleProducts = {
            "Milk",
            "Bread",
            "Eggs",
            "Rice"
        };
        
        for (int i = 0; i < sampleProducts.length; i++) {
            Row row = productSheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(sampleProducts[i]);
        }
        
        // Auto-size column
        productSheet.autoSizeColumn(0);
        
        System.out.println("✓ ProductData sheet created with " + sampleProducts.length + " sample products");
        
        // Write to file
        File file = new File(filePath);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
            workbook.close();
            
            System.out.println("\n==========================================");
            System.out.println("✓✓✓ SUCCESS! ✓✓✓");
            System.out.println("==========================================");
            System.out.println("Excel file created successfully at:");
            System.out.println(file.getAbsolutePath());
            System.out.println("\nFile Details:");
            System.out.println("- File Size: " + file.length() + " bytes");
            System.out.println("- Sheets: 2 (LoginData, ProductData)");
            System.out.println("- LoginData rows: " + (sampleMobileNumbers.length + 1) + " (including header)");
            System.out.println("- ProductData rows: " + (sampleProducts.length + 1) + " (including header)");
            System.out.println("\n⚠ IMPORTANT: Please update the mobile numbers in LoginData sheet");
            System.out.println("   with your actual mobile numbers before running tests!");
            System.out.println("==========================================");
            
        } catch (IOException e) {
            System.err.println("✗ Error writing Excel file: " + e.getMessage());
            throw e;
        }
    }
}