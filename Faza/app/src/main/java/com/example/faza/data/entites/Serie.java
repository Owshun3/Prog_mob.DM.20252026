package com.example.faza.data.entites;

public class Serie {

    private long id;
    private long idExercice;

    private double poids;
    private int repetitions;
    private int rir;
    private boolean validee;

    public Serie() {}

    public Serie(double poids, int repetitions) {
        this.poids = poids;
        this.repetitions = repetitions;
        this.rir = -1;
        this.validee = false;
    }

    public Serie copie() {
        Serie s = new Serie();
        s.poids = this.poids;
        s.repetitions = this.repetitions;
        s.rir = this.rir;
        s.validee = false;
        return s;
    }

    public void toggleValidee() {
        validee = !validee;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdExercice() { return idExercice; }
    public void setIdExercice(long idExercice) { this.idExercice = idExercice; }

    public double getPoids() { return poids; }
    public void setPoids(double poids) { this.poids = poids; }

    public int getRepetitions() { return repetitions; }
    public void setRepetitions(int repetitions) { this.repetitions = repetitions; }

    public int getRir() { return rir; }
    public void setRir(int rir) { this.rir = rir; }

    public boolean isValidee() { return validee; }
    public void setValidee(boolean validee) { this.validee = validee; }
}
