package com.kan.config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Central API configuration for REST Assured.
 * Reads base URL from system property or environment variable; falls back to localhost.
 */
public final class ApiConfig {

    public static final String BASE_URL =
            System.getProperty("api.base.url",
                    System.getenv().getOrDefault("API_BASE_URL", "http://localhost:8080"));

    public static final String EMPLOYEES_PATH = "/api/employees";

    private ApiConfig() {}

    /**
     * Builds a shared RequestSpecification with logging enabled.
     */
    public static RequestSpecification buildRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    /**
     * Convenience: configure RestAssured global base URI once.
     */
    public static void configure() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
