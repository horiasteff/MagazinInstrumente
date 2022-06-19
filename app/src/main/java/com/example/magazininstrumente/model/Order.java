package com.example.magazininstrumente.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    private String idComanda;
    private String numeComanda;
    private String prenumeComanda;
    private String emailComanda;
    private String adresaComanda;
    private String telefonComanda;
    private String costTotalComanda;
    private String tipPlata;
    private String dataComanda;
    private List<Product> produse;
    private Courier courier;


    public Order(){}

    public Order(String numeComanda, String prenumeComanda, String emailComanda, String adresaComanda, String telefonComanda, String costTotalComanda, String tipPlata, String dataComanda, List<Product> produse, Courier courier) {
        this.numeComanda = numeComanda;
        this.prenumeComanda = prenumeComanda;
        this.emailComanda = emailComanda;
        this.adresaComanda = adresaComanda;
        this.telefonComanda = telefonComanda;
        this.tipPlata = tipPlata;
        this.costTotalComanda = costTotalComanda;
        this.dataComanda = dataComanda;
        this.produse = produse;
        this.courier = courier;
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

    public String getTipPlata() {
        return tipPlata;
    }

    public void setTipPlata(String tipPlata) {
        this.tipPlata = tipPlata;
    }

    public String getCostTotalComanda() {
        return costTotalComanda;
    }

    public void setCostTotalComanda(String costTotalComanda) {
        this.costTotalComanda = costTotalComanda;
    }

    public String getDataComanda() {
        return dataComanda;
    }

    public void setDataComanda(String dataComanda) {
        this.dataComanda = dataComanda;
    }

    public List<Product> getProduse() {
        return produse;
    }

    public void setProduse(List<Product> produse) {
        this.produse = produse;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    @Override
    public String toString() {
        return "Order{" +
                "idComanda='" + idComanda + '\'' +
                ", numeComanda='" + numeComanda + '\'' +
                ", prenumeComanda='" + prenumeComanda + '\'' +
                ", emailComanda='" + emailComanda + '\'' +
                ", adresaComanda='" + adresaComanda + '\'' +
                ", telefonComanda='" + telefonComanda + '\'' +
                ", costTotalComanda='" + costTotalComanda + '\'' +
                ", tipPlata='" + tipPlata + '\'' +
                ", dataComanda='" + dataComanda + '\'' +
                ", produse=" + produse +
                ", courier=" + courier +
                '}';
    }
}
