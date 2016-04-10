package org.jmmo.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "token_log")
public class TokenLog {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Timestamp time;

    private Timestamp expiration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Operation operation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "TokenLog{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", time=" + time +
                ", expiration=" + expiration +
                ", operation=" + operation +
                '}';
    }

    public enum Operation {
        CREATED, DISMISSED
    }
}
