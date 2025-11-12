package com.example.faza.data.entites;

import java.util.ArrayList;
import java.util.List;

public class Exercice {
    private long id;
    private String nom;
    private String groupePrincipal;
    private String groupeSecondaire;
    private String description;
    private int restPause;
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

    public String getDescription(){ return this.description; }
    public void setDescription(String value){ this.description = description; }
    public String getGroupePrincipal() {
        return groupePrincipal;
    }
    public void setGroupePrincipal(String groupePrincipal) {
        this.groupePrincipal = groupePrincipal;
    }
    public String getGroupeSecondaire() {
        return groupeSecondaire;
    }
    public void setGroupeSecondaire(String groupeSecondaire) {
        this.groupeSecondaire = groupeSecondaire;
    }

    public String toCSV() {
        if (id == -1 || nom == null) {
            throw new IllegalArgumentException("Erreur: toCSV() d’un exercice invalide (id ou nom manquant)");
        }
        return id + ";" +
                nom + ";" +
                (groupePrincipal != null ? groupePrincipal : "") + ";" +
                (groupeSecondaire != null ? groupeSecondaire : "") + ";" +
                (description != null ? description : "") + ";" +
                restPause + ";" +
                (urlVideo != null ? urlVideo : "") + ";" +
                (miniature != null ? miniature : "");
    }

    public static Exercice fromCSV(String line) {
        String[] champs = line.split(";", -1); // -1 pour garder les champs vides
        if (champs.length != 8) {
            throw new IllegalArgumentException("Erreur: ligne CSV invalide pour Exercice (8 champs attendus): " + line);
        }

        try {
            Exercice exercice = new Exercice();
            exercice.setId(Long.parseLong(champs[0]));
            exercice.setNom(champs[1]);
            exercice.setGroupePrincipal(champs[2]);
            exercice.setGroupeSecondaire(champs[3]);
            exercice.setDescription(champs[4]);
            exercice.restPause = Integer.parseInt(champs[5]);
            exercice.urlVideo = champs[6];
            exercice.miniature = champs[7];
            return exercice;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Erreur de parsing dans la ligne CSV pour Exercice: " + line, e);
        }
    }

}
