package com.example.faza.data.entites;

import java.util.ArrayList;
import java.util.List;

public class Exercice {

    private long id;
    private long idProgramme;
    private long idCatalogue;

    private String nom;
    private String groupePrincipal;
    private String groupeSecondaire;
    private String miniature;
    private String urlVideo;
    private boolean copie = false;
    private boolean uiExpanded = false;

    private final List<Serie> series = new ArrayList<>();

    public Exercice() {}

    public Exercice copie() {
        Exercice e = new Exercice();
        e.setIdCatalogue(this.idCatalogue);
        e.nom = nom;
        e.groupePrincipal = groupePrincipal;
        e.groupeSecondaire = groupeSecondaire;
        e.miniature = miniature;
        e.urlVideo = urlVideo;
        e.copie = true;
        for (Serie s : series) {
            e.series.add(s.copie());
        }

        return e;
    }

    public void toggleExpanded() {
        uiExpanded = !uiExpanded;
    }

    public String getResume() {
        if (series.isEmpty()) return "";

        int minRep = series.get(0).getRepetitions();
        int maxRep = minRep;
        double minP = series.get(0).getPoids();
        double maxP = minP;

        for (Serie s : series) {
            int r = s.getRepetitions();
            double p = s.getPoids();

            if (r < minRep) minRep = r;
            if (r > maxRep) maxRep = r;
            if (p < minP) minP = p;
            if (p > maxP) maxP = p;
        }

        return series.size() + " séries - " +
                minRep + "-" + maxRep + " reps - " +
                minP + "-" + maxP + " kg";
    }

    public void ajouterSerie(Serie s) {
        if (!copie) {
            throw new IllegalStateException("Impossible de modifier un exercice non-copié");
        }
        series.add(s);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdProgramme() { return idProgramme; }
    public void setIdProgramme(long idProgramme) { this.idProgramme = idProgramme; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getGroupePrincipal() { return groupePrincipal; }
    public void setGroupePrincipal(String groupePrincipal) { this.groupePrincipal = groupePrincipal; }

    public String getGroupeSecondaire() { return groupeSecondaire; }
    public void setGroupeSecondaire(String groupeSecondaire) { this.groupeSecondaire = groupeSecondaire; }

    public String getMiniature() { return miniature; }
    public void setMiniature(String miniature) { this.miniature = miniature; }

    public String getUrlVideo() { return urlVideo; }
    public void setUrlVideo(String urlVideo) { this.urlVideo = urlVideo; }

    public long getIdCatalogue() { return idCatalogue; }
    public void setIdCatalogue(long idCatalogue) { this.idCatalogue = idCatalogue; }

    public boolean isUiExpanded() { return uiExpanded; }
    public boolean isCopie(){return copie;}
    public void setEstUneCopie(boolean b){this.copie = b;}
    public List<Serie> getSeries() { return series; }
}
