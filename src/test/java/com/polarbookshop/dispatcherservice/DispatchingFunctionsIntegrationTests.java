package com.polarbookshop.dispatcherservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@FunctionalSpringBootTest
class DispatchingFunctionsIntegrationTests {

    @Autowired
    FunctionCatalog catalog;

    @Test
    void packOrder() {
        Function<OrderAcceptedMessage, Long> pack =
                catalog.lookup(Function.class, "pack");
        long orderId = 121;

        assertThat(pack.apply(new OrderAcceptedMessage(orderId))).isEqualTo(orderId);
    }

    @Test
    void labelOrder() {
        Function<Long, Flux<OrderDispatchedMessage>> label =
                catalog.lookup(Function.class, "label");
        long orderId = 121;

        StepVerifier.create(label.apply(orderId))
                .expectNextMatches(orderDispatchedMessage ->
                        orderDispatchedMessage.equals(new OrderDispatchedMessage(orderId)))
                .verifyComplete();
    }

    @Test
    void packAndLabelOrder() {
        Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>> packAndLabel =
                catalog.lookup(Function.class, "pack|label");
        long orderId = 121;

        StepVerifier.create(packAndLabel.apply(
                new OrderAcceptedMessage(orderId)
        ))
        .expectNextMatches(dispatchedOrder ->
                dispatchedOrder.equals(new OrderDispatchedMessage(orderId)))
        .verifyComplete();
    }
}