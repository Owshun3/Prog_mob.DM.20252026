package com.example.faza.data.managers;

import com.example.faza.data.entites.Entrainement;

import java.util.ArrayList;

public class ManagerEntrainement {
    private ArrayList<Entrainement> entrainements;

    public ManagerEntrainement(){
        this.entrainements = new ArrayList<>();
    }
    public ArrayList<Entrainement> getEntrainements(){
        return this.entrainements;
    }
    public void setEntrainements(ArrayList<Entrainement> entrainements){
        this.entrainements = entrainements;
    }

    public void ajouterEntrainement(Entrainement e) {
        entrainements.add(e);
    }

    public boolean supprimerEntrainement(Entrainement e) {
        return entrainements.remove(e);
    }

    public Entrainement getEntrainementParId(long id) {
        for (Entrainement e : entrainements) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public void modifierEntrainementParId(long id, Entrainement nouveau) {
        for (int i = 0; i < entrainements.size(); i++) {
            if (entrainements.get(i).getId() == id) {
                entrainements.set(i, nouveau);
                return;
            }
        }
    }
}
