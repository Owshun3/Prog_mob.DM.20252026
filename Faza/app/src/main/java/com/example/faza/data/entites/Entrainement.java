package com.example.faza.data.entites;

import java.util.ArrayList;
import java.util.List;

public class Entrainement extends Programme{
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

    public Entrainement() {
        exercices = new ArrayList<>();
    }

    public Entrainement(long idUser, String dateSeance, int dureeMin, String photoFin,
                        String commentaire) {
        super(idUser, photoFin, commentaire);
        this.photoFin = photoFin;
        this.dateSeance = dateSeance;
        this.dureeMin = dureeMin;
    }


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDateSeance() { return dateSeance; }
    public void setDateSeance(String dateSeance) { this.dateSeance = dateSeance; }

    public int getDureeMin() { return dureeMin; }
    public void setDureeMin(int dureeMin) { this.dureeMin = dureeMin; }

    public String getPhotoFin() { return photoFin; }
    public void setPhotoFin(String photoFin) { this.photoFin = photoFin; }
}
