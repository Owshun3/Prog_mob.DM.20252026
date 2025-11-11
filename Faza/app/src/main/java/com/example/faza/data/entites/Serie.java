package com.example.faza.data.entites;

public class Serie {
    private long id;
    private double poids;
    private int repetitions;
    private int rir;
    private int restPause;
    private boolean validee;

    public Serie(double poids, int repetitions) {
        this.poids = poids;
        this.repetitions = repetitions;
    }

    public double getPoids() { return poids; }
    public int getRepetitions() { return repetitions; }
}
