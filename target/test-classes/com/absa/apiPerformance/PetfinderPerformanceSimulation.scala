
package com.absa.apiPerformance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PetfinderPerformanceSimulation extends Simulation {

  
  val CLIENT_ID     = "gbfuckDsPVmzzxskuQpgdeQ5tvZYX6NTa9vFszNJLkg8oTeQOK"
  val CLIENT_SECRET = "Y0E5cR6pNrzJinSZuvFlSVDws5NZINH4OdkJtK0d"

  // HTTP configuration
  val httpConf = http
    .baseUrl("https://api.petfinder.com")
    .acceptHeader("application/json")


  val tokenFeeder = Iterator.once(Map(
    "access_token" -> {
      val response = http("Fetch OAuth Token")
        .post("/v2/oauth2/token")
        .header("Content-Type", "application/x-www-form-urlencoded")
        .formParam("grant_type",    "client_credentials")
        .formParam("client_id",     CLIENT_ID)
        .formParam("client_secret", CLIENT_SECRET)
        .check(status.is(200))
        .check(jsonPath("$.access_token").saveAs("token"))
        .execute()  // synchronous call
      response.session("token").as[String]
    }
  ))

  val scn = scenario("Petfinder Load Test")
    .feed(tokenFeeder)
    .exec(
      http("Get Animals")
        .get("/v2/animals")
        .header("Authorization", "Bearer ${token}")
        .check(status.is(200))
        // simple JSON checks:
        .check(jsonPath("$.animals").exists)
        .check(jsonPath("$.animals[0].id").exists)
    )
    .pause(1)

  setUp(
    scn.inject(
      rampUsers(10) during (1.minute),
      constantUsersPerSec(10) during (2.minutes),
      rampUsers(25) during (2.minutes),
      constantUsersPerSec(25) during (3.minutes),
      rampUsers(0)  during (1.minute)
    )
  ).protocols(httpConf)
   .assertions(
     global.responseTime.percentile(95).lte(2000),
     global.failedRequests.percent.lte(0.1)
   )
}
