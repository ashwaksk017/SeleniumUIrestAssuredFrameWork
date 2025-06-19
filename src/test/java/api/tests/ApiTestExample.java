package api.tests;


import java.lang.reflect.Method;
import java.util.Map;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.api.framework.ApiRequestBuilder;
import com.api.framework.ApiResponseValidator;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.reporter.extent.ExtentManager;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiTestExample {

    private ExtentReports extent;

    @BeforeSuite
    public void beforeSuite() {
        extent = ExtentManager.getExtentReports();
    }

    @BeforeMethod
    public void setup(Method method) {
        // Start a new Extent test and set it to thread-local storage
        ExtentTest test = extent.createTest("API Test: " + method.getName());
        ExtentManager.setTest(test);
    }

    @Test
    public void testGetUserById() {
        // Base spec
        RequestSpecification baseSpec = RestAssured
                .given()
                .baseUri("https://reqres.in/api");

        // Build request with query/path parameters
        ApiRequestBuilder builder = new ApiRequestBuilder(baseSpec, ExtentManager.getTest());
        RequestSpecification request = builder
                .addPathParams(Map.of("userId", 2))
                .build();

        // Make the GET request
        Response response = request.get("/users/{userId}");

        // Validate response
        ApiResponseValidator.verifyStatusCode(response, 200);
        ApiResponseValidator.verifyJsonFieldEquals(response, "data.id", 2);
        ApiResponseValidator.verifyJsonFieldNotNull(response, "data.first_name");
        ApiResponseValidator.logResponseIfFailed(response);
    }

    @AfterSuite
    public void afterSuite() {
        ExtentManager.flushReports();
    }
}
