package apl.vada.lib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import apl.vada.lib.db.model.FTSModel;
import apl.vada.lib.db.util.Constants;
import apl.vada.lib.db.util.DatabaseUtil;
import apl.vada.lib.db.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class SQLiteFTS {

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TABLE_CATEGORY = "tableCategory";
    private static final String COLUMN_DATA = "data";
    private static SQLiteDatabase mDataBase = null;
    private boolean mUseTablesCategory;
    private String mTableName;
    private PreferencesUtil mPreferencesUtil;

    @SuppressWarnings("unused")
    public SQLiteFTS(Context context, boolean useTablesCategory) {
        this.mUseTablesCategory = useTablesCategory;

        SQLiteHelper simpleHelper = new SQLiteHelper(context, Constants.SHARED_LOCAL_PREFERENCES,
                new PreferencesUtil(context).getDatabaseVersion(Constants.SHARED_LOCAL_PREFERENCES), null, true);

        if (mDataBase == null || !mDataBase.isOpen())
            mDataBase = simpleHelper.getWritableDatabase();

        mTableName = DatabaseUtil.getFTSTableName(context);

        createTableIfNotExist(context);
    }

    @SuppressWarnings("unused")
    public void dropTable() {
        mDataBase.execSQL(String.format(Constants.FTS_DROP_VIRTUAL_TABLE, mTableName));
        mPreferencesUtil.setVirtualTableDropped();
        mPreferencesUtil.commit();
    }

    public void createTableIfNotExist(Context context) {
        mPreferencesUtil = new PreferencesUtil(context);
        if (!mPreferencesUtil.isVirtualTableCreated()) {

            String createVirtualFTSTable;
            if (mUseTablesCategory) {
                createVirtualFTSTable = String.format(Constants.FTS_CREATE_VIRTUAL_TABLE_WITH_CATEGORY, mTableName,
                        COLUMN_ID, COLUMN_TABLE_CATEGORY, COLUMN_DATA);
            } else {
                createVirtualFTSTable = String.format(Constants.FTS_CREATE_VIRTUAL_TABLE, mTableName,
                        COLUMN_ID, COLUMN_DATA);
            }

            mDataBase.execSQL(createVirtualFTSTable);

            mPreferencesUtil.setVirtualTableCreated();
            mPreferencesUtil.commit();
        }
    }

    @SuppressWarnings("unused")
    public void create(FTSModel ftsModel) {
        if (!TextUtils.isEmpty(ftsModel.getData())) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_ID, ftsModel.getId());
            contentValues.put(COLUMN_DATA, ftsModel.getData().toLowerCase());

            if (mUseTablesCategory) {
                contentValues.put(COLUMN_TABLE_CATEGORY, ftsModel.getTableCategory());
            }

            mDataBase.insert(mTableName, null, contentValues);
        }
    }

    @SuppressWarnings("unused")
    public void createAll(List<FTSModel> ftsModels) {
        for (FTSModel ftsModel : ftsModels) {
            create(ftsModel);
        }
    }

    @SuppressWarnings("unused")
    public List<FTSModel> search(String incomingQuery, boolean resultDesc) {
        List<FTSModel> ftsModels = new ArrayList<FTSModel>();

        String query = incomingQuery.replaceAll(
                Constants.SPECIAL_SYMBOLS_REGEX, Constants.EMPTY).toLowerCase();

        if (query.contains(Constants.FTS_SQL_OR) ||
                query.contains(Constants.FTS_SQL_AND) ||
                query.length() <= Constants.QUERY_MINIMUM_LENGTH) {
            return ftsModels;
        }

        String format;
        if (resultDesc) {
            format = String.format(Constants.FTS_SQL_FORMAT, mTableName, mTableName, COLUMN_DATA, query,
                    COLUMN_ID, Constants.DESC);
        } else {
            format = String.format(Constants.FTS_SQL_FORMAT, mTableName, mTableName, COLUMN_DATA, query,
                    COLUMN_ID, Constants.ASC);
        }

        Cursor cursor = mDataBase.rawQuery(format, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            FTSModel ftsModel;
            if (mUseTablesCategory) {
                ftsModel = new FTSModel(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_TABLE_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATA)));
            } else {
                ftsModel = new FTSModel(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATA)));
            }

            cursor.moveToNext();
            ftsModels.add(ftsModel);
        }

        cursor.close();

        return ftsModels;
    }

}
