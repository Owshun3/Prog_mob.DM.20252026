package com.example.faza;

import java.util.Date;

public class User {
    private String pseudo;
    private Date naissance;
    private int taille;
    private float poids;
    private String unite;
    private String theme;
    private String photoUri;

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_THEME = "theme";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_UNIT = "unit";


    public User() {}

    public User(String pseudo, Date naissance, int taille, float poids,
                String unite, String theme, String photoUri) {
        this.pseudo = pseudo;
        this.naissance = naissance;
        this.taille = taille;
        this.poids = poids;
        this.unite = unite;
        this.theme = theme;
        this.photoUri = photoUri;
    }

    public String getPseudo() {return pseudo;}
    public Date getNaissance() {return naissance;}
    public int getTaille() {return taille;}
    public float getPoids() {return poids;}
    public String getUnite() {return unite;}
    public String getTheme() {return theme;}
    public String getPhotoUri() {return photoUri;}

    public void setPseudo(String pseudo) {this.pseudo = pseudo;}
    public void setNaissance(Date naissance) {this.naissance = naissance;}
    public void setTaille(int taille) {this.taille = taille;}
    public void setPoids(float poids) {this.poids = poids;}
    public void setUnite(String unite) {this.unite = unite;}
    public void setTheme(String theme) {this.theme = theme;}
    public void setPhotoUri(String photoUri) {this.photoUri = photoUri;}
}
