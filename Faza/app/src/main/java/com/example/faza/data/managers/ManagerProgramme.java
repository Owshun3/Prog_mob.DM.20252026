package com.example.faza.data.managers;

import com.example.faza.data.entites.Programme;

import java.util.ArrayList;

public class ManagerProgramme {
    private ArrayList<Programme> programmes;

    public ManagerProgramme(){
        this.programmes = new ArrayList<>();
    }
    public ArrayList<Programme> getProgrammes(){
        return this.programmes;
    }
    public void setProgrammes(ArrayList<Programme> programmes){
        this.programmes = programmes;
    }

    public void ajouterProgramme(Programme p) {
        programmes.add(p);
    }

    public boolean supprimerProgramme(Programme p) {
        return programmes.remove(p);
    }

    public Programme getProgrammeParId(long id) {
        for (Programme p : programmes) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public void modifierProgrammeParId(long id, Programme nouveau) {
        for (int i = 0; i < programmes.size(); i++) {
            if (programmes.get(i).getId() == id) {
                programmes.set(i, nouveau);
                return;
            }
        }
    }
}
