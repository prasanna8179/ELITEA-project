package com.elitea.apiautomation.tests;

import com.elitea.apiautomation.config.BaseTest;
import com.elitea.apiautomation.models.CreateRequest;
import com.elitea.apiautomation.models.CreateResponse;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CreateApiTest extends BaseTest {

    @Test
    public void shouldCreateResourceSuccessfully() {
        CreateRequest request = new CreateRequest("morpheus", "leader");

        CreateResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/api/users")
        .then()
                .statusCode(201)
                .extract()
                .as(CreateResponse.class);

        Assert.assertEquals(response.getName(), request.getName());
        Assert.assertEquals(response.getJob(), request.getJob());
        Assert.assertNotNull(response.getId());
        Assert.assertNotNull(response.getCreatedAt());
    }
}
