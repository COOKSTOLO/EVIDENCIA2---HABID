package com.example.acividad8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "listados.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ITEMS = "items";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_SEASONS = "seasons";
    private static final String COL_RELEASE_DATE = "releaseDate";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_ITEMS + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, " + COL_SEASONS + " INTEGER, " + COL_RELEASE_DATE + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public long addItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, item.getTitle());
        cv.put(COL_SEASONS, item.getSeasons());
        cv.put(COL_RELEASE_DATE, item.getReleaseDate());
        long id = db.insert(TABLE_ITEMS, null, cv);
        db.close();
        return id;
    }

    public int updateItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, item.getTitle());
        cv.put(COL_SEASONS, item.getSeasons());
        cv.put(COL_RELEASE_DATE, item.getReleaseDate());
        int rows = db.update(TABLE_ITEMS, cv, COL_ID + "=?", new String[]{String.valueOf(item.getId())});
        db.close();
        return rows;
    }

    public int deleteItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_ITEMS, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public List<Item> getAllItems() {
        List<Item> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_ITEMS, null, null, null, null, null, COL_ID + " DESC");
        if (c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(COL_ID));
                String title = c.getString(c.getColumnIndexOrThrow(COL_TITLE));
                int seasons = c.getInt(c.getColumnIndexOrThrow(COL_SEASONS));
                String date = c.getString(c.getColumnIndexOrThrow(COL_RELEASE_DATE));
                list.add(new Item(id, title, seasons, date));
            }
            c.close();
        }
        db.close();
        return list;
    }
}

