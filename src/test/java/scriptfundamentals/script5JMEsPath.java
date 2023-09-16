package scriptfundamentals;

import io.gatling.javaapi.core.CheckBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script5JMEsPath extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");
//    responseTimeInMillis
//    Returns the response time of this request in milliseconds = the time between
//    starting to send the request and finishing to receive the response.
    private ScenarioBuilder scenarioBuilder=scenario("Main -pause scenarios")
            .exec(http("pause 1").get("/api/videogame")
                    .check(status().is(200))
                    .check(responseTimeInMillis().lte(800))
//                    .check(jmesPath("[? category==`Puzzle`].name").ofList().is(List.of("\"Tetris\"," +
//                            "\"Minecraft\"")))
                    .check(jmesPath("[?id==`6`].reviewScore").ofList().is(List.of(81)))

            )

            .pause(5)
            .exec(http("pauce 2").get("api/videogame/2")
                    .check(status().in(200,201,202,203,203,204)))
            .pause(1,10)
            .exec(http("Pause 3").get("/api/videogame")
                    .check(status().not(404),status().not(500),status().is(200))
                    .check(jmesPath("[?id== `2`].name").ofList().is(List.of("Gran Turismo 3")))
            )
            .pause(Duration.ofMillis(3000));

    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(1))
       ).protocols(httpProtocolBuilder);

    }



}
