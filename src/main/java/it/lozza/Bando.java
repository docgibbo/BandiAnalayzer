package it.lozza;

public class Bando{

    private String title;
    private String description;
    private String dataChiusura;
    private String stato;
    private String categoria;
    private String tipologia;
    private String enteErogatore;

    public Bando() {
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

}
