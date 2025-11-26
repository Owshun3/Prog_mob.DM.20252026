package com.example.faza.data.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.faza.data.DatabaseHelper;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.entites.Serie;

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
                    "SELECT id, nom, commentaire, charge_totale, nb_series, nb_repetitions " +
                            "FROM programme WHERE type='LIBRARY' ORDER BY id DESC",
                    null
            );

            while (c.moveToNext()) {
                Programme p = new Programme();
                p.setId(c.getLong(0));
                p.setNom(c.getString(1));
                p.setCommentaire(c.getString(2));
                Cursor ce = db.rawQuery(
                        "SELECT id, nom, groupe_principal, groupe_secondaire, miniature, url_video " +
                                "FROM exercice WHERE id_programme=?",
                        new String[]{String.valueOf(p.getId())}
                );

                while (ce.moveToNext()) {
                    Exercice ex = new Exercice();
                    ex.setId(ce.getLong(0));
                    ex.setNom(ce.getString(1));
                    ex.setGroupePrincipal(ce.getString(2));
                    ex.setGroupeSecondaire(ce.getString(3));
                    ex.setMiniature(ce.getString(4));
                    ex.setUrlVideo(ce.getString(5));

                    p.getExercices().add(ex);
                }
                ce.close();

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
        values.put("charge_totale", 0);
        values.put("nb_series", 0);
        values.put("nb_repetitions", 0);

        long id = db.insert("programme", null, values);

        Programme p = new Programme();
        for (Exercice ex : p.getExercices()) {
            ContentValues ve = new ContentValues();
            ve.put("id_programme", id);
            ve.put("nom", ex.getNom());
            ve.put("groupe_principal", ex.getGroupePrincipal());
            ve.put("groupe_secondaire", ex.getGroupeSecondaire());
            ve.put("url_video", ex.getUrlVideo());
            ve.put("miniature", ex.getMiniature());

            long idEx = db.insert("exercice", null, ve);
            ex.setId(idEx);
        }

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

        db.delete("exercice", "id_programme=?", new String[]{String.valueOf(p.getId())});

        for (Exercice ex : p.getExercices()) {
            ContentValues ve = new ContentValues();
            ve.put("id_programme", p.getId());
            ve.put("nom", ex.getNom());
            ve.put("groupe_principal", ex.getGroupePrincipal());
            ve.put("groupe_secondaire", ex.getGroupeSecondaire());
            ve.put("url_video", ex.getUrlVideo());
            ve.put("miniature", ex.getMiniature());

            long idEx = db.insert("exercice", null, ve);
            ex.setId(idEx);
        }


        db.update("programme", values, "id=?", new String[]{String.valueOf(p.getId())});
    }

    public Programme chargerProgrammeSessionComplet(Context ctx, long idProgramme) {
        SQLiteDatabase db = DatabaseHelper.getInstance(ctx).getReadableDatabase();

        Programme p = new Programme();

        Cursor c = db.rawQuery(
                "SELECT nom, commentaire, charge_totale, nb_series, nb_repetitions " +
                        "FROM programme WHERE id=?",
                new String[]{String.valueOf(idProgramme)}
        );

        if (!c.moveToFirst()) {
            c.close();
            return p;
        }

        p.setId(idProgramme);
        p.setNom(c.getString(0));
        p.setCommentaire(c.getString(1));
        p.setChargeTotale(c.getDouble(2));
        p.setNbSeries(c.getInt(3));
        p.setNbRepetitions(c.getInt(4));
        c.close();

        Cursor ce = db.rawQuery(
                "SELECT id, nom, groupe_principal, groupe_secondaire, miniature, url_video " +
                        "FROM exercice ex " +
                        "JOIN programme_exercice pe ON ex.id = pe.id_exercice " +
                        "WHERE pe.id_programme=? ORDER BY pe.ordre ASC",
                new String[]{String.valueOf(idProgramme)}
        );

        while (ce.moveToNext()) {
            Exercice e = new Exercice();

            long idEx = ce.getLong(0);
            e.setId(idEx);
            e.setNom(ce.getString(1));
            e.setGroupePrincipal(ce.getString(2));
            e.setGroupeSecondaire(ce.getString(3));
            e.setMiniature(ce.getString(4));
            e.setUrlVideo(ce.getString(5));

            Cursor cs = db.rawQuery(
                    "SELECT id, poids, repetitions, rir, validee " +
                            "FROM serie WHERE id_exercice=?",
                    new String[]{String.valueOf(idEx)}
            );

            while (cs.moveToNext()) {
                Serie s = new Serie(
                        cs.getDouble(1),
                        cs.getInt(2)
                );
                s.setId(cs.getLong(0));
                s.setRir(cs.getInt(3));
                s.setValidee(cs.getInt(4) == 1);

                e.getSeries().add(s);
            }
            cs.close();

            p.getExercices().add(e);
        }
        ce.close();

        return p;
    }
}
