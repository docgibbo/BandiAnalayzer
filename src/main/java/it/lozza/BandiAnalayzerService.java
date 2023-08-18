package it.lozza;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.TextSearchOptions;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoDatabase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;

import jakarta.inject.Inject;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BandiAnalayzerService {
    @Inject
    ReactiveMongoClient mongoClient;

    @Inject
    @ConfigProperty(name = "quarkus.mongodb.database")
    private String databaseName;

    public Uni<List<Bando>> list(String collectionName) {
        List<Bando> list = new ArrayList<>();
        ReactiveMongoDatabase database = mongoClient.getDatabase(this.databaseName);
        return database.getCollection(collectionName).find().map(doc -> {
            Bando bando = new Bando();
            bando.setTitle(doc.getString("title"));
            bando.setDescription(doc.getString("description"));
            return bando;
        }).collect().asList();
    }

    public Uni<List<Bando>> search(String collectionName, String queryText) {
        List<Bando> list = new ArrayList<>();
        ReactiveMongoDatabase database = mongoClient.getDatabase(this.databaseName);

        TextSearchOptions options = new TextSearchOptions().caseSensitive(false);
        Bson filter = Filters.text(queryText, options);

        return database.getCollection(collectionName).find(filter).map(doc -> {
            Bando bando = new Bando();
            bando.setTitle(doc.getString("title"));
            bando.setDescription(doc.getString("description"));
            return bando;
        }).collect().asList();
    }
}
