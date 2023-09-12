package it.lozza;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import io.quarkus.mongodb.FindOptions;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class BandiConsumerService {

    @Inject
    MongoClient mongoClient;

    @Inject
    @ConfigProperty(name = "quarkus.mongodb.database")
    private String databaseName;

    public String execPOST(String url) {
        String result = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("User-Agent", "PostmanRuntime/7.32.3");
            httpPost.setHeader("Accept", "*/*");
            httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpPost.setHeader("Connection", "keep-alive");
            CloseableHttpResponse response = client.execute(httpPost);
            try {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    result = EntityUtils.toString(responseEntity);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String execGET(String url) {
        String result = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            System.out.println(url);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "PostmanRuntime/7.32.3");
            httpGet.setHeader("Accept", "*/*");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpGet.setHeader("Connection", "keep-alive");
            CloseableHttpResponse response = client.execute(httpGet);
            try {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    result = EntityUtils.toString(responseEntity);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Bando> parseBandi(String bandiHtml) {
        ArrayList<Bando> bandi = new ArrayList<>();
        Document doc = Jsoup.parse(bandiHtml);
        Elements cards = doc.select("div[class^=card card-bg card-big]");
        for (int i = 0; i < cards.size(); i++) {
            try {
                Bando bando = new Bando();
                String stato = Jsoup.parse(cards.get(i).select("small[class^=badge]").first().html()).text();
                switch (stato) {
                    case "Aperto":
                        bando.setStato(Utility.STATO.APERTO.toString());
                    case "In apertura":
                        bando.setStato(Utility.STATO.IN_APERTURA.toString());
                    case "Chiuso":
                        Utility.STATO.CHIUSO.toString();
                }
                try {
                    bando.setDataChiusura(cards.get(i).select("div[class^=etichetta mt-1]").first().getElementsByTag("strong").first().html());
                } catch (Exception e) {
                    bando.setDataChiusura(null);
                }
                bando.setTipologia(cards.get(i).select("div[class^=etichetta mt-1]").first().select("p[class^=mt-1]").first().html());
                bando.setTitle(Jsoup.parse(cards.get(i).select("h4[class^=card-title]").first().html()).text());
                Elements descriptions = cards.get(i).select("p[class^=card-text]");
                bando.setDescription(Jsoup.parse(descriptions.get(0).html()).text());
                try {
                    bando.setCategoria(cards.get(i).select("span[class^=categoria]").first().getElementsByTag("strong").first().html());
                } catch (Exception e) {
                    bando.setCategoria(Utility.CATEG_ALL);
                }
                bando.setEnteErogatore(Utility.ENTI_EROGATORI.REGIONE.toString());
                bandi.add(bando);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(bandiHtml);
            }
        }

        return bandi;
    }

    /*public Uni<String> loadBandi(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Bando> bandi = mapper.readValue(json, new TypeReference<ArrayList<Bando>>() {});
            ReactiveMongoCollection<Bando> collection = mongoClient
                    .getDatabase(databaseName)
                    .getCollection(Utility.COLLECTION_NAME, Bando.class);
            Multi<Uni<Void>> insertOperations = Multi.createFrom()
                    .iterable(bandi)
                    .onItem().transformToUniAndMerge(doc -> collection.insertOne(doc).map(result -> null));
            return Uni.combine().all().unis((Uni<?>) insertOperations.collect().asList(), this::publishKafkaMessages)
                    .combinedWith(results -> "Documents inserted and Kafka messages published");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public void loadBandi(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Bando> bandi = mapper.readValue(json, new TypeReference<ArrayList<Bando>>() {});
            if (bandi != null && !bandi.isEmpty()) {
                MongoCollection<Bando> collection = mongoClient
                        .getDatabase(databaseName)
                        .getCollection(Utility.COLLECTION_NAME, Bando.class);
                collection.insertMany(bandi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis());
    }

    public void clearBandi() {
    }
}
