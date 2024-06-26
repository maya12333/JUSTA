package com.example.justa;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String phone;
    private String password;
    private String type;
    private String uri;

    private int counter;

    public User(String username, String phone, String password, String type) {
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.type = type;
        this.counter = 0;
    }

    public User(){

    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {

        return username;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                ", counter=" + counter +
                '}';
    }
}
