package scriptfundamentals2;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
public class script1 extends Simulation{

    private HttpProtocolBuilder httpProtocolBuilder=http
            .baseUrl("https://www.videogamedb.uk/")
            .acceptHeader("application/json");


    private ScenarioBuilder scenarioBuilder=scenario("Load Test 1")
            .exec(http("Scenario name = get all values in list")
                    .get("/api/videogame"));


    {
        setUp(
                scenarioBuilder.injectOpen(atOnceUsers(2))

        ).protocols(httpProtocolBuilder);
    }

}
