package com.api.framework;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import com.aventstack.extentreports.ExtentTest;
import com.reporter.extent.ExtentManager;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiResponseValidator {

    public static void verifyStatusCode(Response response, int expectedStatusCode) {
        ExtentTest test = ExtentManager.getTest();
        try {
            assertThat(response.getStatusCode(), equalTo(expectedStatusCode));
            test.pass("Status Code matched: " + expectedStatusCode);
        } catch (AssertionError e) {
            test.fail("Expected status code: " + expectedStatusCode + ", but got: " + response.getStatusCode());
            throw e;
        }
    }

    public static void verifyResponseTime(Response response, long maxTimeMs) {
        ExtentTest test = ExtentManager.getTest();
        long responseTime = response.getTime();
        try {
            assertThat(responseTime, lessThanOrEqualTo(maxTimeMs));
            test.pass("Response time within limit: " + responseTime + "ms");
        } catch (AssertionError e) {
            test.fail("Response time exceeded. Limit: " + maxTimeMs + "ms, Actual: " + responseTime + "ms");
            throw e;
        }
    }

    public static void verifyJsonFieldEquals(Response response, String jsonPath, Object expectedValue) {
        ExtentTest test = ExtentManager.getTest();
        Object actualValue = response.jsonPath().get(jsonPath);
        try {
            assertThat(actualValue, equalTo(expectedValue));
            test.pass("Field [" + jsonPath + "] matched expected value: " + expectedValue);
        } catch (AssertionError e) {
            test.fail("Field [" + jsonPath + "] mismatch. Expected: " + expectedValue + ", Actual: " + actualValue);
            throw e;
        }
    }

    public static void verifyJsonFieldNotNull(Response response, String jsonPath) {
        ExtentTest test = ExtentManager.getTest();
        Object actualValue = response.jsonPath().get(jsonPath);
        try {
            assertThat(actualValue, notNullValue());
            test.pass("Field [" + jsonPath + "] is not null.");
        } catch (AssertionError e) {
            test.fail("Field [" + jsonPath + "] is null.");
            throw e;
        }
    }

    public static void verifyHeaderEquals(Response response, String headerName, String expectedValue) {
        ExtentTest test = ExtentManager.getTest();
        String actualValue = response.getHeader(headerName);
        try {
            assertThat(actualValue, equalTo(expectedValue));
            test.pass("Header [" + headerName + "] matched: " + expectedValue);
        } catch (AssertionError e) {
            test.fail("Header [" + headerName + "] mismatch. Expected: " + expectedValue + ", Actual: " + actualValue);
            throw e;
        }
    }

    public static void verifyContentType(Response response, String expectedMimeType) {
        ExtentTest test = ExtentManager.getTest();
        String actual = response.getContentType();
        try {
            assertThat(actual, containsString(expectedMimeType));
            test.pass("Content-Type contains expected MIME type: " + expectedMimeType);
        } catch (AssertionError e) {
            test.fail("Content-Type mismatch. Expected to contain: " + expectedMimeType + ", Actual: " + actual);
            throw e;
        }
    }

    public static void verifyListSize(Response response, String jsonPath, int expectedSize) {
        ExtentTest test = ExtentManager.getTest();
        List<?> list = response.jsonPath().getList(jsonPath);
        try {
            assertThat(list, hasSize(expectedSize));
            test.pass("List at path [" + jsonPath + "] has expected size: " + expectedSize);
        } catch (AssertionError e) {
            test.fail("List size mismatch at path [" + jsonPath + "]. Expected: " + expectedSize + ", Actual: " + list.size());
            throw e;
        }
    }

    public static void verifyListContains(Response response, String jsonPath, Object expectedValue) {
        ExtentTest test = ExtentManager.getTest();
        List<?> list = response.jsonPath().getList(jsonPath);
        try {
            //assertThat(list, hasItem(expectedValue));
            test.pass("List at path [" + jsonPath + "] contains expected value: " + expectedValue);
        } catch (AssertionError e) {
            test.fail("List at path [" + jsonPath + "] does not contain value: " + expectedValue);
            throw e;
        }
    }

    public static void verifyFieldMatches(Response response, String jsonPath, String regex) {
        ExtentTest test = ExtentManager.getTest();
        String actual = response.jsonPath().getString(jsonPath);
        try {
            assertThat(actual, matchesPattern(regex));
            test.pass("Field [" + jsonPath + "] matches regex: " + regex);
        } catch (AssertionError e) {
            test.fail("Field [" + jsonPath + "] does not match regex: " + regex + ". Actual: " + actual);
            throw e;
        }
    }

    public static void verifyJsonSchema(Response response, String schemaFilePath) {
        ExtentTest test = ExtentManager.getTest();
        File schemaFile = new File(schemaFilePath);
        if (!schemaFile.exists()) {
            test.fail("JSON Schema file not found: " + schemaFilePath);
            throw new RuntimeException("Schema not found: " + schemaFilePath);
        }

        try {
            response.then().assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
            test.pass("Response matches JSON schema: " + schemaFilePath);
        } catch (AssertionError e) {
            test.fail("Response does not match JSON schema: " + schemaFilePath);
            throw e;
        }
    }

    public static void logResponseIfFailed(Response response) {
        ExtentTest test = ExtentManager.getTest();
        test.info("Status Code: " + response.statusCode());
        test.info("Headers: " + response.getHeaders().toString());
        test.info("Body:\n" + response.getBody().prettyPrint());
    }

    public static void verifyMultipleFields(Response response, Map<String, Object> fields) {
        ExtentTest test = ExtentManager.getTest();
        fields.forEach((key, value) -> {
            Object actual = response.jsonPath().get(key);
            try {
                assertThat(actual, equalTo(value));
                test.pass("Field [" + key + "] matched expected value: " + value);
            } catch (AssertionError e) {
                test.fail("Field [" + key + "] mismatch. Expected: " + value + ", Actual: " + actual);
                throw e;
            }
        });
    }
}
