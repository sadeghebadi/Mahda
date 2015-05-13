package apl.vada.lib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import apl.vada.lib.db.util.Constants;
import apl.vada.lib.db.util.DatabaseUtil;
import apl.vada.lib.db.util.PreferencesUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "WeakerAccess", "CanBeFinal"})
public class SQLiteHelper extends SQLiteOpenHelper {

    private String mAssetsDatabaseName;
    private Context mContext;
    private PreferencesUtil mSharedPreferencesUtil;
    private String mSharedPreferencesPlace;
    private static final Object mWritableObjectLock = new Object();
    private static final Object mReadableObjectLock = new Object();

    /**
     * @param context                - see {@link android.content.Context}
     * @param sharedPreferencesPlace - see {@link apl.vada.lib.db.util.Constants}
     * @param databaseVersion        - see {@link android.database.sqlite.SQLiteOpenHelper}
     * @param assetsDatabaseName     - Load assets or internal (if null) sqlite database if need
     * @param isFTS                  - see {@link apl.vada.lib.db.SQLiteFTS}
     */
    public SQLiteHelper(Context context, String sharedPreferencesPlace,
                              int databaseVersion, String assetsDatabaseName, boolean isFTS) {
        super(context, DatabaseUtil.getFullDatabaseName(assetsDatabaseName, context, isFTS), null, databaseVersion);
        this.mAssetsDatabaseName = assetsDatabaseName;
        this.mContext = context;
        this.mSharedPreferencesPlace = sharedPreferencesPlace;
        mSharedPreferencesUtil = new PreferencesUtil(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        List<String> sqlQueries = mSharedPreferencesUtil.getList(
                String.format(Constants.SHARED_DATABASE_QUERIES, mSharedPreferencesPlace));
        if (sqlQueries != null) { // execute sql queries in order
            for (String sqlQuery : sqlQueries) {
                sqLiteDatabase.execSQL(sqlQuery);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        List<String> tables = mSharedPreferencesUtil.getList(
                String.format(Constants.SHARED_DATABASE_TABLES, Constants.SHARED_LOCAL_PREFERENCES));
        if (tables != null) { // drop tables in order
            for (String table : tables) {
                sqLiteDatabase.execSQL(String.format(Constants.FORMAT_TWINS,
                        Constants.SQL_DROP_TABLE_IF_EXISTS, table));
            }
        }

        onCreate(sqLiteDatabase);
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        checkDatabaseFromAssets();
        synchronized (mWritableObjectLock) {
            if (mAssetsDatabaseName == null) {
                return super.getWritableDatabase();
            } else {
                return SQLiteDatabase.openDatabase(DatabaseUtil.getFullDatabasePath(mContext, mAssetsDatabaseName),
                        null, SQLiteDatabase.OPEN_READWRITE);
            }
        }
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        checkDatabaseFromAssets();
        synchronized (mReadableObjectLock) {
            if (mAssetsDatabaseName == null) {
                return super.getReadableDatabase();
            } else {
                return SQLiteDatabase.openDatabase(DatabaseUtil.getFullDatabasePath(mContext, mAssetsDatabaseName),
                        null, SQLiteDatabase.OPEN_READONLY);
            }
        }
    }

    private void checkDatabaseFromAssets() {
        if (mAssetsDatabaseName != null) {
            if (!isDatabaseExist()) {
                super.getWritableDatabase(); // create empty database
                super.close();
                copyDatabaseFromAssets();
            }
        }
    }

    private void copyDatabaseFromAssets() {
        try {
            InputStream inputStream = mContext.getAssets().open(mAssetsDatabaseName);
            OutputStream outputStream = new FileOutputStream(DatabaseUtil.getFullDatabasePath(mContext,
                    mAssetsDatabaseName));

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isDatabaseExist() {
        return new File(DatabaseUtil.getFullDatabasePath(mContext, mAssetsDatabaseName)).exists();
    }

}
