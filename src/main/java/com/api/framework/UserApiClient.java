package com.api.framework;


import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import com.api.framework.pojos.CreateUserRequest;

public class UserApiClient extends BaseApiClient {

    public Response loginUser(Map<String, Object> credentials) {
        RequestSpecification spec = new ApiRequestBuilder(given())
                .setBody(credentials)
                .build();

        return spec.post(ApiEndpoints.USER_LOGIN);
    }

    public Response getUserProfile(String userId) {
        RequestSpecification spec = new ApiRequestBuilder(given())
                .addPathParams(Map.of("userId", userId))
                .build();

        return spec.get(ApiEndpoints.GET_USER_PROFILE);
    }

    public Response createUserFromJson(String jsonFilePath) {
        RequestSpecification spec = new ApiRequestBuilder(given())
                .setBodyFromJsonFile(jsonFilePath, CreateUserRequest.class)
                .build();

        return spec.post(ApiEndpoints.CREATE_USER);
    }

}

