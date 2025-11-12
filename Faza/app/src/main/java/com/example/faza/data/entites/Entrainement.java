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

    @Override
    public String toCSV() {
        if (id == -1 || getNom() == null || dateSeance == null) {
            throw new IllegalArgumentException("Erreur: toCSV() d’un entrainement invalide (id, nom ou date manquante)");
        }
        return id + ";" +
                getIdUser() + ";" +
                getNom() + ";" +
                getChargeTotale() + ";" +
                getNbSeries() + ";" +
                getNbRepetitions() + ";" +
                (photoFin != null ? photoFin : "") + ";" +
                (getCommentaire() != null ? getCommentaire() : "") + ";" +
                dateSeance + ";" +
                dureeMin;
    }

    public static Entrainement fromCSV(String line) {
        String[] champs = line.split(";", -1);
        if (champs.length != 10) {
            throw new IllegalArgumentException("Erreur: ligne CSV invalide pour Entrainement (10 champs attendus): " + line);
        }

        try {
            Entrainement e = new Entrainement();
            e.setId(Long.parseLong(champs[0]));
            e.setIdUser(Long.parseLong(champs[1]));
            e.setNom(champs[2]);
            e.setChargeTotale(Double.parseDouble(champs[3]));
            e.setNbSeries(Integer.parseInt(champs[4]));
            e.setNbRepetitions(Integer.parseInt(champs[5]));
            e.setPhotoFin(champs[6]);
            e.setCommentaire(champs[7]);
            e.setDateSeance(champs[8]);
            e.setDureeMin(Integer.parseInt(champs[9]));
            return e;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Erreur de parsing dans la ligne CSV pour Entrainement: " + line, ex);
        }
    }

}
