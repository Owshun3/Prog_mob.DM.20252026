package com.example.faza.data.entites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.faza.data.DatabaseHelper;

import java.util.Date;

public class User {

    private long id;
    private String pseudo;
    private Date naissance;
    private int taille;
    private float poids;
    private String unite;
    private String theme;
    private String photoUri;

    public User(String pseudo,
                Date naissance,
                int taille,
                float poids,
                String unite,
                String theme,
                String photoUri) {

        this(-1, pseudo, naissance, taille, poids, unite, theme, photoUri);
    }

    public User() {}

    public User(long id,
                String pseudo,
                Date naissance,
                int taille,
                float poids,
                String unite,
                String theme,
                String photoUri) {

        this.id = id;
        this.pseudo = pseudo;
        this.naissance = naissance;
        this.taille = taille;
        this.poids = poids;
        this.unite = (unite != null ? unite : "kg");
        this.theme = (theme != null ? theme : "clair");
        this.photoUri = photoUri;
    }

    public static User get(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.T_USER,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {

            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String pseudo = cursor.getString(cursor.getColumnIndexOrThrow("nom"));
            long naissanceMillis = cursor.getLong(cursor.getColumnIndexOrThrow("date_naissance"));
            int taille = cursor.getInt(cursor.getColumnIndexOrThrow("taille_cm"));
            float poids = cursor.getFloat(cursor.getColumnIndexOrThrow("poids_kg"));
            String unite = cursor.getString(cursor.getColumnIndexOrThrow("unite_poids"));
            String theme = cursor.getString(cursor.getColumnIndexOrThrow("theme"));
            String photo = cursor.getString(cursor.getColumnIndexOrThrow("photo_profil"));

            cursor.close();

            return new User(
                    id,
                    pseudo,
                    new Date(naissanceMillis),
                    taille,
                    poids,
                    unite,
                    theme,
                    photo
            );
        }
        return null;
    }

    public long insert(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("nom", pseudo);
        v.put("date_naissance", naissance != null ? naissance.getTime() : 0L);
        v.put("taille_cm", taille);
        v.put("poids_kg", poids);
        v.put("unite_poids", unite);
        v.put("theme", theme);
        v.put("photo_profil", photoUri);

        long newId = db.insert(DatabaseHelper.T_USER, null, v);
        if (newId != -1) this.id = newId;

        return newId;
    }

    public int update(Context context) {
        if (id <= 0) return 0;

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("nom", pseudo);
        v.put("date_naissance", naissance != null ? naissance.getTime() : 0L);
        v.put("taille_cm", taille);
        v.put("poids_kg", poids);
        v.put("unite_poids", unite);
        v.put("theme", theme);
        v.put("photo_profil", photoUri);

        return db.update(
                DatabaseHelper.T_USER,
                v,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public static void delete(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.T_USER, null, null);
    }


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getPseudo() { return pseudo; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }

    public Date getNaissance() { return naissance; }
    public void setNaissance(Date naissance) { this.naissance = naissance; }

    public int getTaille() { return taille; }
    public void setTaille(int taille) { this.taille = taille; }

    public float getPoids() { return poids; }
    public void setPoids(float poids) { this.poids = poids; }

    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getPhotoUri() { return photoUri; }
    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }
}
