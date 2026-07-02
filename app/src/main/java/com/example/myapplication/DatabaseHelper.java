package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rocket_settings.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SETTINGS = "settings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SORT_OPTION = "sort_option"; // 0 - новые, 1 - старые, 2 - перенесенные

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SETTINGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SORT_OPTION + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);

        // Вставляем дефолтную настройку при первом создании БД
        ContentValues values = new ContentValues();
        values.put(COLUMN_SORT_OPTION, 0);
        db.insert(TABLE_SETTINGS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);
    }

    // Метод для сохранения опции сортировки
    public void saveSortOption(int option) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SORT_OPTION, option);

        // Обновляем первую строку (id = 1)
        db.update(TABLE_SETTINGS, values, COLUMN_ID + " = ?", new String[]{"1"});
        db.close();
    }

    // Метод для получения текущей опции сортировки
    public int getSortOption() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SETTINGS, new String[]{COLUMN_SORT_OPTION},
                COLUMN_ID + " = ?", new String[]{"1"}, null, null, null);

        int option = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                option = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return option;
    }
}