package com.example.faza.ui.Onboarding;

import java.util.Date;

public class OnboardingData {
    private static OnboardingData instance;
    public static OnboardingData getInstance() {
        if (instance == null) instance = new OnboardingData();
        return instance;
    }

    private String genre;
    private String pseudo;
    private Date date_naissance;
    private float poids;
    private int taille;

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getPseudo() { return pseudo; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }

    public Date getNaissance() { return date_naissance; }
    public void setNaissance(Date naissance) { this.date_naissance = naissance; }

    public float getPoids() { return poids; }
    public void setPoids(float poids) { this.poids = poids; }

    public int getTaille() {return taille;}
    public void setTaille(int taille) {this.taille = taille;}
}