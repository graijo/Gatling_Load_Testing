package scriptfundamentals;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
public class script2Pause extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");

    private ScenarioBuilder scenarioBuilder=scenario("Main -pause scenarios")
            .exec(http("pause 1").get("/api/videogame"))

            .pause(5)
            .exec(http("pauce 2").get("api/videogame/2"))
            .pause(1,10)
            .exec(http("Pause 3").get("/api/videogame"))
            .pause(Duration.ofMillis(3000));

    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(1))
       ).protocols(httpProtocolBuilder);

    }



}
