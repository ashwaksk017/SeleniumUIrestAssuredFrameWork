package com.data.loader;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import org.testng.annotations.DataProvider;

import com.framework.actions.ConfigReader;

public class CsvDataLoader {
    public static List<Map<String, String>> loadCsvAsMap(String filePath) {
        List<Map<String, String>> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String[] headers = reader.readLine().split(",");
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i].trim(), values[i].trim());
                }
                data.add(row);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load CSV file: " + filePath, e);
        }
        return data;
    }
    
    
	/*
	 * @DataProvider(name = "loginData") public static Object[][] readCSVData()
	 * throws Exception { String path = "testdata/loginData.csv"; List<Map<String,
	 * String>> dataList = new ArrayList<>();
	 * 
	 * try (BufferedReader br = new BufferedReader(new FileReader(path))) { String[]
	 * headers = br.readLine().split(","); String line;
	 * 
	 * while ((line = br.readLine()) != null) { String[] values = line.split(",");
	 * Map<String, String> map = new HashMap<>(); for (int i = 0; i <
	 * headers.length; i++) { map.put(headers[i].trim(), values[i].trim()); }
	 * dataList.add(map); } }
	 * 
	 * Object[][] result = new Object[dataList.size()][1]; for (int i = 0; i <
	 * dataList.size(); i++) { result[i][0] = dataList.get(i); // each row as a
	 * single Map }
	 * 
	 * return result; }
	 */
    
    
    
    @DataProvider(name = "loginData")
    public static Object[][] readCSVData() throws Exception {
        String path = ConfigReader.get("csv.loginDataPath");
        if (path == null || path.isEmpty()) {
            throw new RuntimeException("CSV file path not configured!");
        }

        List<Map<String, String>> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String[] headers = br.readLine().split(",");
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    map.put(headers[i].trim(), values[i].trim());
                }
                dataList.add(map);
            }
        }

        Object[][] result = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            result[i][0] = dataList.get(i); // each row as a single Map
        }

        return result;
    }
}
