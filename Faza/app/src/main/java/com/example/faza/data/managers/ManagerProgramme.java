package com.example.faza.data.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.example.faza.data.DBAdapter;
import com.example.faza.data.entites.Exercice;
import com.example.faza.data.entites.Programme;
import java.util.ArrayList;

public class ManagerProgramme {

    private final Context context;
    private ArrayList<Programme> programmes = new ArrayList<>();

    public ManagerProgramme(Context ctx) {
        this.context = ctx;
        //chargerDepuisBDD();
    }

    public ArrayList<Programme> getProgrammes() { return programmes; }
    public void ajouterProgramme(Programme p) { programmes.add(p); }

    /*
    private void chargerDepuisBDD() {
        programmes.clear();

        DBAdapter db = new DBAdapter(context).open();
        Cursor c = db.getAll("programme");

        while (c.moveToNext()) {
            long idEx = c.getLong(0);

            Exercice ex = chargerExerciceDepuisBD(idEx);

            if (ex != null) {
                //liste.add(ex);
            }
        }


        c.close();
        db.close();
    }

    private Exercice chargerExerciceDepuisBD(long idEx) {
        DBAdapter db = new DBAdapter(context).open();

        Cursor c = db.rawQuery(
                "SELECT nom, groupe_principal, groupe_secondaire, description, url_video, miniature " +
                        "FROM exercice WHERE id = ?",
                new String[]{String.valueOf(idEx)}
        );

        if (!c.moveToFirst()) return null;

        Exercice e = new Exercice();
        e.setId(idEx);
        e.setNom(c.getString(0));
        e.setGroupePrincipal(c.getString(1));
        e.setGroupeSecondaire(c.getString(2));
        e.setDescription(c.getString(3));
        e.setUrlVideo(c.getString(4));
        e.setMiniature(c.getString(5));

        c.close();
        db.close();
        return e;
    }
    */

    public Programme creerProgramme(Context ctx, String nom) {
        DBAdapter db = new DBAdapter(ctx).open();

        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("description", "");
        values.put("date_creation", "");

        long id = db.insert("programme", values);

        db.close();

        Programme p = new Programme();
        p.setId(id);
        p.setNom(nom);

        programmes.add(p);

        return p;
    }

    public void supprimerProgramme(Programme p) {
        DBAdapter db = new DBAdapter(context).open();

        db.execSQL("DELETE FROM programme_exercice WHERE id_programme=" + p.getId());
        db.delete("programme", p.getId());

        db.close();

        programmes.remove(p);
    }

    public void sauvegarderProgramme(Programme p) {
        updateProgramme(p);
        //saveExercicesForProgramme(p);
    }

    public void updateProgramme(Programme p) {
        DBAdapter db = new DBAdapter(context).open();

        ContentValues values = new ContentValues();
        values.put("nom", p.getNom());
        values.put("description", p.getCommentaire());

        db.update("programme", values, p.getId());
        db.close();
    }

    /*
    public void saveExercicesForProgramme(Programme p) {
        DBAdapter db = new DBAdapter(context).open();
        long programmeId = p.getId();

        db.execSQL("DELETE FROM programme_exercice WHERE id_programme=" + programmeId);
        int ordre = 0;

        for (Exercice ex : p.getExercices()) {
            ContentValues values = new ContentValues();
            values.put("id_programme", programmeId);
            values.put("id_exercice", ex.getId());
            values.put("nb_series_defaut", ex.getSeries().size());
            values.put("nb_reps_defaut", ex.getSeries().isEmpty() ? 0 : ex.getSeries().get(0).getRepetitions());
            values.put("poids_min", 0);
            values.put("poids_max", 0);

            db.insert("programme_exercice", values);
            ordre++;
        }

        db.close();
    }
    */
    public Programme getProgrammeById(long id) {
        for (Programme p : programmes) {
            if (p.getId() == id) return p;
        }
        return null;
    }
}
