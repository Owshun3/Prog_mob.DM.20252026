package com.example.faza.data.managers;

import com.example.faza.data.entites.Exercice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManagerExercice {

    private final HashMap<String, Exercice> exercices = new HashMap<>();

    public void clear() {
        exercices.clear();
    }

    public void ajouterSiAbsent(Exercice e) {
        if (!exercices.containsKey(e.getNom())) {
            exercices.put(e.getNom(), e);
        }
    }

    public ArrayList<Exercice> getTous() {
        return new ArrayList<>(exercices.values());
    }

    public ArrayList<Exercice> rechercher(String texte) {
        ArrayList<Exercice> res = new ArrayList<>();
        String t = texte.toLowerCase();

        for (Exercice e : exercices.values()) {
            if (e.getNom().toLowerCase().contains(t)) {
                res.add(e);
            }
        }
        return res;
    }

}
