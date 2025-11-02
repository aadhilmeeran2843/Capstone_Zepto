package com.zepto.tests;

import java.io.File;
import org.testng.annotations.Test;
import com.zepto.utilities.ExcelUtils;

public class ExcelDebugTest {
    
    @Test
    public void debugExcelLocation() {
        System.out.println("\n========================================");
        System.out.println("🔍 EXCEL FILE DEBUG TEST");
        System.out.println("========================================\n");
        
        // Get project directory
        String projectDir = System.getProperty("user.dir");
        System.out.println("📁 Project Directory: " + projectDir);
        
        // Expected Excel path
        String excelPath = projectDir + "/testdata/testdata.xlsx";
        System.out.println("📄 Expected Excel Path: " + excelPath);
        
        // Alternative path with backslashes
        String excelPathAlt = projectDir + "\\testdata\\testdata.xlsx";
        System.out.println("📄 Alternative Path: " + excelPathAlt);
        
        System.out.println("\n--- Checking File Existence ---");
        
        // Check if file exists
        File excelFile = new File(excelPath);
        System.out.println("✓ Using path: " + excelPath);
        System.out.println("✓ File exists? " + excelFile.exists());
        System.out.println("✓ Is file? " + excelFile.isFile());
        System.out.println("✓ Can read? " + excelFile.canRead());
        System.out.println("✓ Absolute path: " + excelFile.getAbsolutePath());
        
        if (!excelFile.exists()) {
            System.out.println("\n❌ FILE NOT FOUND!");
            System.out.println("\n--- Checking testdata folder ---");
            
            File testdataFolder = new File(projectDir + "/testdata");
            System.out.println("Testdata folder exists? " + testdataFolder.exists());
            System.out.println("Is directory? " + testdataFolder.isDirectory());
            
            if (testdataFolder.exists() && testdataFolder.isDirectory()) {
                System.out.println("\n--- Files in testdata folder ---");
                File[] files = testdataFolder.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        System.out.println("  - " + file.getName());
                    }
                } else {
                    System.out.println("  (Folder is empty)");
                }
            } else {
                System.out.println("\n❌ testdata folder doesn't exist!");
                System.out.println("Please create: " + projectDir + "\\testdata\\");
            }
            
            System.out.println("\n--- Solution ---");
            System.out.println("1. Create folder: " + projectDir + "\\testdata");
            System.out.println("2. Place testdata.xlsx inside this folder");
            System.out.println("3. Refresh Eclipse project (F5)");
            
        } else {
            System.out.println("\n✅ FILE FOUND!");
            System.out.println("\n--- Trying to read Excel ---");
            
            try {
                ExcelUtils.setExcelFile(excelPath, "LoginData");
                System.out.println("✅ Excel file opened successfully!");
                
                int rowCount = ExcelUtils.getRowCount();
                int colCount = ExcelUtils.getColumnCount();
                System.out.println("Rows: " + rowCount);
                System.out.println("Columns: " + colCount);
                
                if (rowCount > 1) {
                    String cellData = ExcelUtils.getCellData(1, 0);
                    System.out.println("Data in Row 1, Col 0: " + cellData);
                    System.out.println("\n✅ Excel reading works perfectly!");
                } else {
                    System.out.println("\n⚠️ Excel has no data rows (only headers)");
                }
                
                //ExcelUtils.closeWorkbook();
                
            } catch (Exception e) {
                System.out.println("\n❌ ERROR reading Excel:");
                System.out.println("Error type: " + e.getClass().getName());
                System.out.println("Error message: " + e.getMessage());
                System.out.println("\nFull stack trace:");
                e.printStackTrace();
                
                System.out.println("\n--- Possible Issues ---");
                System.out.println("1. File is not in .xlsx format (must be Excel 2007+)");
                System.out.println("2. File is corrupted");
                System.out.println("3. Sheet name 'LoginData' doesn't exist");
                System.out.println("4. File is open in Excel (close it first)");
            }
        }
        
        System.out.println("\n========================================");
        System.out.println("🔍 DEBUG TEST COMPLETED");
        System.out.println("========================================\n");
    }
}