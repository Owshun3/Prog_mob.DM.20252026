package com.example.faza;

import java.util.ArrayList;
import java.util.List;

public class Seance {
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

    public Seance(long idUser, String dateSeance, int dureeMin, String photoFin, String commentaire) {
        this.idUser = idUser;
        this.dateSeance = dateSeance;
        this.dureeMin = dureeMin;
        this.photoFin = photoFin;
        this.commentaire = commentaire;
        this.exercices = new ArrayList<>();
    }

    public void calculerChargeEtStats() {
        double total = 0;
        int totalSeries = 0;
        int totalReps = 0;

        for (Exercice e : exercices) {
            for (Serie s : e.getSeries()) {
                total += s.getPoids() * s.getRepetitions();
                totalSeries++;
                totalReps += s.getRepetitions();
            }
        }

        this.chargeTotale = total;
        this.nbSeries = totalSeries;
        this.nbRepetitions = totalReps;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdUser() { return idUser; }
    public void setIdUser(long idUser) { this.idUser = idUser; }

    public String getDateSeance() { return dateSeance; }
    public void setDateSeance(String dateSeance) { this.dateSeance = dateSeance; }

    public int getDureeMin() { return dureeMin; }
    public void setDureeMin(int dureeMin) { this.dureeMin = dureeMin; }

    public double getChargeTotale() { return chargeTotale; }
    public void setChargeTotale(double chargeTotale) { this.chargeTotale = chargeTotale; }

    public int getNbSeries() { return nbSeries; }
    public void setNbSeries(int nbSeries) { this.nbSeries = nbSeries; }

    public int getNbRepetitions() { return nbRepetitions; }
    public void setNbRepetitions(int nbRepetitions) { this.nbRepetitions = nbRepetitions; }

    public String getPhotoFin() { return photoFin; }
    public void setPhotoFin(String photoFin) { this.photoFin = photoFin; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public List<Exercice> getExercices() { return exercices; }
    public void setExercices(List<Exercice> exercices) { this.exercices = exercices; }

    public void ajouterExercice(Exercice e) {
        exercices.add(e);
    }
}
