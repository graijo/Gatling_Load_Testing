package scriptfundamentals2;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script4responseTimeInMillis extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");
//    responseTimeInMillis
//    Returns the response time of this request in milliseconds = the time between
//    starting to send the request and finishing to receive the response.
    private ScenarioBuilder scenarioBuilder=scenario("Main -pause scenarios")
            .exec(http("pause 1").get("/api/videogame")
                    .check(status().is(200))
                    .check(responseTimeInMillis().lte(600))
                    //$[0].name
                    //$[?(@.id==1)].name
                    .check(jsonPath("$[?(@.id==1)].name").is( "Resident Evil 4"))

            )

            .pause(5)
            .exec(http("pauce 2").get("api/videogame/2")
                    .check(status().in(200,201,202,203,203,204)))
            .pause(1,10)
            .exec(http("Pause 3").get("/api/videogame")
                    .check(status().not(404),status().not(500),status().is(200))
                    .check(jsonPath("$[?(@.id==1)].name").is( "Resident Evil 5"))
                    .check(responseTimeInMillis().lte(100))
            )
            .pause(Duration.ofMillis(3000));

    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(1))
       ).protocols(httpProtocolBuilder);

    }



}
