package com.example.faza.data.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.faza.data.DatabaseHelper;
import com.example.faza.data.entites.Programme;
import java.util.ArrayList;

public class ManagerProgramme {

    private final Context context;
    private final ArrayList<Programme> programmes = new ArrayList<>();

    public ManagerProgramme(Context ctx) {
        this.context = ctx;
        chargerDepuisBD();
    }

    public ArrayList<Programme> getProgrammes() {
        return programmes;
    }

    public Programme creerProgramme(Context ctx, String nom) {

        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("description", "");
        values.put("date_creation", "");

        long id = DatabaseHelper.getInstance(ctx)
                .getWritableDatabase()
                .insert(DatabaseHelper.T_PROGRAMME, null, values);

        Programme p = new Programme();
        p.setId(id);
        p.setNom(nom);

        programmes.add(p);
        return p;
    }

    public Programme getProgrammeById(long id) {
        for (Programme p : programmes)
            if (p.getId() == id)
                return p;

        return null;
    }

    public void supprimerProgramme(Programme p) {

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        db.execSQL("DELETE FROM programme_exercice WHERE id_programme=" + p.getId());
        db.delete(DatabaseHelper.T_PROGRAMME, "id = ?", new String[]{String.valueOf(p.getId())});

        programmes.remove(p);
    }

    public void sauvegarderProgramme(Programme p) {
        ContentValues values = new ContentValues();
        values.put("nom", p.getNom());
        values.put("description", p.getCommentaire());

        DatabaseHelper.getInstance(context)
                .getWritableDatabase()
                .update(DatabaseHelper.T_PROGRAMME,
                        values,
                        "id=?",
                        new String[]{String.valueOf(p.getId())});
    }

    private void chargerDepuisBD() {
        programmes.clear();

        Cursor c = DatabaseHelper.getInstance(context)
                .getReadableDatabase()
                .rawQuery("SELECT id, nom FROM programme ORDER BY id DESC", null);

        while (c.moveToNext()) {
            Programme p = new Programme();
            p.setId(c.getLong(0));
            p.setNom(c.getString(1));

            programmes.add(p);
        }
        c.close();
    }
}

