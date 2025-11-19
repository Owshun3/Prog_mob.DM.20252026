package com.example.faza.data.managers;

import android.content.ContentValues;
import android.content.Context;
import com.example.faza.data.DBAdapter;
import com.example.faza.data.entites.Entrainement;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Programme;
import com.example.faza.data.entites.Serie;

import java.util.ArrayList;

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

    public void sauvegarderEntrainement(Context ctx, Entrainement e) {
        updateEntrainement(ctx, e);
        saveExosEtSeries(ctx, e);
    }

    private void updateEntrainement(Context ctx, Entrainement e) {
        DBAdapter db = new DBAdapter(ctx).open();
        ContentValues v = new ContentValues();
        v.put("date_seance", e.getDateSeance());
        v.put("duree_min", e.getDureeMin());
        v.put("photo_fin", e.getPhotoFin());
        db.update("entrainement", v, e.getId());
        db.close();
    }

    private void saveExosEtSeries(Context ctx, Entrainement e) {
        DBAdapter db = new DBAdapter(ctx).open();
        long idE = e.getId();
        db.execSQL("DELETE FROM entrainement_exercice WHERE id_entrainement=" + idE);

        for (Exercice ex : e.getProgramme().getExercices()) {
            ContentValues vEx = new ContentValues();
            vEx.put("id_entrainement", idE);
            vEx.put("id_exercice", ex.getId());
            vEx.put("ordre", 0);
            long idEE = db.insert("entrainement_exercice", vEx);

            db.execSQL("DELETE FROM serie WHERE id_entrainement_exercice=" + idEE);

            for (Serie s : ex.getSeries()) {
                ContentValues vS = new ContentValues();
                vS.put("id_entrainement_exercice", idEE);
                vS.put("numero", 0);
                vS.put("poids", s.getPoids());
                vS.put("repetitions", s.getRepetitions());
                vS.put("rir", s.getRir());
                vS.put("validee", s.isValidee() ? 1 : 0);
                db.insert("serie", vS);
            }
        }

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
            Exercice c = new Exercice();
            c.setId(ex.getId());
            c.setNom(ex.getNom());
            c.setDescription(ex.getDescription());
            c.setGroupePrincipal(ex.getGroupePrincipal());
            c.setGroupeSecondaire(ex.getGroupeSecondaire());
            ArrayList<Serie> ss = new ArrayList<>();
            for (Serie s : ex.getSeries()) {
                Serie cs = new Serie(s.getPoids(), s.getRepetitions());
                cs.setRir(s.getRir());
                cs.setValidee(false);
                ss.add(cs);
            }
            c.setSeries(ss);
            exs.add(c);
        }
        p.setExercices(exs);
        return p;
    }
}
