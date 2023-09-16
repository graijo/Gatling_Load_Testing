package scriptfundamentals;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script8RefactorToMethod extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");
    //refactor to methods
    ChainBuilder callAllGamesList=exec(http("call all vide games")
            .get("/api/videogame")
            .check(status().is(200))
            .check(responseTimeInMillis().lte(2000))
            .check(jsonPath("$[?(@.id==1)].name").is( "Resident Evil 4"))
    );

    ChainBuilder callAGame=exec(http("call all vide games")
            .get("api/videogame/1")
            .check(status().is(200))
            .check(status().not(404),status().not(500),status().is(200))
                    .check(jsonPath("$.name").is( "Resident Evil 4"))
                    .check(responseTimeInMillis().lte(2000))
    );
    //call refactored method

    private ScenarioBuilder scenarioBuilder=scenario("Refactor into methods")
            .exec(callAllGamesList)
            .pause(5)
            .exec(callAGame);


    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(1))
       ).protocols(httpProtocolBuilder);

    }



}
