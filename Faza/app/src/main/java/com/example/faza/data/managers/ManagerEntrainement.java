package com.example.faza.data.managers;

import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.entites.Serie;

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
        Programme p = new Programme();
        p.setNom("Entraînement libre");
        e.setProgramme(p);
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

    private Programme copierProgrammePourEntrainement(Programme source) {
        Programme p = new Programme();
        p.setNom(source.getNom());
        ArrayList<Exercice> exos = new ArrayList<>();
        for (Exercice ex : source.getExercices()) {
            Exercice copieEx = new Exercice();
            copieEx.setNom(ex.getNom());
            copieEx.setDescription(ex.getDescription());
            copieEx.setGroupePrincipal(ex.getGroupePrincipal());
            copieEx.setGroupeSecondaire(ex.getGroupeSecondaire());
            copieEx.setSeries(new ArrayList<>());
            for (Serie s : ex.getSeries()) {
                Serie copieS = new Serie(s.getPoids(), s.getRepetitions());
                copieS.setRir(s.getRir());
                copieS.setValidee(false);
                copieEx.ajouterSerie(copieS);
            }
            exos.add(copieEx);
        }
        p.setExercices(exos);
        return p;
    }
}
