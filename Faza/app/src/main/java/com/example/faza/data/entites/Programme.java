package com.example.faza.data.entites;

import java.util.ArrayList;
import java.util.List;

public class Programme {

    private long id;
    private long idUser;
    private String nom;
    private String commentaire;

    private double chargeTotale;
    private int nbSeries;
    private int nbRepetitions;

    private final List<Exercice> exercices = new ArrayList<>();

    public Programme() {}

    public Programme copieDeep() {
        Programme p = new Programme();
        p.nom = nom;
        p.commentaire = commentaire;
        for (Exercice e : exercices) {
            p.exercices.add(e.copie());
        }
        return p;
    }

    public void recalculerStats() {
        chargeTotale = 0;
        nbSeries = 0;
        nbRepetitions = 0;

        for (Exercice e : exercices) {
            for (Serie s : e.getSeries()) {
                chargeTotale += s.getPoids() * s.getRepetitions();
                nbSeries++;
                nbRepetitions += s.getRepetitions();
            }
        }
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

    public void ajouterExercice(Exercice e) { exercices.add(e); }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdUser() { return idUser; }
    public void setIdUser(long idUser) { this.idUser = idUser; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public double getChargeTotale() { return chargeTotale; }
    public void setChargeTotale(double chargeTotale) {
        this.chargeTotale = chargeTotale;
    }

    public int getNbSeries() { return nbSeries; }
    public void setNbSeries(int nbSeries) {
        this.nbSeries = nbSeries;
    }
    public int getNbRepetitions() { return nbRepetitions; }
    public void setNbRepetitions(int nbRepetitions) {
        this.nbRepetitions = nbRepetitions;
    }

    public List<Exercice> getExercices() { return exercices; }
}
