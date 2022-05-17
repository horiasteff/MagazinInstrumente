package com.example.magazininstrumente.model;

import java.io.Serializable;
import java.util.List;

public class Comanda implements Serializable {
    private String idComanda;
    private String numeComanda;
    private String prenumeComanda;
    private String emailComanda;
    private String adresaComanda;
    private String telefonComanda;
    private String costTotalComanda;
    private List<Product> produse;

    public Comanda(){}

    public Comanda(String numeComanda, String prenumeComanda, String emailComanda, String adresaComanda, String telefonComanda, String costTotalComanda, List<Product> produse) {
        this.numeComanda = numeComanda;
        this.prenumeComanda = prenumeComanda;
        this.emailComanda = emailComanda;
        this.adresaComanda = adresaComanda;
        this.telefonComanda = telefonComanda;
        this.costTotalComanda = costTotalComanda;
        this.produse = produse;
    }

    public String getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(String idComanda) {
        this.idComanda = idComanda;
    }

    public String getNumeComanda() {
        return numeComanda;
    }

    public void setNumeComanda(String numeComanda) {
        this.numeComanda = numeComanda;
    }

    public String getPrenumeComanda() {
        return prenumeComanda;
    }

    public void setPrenumeComanda(String prenumeComanda) {
        this.prenumeComanda = prenumeComanda;
    }

    public String getEmailComanda() {
        return emailComanda;
    }

    public void setEmailComanda(String emailComanda) {
        this.emailComanda = emailComanda;
    }

    public String getAdresaComanda() {
        return adresaComanda;
    }

    public void setAdresaComanda(String adresaComanda) {
        this.adresaComanda = adresaComanda;
    }

    public String getTelefonComanda() {
        return telefonComanda;
    }

    public void setTelefonComanda(String telefonComanda) {
        this.telefonComanda = telefonComanda;
    }

    public String getCostTotalComanda() {
        return costTotalComanda;
    }

    public void setCostTotalComanda(String costTotalComanda) {
        this.costTotalComanda = costTotalComanda;
    }

    public List<Product> getProduse() {
        return produse;
    }

    public void setProduse(List<Product> produse) {
        this.produse = produse;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "idComanda='" + idComanda + '\'' +
                ", numeComanda='" + numeComanda + '\'' +
                ", prenumeComanda='" + prenumeComanda + '\'' +
                ", emailComanda='" + emailComanda + '\'' +
                ", adresaComanda='" + adresaComanda + '\'' +
                ", telefonComanda='" + telefonComanda + '\'' +
                ", costTotalComanda='" + costTotalComanda + '\'' +
                ", produse=" + produse +
                '}';
    }
}
