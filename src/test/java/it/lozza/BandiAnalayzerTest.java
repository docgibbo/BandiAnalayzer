package it.lozza;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class BandiAnalayzerTest {
    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/bandi/search/xxx")
                .then()
                .statusCode(200)
                .body(is("Hello from RESTEasy Reactive"));
    }
}
