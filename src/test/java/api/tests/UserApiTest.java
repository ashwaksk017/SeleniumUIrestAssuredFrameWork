package api.tests;


import io.restassured.response.Response;
import org.testng.annotations.*;

import com.api.framework.ApiResponseValidator;
import com.api.framework.UserApiClient;
import com.data.loader.CsvDataLoader;

import java.util.Map;

public class UserApiTest {

    private UserApiClient userApiClient;

    @BeforeClass
    public void setup() {
        userApiClient = new UserApiClient();
    }

    @Test(dataProvider = "loginData", dataProviderClass = CsvDataLoader.class)
    public void testLogin(Map<String, String> data) {
        Response response = userApiClient.loginUser(Map.of(
            "username", data.get("username"),
            "password", data.get("password")
        ));

        ApiResponseValidator.verifyStatusCode(response, 200);
        ApiResponseValidator.verifyJsonFieldEquals(response, "status", "success");
        ApiResponseValidator.verifyJsonFieldNotNull(response, "token");
    }
}
