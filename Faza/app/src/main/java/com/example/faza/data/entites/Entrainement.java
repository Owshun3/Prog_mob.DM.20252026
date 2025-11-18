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
    private String dateSeance;
    private int dureeMin;
    private String photoFin;
    private Programme programme;

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
    }
*/
    public long getId(){return this.id;}
    public void setId(long id){this.id = id;}
    public String getDateSeance() { return dateSeance; }
    public void setDateSeance(String dateSeance) { this.dateSeance = dateSeance; }

    public int getDureeMin() { return dureeMin; }
    public void setDureeMin(int dureeMin) { this.dureeMin = dureeMin; }
    public String getPhotoFin() { return photoFin; }
    public void setPhotoFin(String photoFin) { this.photoFin = photoFin; }

    public Programme getProgramme(){return programme;}
    public void setProgramme(Programme programme){this.programme = programme;}
}
