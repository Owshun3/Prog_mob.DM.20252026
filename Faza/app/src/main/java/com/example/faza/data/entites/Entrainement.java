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
    private long idUser;
    private String dateSeance;
    private int dureeMin;
    private double chargeTotale;
    private int nbSeries;
    private int nbRepetitions;
    private String photoFin;
    private String commentaire;

    private List<Exercice> exercices;

    public Entrainement() {
        exercices = new ArrayList<>();
    }

    public Entrainement(long idUser, String dateSeance, int dureeMin, String photoFin,
                        String commentaire) {
        this.idUser = idUser;
        this.photoFin = photoFin;
        this.dateSeance = dateSeance;
        this.dureeMin = dureeMin;
        this.commentaire = commentaire;
    }

    public long insert(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_user", idUser);
        values.put("date_seance", dateSeance);
        values.put("duree_min", dureeMin);
        values.put("charge_totale", chargeTotale);
        values.put("nb_series", nbSeries);
        values.put("nb_repetitions", nbRepetitions);
        values.put("photo_fin", photoFin);
        values.put("commentaire", commentaire);

        long id = db.insert(DatabaseHelper.TABLE_ENTRAINEMENT, null, values);
        this.id = id;

        db.close();
        return id;
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
                e.setIdUser(cursor.getLong(cursor.getColumnIndexOrThrow("id_user")));
                e.setDateSeance(cursor.getString(cursor.getColumnIndexOrThrow("date_seance")));
                e.setDureeMin(cursor.getInt(cursor.getColumnIndexOrThrow("duree_min")));
                e.setChargeTotale(cursor.getDouble(cursor.getColumnIndexOrThrow("charge_totale")));
                e.setNbSeries(cursor.getInt(cursor.getColumnIndexOrThrow("nb_series")));
                e.setNbRepetitions(cursor.getInt(cursor.getColumnIndexOrThrow("nb_repetitions")));
                e.setPhotoFin(cursor.getString(cursor.getColumnIndexOrThrow("photo_fin")));
                e.setCommentaire(cursor.getString(cursor.getColumnIndexOrThrow("commentaire")));
                list.add(e);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return list;
    }

    public static Entrainement getById(Context context, long id) {
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
    }

    public int update(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("duree_min", dureeMin);
        values.put("commentaire", commentaire);
        values.put("charge_totale", chargeTotale);
        values.put("nb_series", nbSeries);
        values.put("nb_repetitions", nbRepetitions);
        values.put("photo_fin", photoFin);

        int rows = db.update(DatabaseHelper.TABLE_ENTRAINEMENT, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdUser() { return idUser; }
    public void setIdUser(long idUser) { this.idUser = idUser; }

    public String getDateSeance() { return dateSeance; }
    public void setDateSeance(String dateSeance) { this.dateSeance = dateSeance; }

    public int getDureeMin() { return dureeMin; }
    public void setDureeMin(int dureeMin) { this.dureeMin = dureeMin; }

    public double getChargeTotale() { return chargeTotale; }
    public void setChargeTotale(double chargeTotale) { this.chargeTotale = chargeTotale; }

    public int getNbSeries() { return nbSeries; }
    public void setNbSeries(int nbSeries) { this.nbSeries = nbSeries; }

    public int getNbRepetitions() { return nbRepetitions; }
    public void setNbRepetitions(int nbRepetitions) { this.nbRepetitions = nbRepetitions; }

    public String getPhotoFin() { return photoFin; }
    public void setPhotoFin(String photoFin) { this.photoFin = photoFin; }

    @Override
    public String toCSV() {
        if (id == -1 || getNom() == null || dateSeance == null) {
            throw new IllegalArgumentException("Erreur: toCSV() d’un entrainement invalide (id, nom ou date manquante)");
        }
        return id + ";" +
                getIdUser() + ";" +
                getNom() + ";" +
                getChargeTotale() + ";" +
                getNbSeries() + ";" +
                getNbRepetitions() + ";" +
                (photoFin != null ? photoFin : "") + ";" +
                (getCommentaire() != null ? getCommentaire() : "") + ";" +
                dateSeance + ";" +
                dureeMin;
    }

    public static Entrainement fromCSV(String line) {
        String[] champs = line.split(";", -1);
        if (champs.length != 10) {
            throw new IllegalArgumentException("Erreur: ligne CSV invalide pour Entrainement (10 champs attendus): " + line);
        }

        try {
            Entrainement e = new Entrainement();
            e.setId(Long.parseLong(champs[0]));
            e.setIdUser(Long.parseLong(champs[1]));
            e.setNom(champs[2]);
            e.setChargeTotale(Double.parseDouble(champs[3]));
            e.setNbSeries(Integer.parseInt(champs[4]));
            e.setNbRepetitions(Integer.parseInt(champs[5]));
            e.setPhotoFin(champs[6]);
            e.setCommentaire(champs[7]);
            e.setDateSeance(champs[8]);
            e.setDureeMin(Integer.parseInt(champs[9]));
            return e;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Erreur de parsing dans la ligne CSV pour Entrainement: " + line, ex);
        }
    }

}
