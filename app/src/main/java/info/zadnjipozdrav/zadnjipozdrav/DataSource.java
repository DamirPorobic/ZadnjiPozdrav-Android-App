package info.zadnjipozdrav.zadnjipozdrav;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDBHelper;

    private String[] mObituaryColumns = {
            DatabaseHelper.COL_OB_ID,
            DatabaseHelper.COL_OB_NAME,
            DatabaseHelper.COL_OB_FAMILYNAME,
            DatabaseHelper.COL_OB_FATHERSNAME,
            DatabaseHelper.COL_OB_MAIDENNAME,
            DatabaseHelper.COL_OB_BIRTHDATE,
            DatabaseHelper.COL_OB_DEATHDATE,
            DatabaseHelper.COL_OB_TEXT,
            DatabaseHelper.COL_OB_FUNERALDATE,
            DatabaseHelper.COL_OB_FUNERALTIME,
            DatabaseHelper.COL_OB_PICTURE,
            DatabaseHelper.COL_OB_RELIGION,
            DatabaseHelper.COL_OB_CEMETERY_ID,
            DatabaseHelper.COL_OB_BOROUGH_ID};

    private String[] mCemeteryColumns = {
            DatabaseHelper.COL_CE_ID,
            DatabaseHelper.COL_CE_NAME,
            DatabaseHelper.COL_CE_LAT,
            DatabaseHelper.COL_CE_LON};

    private String[] mBoroughColumns = {
            DatabaseHelper.COL_BO_ID,
            DatabaseHelper.COL_BO_NAME};

    public DataSource(Context context) {
        mDBHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public void close() {
        mDBHelper.close();
    }

    public Obituary createObituary(String name, String familyName, String fathersName,
                                   String maidenName, long birthDate, long deathDate, String text,
                                   long funeralDate, long funeralTime, byte[] picture,
                                   String religion, long cemeteryId, long boroughId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_OB_NAME, name);
        values.put(DatabaseHelper.COL_OB_FAMILYNAME, familyName);
        values.put(DatabaseHelper.COL_OB_FATHERSNAME, fathersName);
        values.put(DatabaseHelper.COL_OB_MAIDENNAME, maidenName);
        values.put(DatabaseHelper.COL_OB_BIRTHDATE, birthDate);
        values.put(DatabaseHelper.COL_OB_DEATHDATE, deathDate);
        values.put(DatabaseHelper.COL_OB_TEXT, text);
        values.put(DatabaseHelper.COL_OB_FUNERALDATE, funeralDate);
        values.put(DatabaseHelper.COL_OB_FUNERALTIME, funeralTime);
        values.put(DatabaseHelper.COL_OB_PICTURE, picture);
        values.put(DatabaseHelper.COL_OB_RELIGION, religion);
        values.put(DatabaseHelper.COL_OB_CEMETERY_ID, cemeteryId);
        values.put(DatabaseHelper.COL_OB_BOROUGH_ID, boroughId);

        long insertedId = mDatabase.insert(DatabaseHelper.TABLE_OBITUARIES, null, values);
        return getObituary(insertedId);
    }

    public Cemetery createCemetery(long id, String name, String address, double lat, double lon) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CE_ID, id);
        values.put(DatabaseHelper.COL_CE_NAME, name);
        values.put(DatabaseHelper.COL_CE_ADDRESS, address);
        values.put(DatabaseHelper.COL_CE_LAT, lat);
        values.put(DatabaseHelper.COL_CE_LON, lon);

        long insertedId = mDatabase.insert(DatabaseHelper.TABLE_CEMETERIES, null, values);
        return getCemetery(insertedId);
    }

    public Borough createBorough(long id, String name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_BO_ID, id);
        values.put(DatabaseHelper.COL_BO_NAME, name);

        long insertedId = mDatabase.insert(DatabaseHelper.TABLE_BOROUGHS, null, values);
        return getBorough(insertedId);
    }

    public Obituary getObituary(long id) {
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_OBITUARIES, mObituaryColumns,
                DatabaseHelper.COL_OB_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        Obituary obituary = cursorToObituary(cursor);
        cursor.close();
        return obituary;
    }

    public Cemetery getCemetery(long id) {
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_CEMETERIES, mCemeteryColumns,
                DatabaseHelper.COL_CE_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        Cemetery cemetery = cursorToCemetery(cursor);
        cursor.close();
        return cemetery;
    }

    public Borough getBorough(long id) {
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_BOROUGHS, mBoroughColumns,
                DatabaseHelper.COL_BO_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        Borough borough = cursorToBorough(cursor);
        cursor.close();
        return borough;
    }

    public void deleteObituary(Obituary obituary) {
        long id = obituary.getId();
        mDatabase.delete(DatabaseHelper.TABLE_OBITUARIES, DatabaseHelper.COL_OB_ID + " = " + id, null);
    }

    public List<Obituary> getAllObituaries() {
        List<Obituary> obituaries = new ArrayList<Obituary>();
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_OBITUARIES, mObituaryColumns, null,
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
        obituary.setCemetery(getCemetery(cursor.getLong(12)));
        obituary.setBorough(getBorough(cursor.getLong(13)));

        return obituary;
    }

    private Cemetery cursorToCemetery(Cursor cursor) {
        Cemetery cemetery = new Cemetery();
        cemetery.setId(cursor.getLong(0));
        cemetery.setName(cursor.getString(1));
        cemetery.setAddress(cursor.getString(2));
        cemetery.setLat(cursor.getLong(3));
        cemetery.setLon(cursor.getLong(4));

        return cemetery;
    }

    private Borough cursorToBorough(Cursor cursor) {
        Borough borough = new Borough();
        borough.setId(cursor.getLong(0));
        borough.setName(cursor.getString(1));

        return borough;
    }
}
