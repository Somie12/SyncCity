package harish.project.synccity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ResourceDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "resources.db";
    private static final int DATABASE_VERSION = 1;

    public ResourceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_RESOURCES_TABLE = "CREATE TABLE " + ResourceContract.ResourceEntry.TABLE_NAME + " ("
                + ResourceContract.ResourceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ResourceContract.ResourceEntry.COLUMN_RESOURCE_NAME + " TEXT NOT NULL, "
                + ResourceContract.ResourceEntry.COLUMN_RESOURCE_TYPE + " TEXT NOT NULL, "
                + ResourceContract.ResourceEntry.COLUMN_RESOURCE_QUANTITY + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_RESOURCES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ResourceContract.ResourceEntry.TABLE_NAME);
        onCreate(db);
    }
}
