package apl.vada.lib.db.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class PreferencesUtil {

    private SharedPreferences.Editor mSharedPreferencesEditor;
    private SharedPreferences mSharedPreferences;

    public PreferencesUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_DATABASE,
                Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public void clearAllPreferences(String place, int databaseVersion) {

        boolean isFTSTableCreated = isVirtualTableCreated();

        mSharedPreferencesEditor.remove(String.format(Constants.SHARED_DATABASE_QUERIES, place));
        mSharedPreferencesEditor.remove(String.format(Constants.SHARED_PREFERENCES_INDEX,
                String.format(Constants.SHARED_DATABASE_QUERIES, place)));
        mSharedPreferencesEditor.remove(String.format(Constants.SHARED_DATABASE_TABLES, place));
        mSharedPreferencesEditor.remove(String.format(Constants.SHARED_PREFERENCES_INDEX,
                String.format(Constants.SHARED_DATABASE_TABLES, place)));

        putDatabaseVersion(databaseVersion, place);

        if (isFTSTableCreated) {
            setVirtualTableCreated();
        }

        mSharedPreferencesEditor.commit();
    }


    private int getNextIndex(String place) {
        return getCurrentIndex(place) + 1;
    }

    private void putCurrentIndex(String place, int index) {
        mSharedPreferencesEditor.putInt(String.format(Constants.SHARED_PREFERENCES_INDEX, place), index);
        mSharedPreferencesEditor.commit();
    }

    private int getCurrentIndex(String place) {
        return mSharedPreferences.getInt(String.format(Constants.SHARED_PREFERENCES_INDEX, place), 1);
    }

    public void putList(String place, List<String> entityList) {
        for (String entity : entityList) {
            int index = getNextIndex(place);
            mSharedPreferencesEditor.putString(String.format(Constants.SHARED_PREFERENCES_LIST, place, index), entity);
            putCurrentIndex(place, index);
        }
    }

    public List<String> getList(String place) {
        List<String> resultList = new ArrayList<String>();

        for (int i = 1; i <= getCurrentIndex(place); i++) {
            String savedString =
                    mSharedPreferences.getString(String.format(Constants.SHARED_PREFERENCES_LIST, place, i), null);
            if (savedString != null) {
                resultList.add(savedString);
            }
        }

        return resultList;
    }

    public void commit() {
        mSharedPreferencesEditor.commit();
    }

    public void putDatabaseVersion(int databaseVersion, String sharedPreferencesPlace) {
        mSharedPreferencesEditor.putInt(String.format(Constants.SHARED_DATABASE_VERSION, sharedPreferencesPlace), databaseVersion);
    }

    public int getDatabaseVersion(String sharedPreferencesPlace) {
        // if not found, return first version -> 1
        return mSharedPreferences.getInt(String.format(Constants.SHARED_DATABASE_VERSION, sharedPreferencesPlace), Constants.FIRST_DATABASE_VERSION);
    }

    public boolean isVirtualTableCreated() {
        return mSharedPreferences.getBoolean(Constants.SHARED_DATABASE_VIRTUAL_TABLE_CREATED, false);
    }

    public void setVirtualTableCreated() {
        mSharedPreferencesEditor.putBoolean(Constants.SHARED_DATABASE_VIRTUAL_TABLE_CREATED, true);
    }

    public void setVirtualTableDropped() {
        mSharedPreferencesEditor.putBoolean(Constants.SHARED_DATABASE_VIRTUAL_TABLE_CREATED, false);
    }

}
