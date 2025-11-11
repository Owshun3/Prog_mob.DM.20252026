package com.example.faza.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;

    private static final String DATABASE_NAME = "faizaDB";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_USER = "user";
    public static final String TABLE_ENTRAINEMENT = "entrainement";
    public static final String TABLE_EXERCICE = "exercice";
    public static final String TABLE_ENTRAINEMENT_EXERCICE = "seance_exercice";
    public static final String TABLE_SERIE = "serie";
    public static final String TABLE_PROGRAMME = "programme";
    public static final String TABLE_PROGRAMME_EXERCICE = "programme_exercice";

    private static final String CREATE_USER =
            "CREATE TABLE " + TABLE_USER + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nom TEXT, " +
                    "date_naissance TEXT, " +
                    "taille_cm REAL, " +
                    "poids_kg REAL, " +
                    "unite_poids TEXT, " +
                    "theme TEXT, " +
                    "photo_profil TEXT, " +
                    "date_creation TEXT);";

    private static final String CREATE_ENTRAINEMENT =
            "CREATE TABLE " + TABLE_ENTRAINEMENT + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_user INTEGER NOT NULL, " +
                    "date_seance TEXT, " +
                    "duree_min INTEGER, " +
                    "charge_totale REAL, " +
                    "nb_series INTEGER, " +
                    "nb_repetitions INTEGER, " +
                    "photo_fin TEXT, " +
                    "commentaire TEXT, " +
                    "FOREIGN KEY(id_user) REFERENCES " + TABLE_USER + "(id));";

    private static final String CREATE_EXERCICE =
            "CREATE TABLE " + TABLE_EXERCICE + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nom TEXT NOT NULL UNIQUE, " +
                    "groupe_principal TEXT, " +
                    "groupe_secondaire TEXT, " +
                    "description TEXT, " +
                    "url_video TEXT, " +
                    "miniature TEXT);";

    private static final String CREATE_SEANCE_EXERCICE =
            "CREATE TABLE " + TABLE_ENTRAINEMENT_EXERCICE + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_seance INTEGER NOT NULL, " +
                    "id_exercice INTEGER NOT NULL, " +
                    "ordre INTEGER, " +
                    "FOREIGN KEY(id_seance) REFERENCES " + TABLE_ENTRAINEMENT + "(id), " +
                    "FOREIGN KEY(id_exercice) REFERENCES " + TABLE_EXERCICE + "(id));";

    private static final String CREATE_SERIE =
            "CREATE TABLE " + TABLE_SERIE + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_seance_exercice INTEGER NOT NULL, " +
                    "numero INTEGER, " +
                    "etiquette TEXT, " +
                    "poids REAL, " +
                    "repetitions INTEGER, " +
                    "rir INTEGER, " +
                    "rest_pause INTEGER, " +
                    "validee INTEGER, " +
                    "FOREIGN KEY(id_seance_exercice) REFERENCES " + TABLE_ENTRAINEMENT_EXERCICE + "(id));";

    private static final String CREATE_PROGRAMME =
            "CREATE TABLE " + TABLE_PROGRAMME + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nom TEXT NOT NULL, " +
                    "description TEXT, " +
                    "date_creation TEXT);";

    private static final String CREATE_PROGRAMME_EXERCICE =
            "CREATE TABLE " + TABLE_PROGRAMME_EXERCICE + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_programme INTEGER NOT NULL, " +
                    "id_exercice INTEGER NOT NULL, " +
                    "nb_series_defaut INTEGER, " +
                    "nb_reps_defaut INTEGER, " +
                    "poids_min REAL, " +
                    "poids_max REAL, " +
                    "FOREIGN KEY(id_programme) REFERENCES " + TABLE_PROGRAMME + "(id), " +
                    "FOREIGN KEY(id_exercice) REFERENCES " + TABLE_EXERCICE + "(id));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_ENTRAINEMENT);
        db.execSQL(CREATE_EXERCICE);
        db.execSQL(CREATE_SEANCE_EXERCICE);
        db.execSQL(CREATE_SERIE);
        db.execSQL(CREATE_PROGRAMME);
        db.execSQL(CREATE_PROGRAMME_EXERCICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAMME_EXERCICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAMME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRAINEMENT_EXERCICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRAINEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}
