package com.example.justa;

public class Recommendation {

    private String nameV;
    private String phoneV;
    private String textV;
    private String phoneN;

    public Recommendation(String nameV, String phoneV, String textV, String phoneN) {
        this.nameV = nameV;
        this.phoneV = phoneV;
        this.textV = textV;
        this.phoneN = phoneN;
    }

    public Recommendation(){

    }

    public String getNameV() {
        return nameV;
    }

    public String getPhoneN() {
        return phoneN;
    }

    public void setPhoneN(String phoneN) {
        this.phoneN = phoneN;
    }

    public void setNameV(String nameV) {
        this.nameV = nameV;
    }

    public String getPhoneV() {
        return phoneV;
    }

    public void setPhoneV(String phoneV) {
        this.phoneV = phoneV;
    }

    public String getTextV() {
        return textV;
    }

    public void setTextV(String textV) {
        this.textV = textV;
    }
}
