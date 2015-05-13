package apl.vada.lib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;
import apl.vada.lib.db.util.Constants;
import apl.vada.lib.db.util.DatabaseUtil;
import apl.vada.lib.db.util.PreferencesUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"CanBeFinal", "UnusedReturnValue"})
public class SQLiteDB {

    private SQLiteHelper mSqLiteHelper;
    private PreferencesUtil mSharedPreferencesUtil;
    private String mSharedPreferencesPlace = Constants.SHARED_LOCAL_PREFERENCES;
    private int mDatabaseVersion = Constants.FIRST_DATABASE_VERSION;
    private boolean mIsAddedSQLDivider;

    @SuppressWarnings("unused")
    public SQLiteDB(Context context, int databaseVersion) {
        this.mDatabaseVersion = databaseVersion;
        init(context);

        mSqLiteHelper = new SQLiteHelper(context, mSharedPreferencesPlace, databaseVersion, null, false);
    }

    @SuppressWarnings("unused")
    public SQLiteDB(Context context) {
        init(context);

        mSqLiteHelper = new SQLiteHelper(context, mSharedPreferencesPlace, mDatabaseVersion, null, false);
    }

    @SuppressWarnings("unused")
    public SQLiteDB(Context context, String assetsDatabaseName) {
        this.mSharedPreferencesPlace = assetsDatabaseName;
        init(context);

        mSqLiteHelper = new SQLiteHelper(context,
                mSharedPreferencesPlace, mDatabaseVersion, assetsDatabaseName, false);
    }

    private void init(Context context) {
        this.mSharedPreferencesUtil = new PreferencesUtil(context);
        commitDatabaseVersion();
    }

    private void commitDatabaseVersion() {
        if (mDatabaseVersion > mSharedPreferencesUtil.getDatabaseVersion(mSharedPreferencesPlace)) {
            mSharedPreferencesUtil.putDatabaseVersion(mDatabaseVersion, mSharedPreferencesPlace);
            mSharedPreferencesUtil.commit();
        }
    }

    private void commit(List<String> tables, List<String> sqlQueries) {
        mSharedPreferencesUtil.putList
                (String.format(Constants.SHARED_DATABASE_TABLES, mSharedPreferencesPlace), tables);
        mSharedPreferencesUtil.putList
                (String.format(Constants.SHARED_DATABASE_QUERIES, mSharedPreferencesPlace), sqlQueries);
        mSharedPreferencesUtil.commit();
    }

    private void checkingCommit(List<String> tables, List<String> sqlQueries, boolean newDatabaseVersion) {
        if (newDatabaseVersion) {
            commit(tables, sqlQueries);
            SQLiteDatabase database = mSqLiteHelper.getWritableDatabase(); // call onCreate();
            database.close();
        } else {
            commit(tables, sqlQueries);
        }
    }

    @SuppressWarnings("unused")
    public void rawQuery(String sql) {
        SQLiteDatabase database = mSqLiteHelper.getWritableDatabase();
        database.execSQL(sql);
        database.close();
    }

    public void create(Class<?>... classes) {
        List<String> savedTables = mSharedPreferencesUtil.
                getList(String.format(Constants.SHARED_DATABASE_TABLES, mSharedPreferencesPlace));
        List<String> savedSQLQueries = mSharedPreferencesUtil.
                getList(String.format(Constants.SHARED_DATABASE_QUERIES, mSharedPreferencesPlace));

        mSharedPreferencesUtil.clearAllPreferences(mSharedPreferencesPlace, mDatabaseVersion);

        List<String> tables = new ArrayList<String>();
        List<String> sqlQueries = new ArrayList<String>();

        uniteClassesToSQL(tables, sqlQueries, classes);

        boolean newDatabaseVersion = false;
        boolean isRebasedTables = false;

        if (mDatabaseVersion > mSharedPreferencesUtil.getDatabaseVersion(mSharedPreferencesPlace))
            newDatabaseVersion = true;

        if (!newDatabaseVersion) {
            isRebasedTables = rebaseTablesIfNeed(savedTables, tables, sqlQueries, savedSQLQueries);
            if (savedSQLQueries.hashCode() != sqlQueries.hashCode() && savedSQLQueries.hashCode() != 1) {
                addNewColumnsIfNeed(tables, sqlQueries, savedSQLQueries);
            }
        }

        if (!isRebasedTables) {
            checkingCommit(tables, sqlQueries, newDatabaseVersion);
        }
    }

    private void uniteClassesToSQL(List<String> tables, List<String> sqlQueries, Class<?>... classes) {
        for (Class classEntity : classes) {
            StringBuilder sqlQueryBuilder = new StringBuilder();
            String table = DatabaseUtil.getTableName(classEntity);
            sqlQueryBuilder.append(String.format(Constants.SQL_CREATE_TABLE_IF_NOT_EXIST, table));

            List<Field> primaryKeys = new ArrayList<Field>();

            int tableFieldsCount = classEntity.getDeclaredFields().length; // remove no annotation fields
            for (Field field : classEntity.getDeclaredFields()) {
                if (field.getAnnotation(Column.class) == null) {
                    tableFieldsCount--;
                }
            }

            int annotatedFieldsIndex = 0;
            for (int i = 0; i < classEntity.getDeclaredFields().length; i++) {

                Field fieldEntity = classEntity.getDeclaredFields()[i];
                Column fieldEntityAnnotation = fieldEntity.getAnnotation(Column.class);
                String column = DatabaseUtil.getColumnName(fieldEntity);

                if (fieldEntityAnnotation != null) { // if field what we need annotated

                    if (fieldEntityAnnotation.isPrimaryKey()) {
                        primaryKeys.add(fieldEntity);

                    } else {
                        sqlQueryBuilder.append(String.format(Constants.FORMAT_TWINS,
                                column, DatabaseUtil.getSQLType(fieldEntity, fieldEntityAnnotation)));

                        mIsAddedSQLDivider = false;

                        if (fieldEntityAnnotation.isAutoincrement()) {
                            sqlQueryBuilder.append(Constants.SPACE);
                            sqlQueryBuilder.append(Constants.AUTOINCREMENT);
                        }

                        if (annotatedFieldsIndex != tableFieldsCount - 1) {
                            mIsAddedSQLDivider = true;
                            sqlQueryBuilder.append(Constants.DIVIDER);
                            sqlQueryBuilder.append(Constants.SPACE);
                        }
                    }

                    annotatedFieldsIndex++;
                }
            }

            makeKeyForTable(sqlQueryBuilder, primaryKeys);

            sqlQueryBuilder.append(Constants.LAST_BRACKET);

            tables.add(table);
            sqlQueries.add(sqlQueryBuilder.toString());
        }
    }

    private void makeKeyForTable(StringBuilder sqlQueryBuilder, List<Field> primaryKeys) {
        if (!mIsAddedSQLDivider && !sqlQueryBuilder.toString().endsWith(Constants.FIRST_BRACKET)) {
            sqlQueryBuilder.append(Constants.DIVIDER);
            sqlQueryBuilder.append(Constants.SPACE);
        }

        if (primaryKeys.size() == 0) {
            sqlQueryBuilder.append(Constants.ID_COLUMN);
            sqlQueryBuilder.append(Constants.SPACE);
            sqlQueryBuilder.append(ColumnType.INTEGER);
            sqlQueryBuilder.append(Constants.SPACE);
            sqlQueryBuilder.append(Constants.PRIMARY_KEY);
            sqlQueryBuilder.append(Constants.SPACE);
            sqlQueryBuilder.append(Constants.AUTOINCREMENT);

        } else if (primaryKeys.size() == 1) {
            Field fieldEntity = primaryKeys.get(0);
            String column = DatabaseUtil.getColumnName(fieldEntity);
            Column fieldEntityAnnotation = fieldEntity.getAnnotation(Column.class);
            sqlQueryBuilder.append(String.format(Constants.FORMAT_TWINS,
                    column, DatabaseUtil.getSQLType(fieldEntity, fieldEntityAnnotation)));

            sqlQueryBuilder.append(Constants.SPACE);
            sqlQueryBuilder.append(Constants.PRIMARY_KEY);

            if (fieldEntityAnnotation.isAutoincrement()) {
                sqlQueryBuilder.append(Constants.SPACE);
                sqlQueryBuilder.append(Constants.AUTOINCREMENT);
            }

        } else {
            StringBuilder primaryKeysBuilder = new StringBuilder();
            boolean isFirst = true;

            for (Field fieldEntity : primaryKeys) {
                String column = DatabaseUtil.getColumnName(fieldEntity);
                Column fieldEntityAnnotation = fieldEntity.getAnnotation(Column.class);
                sqlQueryBuilder.append(String.format(Constants.FORMAT_TWINS,
                        column, DatabaseUtil.getSQLType(fieldEntity, fieldEntityAnnotation)));

                sqlQueryBuilder.append(Constants.DIVIDER);
                sqlQueryBuilder.append(Constants.SPACE);

                if (!isFirst) {
                    primaryKeysBuilder.append(Constants.DIVIDER);
                    primaryKeysBuilder.append(Constants.SPACE);
                }

                primaryKeysBuilder.append(column);

                isFirst = false;
            }

            sqlQueryBuilder.append(Constants.PRIMARY_KEY);
            sqlQueryBuilder.append(Constants.SPACE);
            sqlQueryBuilder.append(String.format(Constants.FORMAT_BRACKETS, primaryKeysBuilder.toString()));
        }
    }

    // Also delete extra tables
    private boolean rebaseTablesIfNeed(List<String> savedTables, List<String> tables,
                                       List<String> sqlQueries, List<String> savedSQLQueries) {

        List<String> extraTables = new ArrayList<String>(savedTables); // possible extra tables
        extraTables.removeAll(tables);

        if (extraTables.size() != 0) { // drop tables
            List<String> extraSqlQueries = new ArrayList<String>(savedSQLQueries); // extra SQL queries
            extraSqlQueries.removeAll(sqlQueries);

            SQLiteDatabase database = mSqLiteHelper.getWritableDatabase();
            for (String extraTable : extraTables) {
                database.execSQL(String.format(Constants.FORMAT_TWINS,
                        Constants.SQL_DROP_TABLE_IF_EXISTS, extraTable));
            }

            commit(tables, sqlQueries);
            mSqLiteHelper.onCreate(database);
            database.close();
            return true;
        }

        List<String> tablesToCreate = new ArrayList<String>(tables); // possible tables to need create
        tablesToCreate.removeAll(savedTables);

        if (tablesToCreate.size() != 0) {
            List<String> sqlQueriesToCreate = new ArrayList<String>(sqlQueries); // extra SQL queries
            sqlQueriesToCreate.removeAll(savedSQLQueries);

            SQLiteDatabase database = mSqLiteHelper.getWritableDatabase();
            for (String sqlQuery : sqlQueriesToCreate) {
                database.execSQL(sqlQuery);
            }

            database.close();
        }

        return false;
    }

    private boolean addNewColumnsIfNeed(List<String> tables, List<String> sqlQueries, List<String> savedSqlQueries) {
        try {
            // if change name of column or add new column, or delete
            boolean isAddNewColumn = false;
            for (int i = 0; i < tables.size(); i++) {

                String table = tables.get(i);

                for (String savedSqlQuery : savedSqlQueries) {

                    if (savedSqlQuery.contains(table)) {
                        List<String> savedColumns = Arrays.asList(savedSqlQueries.get(i).
                                replace(String.format(
                                        Constants.SQL_CREATE_TABLE_IF_NOT_EXIST, table), Constants.EMPTY).
                                replace(Constants.LAST_BRACKET, Constants.EMPTY).
                                split(Constants.DIVIDER_WITH_SPACE));

                        List<String> columns = Arrays.asList(sqlQueries.get(i).
                                replace(String.format(
                                        Constants.SQL_CREATE_TABLE_IF_NOT_EXIST, table), Constants.EMPTY).
                                replace(Constants.LAST_BRACKET, Constants.EMPTY).
                                split(Constants.DIVIDER_WITH_SPACE));

                        List<String> extraColumns = new ArrayList<String>(columns);
                        extraColumns.removeAll(savedColumns);

                        if (extraColumns.size() > 0) {

                            SQLiteDatabase database = mSqLiteHelper.getWritableDatabase();
                            for (String column : extraColumns) {
                                database.execSQL(String.format(
                                        Constants.SQL_ALTER_TABLE_ADD_COLUMN, table, column));
                            }
                            database.close();
                        }

                        isAddNewColumn = true;
                    }
                }
            }

            return isAddNewColumn;

        } catch (IndexOutOfBoundsException exception) {
            throw new RuntimeException("Duplicated class on method create(...)");
        }

    }

}
