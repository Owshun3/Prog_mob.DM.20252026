package com.example.faza.data.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.faza.data.DatabaseHelper;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Programme;

import java.util.ArrayList;
import java.util.List;

public class ManagerEntrainement {

    private final ArrayList<Entrainement> cache = new ArrayList<>();

    public Entrainement creerDepuisProgramme(Context ctx, Programme source) {
        long idEntrainement = insertEntrainement(ctx);

        Programme pSession = creerProgrammeSessionDepuisSource(ctx, idEntrainement, source);

        Entrainement e = new Entrainement();
        e.setId(idEntrainement);
        e.setProgramme(pSession);

        cache.add(e);
        return e;
    }

    public Entrainement creerVierge(Context ctx) {
        long idEntrainement = insertEntrainement(ctx);

        Programme p = new Programme();
        p.setNom("Entraînement libre");
        long idProgramme = insertProgrammeSession(ctx, idEntrainement, p);
        p.setId(idProgramme);

        Entrainement e = new Entrainement();
        e.setId(idEntrainement);
        e.setProgramme(p);

        cache.add(e);
        return e;
    }

    private long insertEntrainement(Context ctx) {
        SQLiteDatabase db = DatabaseHelper.getInstance(ctx).getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("date_seance", "");
        v.put("duree_min", 0);
        v.put("photo_fin", "");
        v.put("charge_totale", 0);
        v.put("nb_series", 0);
        v.put("nb_repetitions", 0);

        return db.insert(DatabaseHelper.T_ENTRAINEMENT, null, v);
    }

    private long insertProgrammeSession(Context ctx, long idEntrainement, Programme p) {
        SQLiteDatabase db = DatabaseHelper.getInstance(ctx).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nom", p.getNom());
        values.put("commentaire", p.getCommentaire() != null ? p.getCommentaire() : "");
        values.put("type", "SESSION");
        values.put("id_entrainement", idEntrainement);
        values.put("charge_totale", 0);
        values.put("nb_series", 0);
        values.put("nb_repetitions", 0);

        return db.insert(DatabaseHelper.T_PROGRAMME, null, values);
    }

    private Programme creerProgrammeSessionDepuisSource(Context ctx, long idEntrainement, Programme source) {
        Programme copie = source.copieDeep();
        long idProgramme = insertProgrammeSession(ctx, idEntrainement, copie);
        copie.setId(idProgramme);
        return copie;
    }

    public void sauvegarder(Context ctx, Entrainement e) {
        if (e == null) return;

        e.recalculerStats();

        SQLiteDatabase db = DatabaseHelper.getInstance(ctx).getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("date_seance", e.getDateSeance());
        v.put("duree_min", e.getDureeMin());
        v.put("photo_fin", e.getPhotoFin());
        v.put("charge_totale", e.getChargeTotale());
        v.put("nb_series", e.getNbSeries());
        v.put("nb_repetitions", e.getNbRepetitions());

        db.update(DatabaseHelper.T_ENTRAINEMENT, v, "id=?", new String[]{String.valueOf(e.getId())});

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
        Cursor c = null;

        try {
            c = db.rawQuery(
                    "SELECT id, date_seance, duree_min, photo_fin, charge_totale, nb_series, nb_repetitions " +
                            "FROM " + DatabaseHelper.T_ENTRAINEMENT + " ORDER BY id DESC",
                    null
            );

            while (c.moveToNext()) {
                Entrainement e = new Entrainement();
                long idEntrainement = c.getLong(0);

                e.setId(idEntrainement);
                e.setDateSeance(c.getString(1));
                e.setDureeMin(c.getInt(2));
                e.setPhotoFin(c.getString(3));
                e.setChargeTotale(c.getDouble(4));
                e.setNbSeries(c.getInt(5));
                e.setNbRepetitions(c.getInt(6));

                Programme p = chargerProgrammeSessionHeader(db, idEntrainement);
                e.setProgramme(p);

                list.add(e);
            }
        } finally {
            if (c != null) c.close();
        }

        return list;
    }

    private Programme chargerProgrammeSessionHeader(SQLiteDatabase db, long idEntrainement) {
        Programme p = new Programme();

        Cursor c = db.rawQuery(
                "SELECT id, nom, commentaire, charge_totale, nb_series, nb_repetitions " +
                        "FROM " + DatabaseHelper.T_PROGRAMME +
                        " WHERE type='SESSION' AND id_entrainement=? LIMIT 1",
                new String[]{String.valueOf(idEntrainement)}
        );

        if (!c.moveToFirst()) {
            c.close();
            return p;
        }

        long idProgramme = c.getLong(0);
        p.setId(idProgramme);
        p.setNom(c.getString(1));
        p.setCommentaire(c.getString(2));
        p.setChargeTotale(c.getDouble(3));
        p.setNbSeries(c.getInt(4));
        p.setNbRepetitions(c.getInt(5));
        c.close();

        return p;
    }
}
