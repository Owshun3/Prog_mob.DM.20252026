package com.example.faza.data.entites;

public class Entrainement {

    private long id;

    private String dateSeance;
    private int dureeMin;
    private String photoFin;

    private Programme programme;

    private double chargeTotale;
    private int nbSeries;
    private int nbRepetitions;

    public Entrainement() {}

    public void recalculerStats() {
        if (programme == null) return;
        programme.recalculerStats();
        chargeTotale = programme.getChargeTotale();
        nbSeries = programme.getNbSeries();
        nbRepetitions = programme.getNbRepetitions();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDateSeance() { return dateSeance; }
    public void setDateSeance(String dateSeance) { this.dateSeance = dateSeance; }

    public int getDureeMin() { return dureeMin; }
    public void setDureeMin(int dureeMin) { this.dureeMin = dureeMin; }

    public String getPhotoFin() { return photoFin; }
    public void setPhotoFin(String photoFin) { this.photoFin = photoFin; }

    public Programme getProgramme() { return programme; }
    public void setProgramme(Programme programme) { this.programme = programme; }

    public double getChargeTotale() { return chargeTotale; }
    public int getNbSeries() { return nbSeries; }
    public int getNbRepetitions() { return nbRepetitions; }
}
