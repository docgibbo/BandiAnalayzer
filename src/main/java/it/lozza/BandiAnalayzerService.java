package it.lozza;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.TextSearchOptions;
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
    MongoClient mongoClient;

    @Inject
    @ConfigProperty(name = "quarkus.mongodb.database")
    private String databaseName;

    public List<Bando> list(String collectionName) {
        List<Bando> list = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase(this.databaseName);
        MongoCursor<Document> cursor = database.getCollection(collectionName).find().iterator();
        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Bando bando = new Bando();
                bando.setTitle(document.getString("title"));
                bando.setDescription(document.getString("description"));
                list.add(bando);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public List<Bando> search(String collectionName, String queryText) {
        List<Bando> list = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase(this.databaseName);

        TextSearchOptions options = new TextSearchOptions().caseSensitive(false);
        Bson filter = Filters.text(queryText, options);
        MongoCursor<Document> cursor = database.getCollection(collectionName).find(filter).iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Bando bando = new Bando();
                bando.setTitle(document.getString("title"));
                bando.setDescription(document.getString("description"));
                list.add(bando);
            }
        } finally {
            cursor.close();
        }
        return list;
    }
}
