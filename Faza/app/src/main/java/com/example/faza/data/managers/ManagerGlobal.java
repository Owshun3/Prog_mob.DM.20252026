package com.example.faza.data.managers;

import android.content.Context;

import com.example.faza.R;
import com.example.faza.data.Service.ExerciceCsvLoader;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ManagerGlobal {

    private static ManagerGlobal instance;

    private final ManagerProgramme managerProgramme;
    private final ManagerEntrainement managerEntrainement;
    private final ManagerExercice managerExercice;
    private final ManagerSerie managerSerie;

    public ManagerGlobal(Context ctx, User u) {
        managerProgramme = new ManagerProgramme(ctx);
        managerEntrainement = new ManagerEntrainement();
        managerExercice = new ManagerExercice();
        managerSerie = new ManagerSerie();
        chargerExercicesDepuisCSV(ctx);
    }

    public static void initialize(Context ctx, User u) {
        if (instance == null) instance = new ManagerGlobal(ctx, u);
    }

    private void chargerExercicesDepuisCSV(Context ctx) {
        try {
            InputStream is = ctx.getResources().openRawResource(R.raw.exercices);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(";");
                if (cols.length < 4) continue;
                Exercice e = new Exercice();
                e.setNom(cols[0].trim());
                e.setGroupePrincipal(cols[1].trim());
                e.setGroupeSecondaire(cols[2].trim());
                e.setMiniature(cols[3].trim());
                //videos à faire plus tard
                managerExercice.ajouterSiAbsent(e);
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ManagerGlobal getInstance() {
        return instance;
    }

    public ManagerProgramme getManagerProgramme() { return managerProgramme; }
    public ManagerEntrainement getManagerEntrainement() { return managerEntrainement; }
    public ManagerExercice getManagerExercice() { return managerExercice; }
    public ManagerSerie getManagerSerie() { return managerSerie; }
}

