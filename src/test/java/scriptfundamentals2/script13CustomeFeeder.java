package scriptfundamentals2;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script13CustomeFeeder extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");


    //custom Feeder
    private static Iterator<Map<String, Object>> customFeeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        Random rand = new Random();
                        int gameId = rand.nextInt(10 - 1 + 1) + 1;
                        return Collections.singletonMap("gameId", gameId);
                    }
            ).iterator();
    ChainBuilder callASpecificGame=feed(customFeeder).exec(http("call a PC game - #{gameId}")
            .get("api/videogame/#{gameId}")
            .check(status().is(200))
            .check(responseTimeInMillis().lte(2000))
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
