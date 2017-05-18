package com.star.sqlitebestpractice;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;


public class DatabaseProvider extends ContentProvider {

    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;

    public static final String AUTHORITY = "com.star.sqlitebestpractice.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri BOOK_CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "book");
    public static final Uri CATEGORY_CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "category");

    public static final String TYPE_DIR = "vnd.android.cursor.dir/vnd.";
    public static final String TYPE_ITEM = "vnd.android.cursor.item/vnd.";
    public static final String TYPE_BOOK_DIR = TYPE_DIR + AUTHORITY + ".book";
    public static final String TYPE_BOOK_ITEM = TYPE_ITEM + AUTHORITY + ".book";
    public static final String TYPE_CATEGORY_DIR = TYPE_DIR + AUTHORITY + ".category";
    public static final String TYPE_CATEGORY_ITEM = TYPE_ITEM + AUTHORITY + ".category";

    private static UriMatcher sUriMatcher;

    private MyDatabaseHelper mMyDatabaseHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        sUriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        sUriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        sUriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    public DatabaseProvider() {
    }

    @Override
    public boolean onCreate() {

        mMyDatabaseHelper = new MyDatabaseHelper(getContext(), MainActivity.DATABASE_BOOK_STORE,
                null, 3);

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase sqLiteDatabase = mMyDatabaseHelper.getWritableDatabase();

        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {

            case BOOK_DIR:
                cursor = sqLiteDatabase.query(MyDatabaseHelper.TABLE_BOOK, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = sqLiteDatabase.query(MyDatabaseHelper.TABLE_BOOK, projection,
                        MyDatabaseHelper.COLUMN_ID + " = ?", new String[] { bookId },
                        null, null, sortOrder);
                break;

            case CATEGORY_DIR:
                cursor = sqLiteDatabase.query(MyDatabaseHelper.TABLE_CATEGORY, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = sqLiteDatabase.query(MyDatabaseHelper.TABLE_CATEGORY, projection,
                        MyDatabaseHelper.COLUMN_CATEGORY_ID + " = ?", new String[] { categoryId },
                        null, null, sortOrder);
                break;

            default:
        }

        return cursor;

    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        SQLiteDatabase sqLiteDatabase = mMyDatabaseHelper.getWritableDatabase();

        Uri uriReturn = null;

        switch (sUriMatcher.match(uri)) {

            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = sqLiteDatabase.insert(MyDatabaseHelper.TABLE_BOOK,
                        null, values);
                uriReturn = ContentUris.withAppendedId(BOOK_CONTENT_URI, newBookId);
                break;

            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = sqLiteDatabase.insert(MyDatabaseHelper.TABLE_CATEGORY,
                        null, values);
                uriReturn = ContentUris.withAppendedId(CATEGORY_CONTENT_URI, newCategoryId);
                break;

            default:
        }

        return uriReturn;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = mMyDatabaseHelper.getWritableDatabase();

        int updatedRows = 0;

        switch (sUriMatcher.match(uri)) {

            case BOOK_DIR:
                updatedRows = sqLiteDatabase.update(MyDatabaseHelper.TABLE_BOOK, values,
                        selection, selectionArgs);
                break;

            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updatedRows = sqLiteDatabase.update(MyDatabaseHelper.TABLE_BOOK, values,
                        MyDatabaseHelper.COLUMN_ID + " = ?", new String[] { bookId });
                break;

            case CATEGORY_DIR:
                updatedRows = sqLiteDatabase.update(MyDatabaseHelper.TABLE_CATEGORY, values,
                        selection, selectionArgs);
                break;

            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updatedRows = sqLiteDatabase.update(MyDatabaseHelper.TABLE_CATEGORY, values,
                        MyDatabaseHelper.COLUMN_CATEGORY_ID + " = ?", new String[] { categoryId });
                break;

            default:
        }

        return updatedRows;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = mMyDatabaseHelper.getWritableDatabase();

        int deletedRows = 0;

        switch (sUriMatcher.match(uri)) {

            case BOOK_DIR:
                deletedRows = sqLiteDatabase.delete(MyDatabaseHelper.TABLE_BOOK,
                        selection, selectionArgs);
                break;

            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deletedRows = sqLiteDatabase.delete(MyDatabaseHelper.TABLE_BOOK,
                        MyDatabaseHelper.COLUMN_ID + " = ?", new String[] { bookId });
                break;

            case CATEGORY_DIR:
                deletedRows = sqLiteDatabase.delete(MyDatabaseHelper.TABLE_CATEGORY,
                        selection, selectionArgs);
                break;

            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deletedRows = sqLiteDatabase.delete(MyDatabaseHelper.TABLE_CATEGORY,
                        MyDatabaseHelper.COLUMN_CATEGORY_ID + " = ?", new String[] { categoryId });
                break;

            default:
        }

        return deletedRows;
    }

    @Override
    public String getType(@NonNull Uri uri) {

        switch (sUriMatcher.match(uri)) {

            case BOOK_DIR:
                return TYPE_BOOK_DIR;

            case BOOK_ITEM:
                return TYPE_BOOK_ITEM;

            case CATEGORY_DIR:
                return TYPE_CATEGORY_DIR;

            case CATEGORY_ITEM:
                return TYPE_CATEGORY_ITEM;

            default:
        }

        return null;
    }
}
