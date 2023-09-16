package scriptfundamentals;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script6ExtractAndSaveResponseValue extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");
    private ScenarioBuilder scenarioBuilder=scenario("Extract data from response and store it in variable for later use")

            .exec(http("get all and extract and save game id").get("/api/videogame")
                    .check(status().not(404),status().not(500),status().is(200))
                    .check(jmesPath("[?id== `4`].name").saveAs("gameName"))
            )
            .pause(Duration.ofMillis(3000))
            .exec(http("Get details of gameName #{gameName}").get("/api/videogame/4")
//                    .check(status().in(200,201,202,203,203,204))
                    .check(jmesPath("name").is("#{gameName}"))
            )

            .pause(1,10)
            ;

    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(1))
       ).protocols(httpProtocolBuilder);

    }



}
