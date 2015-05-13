package apl.vada.lib.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.Constants;
import apl.vada.lib.db.util.DatabaseUtil;
import apl.vada.lib.db.util.PreferencesUtil;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
public abstract class SQLiteDAO<T> {

    private Class<T> mClass;
    private SQLiteHelper mSqliteHelper;
    private String mPrimaryKeyColumnName;
    private String mLocalDatabasePath;
    private String mAssetsDatabaseName;

    private static SQLiteDatabase mInternalDatabase;
    private static SQLiteDatabase mAssetsDatabase;
    private static SQLiteDatabase mLocalDatabase;

    public SQLiteDAO(Class<T> tClass, Context context) {
        mSqliteHelper = new SQLiteHelper(context, Constants.SHARED_LOCAL_PREFERENCES,
                new PreferencesUtil(context).getDatabaseVersion(Constants.SHARED_LOCAL_PREFERENCES), null, false);
        init(tClass);
    }

    public SQLiteDAO(Class<T> tClass, Context context, String assetsDatabaseName) {
        mSqliteHelper = new SQLiteHelper(context, assetsDatabaseName,
                new PreferencesUtil(context).getDatabaseVersion(assetsDatabaseName), assetsDatabaseName, false);
        this.mAssetsDatabaseName = assetsDatabaseName;
        init(tClass);
    }

    public SQLiteDAO(Class<T> tClass, String localDatabasePath) {
        this.mLocalDatabasePath = localDatabasePath;
        init(tClass);
    }

    private void init(Class<T> tClass) {
        this.mClass = tClass;
        mPrimaryKeyColumnName = getPrimaryKeyColumnName();
    }

    protected SQLiteDatabase getDatabase() {
        if (mAssetsDatabaseName != null) {

            if (mAssetsDatabase == null || !mAssetsDatabase.isOpen()) {
                mAssetsDatabase = mSqliteHelper.getWritableDatabase();
            }
            return mAssetsDatabase;

        } else if (mLocalDatabasePath != null) {

            if (mLocalDatabase == null || !mLocalDatabase.isOpen()) {
                mLocalDatabase = SQLiteDatabase.openDatabase(mLocalDatabasePath, null, SQLiteDatabase.OPEN_READWRITE);
            }
            return mLocalDatabase;

        } else {

            if (mInternalDatabase == null || !mInternalDatabase.isOpen()) {
                mInternalDatabase = mSqliteHelper.getWritableDatabase();
            }
            return mInternalDatabase;

        }
    }

    private String getPrimaryKeyColumnName() {

        for (Field field : mClass.getDeclaredFields()) {

            Column fieldEntityAnnotation = field.getAnnotation(Column.class);

            if (fieldEntityAnnotation != null) {
                String columnName = DatabaseUtil.getColumnName(field);

                if (columnName != null) {
                    Column annotationColumn = field.getAnnotation(Column.class);

                    if (annotationColumn.isPrimaryKey()) {
                        return columnName;
                    }

                }
            }
        }

        return Constants.ID_COLUMN;

    }

    private String[] getColumns() {
        boolean isHaveAnyKey = false;
        List<String> columnsList = new ArrayList<String>();

        for (Field field : mClass.getDeclaredFields()) {
            Column fieldEntityAnnotation = field.getAnnotation(Column.class);
            if (fieldEntityAnnotation != null) {
                String columnName = DatabaseUtil.getColumnName(field);
                if (columnName != null)
                    columnsList.add(columnName);
                if (fieldEntityAnnotation.isPrimaryKey()) {
                    isHaveAnyKey = true;
                }
            }
        }

        if (!isHaveAnyKey) {
            columnsList.add(Constants.ID_COLUMN);
        }

        String[] columnsArray = new String[columnsList.size()];

        return columnsList.toArray(columnsArray);
    }

    private void bindObject(T newTObject, Cursor cursor) throws NoSuchFieldException, IllegalAccessException {
        for (Field field : mClass.getDeclaredFields()) {
            if (!field.isAccessible())
                field.setAccessible(true); // for private variables
            Column fieldEntityAnnotation = field.getAnnotation(Column.class);
            if (fieldEntityAnnotation != null) {
                field.set(newTObject, getValueFromCursor(cursor, field));
            }
        }
    }

    // Get content from specific types
    private Object getValueFromCursor(Cursor cursor, Field field) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        Object value = null;

        int columnIndex = cursor.getColumnIndex(DatabaseUtil.getColumnName(field));

        if (fieldType.isAssignableFrom(Long.class) || fieldType.isAssignableFrom(long.class)) {
            value = cursor.getLong(columnIndex);
        } else if (fieldType.isAssignableFrom(String.class)) {
            value = cursor.getString(columnIndex);
        } else if ((fieldType.isAssignableFrom(Integer.class) || fieldType.isAssignableFrom(int.class))) {
            value = cursor.getInt(columnIndex);
        } else if ((fieldType.isAssignableFrom(Byte[].class) || fieldType.isAssignableFrom(byte[].class))) {
            value = cursor.getBlob(columnIndex);
        } else if ((fieldType.isAssignableFrom(Double.class) || fieldType.isAssignableFrom(double.class))) {
            value = cursor.getDouble(columnIndex);
        } else if ((fieldType.isAssignableFrom(Float.class) || fieldType.isAssignableFrom(float.class))) {
            value = cursor.getFloat(columnIndex);
        } else if ((fieldType.isAssignableFrom(Short.class) || fieldType.isAssignableFrom(short.class))) {
            value = cursor.getShort(columnIndex);
        } else if (fieldType.isAssignableFrom(Byte.class) || fieldType.isAssignableFrom(byte.class)) {
            value = (byte) cursor.getShort(columnIndex);
        } else if (fieldType.isAssignableFrom(Boolean.class) || fieldType.isAssignableFrom(boolean.class)) {
            int booleanInteger = cursor.getInt(columnIndex);
            value = booleanInteger == 1;
        } else if (fieldType.isAssignableFrom(Date.class)) {
            value = new Date(cursor.getLong(columnIndex));
        }
        return value;
    }

    // Put in content value from object to specific type
    private void putInContentValues(ContentValues contentValues, Field field, Object object)
            throws IllegalAccessException {
        if (!field.isAccessible())
            field.setAccessible(true); // for private variables
        Object fieldValue = field.get(object);
        String key = DatabaseUtil.getColumnName(field);
        if (fieldValue instanceof Long) {
            contentValues.put(key, Long.valueOf(fieldValue.toString()));
        } else if (fieldValue instanceof String) {
            contentValues.put(key, fieldValue.toString());
        } else if (fieldValue instanceof Integer) {
            contentValues.put(key, Integer.valueOf(fieldValue.toString()));
        } else if (fieldValue instanceof Float) {
            contentValues.put(key, Float.valueOf(fieldValue.toString()));
        } else if (fieldValue instanceof Byte) {
            contentValues.put(key, Byte.valueOf(fieldValue.toString()));
        } else if (fieldValue instanceof Short) {
            contentValues.put(key, Short.valueOf(fieldValue.toString()));
        } else if (fieldValue instanceof Boolean) {
            contentValues.put(key, Boolean.parseBoolean(fieldValue.toString()));
        } else if (fieldValue instanceof Double) {
            contentValues.put(key, Double.valueOf(fieldValue.toString()));
        } else if (fieldValue instanceof Date) {
            contentValues.put(key, String.valueOf(((Date) fieldValue).getTime()));
        } else if (fieldValue instanceof Byte[] || fieldValue instanceof byte[]) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(fieldValue);
                contentValues.put(key, outputStream.toByteArray());
                objectOutputStream.flush();
                objectOutputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected List<T> readAll(Cursor cursor) {
        try {

            List<T> list = new ArrayList<T>();

            for (int i = 0; i < cursor.getCount(); i++) {
                list.add(read(cursor));
                cursor.moveToNext();
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unused")
    protected T read(Cursor cursor) {
        if (cursor != null) {
            try {

                T newTObject = mClass.newInstance();
                bindObject(newTObject, cursor);
                return newTObject;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }
    }

    @SuppressWarnings("unused")
    private Cursor selectCursorAscFromTable() {
        return selectCursorFromTable(null, null, null, null, null);
    }

    @SuppressWarnings("unused")
    private Cursor selectCursorDescFromTable() {
        return selectCursorFromTable(null, null, null, null,
                String.format(Constants.FORMAT_TWINS, mPrimaryKeyColumnName, Constants.DESC));
    }

    @SuppressWarnings("unused")
    protected Cursor selectCursorFromTable(String selection, String[] selectionArgs,
                                         String groupBy, String having, String orderBy) {
        try {
            String table = DatabaseUtil.getTableName(mClass);
            String[] columns = getColumns();

            SQLiteDatabase database = getDatabase();
            Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            cursor.moveToFirst();
            return cursor;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unused")
    protected Cursor selectCursorFromTableWithLimit(String selection, String[] selectionArgs,
                                         String groupBy, String having, String orderBy, String limit) {
        try {
            String table = DatabaseUtil.getTableName(mClass);
            String[] columns = getColumns();

            SQLiteDatabase database = getDatabase();
            Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            cursor.moveToFirst();
            return cursor;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private float averageQuery(String query) {
        SQLiteDatabase database = getDatabase();
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        float averageResult = cursor.getFloat(Constants.FIRST_ELEMENT);

        cursor.close();

        return averageResult;
    }

    private ContentValues getFilledContentValues(Object object) throws IllegalAccessException {
        ContentValues contentValues = new ContentValues();

        for (Field field : object.getClass().getDeclaredFields()) {
            Column fieldEntityAnnotation = field.getAnnotation(Column.class);
            if (fieldEntityAnnotation != null) {
                if (!fieldEntityAnnotation.isAutoincrement()) {
                    putInContentValues(contentValues, field, object);
                }
            }
        }
        return contentValues;
    }

    @SuppressWarnings("unused")
    public static void updateDatabases() {
        if (mAssetsDatabase != null) {
            mAssetsDatabase.close();
            mAssetsDatabase = null;
        }

        if (mLocalDatabase != null) {
            mLocalDatabase.close();
            mLocalDatabase = null;
        }

        if (mInternalDatabase != null) {
            mInternalDatabase.close();
            mInternalDatabase = null;
        }
    }

    @SuppressWarnings("unused")
    public int getCount() {
        Cursor cursor = selectCursorAscFromTable();

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    @SuppressWarnings("unused")
    public long getLastRowId() {
        Cursor cursor = selectCursorAscFromTable();

        cursor.moveToLast();

        long id;
        if (cursor.getPosition() == -1) {
            id = Constants.ZERO_RESULT;
        } else {
            id = cursor.getLong(cursor.getColumnIndex(mPrimaryKeyColumnName));
        }

        cursor.close();

        return id;
    }

    @SuppressWarnings("unused")
    public long createIfNotExist(T object, String columnName, String columnValue) {
        long result = Constants.ZERO_RESULT;

        Cursor cursor = selectCursorFromTable(String.format(Constants.FORMAT_COLUMN,
                columnName), new String[]{columnValue}, null, null, null);

        if (cursor.getCount() == 0) {
            result = create(object);
        }

        cursor.close();

        return result;
    }

    @SuppressWarnings("unused")
    public long createIfNotExist(T object, String firstColumnName, String firstColumnValue,
                                 String secondColumnName, String secondColumnValue) {
        long result = Constants.ZERO_RESULT;

        Cursor cursor =
                selectCursorFromTable(String.format(Constants.FORMAT_COLUMNS_COMMA,
                        firstColumnName, secondColumnName),
                        new String[]{firstColumnValue, secondColumnValue}, null, null, null);

        if (cursor.getCount() == 0) {
            result = create(object);
        }

        cursor.close();

        return result;
    }

    @SuppressWarnings("unused")
    public void createAll(List<T> objects) {
        for (T object : objects) {
            create(object);
        }
    }

    @SuppressWarnings("unused")
    public long create(T object) {
        try {
            ContentValues contentValues = new ContentValues();

            for (Field field : object.getClass().getDeclaredFields()) {
                Column fieldEntityAnnotation = field.getAnnotation(Column.class);
                if (fieldEntityAnnotation != null) {
                    if (!fieldEntityAnnotation.isAutoincrement()) {
                        putInContentValues(contentValues, field, object);
                    }
                }
            }

            SQLiteDatabase database = getDatabase();
            return database.insert(DatabaseUtil.getTableName(object.getClass()), null, contentValues);

        } catch (Exception e) {
            e.printStackTrace();
            return Constants.ZERO_RESULT;
        }
    }

    @SuppressWarnings("unused")
    public T read(long id) {
        Cursor cursor = selectCursorFromTable(
                String.format(Constants.FORMAT_COLUMN, mPrimaryKeyColumnName),
                new String[]{Long.toString(id)}
                , null, null, null);

        try {
            T newTObject = mClass.newInstance();
            bindObject(newTObject, cursor);
            return newTObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            cursor.close();
        }

    }

    @SuppressWarnings("unused")
    public T readWhere(String columnName, String columnValue) {
        Cursor cursor = selectCursorFromTable(String.format(Constants.FORMAT_COLUMN, columnName),
                new String[]{columnValue}, null, null, null);
        T object = null;
        if (cursor != null) {
            object = read(cursor);
            cursor.close();
        }
        return object;
    }

    @SuppressWarnings("unused")
    public T readWhere(String firstColumnName, String firstColumnValue,
                       String secondColumnName, String secondColumnValue) {
        Cursor cursor = selectCursorFromTable(String.format(Constants.FORMAT_COLUMNS_COMMA,
                firstColumnName, secondColumnName),
                new String[]{firstColumnValue, secondColumnValue}, null, null, null);
        T object = null;
        if (cursor != null) {
            object = read(cursor);
            cursor.close();
        }
        return object;
    }

    @SuppressWarnings("unused")
    public T readWhereWithOrder(String columnName, String columnValue, String column, String order) {
        Cursor cursor = selectCursorFromTable(String.format(Constants.FORMAT_COLUMN, columnName),
                new String[]{columnValue}, null, null, String.format(Constants.FORMAT_TWINS, column, order));
        T object = null;
        if (cursor != null) {
            object = read(cursor);
            cursor.close();
        }
        return object;
    }

    @SuppressWarnings("unused")
    public List<T> readAllAsc() {
        Cursor cursor = selectCursorAscFromTable();
        List<T> objects = readAll(cursor);
        cursor.close();
        return objects;
    }

    @SuppressWarnings("unused")
    public List<T> readAllDesc() {
        Cursor cursor = selectCursorDescFromTable();
        List<T> objects = readAll(cursor);
        cursor.close();
        return objects;
    }

    @SuppressWarnings("unused")
    public List<T> readAllWithOrder(String column, String order) {
        Cursor cursor = selectCursorFromTable(null, null, null, null,
                String.format(Constants.FORMAT_TWINS, column, order));
        List<T> objects = readAll(cursor);
        cursor.close();
        return objects;
    }

    @SuppressWarnings("unused")
    public List<T> readAllWhere(String columnName, String columnValue) {
        Cursor cursor = selectCursorFromTable(String.format(Constants.FORMAT_COLUMN, columnName),
                new String[]{columnValue}, null, null, null);
        List<T> objects = readAll(cursor);
        cursor.close();
        return objects;
    }

    @SuppressWarnings("unused")
    public List<T> readAllWhereWithOrder(String columnName, String columnValue, String column, String order) {
        Cursor cursor = selectCursorFromTable(String.format(Constants.FORMAT_COLUMN, columnName),
                new String[]{columnValue}, null, null, String.format(Constants.FORMAT_TWINS, column, order));
        List<T> objects = readAll(cursor);
        cursor.close();
        return objects;
    }

    @SuppressWarnings("unused")
    public List<T> readAllWhereInWithOrder(String columnName, String in, String column, String order) {
        Cursor cursor = selectCursorFromTable(String.format(Constants.SQL_IN, columnName, in),
                null, null, null, String.format(Constants.FORMAT_TWINS, column, order));
        if (cursor != null) {
            List<T> objects = readAll(cursor);
            cursor.close();
            return objects;
        } else {
            return new ArrayList<T>();
        }
    }

    @SuppressWarnings("unused")
    public List<T> readAllWhereIn(String columnName, String in) {
        Cursor cursor = selectCursorFromTable(String.format(Constants.SQL_IN, columnName, in),
                null, null, null, null);
        if (cursor != null) {
            List<T> objects = readAll(cursor);
            cursor.close();
            return objects;
        } else {
            return new ArrayList<T>();
        }
    }

    @SuppressWarnings("unused")
    public long update(String columnName, String columnValue, T newObject) {
        try {

            ContentValues contentValues = getFilledContentValues(newObject);

            SQLiteDatabase database = getDatabase();
            return database.update(DatabaseUtil.getTableName(newObject.getClass()), contentValues,
                    String.format(Constants.FORMAT_COLUMN, columnName), new String[]{columnValue});

        } catch (Exception e) {
            e.printStackTrace();
            return Constants.ZERO_RESULT;
        }
    }

    @SuppressWarnings("unused")
    public long update(String firstColumnName, String firstColumnValue,
                       String secondColumnName, String secondColumnValue, T newObject) {
        try {

            ContentValues contentValues = getFilledContentValues(newObject);

            SQLiteDatabase database = getDatabase();
            return database.update(DatabaseUtil.getTableName(newObject.getClass()), contentValues,
                    String.format(Constants.FORMAT_COLUMNS_COMMA, firstColumnName, secondColumnName),
                    new String[]{firstColumnValue, secondColumnValue});

        } catch (Exception e) {
            e.printStackTrace();
            return Constants.ZERO_RESULT;
        }
    }

    @SuppressWarnings("unused")
    public long update(long id, T newObject) {
        return update(mPrimaryKeyColumnName, String.valueOf(id), newObject);
    }

    @SuppressWarnings("unused")
    public long delete(long id) {
        SQLiteDatabase database = getDatabase();
        return database.delete(
                DatabaseUtil.getTableName(mClass),
                String.format(Constants.FORMAT_COLUMN, mPrimaryKeyColumnName),
                new String[]{String.valueOf(id)});
    }

    @SuppressWarnings("unused")
    public long deleteWhere(String columnName, String columnValue) {
        SQLiteDatabase database = getDatabase();
        return database.delete(
                DatabaseUtil.getTableName(mClass), String.format(Constants.FORMAT_COLUMN, columnName),
                new String[]{columnValue});
    }

    @SuppressWarnings("unused")
    public long deleteWhere(String firstColumnName, String firstColumnValue,
                            String secondColumnName, String secondColumnValue) {
        SQLiteDatabase database = getDatabase();
        return database.delete(
                DatabaseUtil.getTableName(mClass), String.format(Constants.FORMAT_COLUMNS_COMMA,
                firstColumnName, secondColumnName),
                new String[]{firstColumnValue, secondColumnValue});
    }

    @SuppressWarnings("unused")
    public long deleteAll() {
        SQLiteDatabase database = getDatabase();
        return database.delete(DatabaseUtil.getTableName(mClass), null, null);
    }

    @SuppressWarnings("unused")
    public float average(String columnName) {
        String query = String.format(Constants.SQL_AVG_QUERY, columnName,
                DatabaseUtil.getTableName(mClass));
        return averageQuery(query);
    }

    @SuppressWarnings("unused")
    public float average(String columnName, String whereColumn, String whereValue) {
        String query = String.format(Constants.SQL_AVG_QUERY_WHERE,
                columnName, DatabaseUtil.getTableName(mClass),
                whereColumn, whereValue);
        return averageQuery(query);
    }

}
