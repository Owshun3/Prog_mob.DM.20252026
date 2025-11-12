package com.example.faza.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.faza.data.DatabaseHelper;
import com.example.faza.data.DBAdapter;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.User;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.entites.Entrainement;

import java.util.ArrayList;
import java.util.List;

public class ManagerGlobal {
    private final DatabaseHelper dbHelper;
    private ArrayList<Programme> programmes;
    private ArrayList<Entrainement> entrainements;
    private User user;

    public ManagerGlobal(Context context, User user) {
        this.dbHelper = DatabaseHelper.getInstance(context);
        this.user = user;
        this.programmes = new ArrayList<>();
        this.entrainements = new ArrayList<>();
    }

    public ArrayList<Programme> getProgrammes(){
        return this.programmes;
    }
    public void setProgrammes(ArrayList<Programme> programmes){
        this.programmes = programmes;
    }

    public ArrayList<Entrainement> getEntrainements(){
        return this.entrainements;
    }
    public void setEntrainements(ArrayList<Entrainement> entrainements){
        this.entrainements = entrainements;
    }

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

}
