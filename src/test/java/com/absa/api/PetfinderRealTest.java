package com.absa.api;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class PetfinderRealTest {

    private static String TOKEN;

    @BeforeAll
    public static void fetchToken() {
        
  
      String CLIENT_ID     = "1yIxd8HkNjV4ymqLi2GhaFFYOps9SrpEeT0cUWSSWwDAVHKxeF" ;
      String CLIENT_SECRET = "kih7OW7Q3yGiVK4i7Y33hEwsS24eEWRyiN6gaNMt";

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
