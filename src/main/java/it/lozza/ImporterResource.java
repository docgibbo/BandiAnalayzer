package it.lozza;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.security.Timestamp;
import java.util.Date;

@Path("/bandi/importer")
public class ImporterResource {
    @Inject
    ImporterService service;

    @Inject
    @Channel("get-bandi")
    Emitter<String> emitter;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/regione")
    public int loadBandiRegione() {
        System.out.println(System.currentTimeMillis());
        for(int page=0; page<35; page++) {
            String url = service.createURLRegione(35, page, "ENTI_E_OPERATORI", true);
            emitter.send(url).whenComplete((success, failure) -> {
                if (failure != null) {
                    System.out.println("D'oh! " + failure.getMessage());
                } else {
                    System.out.println("Message processed successfully: " + success);
                }
            });
            System.out.println("url inviata");
        }
        return 0;
    }
}
