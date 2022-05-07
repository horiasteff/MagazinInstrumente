package com.example.magazininstrumente.model;

import java.io.Serializable;

public class Product implements Serializable {

    private String id;
    private String denumire;
    private String pret;
    private String categorie;
    private String descriere;
    private String urlImagine;

    public Product() {
    }

    public Product(String denumire, String pret, String categorie, String descriere, String urlImagine) {
        this.denumire = denumire;
        this.pret = pret;
        this.categorie = categorie;
        this.descriere = descriere;
        this.urlImagine = urlImagine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getUrlImagine() {
        return urlImagine;
    }

    public void setUrlImagine(String urlImagine) {
        this.urlImagine = urlImagine;
    }

    @Override
    public String toString() {
        return id + denumire + pret + categorie + descriere;
    }
}
