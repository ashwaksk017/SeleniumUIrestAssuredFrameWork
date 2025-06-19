package com.api.framework;

import com.aventstack.extentreports.ExtentTest;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ApiRequestBuilder {

	private RequestSpecification requestSpec;
	private ExtentTest test;

	public ApiRequestBuilder(RequestSpecification baseSpec) {
		this.requestSpec = baseSpec;
	}

	public ApiRequestBuilder(RequestSpecification baseSpec, ExtentTest test) {
		this.requestSpec = baseSpec;
		this.test = test;
	}

	public ApiRequestBuilder addHeaders(Map<String, String> headers) {
		if (headers != null && !headers.isEmpty()) {
			requestSpec.headers(headers);
			test.info("Headers added: " + headers.toString());
		}
		return this;
	}

	public ApiRequestBuilder addQueryParams(Map<String, ?> queryParams) {
		if (queryParams != null && !queryParams.isEmpty()) {
			requestSpec.queryParams(queryParams);
			test.info("Query params added: " + queryParams.toString());
		}
		return this;
	}

	public ApiRequestBuilder addPathParams(Map<String, ?> pathParams) {
		if (pathParams != null && !pathParams.isEmpty()) {
			requestSpec.pathParams(pathParams);
			test.info("Path params added: " + pathParams.toString());
		}
		return this;
	}

	public ApiRequestBuilder addFormParams(Map<String, ?> formParams) {
		if (formParams != null && !formParams.isEmpty()) {
			requestSpec.formParams(formParams);
			requestSpec.contentType(ContentType.URLENC);
			test.info("Form params added: " + formParams.toString());
		}
		return this;
	}

	public ApiRequestBuilder setBody(Object body) {
		if (body != null) {
			requestSpec.body(body);
			test.info("Body set: " + body.toString());
		}
		return this;
	}

	public ApiRequestBuilder setBodyFromJsonFile(String jsonFilePath, Class<?> type) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Object bodyObj = mapper.readValue(new File(jsonFilePath), type);
			requestSpec.body(bodyObj);
			test.info("Body loaded from file: " + jsonFilePath);
		} catch (IOException e) {
			test.fail("Failed to load JSON file: " + jsonFilePath);
			throw new RuntimeException("Failed to load JSON file: " + jsonFilePath, e);
		}
		return this;
	}

	public ApiRequestBuilder attachFile(String paramName, File file) {
		if (file != null && file.exists()) {
			requestSpec.multiPart(paramName, file);
			test.info("Attached file for param [" + paramName + "]: " + file.getName());
		} else {
			test.fail("File not found for multipart upload: " + file);
			throw new RuntimeException("File not found for multipart upload: " + file);
		}
		return this;
	}

	public ApiRequestBuilder setContentType(ContentType contentType) {
		requestSpec.contentType(contentType);
		test.info("Content-Type set: " + contentType.toString());
		return this;
	}

	public RequestSpecification build() {
		test.info("RequestSpecification build completed.");
		return requestSpec;
	}
}
