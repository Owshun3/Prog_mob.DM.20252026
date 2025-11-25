package com.example.faza.data.managers;

import com.example.faza.data.entites.Exercice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManagerExercice {

    private long nextCatalogueId = 1;

    private final HashMap<String, Exercice> exercices = new HashMap<>();

    public void clear() {
        exercices.clear();
    }

    public void ajouterSiAbsent(Exercice e) {
        if (!exercices.containsKey(e.getNom())) {
            e.setIdCatalogue(nextCatalogueId++);
            exercices.put(e.getNom(), e);
        }
    }

    public ArrayList<Exercice> getTous() {
        return new ArrayList<>(exercices.values());
    }

    public ArrayList<Exercice> rechercher(String texte) {
        ArrayList<Exercice> res = new ArrayList<>();
        if (texte == null) {
            res.addAll(getTous());
            return res;
        }
        String t = texte.toLowerCase();
        for (Exercice e : exercices.values()) {
            String nom = e.getNom();
            if (nom != null && nom.toLowerCase().contains(t)) {
                res.add(e);
            }
        }
        return res;
    }


}
