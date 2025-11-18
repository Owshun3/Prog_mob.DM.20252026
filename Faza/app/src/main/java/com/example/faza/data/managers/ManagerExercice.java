package com.example.faza.data.managers;

import com.example.faza.data.entites.Exercice;

import java.util.ArrayList;

public class ManagerExercice {
    private ArrayList<Exercice> exercices;

    public ManagerExercice() {
        this.exercices = new ArrayList<>();
    }

    public ArrayList<Exercice> getExercices() {
        return exercices;
    }

    public void setExercices(ArrayList<Exercice> exercices) {
        if (exercices == null) throw new IllegalArgumentException("Liste d'exercices nulle");
        this.exercices = exercices;
    }

    public void ajouterExercice(Exercice e) {
        if (e == null) throw new IllegalArgumentException("Exercice nul");
        exercices.add(e);
    }

    public boolean supprimerExercice(Exercice e) {
        return exercices.remove(e);
    }

    public Exercice getExerciceById(long id) {
        for (Exercice e : exercices) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public void modifierExerciceById(long id, Exercice nouveau) {
        for (int i = 0; i < exercices.size(); i++) {
            if (exercices.get(i).getId() == id) {
                exercices.set(i, nouveau);
                return;
            }
        }
    }
}
