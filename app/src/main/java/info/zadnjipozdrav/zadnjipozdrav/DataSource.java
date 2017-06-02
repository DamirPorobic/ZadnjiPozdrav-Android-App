package info.zadnjipozdrav.zadnjipozdrav;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
            DatabaseHelper.COL_CE_ADDRESS,
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

    /**
     * Adds Obituary entry to SQLite database.
     * @param id           ID that should be used for the new entry.
     * @param name         First Name.
     * @param familyName   Family Name
     * @param fathersName  Name of the Father.
     * @param maidenName   Maiden Name in case of female person.
     * @param birthDate    Date of birth.
     * @param deathDate    Date of death.
     * @param text         Free text, ususaly used for last ferwalls.
     * @param funeralDate  Date of funeral.
     * @param funeralTime  Time of funeral.
     * @param picture      Picture of the person.
     * @param religion     ID of religion, to be populated from other table.
     * @param cemeteryId   ID of cemetery where the funeral will take place.
     * @param boroughId    ID of borough where the person was registered as death.
     * @return             When successful, returns the ID of the new row, otherwise returns -1.
     */
    public long createObituary(long id, String name, String familyName, String fathersName,
                                   String maidenName, long birthDate, long deathDate, String text,
                                   long funeralDate, long funeralTime, byte[] picture,
                                   int religion, long cemeteryId, long boroughId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_OB_ID, id);
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

        return mDatabase.replace(DatabaseHelper.TABLE_OBITUARIES, null, values);
    }

    /**
     * Adds cemetery entry to the SQLite database.
     *
     * @param id       ID that should be used for the new entry.
     * @param name     Name for the new Borough.
     * @param address  Real address of the Borough.
     * @param lat      Latitude.
     * @param lon      Longitude.
     * @return         When successful, returns the ID of the new row, otherwise returns -1.
     */
    public long createCemetery(long id, String name, String address, double lat, double lon) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CE_ID, id);
        values.put(DatabaseHelper.COL_CE_NAME, name);
        values.put(DatabaseHelper.COL_CE_ADDRESS, address);
        values.put(DatabaseHelper.COL_CE_LAT, lat);
        values.put(DatabaseHelper.COL_CE_LON, lon);

        return mDatabase.replace(DatabaseHelper.TABLE_CEMETERIES, null, values);
    }

    /**
     * Adds Borough entry to SQLite database.
     *
     * @param id    ID of new added borough, this id will be used in the database.
     * @param name  Name of the new borough.
     * @return      When successful, returns the ID of the new row, otherwise returns -1.
     */
    public long createBorough(long id, String name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_BO_ID, id);
        values.put(DatabaseHelper.COL_BO_NAME, name);

        return mDatabase.replace(DatabaseHelper.TABLE_BOROUGHS, null, values);
    }

    /**
     * Returns Obituary object for provided is.
     * @param id  ID of Obituary that is required.
     * @return    Returns Obituary object or null when the id could not be found or in case od error
     */
    public Obituary getObituary(long id) {
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_OBITUARIES, mObituaryColumns,
                DatabaseHelper.COL_OB_ID + " = " + id, null, null, null, null);

        Obituary obituary = null;
        if (cursor.moveToFirst()) {
            obituary = cursorToObituary(cursor);
        }
        cursor.close();
        return obituary;
    }

    /**
     * Returns Cemetery object for provided is.
     * @param id  ID of Cemetery that is required.
     * @return    Returns Cemetery object or null when the id could not be found or in case od error
     */
    public Cemetery getCemetery(long id) {
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_CEMETERIES, mCemeteryColumns,
                DatabaseHelper.COL_CE_ID + " = " + id, null, null, null, null);

        Cemetery cemetery = null;
        if (cursor.moveToFirst()) {
            cemetery = cursorToCemetery(cursor);
        }
        cursor.close();
        return cemetery;
    }

    /**
     * Returns Borough object for provided is.
     * @param id  ID of Borough that is required.
     * @return    Returns Borough object or null when the id could not be found or in case od error
     */
    public Borough getBorough(long id) {
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_BOROUGHS, mBoroughColumns,
                DatabaseHelper.COL_BO_ID + " = " + id, null, null, null, null);

        Borough borough = null;
        if (cursor.moveToFirst()) {
            borough = cursorToBorough(cursor);
        }
        cursor.close();
        return borough;
    }

    /**
     * Deletes row in Obituary table with matching ID.
     * @param id  ID of row that should be deleted.
     * @return    True is successful, otherwise false.
     */
    public boolean deleteObituary(long id) {
        int result = mDatabase.delete(DatabaseHelper.TABLE_OBITUARIES, DatabaseHelper.COL_OB_ID + " = " + id, null);
        return (result > 0);
    }

    /**
     * Provides a list with all Obituaries.
     * @return   Returns a list with all Obituaries, in case of error returns empty List.
     */
    public List<Obituary> getAllObituaries() {
        List<Obituary> obituaries = new ArrayList<>();
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_OBITUARIES, mObituaryColumns, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Obituary obituary = cursorToObituary(cursor);
                if (obituary != null) {
                    obituaries.add(obituary);
                }
                cursor.moveToNext();
            }
        }

        cursor.close();
        return obituaries;
    }

    /**
     * Provides a list with all Boroughs.
     * @return   Returns a list with all Boroughs, in case of error returns empty List.
     */
    public List<Borough> getAllBorough() {
        List<Borough> boroughs = new ArrayList<>();
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_BOROUGHS, mBoroughColumns, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Borough borough = cursorToBorough(cursor);
                if (borough != null) {
                    boroughs.add(borough);
                }
                cursor.moveToNext();
            }
        }

        cursor.close();
        return boroughs;
    }

    /**
     * Parses first row cursor into Obituary object.
     * @param cursor  Cursor from which the entries for the obituary will be taken.
     * @return        Obituary object or null in case the operation failed.
     */
    private Obituary cursorToObituary(Cursor cursor) {
        try {
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
            obituary.setReligion(cursor.getInt(11));
            obituary.setCemetery(getCemetery(cursor.getLong(12)));
            obituary.setBorough(getBorough(cursor.getLong(13)));
            return obituary;
        } catch (Exception e) {
            Log.e("cursorToObituary", "Failed to get Obituary from Cursor.");
        }
        return null;
    }

    /**
     * Parses first row cursor into Cemetery object.
     * @param cursor  Cursor from which the entries for the Cemetery will be taken.
     * @return        Cemetery object or null in case the operation failed.
     */
    private Cemetery cursorToCemetery(Cursor cursor) {
        try {
            Cemetery cemetery = new Cemetery();
            cemetery.setId(cursor.getLong(0));
            cemetery.setName(cursor.getString(1));
            cemetery.setAddress(cursor.getString(2));
            cemetery.setLat(cursor.getDouble(3));
            cemetery.setLon(cursor.getDouble(4));
            return cemetery;
        } catch (Exception e) {
            Log.e("cursorToCemetery", "Failed to get Cemetery from Cursor.");
        }
        return null;
    }

    private Borough cursorToBorough(Cursor cursor) {
        try {
            Borough borough = new Borough();
            borough.setId(cursor.getLong(0));
            borough.setName(cursor.getString(1));
            return borough;
        } catch (Exception e) {
            Log.e("cursorToBorough", "Failed to get Borough from Cursor.");
        }
        return null;
    }
}
