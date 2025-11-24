package com.example.faza.data.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.faza.data.DatabaseHelper;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.entites.Serie;

import java.util.ArrayList;
import java.util.List;

public class ManagerEntrainement {

    private final ArrayList<Entrainement> cache = new ArrayList<>();

    public Entrainement creerDepuisProgramme(Context ctx, Programme source) {
        Programme copie = source.copieDeep();
        long id = insert(ctx);
        Entrainement e = new Entrainement();
        e.setId(id);
        e.setProgramme(copie);
        cache.add(e);
        return e;
    }

    public Entrainement creerVierge(Context ctx) {
        Programme p = new Programme();
        p.setNom("Entraînement libre");

        long id = insert(ctx);

        Entrainement e = new Entrainement();
        e.setId(id);
        e.setProgramme(p);

        cache.add(e);
        return e;
    }

    private long insert(Context ctx) {
        ContentValues v = new ContentValues();
        v.put("date_seance", "");
        v.put("duree_min", 0);
        v.put("photo_fin", "");
        v.put("charge_totale", 0);
        v.put("nb_series", 0);
        v.put("nb_repetitions", 0);

        return DatabaseHelper.getInstance(ctx)
                .getWritableDatabase()
                .insert(DatabaseHelper.T_ENTRAINEMENT, null, v);
    }

    public void sauvegarder(Context ctx, Entrainement e) {
        e.recalculerStats();

        ContentValues v = new ContentValues();
        v.put("date_seance", e.getDateSeance());
        v.put("duree_min", e.getDureeMin());
        v.put("photo_fin", e.getPhotoFin());
        v.put("charge_totale", e.getChargeTotale());
        v.put("nb_series", e.getNbSeries());
        v.put("nb_repetitions", e.getNbRepetitions());

        DatabaseHelper.getInstance(ctx)
                .getWritableDatabase()
                .update(DatabaseHelper.T_ENTRAINEMENT, v, "id=?", new String[]{String.valueOf(e.getId())});

        if (!cache.contains(e)) cache.add(e);
    }

    public Entrainement getById(long id) {
        for (Entrainement e : cache) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public List<Entrainement> getAll(Context ctx) {

        ArrayList<Entrainement> list = new ArrayList<>();

        SQLiteDatabase db = DatabaseHelper.getInstance(ctx).getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT id, date_seance, duree_min, photo_fin " +
                        "FROM entrainement ORDER BY id DESC",
                null
        );

        while (c.moveToNext()) {

            Entrainement e = new Entrainement();
            long idEntrainement = c.getLong(0);

            e.setId(idEntrainement);
            e.setDateSeance(c.getString(1));
            e.setDureeMin(c.getInt(2));
            e.setPhotoFin(c.getString(3));

            Programme p = chargerProgrammeAvecExercicesEtSeries(ctx, idEntrainement);
            e.setProgramme(p);

            e.recalculerStats();

            list.add(e);
        }

        c.close();
        return list;
    }

    private Programme chargerProgrammeAvecExercicesEtSeries(Context ctx, long idEntrainement) {

        SQLiteDatabase db = DatabaseHelper.getInstance(ctx).getReadableDatabase();

        long idProgramme = chargerIdProgramme(db, idEntrainement);
        if (idProgramme == -1) {
            db.close();
            return new Programme();
        }

        Programme p = chargerProgrammeSimple(db, idProgramme);
        chargerExercicesEtSeries(db, p);

        db.close();
        return p;
    }

    private long chargerIdProgramme(SQLiteDatabase db, long idEntrainement) {
        Cursor c = db.rawQuery(
                "SELECT id_programme FROM entrainement WHERE id=?",
                new String[]{String.valueOf(idEntrainement)}
        );

        if (!c.moveToFirst()) {
            c.close();
            return -1;
        }

        long idProgramme = c.getLong(0);
        c.close();
        return idProgramme;
    }


    private Programme chargerProgrammeSimple(SQLiteDatabase db, long idProgramme) {

        Programme p = new Programme();

        Cursor c = db.rawQuery(
                "SELECT nom FROM programme WHERE id=?",
                new String[]{String.valueOf(idProgramme)}
        );

        if (c.moveToFirst()) {
            p.setId(idProgramme);
            p.setNom(c.getString(0));
        }

        c.close();
        return p;
    }

    private void chargerExercicesEtSeries(SQLiteDatabase db, Programme p) {

        Cursor ce = db.rawQuery(
                "SELECT id, nom, groupe_principal, miniature, url_video FROM exercice WHERE id_programme=?",
                new String[]{String.valueOf(p.getId())}
        );

        while (ce.moveToNext()) {

            Exercice ex = new Exercice();
            long idEx = ce.getLong(0);

            ex.setId(idEx);
            ex.setNom(ce.getString(1));
            ex.setGroupePrincipal(ce.getString(2));
            ex.setMiniature(ce.getString(3));
            ex.setUrlVideo(ce.getString(4));

            ex.setEstUneCopie(true);

            chargerSeriesPourExercice(db, ex);
            p.getExercices().add(ex);
        }

        ce.close();
    }


    private void chargerSeriesPourExercice(SQLiteDatabase db, Exercice ex) {

        Cursor cs = db.rawQuery(
                "SELECT id, poids, repetitions, rir, validee " +
                        "FROM serie WHERE id_exercice=?",
                new String[]{String.valueOf(ex.getId())}
        );

        while (cs.moveToNext()) {

            Serie s = new Serie(
                    cs.getDouble(1),
                    cs.getInt(2)
            );

            s.setId(cs.getLong(0));
            s.setRir(cs.getInt(3));
            s.setValidee(cs.getInt(4) == 1);

            ex.getSeries().add(s);
        }

        cs.close();
    }
}
