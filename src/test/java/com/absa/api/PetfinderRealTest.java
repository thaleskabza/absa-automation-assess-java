package com.absa.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PetfinderRealTest {

    private static String TOKEN;

    @BeforeAll
    public static void fetchToken() {
        // ← replace with your real credentials
        String CLIENT_ID     = "gbfuckDsPVmzzxskuQpgdeQ5tvZYX6NTa9vFszNJLkg8oTeQOK";
        String CLIENT_SECRET = "Y0E5cR6pNrzJinSZuvFlSVDws5NZINH4OdkJtK0d";

        TOKEN =
          given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("grant_type",    "client_credentials")
            .formParam("client_id",     CLIENT_ID)
            .formParam("client_secret", CLIENT_SECRET)
          .when()
            .post("https://api.petfinder.com/v2/oauth2/token")
          .then()
            .statusCode(200)
            .extract()
            .path("access_token");
    }

    @Test
    void fetchTypes_andVerifyDogExists() {
        given()
          .auth().oauth2(TOKEN)
        .when()
          .get("https://api.petfinder.com/v2/types")
        .then()
          .statusCode(200)
          .body("types.name", hasItem("Dog"));
    }

    @Test
    void fetchBreeds_andSearchGoldenRetriever() {
        given()
          .auth().oauth2(TOKEN)
        .when()
          .get("https://api.petfinder.com/v2/types/dog/breeds")
        .then()
          .statusCode(200)
          .body("breeds.name", hasItem("Golden Retriever"));

        given()
          .auth().oauth2(TOKEN)
          .queryParam("type", "dog")
          .queryParam("breed", "Golden Retriever")
          .queryParam("limit", 1)
        .when()
          .get("https://api.petfinder.com/v2/animals")
        .then()
          .statusCode(200)
          .body("animals.size()", greaterThan(0));
    }
}
