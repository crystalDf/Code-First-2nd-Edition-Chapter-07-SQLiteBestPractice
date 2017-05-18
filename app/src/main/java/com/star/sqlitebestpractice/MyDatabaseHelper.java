package com.star.sqlitebestpractice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_BOOK = "Book";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_PAGES = "pages";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FOREIGN_CATEGORY_ID = "category_id";

    public static final String TABLE_CATEGORY = "Category";
    public static final String COLUMN_CATEGORY_ID = COLUMN_ID;
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_CATEGORY_CODE = "category_code";

    public static final String CREATE_BOOK = "create table " + TABLE_BOOK + " (" +
            COLUMN_ID + " integer primary key autoincrement" + ", " +
            COLUMN_AUTHOR + " text" + ", " +
            COLUMN_PRICE + " real" + ", " +
            COLUMN_PAGES + " integer" + ", " +
            COLUMN_NAME + " text" + ", " +
            COLUMN_FOREIGN_CATEGORY_ID + " integer" +
            ")";

    public static final String CREATE_CATEGORY = "create table " + TABLE_CATEGORY + " (" +
            COLUMN_CATEGORY_ID + " integer primary key autoincrement" + ", " +
            COLUMN_CATEGORY_NAME + " text" + ", " +
            COLUMN_CATEGORY_CODE + " integer" +
            ")";

    public static final String ALTER_BOOK = "alter table " + TABLE_BOOK + " add column " +
            COLUMN_FOREIGN_CATEGORY_ID + " integer";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_CATEGORY);
            case 2:
                db.execSQL(ALTER_BOOK);
            default:
        }
    }
}
