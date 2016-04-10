package org.jmmo.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String token;

    private Timestamp token_creation;

    private Timestamp token_expiration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getTokenCreation() {
        return token_creation;
    }

    public void setTokenCreation(Timestamp tokenCreation) {
        this.token_creation = tokenCreation;
    }

    public Timestamp getTokenExpiration() {
        return token_expiration;
    }

    public void setTokenExpiration(Timestamp tokenExpiration) {
        this.token_expiration = tokenExpiration;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", token_creation=" + token_creation +
                ", token_expiration=" + token_expiration +
                '}';
    }
}
