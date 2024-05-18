package com.example.justa;

public class Request implements Comparable<Request>{

    private String phone;
    private String date;
    private String text;
    private String address;
    private String uid;

    private boolean taken;

    public Request(String phone, String date, String text, String address, boolean taken) {
        this.phone = phone;
        this.date = date;
        this.text = text;
        this.address = address;
        this.taken = taken;
    }

    public Request(){}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Request{" +
                "address='" + address + '\'' +
                '}';
    }

    @Override
    public int compareTo(Request request) {
        boolean b1 = request.taken;
        boolean b2 = this.taken;

        if (b1 && b2){
            return 1;
        }

        return -1;
    }
}
