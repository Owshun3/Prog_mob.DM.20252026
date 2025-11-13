package com.example.faza.data.managers;

import android.content.Context;

import com.example.faza.data.DatabaseHelper;
import com.example.faza.data.entites.User;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.entites.Entrainement;

import java.util.ArrayList;

public class ManagerGlobal {
    private static ManagerGlobal instance;
    private final Context context;
    private final DatabaseHelper dbHelper;
    private User user;
    private final ManagerProgramme managerProgramme = new ManagerProgramme();
    private final ManagerExercice managerExercice = new ManagerExercice();
    private final ManagerSerie managerSerie = new ManagerSerie();
    public ManagerGlobal(Context context, User user) {
        this.context = context.getApplicationContext();
        this.dbHelper = DatabaseHelper.getInstance(context);
        this.user = user;
    }
    public static synchronized void intialize(Context context, User user){
        if(instance == null){
            instance = new ManagerGlobal(context, user);
        }
    }
    public static ManagerGlobal getInstance(){
        if(instance == null){
            throw new IllegalStateException("ManagerGlobal non initialisé !");
        }
        return instance;
    }

    public ManagerProgramme getManagerProgramme() { return managerProgramme; }
    public ManagerExercice getManagerExercice() { return managerExercice; }
    public ManagerSerie getManagerSerie() { return managerSerie; }

    /*
    public String toCSV() {
        StringBuilder sb = new StringBuilder();

        if (user != null) {
            sb.append("User\n");
            sb.append(user.toCSV()).append("\n");
        }

        if (programmes != null) {
            for (Programme p : programmes) {
                sb.append("Programme\n");
                sb.append(p.toCSV()).append("\n");
            }
        }

        if (entrainements != null) {
            for (Entrainement e : entrainements) {
                sb.append("Entrainement\n");
                sb.append(e.toCSV()).append("\n");
            }
        }

        return sb.toString();
    }

    public void fromCSV(List<String> lignes) {
        Programme currentProgramme = null;
        Entrainement currentEntrainement = null;

        for (String ligne : lignes) {
            switch (ligne) {
                case "User":
                    currentProgramme = null;
                    currentEntrainement = null;
                    break;
                case "Programme":
                    currentProgramme = new Programme();
                    programmes.add(currentProgramme);
                    break;
                case "Entrainement":
                    currentEntrainement = new Entrainement();
                    entrainements.add(currentEntrainement);
                    break;
                default:
                    if (currentProgramme != null) {
                        if (ligne.startsWith("Exercice")) continue;
                        currentProgramme.ajouterExercice(Exercice.fromCSV(ligne));
                    } else if (currentEntrainement != null) {
                        if (ligne.startsWith("Exercice")) continue;
                        currentEntrainement.ajouterExercice(Exercice.fromCSV(ligne));
                    } else if (ligne.contains(";")) {
                        user = User.fromCSV(ligne);
                    }
            }
        }
    }
    */

}
