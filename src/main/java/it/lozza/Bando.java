package it.lozza;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bando implements Serializable {

    private String title;
    private String description;
    private String dataChiusura;
    private String stato;
    private String categoria;
    private String tipologia;
    private String enteErogatore;

    public Bando() {
    }

    public Bando(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(json, Map.class);
            this.title = (map.get("title") != null) ? map.get("title").toString() : "";
            this.description = (map.get("description") != null) ? map.get("description").toString() : "";
            this.dataChiusura = (map.get("dataChiusura") != null) ? map.get("dataChiusura").toString() : "";
            this.stato = (map.get("stato") != null) ? map.get("stato").toString() : "";
            this.categoria = (map.get("categoria") != null) ? map.get("categoria").toString() : "";
            this.tipologia = (map.get("tipologia") != null) ? map.get("tipologia").toString() : "";
            this.enteErogatore = (map.get("enteErogatore") != null) ? map.get("enteErogatore").toString() : "";
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(this.toString());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDataChiusura(String dataChiusura) {
        this.dataChiusura = dataChiusura;
    }

    public String getDataChiusura() {
        return this.dataChiusura;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getStato() {
        return this.stato;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public void setEnteErogatore(String enteErogatore) {
        this.enteErogatore = enteErogatore;
    }

    public String getEnteErogatore() {
        return this.enteErogatore;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getTipologia() {
        return this.tipologia;
    }

    public String toString(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return super.toString();
        }
    }

}
