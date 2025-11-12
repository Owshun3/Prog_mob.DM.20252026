package com.example.faza.data.entites;

import java.util.ArrayList;
import java.util.List;

public class Programme {
    private long id;
    private long idUser;
    private String nom;
    private double chargeTotale;
    private int nbSeries;
    private int nbRepetitions;
    private String photoFin;
    private String commentaire;

    private List<Exercice> exercices;

    public Programme() {
        exercices = new ArrayList<>();
    }

    public Programme(long idUser, String nom, String commentaire) {
        this.idUser = idUser;
        this.nom = nom;
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
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getChargeTotale() { return chargeTotale; }
    public void setChargeTotale(double chargeTotale) { this.chargeTotale = chargeTotale; }
    public int getNbSeries() { return nbSeries; }
    public void setNbSeries(int nbSeries) { this.nbSeries = nbSeries; }
    public int getNbRepetitions() { return nbRepetitions; }
    public void setNbRepetitions(int nbRepetitions) { this.nbRepetitions = nbRepetitions; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public List<Exercice> getExercices() { return exercices; }
    public void setExercices(List<Exercice> exercices) { this.exercices = exercices; }

    public void ajouterExercice(Exercice e) {
        exercices.add(e);
    }

    public String toCSV() {
        if (id == -1 || nom == null) {
            throw new IllegalArgumentException("Erreur: toCSV() d’un programme invalide (id ou nom manquant)");
        }
        return id + ";" +
                idUser + ";" +
                nom + ";" +
                chargeTotale + ";" +
                nbSeries + ";" +
                nbRepetitions + ";" +
                (photoFin != null ? photoFin : "") + ";" +
                (commentaire != null ? commentaire : "");
    }

    public static Programme fromCSV(String line) {
        String[] champs = line.split(";", -1);
        if (champs.length != 8) {
            throw new IllegalArgumentException("Erreur: ligne CSV invalide pour Programme (8 champs attendus): " + line);
        }

        try {
            Programme p = new Programme();
            p.setId(Long.parseLong(champs[0]));
            p.setIdUser(Long.parseLong(champs[1]));
            p.setNom(champs[2]);
            p.setChargeTotale(Double.parseDouble(champs[3]));
            p.setNbSeries(Integer.parseInt(champs[4]));
            p.setNbRepetitions(Integer.parseInt(champs[5]));
            p.photoFin = champs[6];
            p.setCommentaire(champs[7]);
            return p;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Erreur de parsing dans la ligne CSV pour Programme: " + line, e);
        }
    }

}
