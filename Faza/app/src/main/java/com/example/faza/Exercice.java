package com.example.faza;

import java.util.ArrayList;
import java.util.List;

public class Exercice {
    private long id;
    private String nom;
    private String groupePrincipal;
    private String groupeSecondaire;
    private String description;
    private String urlVideo;
    private String miniature;

    private List<Serie> series;

    public Exercice() {
        series = new ArrayList<>();
    }

    public Exercice(String nom) {
        this.nom = nom;
        this.series = new ArrayList<>();
    }

    public List<Serie> getSeries() { return series; }
    public void setSeries(List<Serie> series) { this.series = series; }

    public void ajouterSerie(Serie s) {
        series.add(s);
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
}
