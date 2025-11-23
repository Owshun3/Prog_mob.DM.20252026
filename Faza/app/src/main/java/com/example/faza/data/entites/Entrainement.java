package com.example.faza.data.entites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.faza.data.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class Entrainement{
    private long id;
    private String nom;
    private String dateTexte;
    private String dateSeance;
    private int dureeMin;
    private String photoFin;
    private Programme programme;

    private double chargeTotale;
    private int nbSeries;
    private int nbRepetitions;

    public Entrainement(long id,String dateSeance, int dureeMin,
                        String photoFin, Programme programme) {
        this.id = id;
        this.dateSeance = dateSeance;
        this.dureeMin = dureeMin;
        this.photoFin = photoFin;
        this.programme = programme;
    }

    public Entrainement() {
        this.programme = new Programme();
        this.photoFin = "";
    }

    public static List<Entrainement> getAll(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Entrainement> list = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ENTRAINEMENT,
                null, null, null, null, null, "date_seance DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Entrainement e = new Entrainement();
                e.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                e.setDateSeance(cursor.getString(cursor.getColumnIndexOrThrow("date_seance")));
                e.setDureeMin(cursor.getInt(cursor.getColumnIndexOrThrow("duree_min")));
                e.setPhotoFin(cursor.getString(cursor.getColumnIndexOrThrow("photo_fin")));

                int idxCharge = cursor.getColumnIndex("charge_totale");
                if (idxCharge != -1) {
                    e.setChargeTotale(cursor.getDouble(idxCharge));
                }

                int idxSeries = cursor.getColumnIndex("nb_series");
                if (idxSeries != -1) {
                    e.setNbSeries(cursor.getInt(idxSeries));
                }

                int idxReps = cursor.getColumnIndex("nb_repetitions");
                if (idxReps != -1) {
                    e.setNbRepetitions(cursor.getInt(idxReps));
                }

                list.add(e);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }

    /*public static Entrainement getById(Context context, long id) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_ENTRAINEMENT,
                null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        Entrainement e = null;
        if (cursor != null && cursor.moveToFirst()) {
            e = new Entrainement();
            e.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            e.setIdUser(cursor.getLong(cursor.getColumnIndexOrThrow("id_user")));
            e.setDateSeance(cursor.getString(cursor.getColumnIndexOrThrow("date_seance")));
            e.setDureeMin(cursor.getInt(cursor.getColumnIndexOrThrow("duree_min")));
            e.setChargeTotale(cursor.getDouble(cursor.getColumnIndexOrThrow("charge_totale")));
            e.setNbSeries(cursor.getInt(cursor.getColumnIndexOrThrow("nb_series")));
            e.setNbRepetitions(cursor.getInt(cursor.getColumnIndexOrThrow("nb_repetitions")));
            e.setPhotoFin(cursor.getString(cursor.getColumnIndexOrThrow("photo_fin")));
            e.setCommentaire(cursor.getString(cursor.getColumnIndexOrThrow("commentaire")));
            cursor.close();
        }

        db.close();
        return e;
    }*/

    public long getId(){return this.id;}
    public void setId(long id){this.id = id;}
    public String getDateSeance() { return dateSeance; }
    public void setDateSeance(String dateSeance) { this.dateSeance = dateSeance; }
    public void setNom(String n) { this.nom = n; }
    public String getNom() { return nom; }
    public Programme getProgramme(){return programme;}
    public double getChargeTotale() { return chargeTotale; }
    public int getNbSeries() { return nbSeries; }
    public int getNbRepetitions() { return nbRepetitions; }

    public void setDateTexte(String d) { this.dateTexte = d; }
    public String getDateTexte() { return dateTexte; }
    public int getDureeMin() { return dureeMin; }
    public void setDureeMin(int dureeMin) { this.dureeMin = dureeMin; }
    public String getPhotoFin() { return photoFin; }
    public void setPhotoFin(String photoFin) { this.photoFin = photoFin; }
    public void setProgramme(Programme programme){this.programme = programme;}
    public void setChargeTotale(double chargeTotale) { this.chargeTotale = chargeTotale; }
    public void setNbSeries(int nbSeries) { this.nbSeries = nbSeries; }
    public void setNbRepetitions(int nbRepetitions) { this.nbRepetitions = nbRepetitions; }

    public int getVolumeTotal() {
        if (programme == null || programme.getExercices() == null) return 0;

        int total = 0;
        for (Exercice e : programme.getExercices()) {
            if (e.getSeries() == null) continue;
            for (Serie s : e.getSeries()) {
                total += s.getPoids() * s.getRepetitions();
            }
        }
        return total;
    }
}
