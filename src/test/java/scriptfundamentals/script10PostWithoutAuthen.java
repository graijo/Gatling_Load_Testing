package scriptfundamentals;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script10PostWithoutAuthen extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");


    ChainBuilder callAllGamesList=exec(http("call all video games")
            .get("/api/videogame")
            .check(status().is(200))
            .check(responseTimeInMillis().lte(2000))
            .check(jsonPath("$[?(@.id==1)].name").is( "Resident Evil 4"))
    );


    ChainBuilder createNewGame= exec(http("Create a new game")
            .post("/api/videogame")
            .body(StringBody("{\n" +
                    "  \"category\": \"Platform\",\n" +
                    "  \"name\": \"Mario\",\n" +
                    "  \"rating\": \"Mature\",\n" +
                    "  \"releaseDate\": \"2012-05-04\",\n" +
                    "  \"reviewScore\": 85\n" +
                    "}"))
            );



    private ScenarioBuilder scenarioBuilder=scenario("Create a new game ---").exec(createNewGame);

    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(1))
       ).protocols(httpProtocolBuilder);

    }



}
