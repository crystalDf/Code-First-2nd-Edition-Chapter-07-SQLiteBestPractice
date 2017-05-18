package com.star.sqlitebestpractice;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    public static final String DATABASE_BOOK_STORE = "BookStore.db";

    private MyDatabaseHelper mMyDatabaseHelper;

    private Button mCreateDatabaseButton;
    private Button mInsertDataButton;
    private Button mUpdateDataButton;
    private Button mDeleteDataButton;
    private Button mQueryDataButton;
    private Button mReplaceDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyDatabaseHelper = new MyDatabaseHelper(this, DATABASE_BOOK_STORE, null, 3);

        mCreateDatabaseButton = (Button) findViewById(R.id.create_database);
        mCreateDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyDatabaseHelper.getWritableDatabase();
            }
        });

        mInsertDataButton = (Button) findViewById(R.id.insert_data);
        mInsertDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sqLiteDatabase = mMyDatabaseHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();

                contentValues.put(MyDatabaseHelper.COLUMN_NAME, "The Da Vinci Code");
                contentValues.put(MyDatabaseHelper.COLUMN_AUTHOR, "Dan Brown");
                contentValues.put(MyDatabaseHelper.COLUMN_PAGES, 454);
                contentValues.put(MyDatabaseHelper.COLUMN_PRICE, 16.96);

                sqLiteDatabase.insert(MyDatabaseHelper.TABLE_BOOK, null, contentValues);

                contentValues.clear();

                contentValues.put(MyDatabaseHelper.COLUMN_NAME, "The Lost Symbol");
                contentValues.put(MyDatabaseHelper.COLUMN_AUTHOR, "Dan Brown");
                contentValues.put(MyDatabaseHelper.COLUMN_PAGES, 510);
                contentValues.put(MyDatabaseHelper.COLUMN_PRICE, 19.95);

                sqLiteDatabase.insert(MyDatabaseHelper.TABLE_BOOK, null, contentValues);

            }
        });

        mUpdateDataButton = (Button) findViewById(R.id.update_data);
        mUpdateDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sqLiteDatabase = mMyDatabaseHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();

                contentValues.put(MyDatabaseHelper.COLUMN_PRICE, 10.99);

                sqLiteDatabase.update(MyDatabaseHelper.TABLE_BOOK, contentValues,
                        MyDatabaseHelper.COLUMN_NAME + " = ?", new String[] {"The Da Vinci Code"});
            }
        });

        mDeleteDataButton = (Button) findViewById(R.id.delete_data);
        mDeleteDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sqLiteDatabase = mMyDatabaseHelper.getWritableDatabase();

                sqLiteDatabase.delete(MyDatabaseHelper.TABLE_BOOK,
                        MyDatabaseHelper.COLUMN_PAGES + " > ?", new String[] {"500"});
            }
        });

        mQueryDataButton = (Button) findViewById(R.id.query_data);
        mQueryDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sqLiteDatabase = mMyDatabaseHelper.getWritableDatabase();

                Cursor cursor = sqLiteDatabase.query(MyDatabaseHelper.TABLE_BOOK,
                        null, null, null, null, null, null);

                if (cursor != null) {

                    while (cursor.moveToNext()) {

                        String name = cursor.getString(cursor.getColumnIndex(
                                MyDatabaseHelper.COLUMN_NAME));
                        String author = cursor.getString(cursor.getColumnIndex(
                                MyDatabaseHelper.COLUMN_AUTHOR));
                        int pages = cursor.getInt(cursor.getColumnIndex(
                                MyDatabaseHelper.COLUMN_PAGES));
                        double price = cursor.getDouble(cursor.getColumnIndex(
                                MyDatabaseHelper.COLUMN_PRICE));

                        Log.d(TAG, "Book name is " + name);
                        Log.d(TAG, "Book author is " + author);
                        Log.d(TAG, "Book pages is " + pages);
                        Log.d(TAG, "Book price is " + price);
                    }

                    cursor.close();
                }
            }
        });

        mReplaceDataButton = (Button) findViewById(R.id.replace_data);
        mReplaceDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sqLiteDatabase = mMyDatabaseHelper.getWritableDatabase();

                sqLiteDatabase.beginTransaction();

                try {
                    sqLiteDatabase.delete(MyDatabaseHelper.TABLE_BOOK, null, null);
                    if (false) {
                        throw new NullPointerException();
                    }

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(MyDatabaseHelper.COLUMN_NAME, "Game of Thrones");
                    contentValues.put(MyDatabaseHelper.COLUMN_AUTHOR, "George Martin");
                    contentValues.put(MyDatabaseHelper.COLUMN_PAGES, 720);
                    contentValues.put(MyDatabaseHelper.COLUMN_PRICE, 20.85);

                    sqLiteDatabase.insert(MyDatabaseHelper.TABLE_BOOK, null, contentValues);

                    sqLiteDatabase.setTransactionSuccessful();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
            }
        });
    }
}
