package info.zadnjipozdrav.zadnjipozdrav;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "zadnjipozdrav.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_OBITUARIES = "obituaries";
    public static final String COL_OB_ID = "_id";
    public static final String COL_OB_NAME = "name";
    public static final String COL_OB_FAMILYNAME = "familyName";
    public static final String COL_OB_FATHERSNAME = "fathersName";
    public static final String COL_OB_MAIDENNAME = "maidenName";
    public static final String COL_OB_BIRTHDATE = "birthDate";
    public static final String COL_OB_DEATHDATE = "deathDate";
    public static final String COL_OB_TEXT = "text";
    public static final String COL_OB_FUNERALDATE = "funeralDate";
    public static final String COL_OB_FUNERALTIME = "funeralTime";
    public static final String COL_OB_PICTURE = "picture";
    public static final String COL_OB_RELIGION = "religion";
    public static final String COL_OB_CEMETERY_ID = "cemetery_id";
    public static final String COL_OB_BOROUGH_ID = "borough_id";

    public static final String TABLE_CEMETERIES = "cemeteries";
    public static final String COL_CE_ID = "_id";
    public static final String COL_CE_NAME = "name";
    public static final String COL_CE_ADDRESS = "address";
    public static final String COL_CE_LAT = "lat";
    public static final String COL_CE_LON = "lon";

    public static final String TABLE_BOROUGHS = "boroughs";
    public static final String COL_BO_ID = "_id";
    public static final String COL_BO_NAME = "name";

    private static final String CREATE_OB_TABLE = "create table "
            + TABLE_OBITUARIES
            + "( " + COL_OB_ID + " integer primary key autoincrement, "
            + COL_OB_NAME + " text not null, "
            + COL_OB_FAMILYNAME + " text not null, "
            + COL_OB_FATHERSNAME + " text, "
            + COL_OB_MAIDENNAME + " text, "
            + COL_OB_BIRTHDATE + " integer not null, "
            + COL_OB_DEATHDATE + " integer not null, "
            + COL_OB_TEXT + " text, "
            + COL_OB_FUNERALDATE + " integer not null, "
            + COL_OB_FUNERALTIME + " text not null, "
            + COL_OB_PICTURE + " blob, "
            + COL_OB_RELIGION + " text, "
            + COL_OB_CEMETERY_ID + " integer, "
            + COL_OB_BOROUGH_ID + " integer"
            + ");";

    private static final String CREATE_CE_TABLE = "create table "
            + TABLE_CEMETERIES
            + "( " + COL_CE_ID + " integer primary key autoincrement, "
            + COL_CE_NAME + " text not null, "
            + COL_CE_ADDRESS + " text, "
            + COL_CE_LAT + " real, "
            + COL_CE_LON + " real"
            + ");";

    private static final String CREATE_BO_TABLE = "create table "
            + TABLE_BOROUGHS
            + "( " + COL_BO_ID + " integer primary key autoincrement, "
            + COL_BO_NAME + " text not null"
            + ");";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_CE_TABLE);
        database.execSQL(CREATE_BO_TABLE);
        database.execSQL(CREATE_OB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CEMETERIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOROUGHS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBITUARIES);
        onCreate(db);
    }
}
