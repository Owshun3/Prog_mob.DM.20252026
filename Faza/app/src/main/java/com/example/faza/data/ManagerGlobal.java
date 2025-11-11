package com.example.faza.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.faza.data.entites.*;

import java.util.ArrayList;
import java.util.List;

public class ManagerGlobal {
    private final DatabaseHelper dbHelper;

    public ManagerGlobal(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }
    /*
    public long insertProgramme(Programme p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", p.getNom());
        values.put("description", p.getDescription());
        values.put("date_creation", p.getDateCreation());
        long id = db.insert("programme", null, values);
        db.close();
        return id;
    }

    public List<Programme> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Programme> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT id, nom, description, date_creation FROM programme", null);
        while (c.moveToNext()) {
            Programme p = new Programme();
            p.setId(c.getLong(0));
            p.setNom(c.getString(1));
            p.setDescription(c.getString(2));
            p.setDateCreation(c.getString(3));
            list.add(p);
        }
        c.close();
        db.close();
        return list;
    }
    */
    public void delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("programme", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
