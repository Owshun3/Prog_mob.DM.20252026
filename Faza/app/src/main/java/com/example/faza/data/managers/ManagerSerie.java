package com.example.faza.data.managers;

import com.example.faza.data.entites.Serie;

import java.util.ArrayList;

public class ManagerSerie {
    private ArrayList<Serie> series;

    public ManagerSerie() {
        this.series = new ArrayList<>();
    }

    public ArrayList<Serie> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<Serie> series) {
        if (series == null) throw new IllegalArgumentException("Liste de séries nulle");
        this.series = series;
    }

    public void ajouterSerie(Serie s) {
        if (s == null) throw new IllegalArgumentException("Série nulle");
        series.add(s);
    }

    public boolean supprimerSerie(Serie s) {
        return series.remove(s);
    }

    public Serie getSerieById(long id) {
        for (Serie s : series) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    public void modifierSerieParId(long id, Serie nouvelle) {
        for (int i = 0; i < series.size(); i++) {
            if (series.get(i).getId() == id) {
                series.set(i, nouvelle);
                return;
            }
        }
    }
}
