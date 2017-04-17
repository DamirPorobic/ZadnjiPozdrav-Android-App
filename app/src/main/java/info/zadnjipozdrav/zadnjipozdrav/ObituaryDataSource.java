package info.zadnjipozdrav.zadnjipozdrav;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ObituaryDataSource {

    private SQLiteDatabase mDataBase;
    private CustomSQLiteHelper mDbHelper;
    private String[] mAllColumns = {
            CustomSQLiteHelper.COLUMN_ID,
            CustomSQLiteHelper.COLUMN_NAME,
            CustomSQLiteHelper.COLUMN_FAMILYNAME,
            CustomSQLiteHelper.COLUMN_FATHERSNAME,
            CustomSQLiteHelper.COLUMN_MAIDENNAME,
            CustomSQLiteHelper.COLUMN_BIRTHDATE,
            CustomSQLiteHelper.COLUMN_DEATHDATE,
            CustomSQLiteHelper.COLUMN_TEXT,
            CustomSQLiteHelper.COLUMN_FUNERALDATE,
            CustomSQLiteHelper.COLUMN_FUNERALTIME,
            CustomSQLiteHelper.COLUMN_PICTURE,
            CustomSQLiteHelper.COLUMN_RELIGION,
            CustomSQLiteHelper.COLUMN_CEMETERYNAME,
            CustomSQLiteHelper.COLUMN_CEMETERYADDRESS,
            CustomSQLiteHelper.COLUMN_CEMETERYLAT,
            CustomSQLiteHelper.COLUMN_CEMETERYLON };

    public ObituaryDataSource(Context context) {
        mDbHelper = new CustomSQLiteHelper(context);
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
        values.put(CustomSQLiteHelper.COLUMN_NAME, name);
        values.put(CustomSQLiteHelper.COLUMN_FAMILYNAME, familyName);
        values.put(CustomSQLiteHelper.COLUMN_FATHERSNAME, fathersName);
        values.put(CustomSQLiteHelper.COLUMN_MAIDENNAME, maidenName);
        values.put(CustomSQLiteHelper.COLUMN_BIRTHDATE, birthDate);
        values.put(CustomSQLiteHelper.COLUMN_DEATHDATE, deathDate);
        values.put(CustomSQLiteHelper.COLUMN_TEXT, text);
        values.put(CustomSQLiteHelper.COLUMN_FUNERALDATE, funeralDate);
        values.put(CustomSQLiteHelper.COLUMN_FUNERALTIME, funeralTime);
        values.put(CustomSQLiteHelper.COLUMN_PICTURE, picture);
        values.put(CustomSQLiteHelper.COLUMN_RELIGION, religion);
        values.put(CustomSQLiteHelper.COLUMN_CEMETERYNAME, cemeteryName);
        values.put(CustomSQLiteHelper.COLUMN_CEMETERYADDRESS, cemeteryAddress);
        values.put(CustomSQLiteHelper.COLUMN_CEMETERYLAT, cemeteryLat);
        values.put(CustomSQLiteHelper.COLUMN_CEMETERYLON, cemeteryLon);

        long insertedId = mDataBase.insert(CustomSQLiteHelper.TABLE_OBITUARIES, null, values);
        Cursor cursor = mDataBase.query(CustomSQLiteHelper.TABLE_OBITUARIES, mAllColumns,
                CustomSQLiteHelper.COLUMN_ID + " = " + insertedId, null, null, null, null);
        cursor.moveToFirst();
        Obituary newObituary = cursorToObituary(cursor);
        cursor.close();
        return newObituary;
    }

    public void deleteObituary(Obituary obituary) {
        long id = obituary.getId();
        mDataBase.delete(CustomSQLiteHelper.TABLE_OBITUARIES, CustomSQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Obituary> getAllObituaries() {
        List<Obituary> obituaries = new ArrayList<Obituary>();
        Cursor cursor = mDataBase.query(CustomSQLiteHelper.TABLE_OBITUARIES, mAllColumns, null,
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
        obituary.setmPicture(cursor.getBlob(10));
        obituary.setReligion(cursor.getString(11));
        obituary.setCemeteryName(cursor.getString(12));
        obituary.setCemeteryAddress(cursor.getString(13));
        obituary.setCemeteryLat(cursor.getDouble(14));
        obituary.setCemeteryLon(cursor.getDouble(15));

        return obituary;
    }
}
