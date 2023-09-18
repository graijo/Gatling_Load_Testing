package scriptfundamentals2;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script9Repeat extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");

    ChainBuilder callAllGamesList=exec(http("call all video games")
            .get("/api/videogame")
            .check(status().is(200))
            .check(responseTimeInMillis().lte(2000))
            .check(jsonPath("$[?(@.id==1)].name").is( "Resident Evil 4"))
    );

    ChainBuilder callAGame=
            repeat(6,"myCounterVar").on(
                    exec(http("call a video games for Iteration #{myCounterVar}")
                            .get("api/videogame/1")
                            .check(status().is(200))
                            .check(status().not(404),status().not(500),status().is(200))
                            .check(jsonPath("$.name").is( "Resident Evil 4"))
                            .check(responseTimeInMillis().lte(2000))
                    )
            );
    //repeat

    private ScenarioBuilder scenarioBuilder=scenario("Refactor into methods")
            .repeat(12).on(exec(callAllGamesList))

            .pause(3)
            .repeat(15).on(exec(callAGame));


    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(1))
       ).protocols(httpProtocolBuilder);

    }



}
