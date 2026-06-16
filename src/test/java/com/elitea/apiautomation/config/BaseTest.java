package com.elitea.apiautomation.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    @BeforeSuite
    public void setUpSuite() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.filters(new AllureRestAssured());
    }
}
