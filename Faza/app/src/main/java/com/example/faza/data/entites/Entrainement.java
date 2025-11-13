package com.example.faza.data.entites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.faza.data.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class Entrainement extends Programme{
    private String dateSeance;
    private int dureeMin;
    private String photoFin;

    public Entrainement(long idUser, String nom, String commentaire, String dateSeance, int dureeMin, String photoFin) {
        super(idUser, nom, commentaire);
        this.dateSeance = dateSeance;
        this.dureeMin = dureeMin;
        this.photoFin = photoFin;
    }

    public Entrainement() {
        super();
        this.photoFin = "null";
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

    public String getDateSeance() { return dateSeance; }
    public void setDateSeance(String dateSeance) { this.dateSeance = dateSeance; }

    public int getDureeMin() { return dureeMin; }
    public void setDureeMin(int dureeMin) { this.dureeMin = dureeMin; }
    public String getPhotoFin() { return photoFin; }
    public void setPhotoFin(String photoFin) { this.photoFin = photoFin; }

    @Override
    public String toCSV() {
        if (getId() == -1 || getNom() == null || dateSeance == null) {
            throw new IllegalArgumentException("Erreur: toCSV() d’un entrainement invalide (id, nom ou date manquante)");
        }
        return getId() + ";" +
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
