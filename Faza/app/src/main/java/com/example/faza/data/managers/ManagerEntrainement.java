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
        SQLiteDatabase db = DatabaseHelper.getInstance(ctx).getWritableDatabase();

        for (Exercice ex : copie.getExercices()) {

            ContentValues ve = new ContentValues();
            ve.put("id_programme", idProgramme);
            ve.put("nom", ex.getNom());
            ve.put("groupe_principal", ex.getGroupePrincipal());
            ve.put("groupe_secondaire", ex.getGroupeSecondaire());
            ve.put("url_video", ex.getUrlVideo());
            ve.put("miniature", ex.getMiniature());

            long idEx = db.insert("exercice", null, ve);
            ex.setId(idEx);

            for (Serie s : ex.getSeries()) {
                ContentValues vs = new ContentValues();
                vs.put("id_exercice", idEx);
                vs.put("poids", s.getPoids());
                vs.put("repetitions", s.getRepetitions());
                vs.put("rir", s.getRir());
                vs.put("validee", s.isValidee() ? 1 : 0);

                long idS = db.insert("serie", null, vs);
                s.setId(idS);
            }
        }

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
        Programme p = e.getProgramme();
        p.calculerChargeEtStats();
        sauvegarderProgrammeSession(ctx, e.getId(), p);
        if (!cache.contains(e)) cache.add(e);
    }

    private void sauvegarderProgrammeSession(Context ctx, long idEntrainement, Programme p) {

        SQLiteDatabase db = DatabaseHelper.getInstance(ctx).getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("nom", p.getNom());
        v.put("commentaire", p.getCommentaire());
        v.put("charge_totale", p.getChargeTotale());
        v.put("nb_series", p.getNbSeries());
        v.put("nb_repetitions", p.getNbRepetitions());

        db.update("programme", v, "id=? AND id_entrainement=?",
                new String[]{String.valueOf(p.getId()), String.valueOf(idEntrainement)});
        sauvegarderExercices(ctx, p);
    }

    private void sauvegarderExercices(Context ctx, Programme p) {

        SQLiteDatabase db = DatabaseHelper.getInstance(ctx).getWritableDatabase();
        db.delete("programme_exercice", "id_programme=?", new String[]{String.valueOf(p.getId())});
        for (int ordre = 0; ordre < p.getExercices().size(); ordre++) {

            Exercice e = p.getExercices().get(ordre);
            ContentValues ve = new ContentValues();
            ve.put("nom", e.getNom());
            ve.put("groupe_principal", e.getGroupePrincipal());
            ve.put("groupe_secondaire", e.getGroupeSecondaire());
            ve.put("miniature", e.getMiniature());
            ve.put("url_video", e.getUrlVideo());

            long idEx;
            if (e.getId() <= 0) {
                idEx = db.insert("exercice", null, ve);
                e.setId(idEx);
            } else {
                idEx = e.getId();
                db.update("exercice", ve, "id=?", new String[]{String.valueOf(idEx)});
            }
            ContentValues vr = new ContentValues();
            vr.put("id_programme", p.getId());
            vr.put("id_exercice", idEx);
            vr.put("ordre", ordre);

            db.insert("programme_exercice", null, vr);
            if (idEx <= 0) continue;
            sauvegarderSeries(db, e);
        }
    }

    private void sauvegarderSeries(SQLiteDatabase db, Exercice e) {

        db.delete("serie", "id_exercice=?", new String[]{String.valueOf(e.getId())});

        for (Serie s : e.getSeries()) {
            ContentValues vs = new ContentValues();
            vs.put("id_exercice", e.getId());
            vs.put("poids", s.getPoids());
            vs.put("repetitions", s.getRepetitions());
            vs.put("rir", s.getRir());
            vs.put("validee", s.isValidee() ? 1 : 0);

            if (s.getId() == 0) {
                long id = db.insert("serie", null, vs);
                s.setId(id);
            } else {
                db.update("serie", vs, "id=?", new String[]{String.valueOf(s.getId())});
            }
        }
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
