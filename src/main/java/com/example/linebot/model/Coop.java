package com.example.linebot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "coop")
public class Coop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coop_id")
    private String coopId;

    @Column(name = "name")
    private String name;

    @Column(name = "coop_code")
    private String coopCode;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoopId() {
        return coopId;
    }

    public void setCoopId(String coopId) {
        this.coopId = coopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoopCode() {
        return coopCode;
    }

    public void setCoopCode(String coopCode) {
        this.coopCode = coopCode;
    }
}
