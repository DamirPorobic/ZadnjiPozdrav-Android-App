package info.zadnjipozdrav.zadnjipozdrav;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CustomSQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "obituaries.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_OBITUARIES = "obituaries";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FAMILYNAME = "familyName";
    public static final String COLUMN_FATHERSNAME = "fathersName";
    public static final String COLUMN_MAIDENNAME = "maidenName";
    public static final String COLUMN_BIRTHDATE = "birthDate";
    public static final String COLUMN_DEATHDATE = "deathDate";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_FUNERALDATE = "funeralDate";
    public static final String COLUMN_FUNERALTIME = "funeralTime";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_RELIGION= "religion";
    public static final String COLUMN_CEMETERYNAME = "cemeteryName";
    public static final String COLUMN_CEMETERYADDRESS = "cemeteryAddress";
    public static final String COLUMN_CEMETERYLAT = "cemeteryLat";
    public static final String COLUMN_CEMETERYLON = "cemeteryLon";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_OBITUARIES
            + "( " + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_FAMILYNAME + " text not null, "
            + COLUMN_FATHERSNAME + " text, "
            + COLUMN_MAIDENNAME + " text, "
            + COLUMN_BIRTHDATE + " integer not null, "
            + COLUMN_DEATHDATE + " integer not null, "
            + COLUMN_TEXT + " text, "
            + COLUMN_FUNERALDATE + " integer not null, "
            + COLUMN_FUNERALTIME + " text not null, "
            + COLUMN_PICTURE + " blob, "
            + COLUMN_RELIGION + " text, "
            + COLUMN_CEMETERYNAME + " text, "
            + COLUMN_CEMETERYADDRESS + " text, "
            + COLUMN_CEMETERYLAT + " real, "
            + COLUMN_CEMETERYLON + " real"
            + ");";

    public CustomSQLiteHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBITUARIES);
        onCreate(db);
    }
}
