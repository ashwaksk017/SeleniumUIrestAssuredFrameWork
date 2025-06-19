package com.data.loader;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonDataLoader {
    public static JsonNode loadJson(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(new File(filePath));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON: " + filePath, e);
        }
    }
}

