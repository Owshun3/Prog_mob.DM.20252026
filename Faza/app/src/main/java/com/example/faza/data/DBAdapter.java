package com.example.faza.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter {
    private final Context context;
    private SQLiteDatabase db;
    private final DatabaseHelper dbHelper;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        dbHelper = DatabaseHelper.getInstance(ctx);
    }

    public DBAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(String tableName, ContentValues values) {
        return db.insert(tableName, null, values);
    }

    public int update(String tableName, ContentValues values, long id) {
        return db.update(tableName, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public int delete(String tableName, long id) {
        return db.delete(tableName, "id = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getById(String tableName, long id) {
        return db.query(tableName, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getAll(String tableName) {
        return db.query(tableName, null, null, null, null, null, "id ASC");
    }

    public Cursor rawQuery(String sql, String[] args) {
        return db.rawQuery(sql, args);
    }

    public long insertProgramme(String nom, String description, String dateCreation) {
        ContentValues values = new ContentValues();
        values.put("nom", nom);
        values.put("description", description);
        values.put("date_creation", dateCreation);
        return insert("programme", values);
    }

    public List<String> getAllProgrammesNames() {
        List<String> result = new ArrayList<>();
        Cursor c = getAll("programme");
        while (c.moveToNext()) {
            result.add(c.getString(c.getColumnIndexOrThrow("nom")));
        }
        c.close();
        return result;
    }
}
