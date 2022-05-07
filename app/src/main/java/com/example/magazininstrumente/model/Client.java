package com.example.magazininstrumente.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Client implements Serializable {

    private String id;
    private String nume;
    private String prenume;
    private String email;
    private String parola;
    private float buget;
    //private HashMap<String,Product> cos;



    public Client(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getBuget() {
        return buget;
    }

    public void setBuget(float buget) {
        this.buget = buget;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

//    public HashMap<String, Product> getCos() {
//        return cos;
//    }
//
//    public void setCos(HashMap<String, Product> cos) {
//        this.cos = cos;
//    }

    @Override
    public String toString() {
        return "Client -" + nume +
                prenume + email + parola ;
    }
}
