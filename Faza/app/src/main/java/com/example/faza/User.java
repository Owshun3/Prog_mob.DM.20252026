package com.example.faza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class User {
    private String pseudo;
    private Date naissance;
    private int taille;
    private float poids;
    private String unite;
    private String theme;
    private String photoUri;

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_THEME = "theme";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_UNIT = "unit";


    public User() {}

    public User(String pseudo, Date naissance, int taille, float poids,
                String unite, String theme, String photoUri) {
        this.pseudo = pseudo;
        this.naissance = naissance;
        this.taille = taille;
        this.poids = poids;
        this.unite = unite;
        this.theme = theme;
        this.photoUri = photoUri;
    }

    public String getPseudo() {return pseudo;}
    public Date getNaissance() {return naissance;}
    public int getTaille() {return taille;}
    public float getPoids() {return poids;}
    public String getUnite() {return unite;}
    public String getTheme() {return theme;}
    public String getPhotoUri() {return photoUri;}

    public void setPseudo(String pseudo) {this.pseudo = pseudo;}
    public void setNaissance(Date naissance) {this.naissance = naissance;}
    public void setTaille(int taille) {this.taille = taille;}
    public void setPoids(float poids) {this.poids = poids;}
    public void setUnite(String unite) {this.unite = unite;}
    public void setTheme(String theme) {this.theme = theme;}
    public void setPhotoUri(String photoUri) {this.photoUri = photoUri;}

    public long insert(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nom", pseudo);
        values.put("date_naissance", naissance.getTime());
        values.put("taille_cm", taille);
        values.put("poids_kg", poids);
        values.put("unite_poids", unite);
        values.put("theme", theme);
        values.put("photo_profil", photoUri);

        return db.insert(DatabaseHelper.TABLE_USER, null, values);
    }

    public static User get(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow("nom")),
                    new Date(cursor.getLong(cursor.getColumnIndexOrThrow("date_naissance"))),
                    cursor.getInt(cursor.getColumnIndexOrThrow("taille_cm")),
                    cursor.getFloat(cursor.getColumnIndexOrThrow("poids_kg")),
                    cursor.getString(cursor.getColumnIndexOrThrow("unite_poids")),
                    cursor.getString(cursor.getColumnIndexOrThrow("theme")),
                    cursor.getString(cursor.getColumnIndexOrThrow("photo_profil"))
            );
            cursor.close();
            return user;
        }
        return null;
    }

    public int update(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nom", pseudo);
        values.put("date_naissance", naissance.getTime());
        values.put("taille_cm", taille);
        values.put("poids_kg", poids);
        values.put("unite_poids", unite);
        values.put("theme", theme);
        values.put("photo_profil", photoUri);

        return db.update(DatabaseHelper.TABLE_USER, values, null, null);
    }

    public static void delete(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_USER, null, null);
    }
}
