package com.example.faza.data.entites;

import java.util.ArrayList;
import java.util.List;

public class Exercice {
    private String nom;
    private String groupePrincipal = "Inconnu";
    private String groupeSecondaire = "Inconnu";
    private String description = "";
    private int restPause = 0;
    private String urlVideo = "";
    private String miniature = "";
    private boolean copie = false;

    private List<Serie> series = new ArrayList<>();

    public Exercice() {
    }

    public String getNom() { return nom; }
    public void setNom(String n) { nom = n; }

    public String getGroupePrincipal() { return groupePrincipal; }
    public void setGroupePrincipal(String gp) { groupePrincipal = gp; }

    public String getDescription(){return description;}
    public void setDescription(String d){description = d;}

    public String getGroupeSecondaire() { return groupeSecondaire; }
    public void setGroupeSecondaire(String gs) { groupeSecondaire = gs; }

    public String getMiniature() { return miniature; }
    public void setMiniature(String m) { miniature = m; }

    public String getUrlVideo() { return urlVideo; }
    public void setUrlVideo(String u) { urlVideo = u; }

    public List<Serie> getSeries() { return series; }
    public void setSeries(List<Serie> series){this.series = series;}

    public boolean isCopie() {
        return copie;
    }
    public void setEstUneCopie(boolean b){copie = b;}

    public Exercice copie() {
        Exercice e = new Exercice();
        e.setNom(this.getNom());
        e.setDescription(this.getDescription());
        e.setGroupePrincipal(this.getGroupePrincipal());
        e.setGroupeSecondaire(this.getGroupeSecondaire());
        e.setMiniature(this.getMiniature());
        e.setUrlVideo(this.getUrlVideo());
        e.setEstUneCopie(true);

        ArrayList<Serie> nouvellesSeries = new ArrayList<>();
        for (Serie s : this.getSeries()) {
            Serie cs = new Serie(s.getPoids(), s.getRepetitions());
            cs.setRir(s.getRir());
            cs.setValidee(false);
            nouvellesSeries.add(cs);
        }
        e.setSeries(nouvellesSeries);

        return e;
    }

    public void ajouterSerie(Serie s){
        if(!isCopie()){
            throw new IllegalArgumentException("On ne peut pas directement modifier un exercice");
        }
        series.add(s);
    }

    public String getResume() {
        int nb = series.size();
        if (nb == 0) return "";
        int minRep = series.get(0).getRepetitions();
        int maxRep = minRep;
        double minPoids = series.get(0).getPoids();
        double maxPoids = minPoids;
        for (Serie s : series) {
            int r = s.getRepetitions();
            if (r < minRep) minRep = r;
            if (r > maxRep) maxRep = r;
            double p = s.getPoids();
            if (p < minPoids) minPoids = p;
            if (p > maxPoids) maxPoids = p;
        }
        return nb + " séries - " + minRep + "-" + maxRep + " reps - " + minPoids + "-" + maxPoids + " kg";
    }

}