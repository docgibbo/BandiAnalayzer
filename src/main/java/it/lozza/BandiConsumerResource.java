package it.lozza;

import io.smallrye.common.annotation.NonBlocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.ArrayList;

@ApplicationScoped
public class BandiConsumerResource {
    @Inject
    BandiConsumerService service;

    @Inject
    @Channel("parse-bandi")
    Emitter<String> parseEmitter;

    @Inject
    @Channel("load-bando")
    Emitter<String> loadEmitter;

    @Incoming("get-bandi")
    @NonBlocking
    public void getBandi(String url) {
        String html = service.execPOST(url);
        parseEmitter.send(html);
    }

    @Incoming("parse-bandi")
    @NonBlocking
    @Transactional
    public void parseBandi(String html) {
        ArrayList<Bando> bandi = service.parseBandi(html);
        if(bandi != null && !bandi.isEmpty())
            loadEmitter.send(bandi.toString());
    }

    @Incoming("load-bando")
    @NonBlocking
    @Transactional
    public void loadBando(String json) {
        service.loadBandi(json);
    }

    @Incoming("clear-bandi")
    @NonBlocking
    public void clearBandi(String filter) {
        System.out.println("Received message44: " + filter);
    }
}
