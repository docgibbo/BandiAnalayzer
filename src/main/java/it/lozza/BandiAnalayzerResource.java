package it.lozza;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import java.util.List;

@Path("/bandi")
public class BandiAnalayzerResource {
    @Inject BandiAnalayzerService bandiAnalayzerService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search/{value}")
    public List<Bando> search(@PathParam("value") String value) {
        List<Bando> list = bandiAnalayzerService.search("bandi", value);
        return list;
    }
}
