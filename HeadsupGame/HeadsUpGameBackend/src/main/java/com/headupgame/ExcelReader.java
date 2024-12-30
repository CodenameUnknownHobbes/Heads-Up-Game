package com.headupgame;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ExcelReader {

    public Map<String, List<String>> readExcel(String filePath) throws IOException {
        Map<String, List<String>> categoryWords = new HashMap<>();

        // Open the Excel file
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);

        // Iterate through each sheet in the workbook
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            List<String> words = new ArrayList<>();

            // Iterate through each row in the sheet
            for (Row row : sheet) {
                StringBuilder rowData = new StringBuilder();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.append(cell.getStringCellValue()).append(", ");
                            break;
                        case NUMERIC:
                            rowData.append(cell.getNumericCellValue()).append(", ");
                            break;
                        default:
                            break;
                    }
                }
                if (rowData.length() > 0) {
                    // Remove trailing comma and space
                    words.add(rowData.substring(0, rowData.length() - 2));
                }
            }
            categoryWords.put(sheetName, words); // Add words to the map with sheet name as key
        }

        workbook.close();
        fis.close();
        return categoryWords;
    }
}
