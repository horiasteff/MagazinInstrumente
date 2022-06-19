package com.example.magazininstrumente.model;

import java.io.Serializable;

public class Courier implements Serializable {

    private String idCurier;
    private String numeCurier;
    private String numarTelefon;


    public Courier(){}

    public Courier(String idCurier, String numeCurier, String numarTelefon) {
        this.idCurier = idCurier;
        this.numeCurier = numeCurier;
        this.numarTelefon = numarTelefon;
        
    }

    public String getIdCurier() {
        return idCurier;
    }

    public void setIdCurier(String idCurier) {
        this.idCurier = idCurier;
    }

    public String getNumeCurier() {
        return numeCurier;
    }

    public void setNumeCurier(String numeCurier) {
        this.numeCurier = numeCurier;
    }

    public String getNumarTelefon() {
        return numarTelefon;
    }

    public void setNumarTelefon(String numarTelefon) {
        this.numarTelefon = numarTelefon;
    }

    @Override
    public String toString() {
        return "Courier{" +
                "idCurier='" + idCurier + '\'' +
                ", numeCurier='" + numeCurier + '\'' +
                ", numarTelefon='" + numarTelefon + '\'' +
                '}';
    }
}
