package it.lozza;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BandiAnalayzerService {
    @Inject
    MongoClient mongoClient;

    public List<Bando> list(){
        List<Bando> list = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase("lozza");
        MongoCursor<Document> cursor = database.getCollection("bandi").find().iterator();
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
