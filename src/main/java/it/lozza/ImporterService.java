package it.lozza;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

@ApplicationScoped
public class ImporterService {
    public String createURLRegione(int totalPageNumber, int page, String target, boolean onlyActive) {
        String result = "";
        String url = "https://www.bandi.regione.lombardia.it/procedimenti/new/api/bandi/getBandiPaginati?";

        QueryString data = new QueryString();
        data.put("titolo", "");
        data.put("maxPageNum", String.valueOf(totalPageNumber));
        data.put("targetStr", target);
        data.put("descrizione", "");
        if(onlyActive){
            data.put("ricercaAvanzata", "true");
            data.put("stato[0]", "APERTO");
            data.put("stato[1]",  "IN%20APERTURA");
        }else{
            data.put("ricercaAvanzata", "false");
        }
        data.put("pageNum", Integer.valueOf(page).toString());

        String params = data.toString();
        return url + params;
    }
}
