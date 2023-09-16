package scriptfundamentals;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class script10PostWithAuthentication extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");



    ChainBuilder callAuthentication=exec(http("Call Authentication API")
            .post("/api/authenticate")
            .body(StringBody(
                    "{\n" +
                            "  \"password\": \"admin\",\n" +
                            "  \"username\": \"admin\"\n" +
                            "}"
            ))
            .check(jmesPath("token").saveAs("tockenValue"))
    );

    ChainBuilder createNewGame= exec(http("Create a new game")
            .post("/api/videogame")
            .header("Authorization","Bearer #{tockenValue}")
            .header("Content-Type", "application/json")
            .body(StringBody("{\n" +
                    "  \"category\": \"Platform\",\n" +
                    "  \"name\": \"Mario\",\n" +
                    "  \"rating\": \"Mature\",\n" +
                    "  \"releaseDate\": \"2012-05-04\",\n" +
                    "  \"reviewScore\": 85\n" +
                    "}"))
            );



    private ScenarioBuilder scenarioBuilder=scenario("Create a new game ---")
            .exec(callAuthentication)
            .pause(5)
            .exec(createNewGame);

    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(88))
       ).protocols(httpProtocolBuilder);

    }



}
