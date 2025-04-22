file://<WORKSPACE>/src/test/java/com/absa/api/PetfinderRealTest.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 624
uri: file://<WORKSPACE>/src/test/java/com/absa/api/PetfinderRealTest.java
text:
```scala
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
        
        String CLIENT_ID     = "gbfuckDsPVmzzxskuQpgdeQ5tvZYX6NTa9vFszNJLkg8oTeQOK";
        String CLIENT_SECRET = "Y0E5cR6pNrzJinSZuvFlSVDws5NZINH4OdkJtK0d";

        TOKEN =
          given()
            .contentType("application/x-www-form-urlencod@@ed")
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

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:389)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator