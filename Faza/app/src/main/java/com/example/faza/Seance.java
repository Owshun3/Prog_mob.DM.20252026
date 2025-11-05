package com.example.faza;

import java.util.ArrayList;
import java.util.List;

public class Seance extends Programme{
    private long id;
    private long idUser;
    private String dateSeance;
    private int dureeMin;
    private double chargeTotale;
    private int nbSeries;
    private int nbRepetitions;
    private String photoFin;
    private String commentaire;

    private List<Exercice> exercices;

    public Seance() {
        exercices = new ArrayList<>();
    }

    public Seance(long idUser, String dateSeance, int dureeMin, String photoFin,
                  String commentaire) {
        super(idUser, dureeMin, photoFin, commentaire);
        this.dateSeance = dateSeance;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDateSeance() { return dateSeance; }
    public void setDateSeance(String dateSeance) { this.dateSeance = dateSeance; }
}
