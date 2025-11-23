package com.example.faza.data.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.faza.data.DBAdapter;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.entites.Serie;

import java.util.ArrayList;
import java.util.List;

public class ManagerEntrainement {

    private final ArrayList<Entrainement> entrainements = new ArrayList<>();

    public Entrainement creerDepuisProgramme(Context ctx, Programme source) {
        Programme copie = copierProgramme(source);
        long id = insertEntrainement(ctx);
        Entrainement e = new Entrainement();
        e.setId(id);
        e.setProgramme(copie);
        entrainements.add(e);
        return e;
    }

    public Entrainement creerVierge(Context ctx) {
        Programme p = new Programme();
        p.setNom("Entraînement libre");
        long id = insertEntrainement(ctx);
        Entrainement e = new Entrainement();
        e.setId(id);
        e.setProgramme(p);
        entrainements.add(e);
        return e;
    }

    public Entrainement creerDepuisCsv(String title, String dateStr) {
        Entrainement e = new Entrainement();
        e.setNom(title);
        e.setDateTexte(dateStr);
        e.setProgramme(new Programme());
        entrainements.add(e);
        return e;
    }

    private long insertEntrainement(Context ctx) {
        DBAdapter db = new DBAdapter(ctx).open();
        ContentValues v = new ContentValues();
        v.put("id_user", 1);
        v.put("date_seance", "");
        v.put("duree_min", 0);
        v.put("charge_totale", 0);
        v.put("nb_series", 0);
        v.put("nb_repetitions", 0);
        v.put("photo_fin", "");
        v.put("commentaire", "");
        long id = db.insert("entrainement", v);
        db.close();
        return id;
    }

    public void sauvegarderEntrainement(Entrainement e) {
        if (e == null) return;
        if (!entrainements.contains(e)) {
            entrainements.add(e);
        }
    }
    /*
    public void sauvegarderEntrainement(Context ctx, Entrainement e) {
        updateEntrainement(ctx, e);
        //saveExosEtSeries(ctx, e);
    }

     */

    private void updateEntrainement(Context ctx, Entrainement e) {
        DBAdapter db = new DBAdapter(ctx).open();
        ContentValues v = new ContentValues();
        v.put("date_seance", e.getDateSeance());
        v.put("duree_min", e.getDureeMin());
        v.put("photo_fin", e.getPhotoFin());
        db.update("entrainement", v, e.getId());
        db.close();
    }

    public Entrainement getById(long id) {
        for (Entrainement e : entrainements) if (e.getId() == id) return e;
        return null;
    }

    private Programme copierProgramme(Programme src) {
        Programme p = new Programme();
        p.setNom(src.getNom());

        ArrayList<Exercice> exs = new ArrayList<>();
        for (Exercice ex : src.getExercices()) {
            exs.add(ex.copie());
        }

        p.setExercices(exs);
        return p;
    }

    public int getTotalSessions(Context ctx) { return Entrainement.getAll(ctx).size(); }

    public int getTotalDurationMinutes(Context ctx) {
        int total = 0;
        for (Entrainement e : Entrainement.getAll(ctx)) {
            total += e.getDureeMin();
        }
        return total;
    }

    public double getTotalVolume(Context ctx) {
        double total = 0;
        for (Entrainement e : Entrainement.getAll(ctx)) {
            total += e.getChargeTotale();
        }
        return total;
    }

    public int getTotalReps(Context ctx) {
        int total = 0;
        for (Entrainement e : Entrainement.getAll(ctx)) {
            total += e.getNbRepetitions();
        }
        return total;
    }

    public ArrayList<Entrainement> getAll(Context ctx) {
        ArrayList<Entrainement> list = new ArrayList<>();

        DBAdapter db = new DBAdapter(ctx).open();
        Cursor c = db.getAll("entrainement");

        if (c.moveToFirst()) {
            do {
                Entrainement e = new Entrainement();
                e.setId(c.getLong(c.getColumnIndexOrThrow("id")));
                e.setDateSeance(c.getString(c.getColumnIndexOrThrow("date_seance")));
                e.setDureeMin(c.getInt(c.getColumnIndexOrThrow("duree_min")));
                e.setPhotoFin(c.getString(c.getColumnIndexOrThrow("photo_fin")));
                list.add(e);
            }
            while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }
}
