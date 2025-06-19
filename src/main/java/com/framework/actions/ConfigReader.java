package com.framework.actions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
	/*
	 * private static Properties prop;
	 * 
	 * static { String env = System.getProperty("env"); if (env == null) { env =
	 * "dev"; // default to dev }
	 * 
	 * String path = "src/test/resources/config-" + env + ".properties";
	 * 
	 * try (FileInputStream fis = new FileInputStream(path)) { prop = new
	 * Properties(); prop.load(fis); } catch (IOException e) { throw new
	 * RuntimeException("Failed to load config file: " + path, e); } }
	 * 
	 * public static String get(String key) { return prop.getProperty(key); }
	 * 
	 * public static int getInt(String key) { return
	 * Integer.parseInt(prop.getProperty(key)); }
	 * 
	 * public static long getLong(String key) { return
	 * Long.parseLong(prop.getProperty(key)); }
	 * 
	 * public static boolean getBoolean(String key) { return
	 * Boolean.parseBoolean(prop.getProperty(key)); }
	 */
	
	private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties file not found in resources");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
    
    public static boolean getBoolean(String key) {
    	return Boolean.parseBoolean(properties.getProperty(key));
    			
    }

	public static long getInt(String string) {
		// TODO Auto-generated method stub
		return Integer.parseInt(properties.getProperty(string));
	}
    		  
	
	
}
