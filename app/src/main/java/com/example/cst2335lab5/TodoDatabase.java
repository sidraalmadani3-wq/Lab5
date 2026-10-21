package com.example.cst2335lab5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "todos.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_TODOS = "todos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "todo_text";
    public static final String COLUMN_URGENT = "urgent";

    public TodoDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TODOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEXT + " TEXT NOT NULL, " +
                COLUMN_URGENT + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        onCreate(db);
    }

    public long addTodo(String text, boolean urgent) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_URGENT, urgent ? 1 : 0);
        return getWritableDatabase().insert(TABLE_TODOS, null, values);
    }

    public Cursor getTodos() {
        return getReadableDatabase().query(TABLE_TODOS,
                new String[]{COLUMN_ID, COLUMN_TEXT, COLUMN_URGENT},
                null, null, null, null, COLUMN_ID + " ASC");
    }

    public int deleteTodo(long id) {
        return getWritableDatabase().delete(TABLE_TODOS, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
    }
}
