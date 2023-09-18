package scriptfundamentals2;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script3CheckResponse extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");

    private ScenarioBuilder scenarioBuilder=scenario("Main -pause scenarios")
            .exec(http("pause 1").get("/api/videogame")
                    .check(status().is(200)))

            .pause(5)
            .exec(http("pauce 2").get("api/videogame/2")
                    .check(status().in(200,201,202,203,203,204)))
            .pause(1,10)
            .exec(http("Pause 3").get("/api/videogame")
                    .check(status().not(404),status().not(500),status().is(200)))
            .pause(Duration.ofMillis(3000));

    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(1))
       ).protocols(httpProtocolBuilder);

    }



}
