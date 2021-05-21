package com.example.ageconsumer.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "DateProvider", port = "1234")
public class PactAgeConsumerTest {

    @BeforeEach
    public void setup(MockServer mockServer) {
        assertThat(mockServer).isNotNull();
    }

    @Pact(consumer = "AgeConsumer")
    public RequestResponsePact testDateProvider(PactDslWithProvider builder) {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        PactDslJsonBody responseWrittenWithDsl = new PactDslJsonBody()
                .numberType("year", 1990)
                .numberType("month", 10)
                .numberType("day", 18)
                .booleanType("isValidDate", true)
                .close()
                .asBody();


        return builder.given("received valid date")
                .uponReceiving("a valid date from provider")
                    .path("/provider/validDate")
                    .method("GET")
                    .queryMatchingDate("date", "1990-10-18")
                .willRespondWith()
                    .status(200)
                    .headers(headers)
                    .body(responseWrittenWithDsl)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "testDateProvider")
    void test(MockServer mockServer) throws IOException {

        HttpResponse response = Request.Get(mockServer.getUrl() + "/provider/validDate?date=1990-10-18")
                .execute()
                .returnResponse();

        assertThat(response).isNotNull();
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(JsonPath.read(response.getEntity().getContent(), "$.isValidDate").toString()).isEqualTo("true");
    }

}
