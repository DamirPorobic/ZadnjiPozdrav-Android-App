package info.zadnjipozdrav.zadnjipozdrav;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private SQLiteDatabase mDataBase;
    private DatabaseHelper mDbHelper;
    private String[] mAllColumns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NAME,
            DatabaseHelper.COLUMN_FAMILYNAME,
            DatabaseHelper.COLUMN_FATHERSNAME,
            DatabaseHelper.COLUMN_MAIDENNAME,
            DatabaseHelper.COLUMN_BIRTHDATE,
            DatabaseHelper.COLUMN_DEATHDATE,
            DatabaseHelper.COLUMN_TEXT,
            DatabaseHelper.COLUMN_FUNERALDATE,
            DatabaseHelper.COLUMN_FUNERALTIME,
            DatabaseHelper.COLUMN_PICTURE,
            DatabaseHelper.COLUMN_RELIGION,
            DatabaseHelper.COLUMN_CEMETERYNAME,
            DatabaseHelper.COLUMN_CEMETERYADDRESS,
            DatabaseHelper.COLUMN_CEMETERYLAT,
            DatabaseHelper.COLUMN_CEMETERYLON };

    public DataSource(Context context) {
        mDbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        mDataBase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Obituary createObituary(String name, String familyName, String fathersName,
                                   String maidenName, long birthDate, long deathDate, String text,
                                   long funeralDate, long funeralTime, byte[] picture,
                                   String religion, String cemeteryName, String cemeteryAddress,
                                   double cemeteryLat, double cemeteryLon) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_FAMILYNAME, familyName);
        values.put(DatabaseHelper.COLUMN_FATHERSNAME, fathersName);
        values.put(DatabaseHelper.COLUMN_MAIDENNAME, maidenName);
        values.put(DatabaseHelper.COLUMN_BIRTHDATE, birthDate);
        values.put(DatabaseHelper.COLUMN_DEATHDATE, deathDate);
        values.put(DatabaseHelper.COLUMN_TEXT, text);
        values.put(DatabaseHelper.COLUMN_FUNERALDATE, funeralDate);
        values.put(DatabaseHelper.COLUMN_FUNERALTIME, funeralTime);
        values.put(DatabaseHelper.COLUMN_PICTURE, picture);
        values.put(DatabaseHelper.COLUMN_RELIGION, religion);
        values.put(DatabaseHelper.COLUMN_CEMETERYNAME, cemeteryName);
        values.put(DatabaseHelper.COLUMN_CEMETERYADDRESS, cemeteryAddress);
        values.put(DatabaseHelper.COLUMN_CEMETERYLAT, cemeteryLat);
        values.put(DatabaseHelper.COLUMN_CEMETERYLON, cemeteryLon);

        long insertedId = mDataBase.insert(DatabaseHelper.TABLE_OBITUARIES, null, values);
        Cursor cursor = mDataBase.query(DatabaseHelper.TABLE_OBITUARIES, mAllColumns,
                DatabaseHelper.COLUMN_ID + " = " + insertedId, null, null, null, null);
        cursor.moveToFirst();
        Obituary newObituary = cursorToObituary(cursor);
        cursor.close();
        return newObituary;
    }

    public void deleteObituary(Obituary obituary) {
        long id = obituary.getId();
        mDataBase.delete(DatabaseHelper.TABLE_OBITUARIES, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Obituary> getAllObituaries() {
        List<Obituary> obituaries = new ArrayList<Obituary>();
        Cursor cursor = mDataBase.query(DatabaseHelper.TABLE_OBITUARIES, mAllColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Obituary obituary = cursorToObituary(cursor);
            obituaries.add(obituary);
            cursor.moveToNext();
        }

        cursor.close();
        return obituaries;
    }

    private Obituary cursorToObituary(Cursor cursor) {
        Obituary obituary = new Obituary();
        obituary.setId(cursor.getLong(0));
        obituary.setName(cursor.getString(1));
        obituary.setFamilyName(cursor.getString(2));
        obituary.setFathersName(cursor.getString(3));
        obituary.setMaidenName(cursor.getString(4));
        obituary.setBirthDate(cursor.getLong(5));
        obituary.setDeathDate(cursor.getLong(6));
        obituary.setText(cursor.getString(7));
        obituary.setFuneralDate(cursor.getLong(8));
        obituary.setFuneralTime(cursor.getLong(9));
        obituary.setPicture(cursor.getBlob(10));
        obituary.setReligion(cursor.getString(11));
        obituary.setCemeteryName(cursor.getString(12));
        obituary.setCemeteryAddress(cursor.getString(13));
        obituary.setCemeteryLat(cursor.getDouble(14));
        obituary.setCemeteryLon(cursor.getDouble(15));

        return obituary;
    }
}
