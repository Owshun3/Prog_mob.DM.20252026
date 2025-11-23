package com.example.faza.data.entites;

public class Serie {
    private long id;
    private double poids;
    private int repetitions;
    private int rir;
    private boolean validee;

    public Serie(double poids, int repetitions) {
        this.id = -1;
        this.poids = poids;
        this.repetitions = repetitions;
        this.rir = -1;
        this.validee = false;
    }

    public Serie(long id, double poids, int repetitions, int rir, boolean validee) {
        this.id = id;
        this.poids = poids;
        this.repetitions = repetitions;
        this.rir = rir;
        this.validee = validee;
    }

    public void valider(){
        validee = !validee;
    }

    public String toCSV(){
        if(id == -1){
            throw new IllegalArgumentException("Erreur: toCSV() d'une série sans id");
        }
        return id+";"+poids+";"+repetitions+";"+rir+";"+validee;
    }

    public static Serie fromCSV(String line){
        String[] attributs = line.split(";");
        if (attributs.length != 5) {
            throw new IllegalArgumentException("Erreur: ligne CSV invalide pour " +
                    "Serie (attendu 5 champs): " + line);
        }
        try {
            long id = Long.parseLong(attributs[0]);
            double poids = Double.parseDouble(attributs[1]);
            int repetitions = Integer.parseInt(attributs[2]);
            int rir = Integer.parseInt(attributs[3]);
            boolean validee = Boolean.parseBoolean(attributs[4]);
            return new Serie(id, poids, repetitions, rir, validee);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Erreur de format dans la ligne " +
                    "CSV pour Serie: " + line, e);
        }
    }
    public long getId() { return id; } public void setId(long id) { this.id = id; }
    public double getPoids() { return poids; } public void setPoids(double poids) { this.poids = poids; }
    public int getRepetitions() { return repetitions; } public void setRepetitions(int repetitions) { this.repetitions = repetitions; }
    public int getRir() { return rir; } public void setRir(int rir) { this.rir = rir; }
    public boolean isValidee() { return validee; } public void setValidee(boolean validee) { this.validee = validee; }


}
