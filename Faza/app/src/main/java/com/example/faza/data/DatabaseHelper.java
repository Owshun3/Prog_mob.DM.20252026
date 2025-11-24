package com.example.faza.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.faza.data.entites.User;

import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    public static final String DB_NAME = "faza.db";
    public static final int DB_VERSION = 1;

    public static final String T_USER = "user";
    public static final String T_ENTRAINEMENT = "entrainement";
    public static final String T_PROGRAMME = "programme";
    public static final String T_EXERCICE = "exercice";
    public static final String T_SERIE = "serie";
    public static final String T_PROGRAMME_EXERCICE = "programme_exercice";

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (instance == null) instance = new DatabaseHelper(ctx.getApplicationContext());
        return instance;
    }

    private DatabaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE user(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT," +
                "date_naissance INTEGER," +
                "taille_cm INTEGER," +
                "poids_kg REAL," +
                "unite_poids TEXT," +
                "theme TEXT," +
                "photo_profil TEXT)");

        db.execSQL(
                "CREATE TABLE programme (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nom TEXT NOT NULL," +
                        "commentaire TEXT," +
                        "type TEXT NOT NULL DEFAULT 'LIBRARY'," +
                        "id_entrainement INTEGER," +
                        "charge_totale REAL DEFAULT 0," +
                        "nb_series INTEGER DEFAULT 0," +
                        "nb_repetitions INTEGER DEFAULT 0" +
                        ");"
        );

        db.execSQL("CREATE TABLE exercice(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT," +
                "groupe_principal TEXT," +
                "groupe_secondaire TEXT," +
                "description TEXT," +
                "url_video TEXT," +
                "miniature TEXT)");

        db.execSQL("CREATE TABLE programme_exercice(" +
                "id_programme INTEGER," +
                "id_exercice INTEGER," +
                "ordre INTEGER)");

        db.execSQL("CREATE TABLE serie(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_exercice INTEGER," +
                "poids REAL," +
                "repetitions INTEGER," +
                "rir INTEGER," +
                "validee INTEGER)");

        db.execSQL("CREATE TABLE entrainement(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date_seance TEXT," +
                "duree_min INTEGER," +
                "photo_fin TEXT," +
                "charge_totale REAL," +
                "nb_series INTEGER," +
                "nb_repetitions INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {}

    public User getUser() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM user LIMIT 1", null);
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }

        User u = new User(
                c.getString(c.getColumnIndexOrThrow("nom")),
                new Date(c.getLong(c.getColumnIndexOrThrow("date_naissance"))),
                c.getInt(c.getColumnIndexOrThrow("taille_cm")),
                c.getFloat(c.getColumnIndexOrThrow("poids_kg")),
                c.getString(c.getColumnIndexOrThrow("unite_poids")),
                c.getString(c.getColumnIndexOrThrow("theme")),
                c.getString(c.getColumnIndexOrThrow("photo_profil"))
        );

        c.close();
        return u;
    }
}
