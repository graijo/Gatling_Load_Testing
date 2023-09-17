package scriptfundamentals;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script12JsonFeedersWithRandomStratergy extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");


    //json Feeder
    FeederBuilder.FileBased<Object> jsonObject=jsonFile("data/GameDataJson.json").random();
    ChainBuilder callASpecificGame=feed(jsonObject).exec(http("call a PC game - #{name}")
            .get("api/videogame/#{id}")
            .check(status().is(200))
            .check(responseTimeInMillis().lte(2000))
            .check(jmesPath("name").isEL("#{name}"))
                    .check(jmesPath("rating").isEL("#{rating}"))
                    .check(bodyString().saveAs("responseBody"))

         )
            .exec(session -> {
                System.out.println("Called a PC game - name");
                System.out.println("responseBody is "+session.getString("responseBody"));
                return session;
            }

         )
            ;




    private ScenarioBuilder scenarioBuilder=scenario("Call a specific game ---")
            .repeat(10).on(exec(callASpecificGame));

    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(1))
       ).protocols(httpProtocolBuilder);

    }



}
