package com.example.justa;

public class Recommendation {

    private String nameV;
    private String phoneV;
    private String textV;

    public Recommendation(String nameV, String phoneV, String textV) {
        this.nameV = nameV;
        this.phoneV = phoneV;
        this.textV = textV;
    }

    public String getNameV() {
        return nameV;
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
