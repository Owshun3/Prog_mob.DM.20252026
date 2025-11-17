package com.example.faza.data.managers;

import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Programme;

import java.util.ArrayList;
public class ManagerEntrainement {

    private final ArrayList<Entrainement> entrainements = new ArrayList<>();
    private long nextId = 1;

    public Entrainement creerDepuisProgramme(Programme p) {
        Entrainement e = new Entrainement();
        e.setId(nextId++);
        e.setProgramme(p);
        entrainements.add(e);
        return e;
    }

    public Entrainement creerVierge() {
        Entrainement e = new Entrainement();
        e.setId(nextId++);
        entrainements.add(e);
        return e;
    }

    public Entrainement getById(long id) {
        for (Entrainement e : entrainements) if (e.getId() == id) return e;
        return null;
    }

    public ArrayList<Entrainement> getAll() {
        return entrainements;
    }

    public void supprimer(Entrainement e) {
        entrainements.remove(e);
    }
}
