package com.framework.actions;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DataUtils {

    // ✅ Read from CSV file
    public static List<Map<String, String>> readCSV(String filePath) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        String[] headers = lines.get(0).split(",");

        for (int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split(",");
            Map<String, String> row = new LinkedHashMap<>();
            for (int j = 0; j < headers.length; j++) {
                row.put(headers[j].trim(), values[j].trim());
            }
            data.add(row);
        }
        return data;
    }

    // ✅ Read from JSON file
    public static List<Map<String, String>> readJSON(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), new TypeReference<List<Map<String, String>>>() {});
    }

    // ✅ Read from Excel file (.xlsx)
    public static List<Map<String, String>> readExcel(String filePath, String sheetName) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            int lastColumn = headerRow.getLastCellNum();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < lastColumn; j++) {
                    String key = headerRow.getCell(j).getStringCellValue();
                    Cell cell = row.getCell(j);
                    String value = getCellValueAsString(cell);
                    rowData.put(key, value);
                }
                data.add(rowData);
            }
        }

        return data;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            case BLANK: return "";
            default: return "";
        }
    }
}
