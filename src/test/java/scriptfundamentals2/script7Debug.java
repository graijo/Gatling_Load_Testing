package scriptfundamentals2;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class script7Debug extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");
    private ScenarioBuilder scenarioBuilder=scenario("Debug test")

            .exec(http("get all and extract and save game id").get("/api/videogame")
                    .check(status().not(404),status().not(500),status().is(200))
                    .check(jmesPath("[?id== `4`].name").saveAs("gameName"))
            )
            .exec(
                    session -> {
                        System.out.println("Session is "+session);
                        System.out.println("gameName variable saved is "+session.getString("gameName"));
                        return session;
                    }
            )

            .pause(Duration.ofMillis(3000))
            .exec(http("Get details of gameName #{gameName}").get("/api/videogame/4")
                    .check(jmesPath("name").is("Super Mario 64"))
                            .check(bodyString().saveAs("responseBody"))
            )
            .exec(
                    session -> {
                        System.out.println("responseBody variable saved is "+session.getString("responseBody"));
                        return session;
                    }
            )

            .pause(1,10)
            ;

    {
       setUp(
               scenarioBuilder.injectOpen(atOnceUsers(1))
       ).protocols(httpProtocolBuilder);

    }



}
