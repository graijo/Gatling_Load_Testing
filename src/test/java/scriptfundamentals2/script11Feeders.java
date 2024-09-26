package scriptfundamentals2;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script11Feeders extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");


    //CSV Feeder
    FeederBuilder.FileBased<String> csvFeeder=csv("data/GameData.csv").circular();
    //Variables are #{columnNames of Csv / Key names of Json body}
    ChainBuilder callASpecificGame=feed(csvFeeder).exec(http("call a PC game - #{gameName}")
            .get("api/videogame/#{gameId}")
            .check(status().is(200))
            .check(responseTimeInMillis().lte(2000))
            .check(jmesPath("name").isEL("#{gameName}"))
         )
            .exec(session -> {
                System.out.println("Called a PC game - #{gameName}");
                return session;
            }

         )
            ;




    private ScenarioBuilder scenarioBuilder=scenario("Call a specific game ---")
            .repeat(4).on(exec(callASpecificGame));

    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(15))
       ).protocols(httpProtocolBuilder);

    }



}
