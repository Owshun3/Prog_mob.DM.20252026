package com.example.faza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Programme {
    private long id;
    private String nom;
    private String description;
    private String dateCreation;
    private List<Exercice> exercices;

    public Programme() {
        exercices = new ArrayList<>();
    }

    public Programme(String nom, String description, String dateCreation) {
        this.nom = nom;
        this.description = description;
        this.dateCreation = dateCreation;
        this.exercices = new ArrayList<>();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }

    public List<Exercice> getExercices() { return exercices; }
    public void setExercices(List<Exercice> exercices) { this.exercices = exercices; }

    public void ajouterExercice(Exercice e) { exercices.add(e); }

    public long insert(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("description", description);
        values.put("date_creation", dateCreation);

        long programmeId = db.insert("programme", null, values);
        this.id = programmeId;

        for (Exercice e : exercices) {
            ContentValues exoValues = new ContentValues();
            exoValues.put("id_programme", programmeId);
            exoValues.put("id_exercice", e.getId());
            exoValues.put("nb_series_defaut", 3);
            exoValues.put("nb_reps_defaut", 10);
            exoValues.put("poids_min", 0);
            exoValues.put("poids_max", 0);
            db.insert("programme_exercice", null, exoValues);
        }

        db.close();
        return programmeId;
    }

    public static Programme getById(Context context, long id) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Programme programme = null;
        Cursor cursor = db.rawQuery(
                "SELECT id, nom, description, date_creation FROM programme WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst()) {
            programme = new Programme();
            programme.id = cursor.getLong(0);
            programme.nom = cursor.getString(1);
            programme.description = cursor.getString(2);
            programme.dateCreation = cursor.getString(3);
            programme.exercices = getExercicesForProgramme(db, id);
        }
        cursor.close();
        db.close();
        return programme;
    }

    public static List<Programme> getAll(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Programme> programmes = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT id, nom, description, date_creation FROM programme", null);

        while (cursor.moveToNext()) {
            Programme p = new Programme();
            p.id = cursor.getLong(0);
            p.nom = cursor.getString(1);
            p.description = cursor.getString(2);
            p.dateCreation = cursor.getString(3);
            p.exercices = getExercicesForProgramme(db, p.id);
            programmes.add(p);
        }
        cursor.close();
        db.close();
        return programmes;
    }

    public void update(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("description", description);
        values.put("date_creation", dateCreation);

        db.update("programme", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void delete(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("programme_exercice", "id_programme = ?", new String[]{String.valueOf(id)});
        db.delete("programme", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    private static List<Exercice> getExercicesForProgramme(SQLiteDatabase db, long programmeId) {
        List<Exercice> exercices = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT e.id, e.nom, e.description, e.groupe_principal, e.groupe_secondaire " +
                        "FROM exercice e " +
                        "JOIN programme_exercice pe ON e.id = pe.id_exercice " +
                        "WHERE pe.id_programme = ?",
                new String[]{String.valueOf(programmeId)}
        );

        while (cursor.moveToNext()) {
            Exercice e = new Exercice();
            e.setId(cursor.getLong(0));
            e.setNom(cursor.getString(1));
            e.setDescription(cursor.getString(2));
            e.setGroupePrincipal(cursor.getString(3));
            e.setGroupeSecondaire(cursor.getString(4));
            exercices.add(e);
        }
        cursor.close();
        return exercices;
    }
}

