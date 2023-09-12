package it.lozza;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.smallrye.common.annotation.NonBlocking;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KafkaConsumerResource {

    @Incoming("test")
    @NonBlocking
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }
}
