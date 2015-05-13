package apl.vada.lib.db.util;

public class Constants {

    /**
     * Use only static reference
     */
    private Constants() {
    }

    // FTS
    public static final int QUERY_MINIMUM_LENGTH = 1;
    public static final String FTS_SQL_OR = "OR";
    public static final String FTS_SQL_AND = "AND";
    public static final String FTS_SQL_FORMAT = "SELECT * FROM %s WHERE %s MATCH \"%s:%s*\" ORDER BY %s %s;";
    public static final String FTS_SQL_TABLE_NAME = "%s_FTS";
    public static final String FTS_CREATE_VIRTUAL_TABLE_WITH_CATEGORY =
            "CREATE VIRTUAL TABLE %s USING fts3(%s, %s, %s, tokenize = porter);";
    public static final String FTS_CREATE_VIRTUAL_TABLE =
            "CREATE VIRTUAL TABLE %s USING fts3(%s, %s, tokenize = porter);";
    public static final String FTS_DROP_VIRTUAL_TABLE = "DROP TABLE IF EXISTS %s";

    // Shared preferences
    public static final String SHARED_PREFERENCES_APPLICATION = "SQLiteSimpleDatabaseApplication";
    public static final String SHARED_PREFERENCES_DATABASE = "SQLiteSimpleDatabaseHelper";
    public static final String SHARED_DATABASE_TABLES = "SQLiteSimpleDatabaseTables_%s";
    public static final String SHARED_DATABASE_QUERIES = "SQLiteSimpleDatabaseQueries_%s";
    public static final String SHARED_DATABASE_VERSION = "SQLiteSimpleDatabaseVersion_%s";
    public static final String SHARED_DATABASE_VIRTUAL_TABLE_CREATED = "SQLiteSimpleDatabaseVirtualTableCreated";
    public static final String SHARED_PREFERENCES_LIST = "List_%s_%s";
    public static final String SHARED_PREFERENCES_INDEX = "%s_Index";
    public static final String SHARED_LOCAL_PREFERENCES = "LOCAL";
    public static final String SHARED_IS_FIRST_APPLICATION_START = "SHARED_IS_FIRST_APPLICATION_START";

    // SQL
    public static final String AUTOINCREMENT = "AUTOINCREMENT";
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String SQL_IN = "%s IN (%s)";
    public static final String SQL_DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS";
    public static final String SQL_CREATE_TABLE_IF_NOT_EXIST = "CREATE TABLE IF NOT EXISTS %s (";
    public static final String SQL_ALTER_TABLE_ADD_COLUMN = "ALTER TABLE %s ADD COLUMN %s ";
    public static final String SQL_AVG_QUERY = "SELECT AVG(%s) FROM %s";
    public static final String SQL_AVG_QUERY_WHERE = "SELECT AVG(%s) FROM %s WHERE %s = '%s'";

    // String.format(..)
    public static final String FORMAT_GLUED_FTS = "%s_FTS%s";
    public static final String FORMAT_GLUED = "%s%s";
    public static final String FORMAT_TWINS = "%s %s";
    public static final String FORMAT_COLUMN = "%s=?";
    public static final String FORMAT_BRACKETS = "(%s)";
    public static final String FORMAT_COLUMNS_COMMA = "%s=? AND %s=?";
    public static final String FORMAT_SHARED_IS_FIRST_APPLICATION_START = "SHARED_IS_FIRST_APPLICATION_START_%s";

    // Other
    public static final int FIRST_DATABASE_VERSION = 1;
    public static final int ZERO_RESULT = -1;
    public static final int FIRST_ELEMENT = 0;
    public static final String SPECIAL_SYMBOLS_REGEX = "[-+.^:,\"']";
    public static final String ID_COLUMN = "_id"; // if we don't make a primary key column
    public static final String DESC = "DESC";
    public static final String ASC = "ASC";
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String AUTO_ASSIGN = "AUTO_ASSIGN";
    public static final String DIVIDER = ",";
    public static final String DIVIDER_WITH_SPACE = ", ";
    public static final String FIRST_BRACKET = "(";
    public static final String LAST_BRACKET = ");";
    public static final String DOT = ".";
    public static final String UNDERSCORE = "_";

}
