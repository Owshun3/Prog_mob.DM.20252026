package com.example.faza.data.managers;

import android.content.Context;

import com.example.faza.data.DatabaseHelper;
import com.example.faza.data.entites.User;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.entites.Entrainement;

import java.util.ArrayList;

public class ManagerGlobal {

    private static ManagerGlobal instance;

    private final ManagerProgramme managerProgramme;
    private final ManagerEntrainement managerEntrainement;
    private final ManagerExercice managerExercice;
    private final ManagerSerie managerSerie;

    public ManagerGlobal(Context ctx, User u) {
        managerProgramme = new ManagerProgramme();
        managerEntrainement = new ManagerEntrainement();
        managerExercice = new ManagerExercice();
        managerSerie = new ManagerSerie();
    }

    public static void initialize(Context ctx, User u) {
        if (instance == null) instance = new ManagerGlobal(ctx, u);
    }

    public static ManagerGlobal getInstance() {
        return instance;
    }

    public ManagerProgramme getManagerProgramme() { return managerProgramme; }
    public ManagerEntrainement getManagerEntrainement() { return managerEntrainement; }
    public ManagerExercice getManagerExercice() { return managerExercice; }
    public ManagerSerie getManagerSerie() { return managerSerie; }
}

