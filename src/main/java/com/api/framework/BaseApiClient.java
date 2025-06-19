package com.api.framework;



import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApiClient {

    protected RequestSpecification requestSpec;

    public BaseApiClient() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(System.getProperty("api.baseUri", "https://api.example.com"))
                .setContentType(ContentType.JSON)
                .build();
    }

    // Extend or override for authentication, headers etc.
    protected RequestSpecification given() {
        return RestAssured.given().spec(requestSpec);
    }
}

