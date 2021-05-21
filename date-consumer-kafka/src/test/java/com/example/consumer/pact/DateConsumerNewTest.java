package com.example.consumer.pact;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.Message;
import au.com.dius.pact.core.model.messaging.MessagePact;
import com.example.consumer.ConsumerDateInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "DateProviderKafka", providerType = ProviderType.ASYNCH)
public class DateConsumerNewTest {

    @Pact(consumer = "DateConsumerKafka")
    public MessagePact validDateMessageFromKafkaProvider(MessagePactBuilder builder) {

        PactDslJsonBody responseWrittenWithDsl = new PactDslJsonBody()
                .dateExpression("localDate", "")
                .booleanType("isLeapYear", true)
                .close()
                .asBody();

        return builder
                .expectsToReceive("valid date from date-provider-kafka")
                .withContent(responseWrittenWithDsl)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "validDateMessageFromKafkaProvider")
    public void testValidDateFromProvider(List<Message> messages) throws JsonProcessingException {
        assertThat(messages).isNotEmpty();
        assertThat(new ObjectMapper().readValue(new String(messages.get(0).contentsAsBytes()), ConsumerDateInfo.class))
                .hasFieldOrProperty("localDate")
                .hasFieldOrPropertyWithValue("isLeapYear", true);
    }
}
