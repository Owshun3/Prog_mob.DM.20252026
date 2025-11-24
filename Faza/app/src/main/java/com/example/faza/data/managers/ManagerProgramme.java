package com.example.faza.data.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.faza.data.DatabaseHelper;
import com.example.faza.data.entites.Programme;

import java.util.ArrayList;
import java.util.List;

public class ManagerProgramme {

    private final Context context;
    private final ArrayList<Programme> programmes = new ArrayList<>();

    public ManagerProgramme(Context ctx) {
        this.context = ctx;
        chargerDepuisBD();
    }

    public List<Programme> getProgrammes() {
        return programmes;
    }

    private void chargerDepuisBD() {
        programmes.clear();

        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = DatabaseHelper.getInstance(context).getReadableDatabase();

            c = db.rawQuery(
                    "SELECT id, nom, commentaire " +
                            "FROM programme WHERE type='LIBRARY' ORDER BY id DESC",
                    null
            );

            while (c.moveToNext()) {
                Programme p = new Programme();
                p.setId(c.getLong(0));
                p.setNom(c.getString(1));
                p.setCommentaire(c.getString(2));

                programmes.add(p);
            }

        } finally {
            if (c != null) c.close();
        }
    }


    public Programme creerProgramme(Context ctx, String nom) {
        SQLiteDatabase db = DatabaseHelper.getInstance(ctx).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("commentaire", "");
        values.put("type", "LIBRARY");
        values.put("id_entrainement", (String) null);

        long id = db.insert("programme", null, values);

        Programme p = new Programme();
        p.setId(id);
        p.setNom(nom);
        p.setCommentaire("");
        p.setChargeTotale(0);
        p.setNbSeries(0);
        p.setNbRepetitions(0);

        programmes.add(0, p);

        return p;
    }

    public void supprimerProgramme(Programme p) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        db.delete("programme", "id=?", new String[]{String.valueOf(p.getId())});
        programmes.remove(p);
    }

    public Programme getProgrammeById(long id) {
        for (Programme p : programmes) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public void sauvegarderProgramme(Programme p) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        p.calculerChargeEtStats();

        ContentValues values = new ContentValues();
        values.put("nom", p.getNom());
        values.put("commentaire", p.getCommentaire());
        values.put("charge_totale", p.getChargeTotale());
        values.put("nb_series", p.getNbSeries());
        values.put("nb_repetitions", p.getNbRepetitions());

        db.update("programme", values, "id=?", new String[]{String.valueOf(p.getId())});
    }
}
