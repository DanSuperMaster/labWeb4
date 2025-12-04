package com.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "dots")
public class Dot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x", nullable = false)
    @NotNull
    private Double x;

    @Column(name = "y", nullable = false)
    @NotNull
    private Double y;

    @Column(name = "r", nullable = false)
    @NotNull
    private Integer r;

    @Column(name = "inArea", nullable = false)
    @NotNull
    private Boolean inArea;

    @Column(name = "user", nullable = false)
    @NotNull
    private String user;

    public Long getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Integer getR() {
        return r;
    }

    public void setR(Integer r) {
        this.r = r;
    }

    public Boolean getInArea() {
        return inArea;
    }

    public void setInArea(Boolean inArea) {
        this.inArea = inArea;
    }
}
