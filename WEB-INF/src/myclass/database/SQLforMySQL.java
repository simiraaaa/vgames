package myclass.database;

import myclass.wrap.MyArray;

/**
 * MySQL用のSQL文が簡単に作れるやつ<br>
 * use文で使うデータベースを指定している前提
 *
 * @author yuki
 *
 */
public class SQLforMySQL {
    //@formatter:off
    private static final String
    CREATE = "CREATE",
    TABLE = "TABLE",
    VALUES = "VALUES",
    START_KAKKO = "(",
    END_KAKKO = ")",
    SPACE = " ",
    DROP = "DROP",
    UNIQUE = "UNIQUE",
    INSERT = "INSERT",
    INTO = "INTO",
    UPDATE = "UPDATE",
    SET = "SET",
    WHERE = "WHERE",
    EQUAL = "=",
    HATENA = "?",
    COMMA = ",",
    SELECT = "SELECT",
    FROM = "FROM",
    DELETE = "DELETE",
    KEY = "KEY",
    PRIMARY_KEY = "PRIMARY" + SPACE + KEY,
    LIMIT = "limit",
    ORDER = "ORDER",
    GROUP = "GROUP",
    BY = "BY",
    LIKE="LIKE";
// @formatter:on
    /**
     * CREATE文
     *
     * @param tableName
     * @param colsAndDataType
     *            カラム名 データ型 [オプション],,,,,
     * @return
     */
    public final static String createTable(String tableName, String colsAndDataType) {
        return CREATE + SPACE + TABLE + SPACE + tableName + SPACE + START_KAKKO + colsAndDataType + END_KAKKO;
    }

    /**
     * CREATE文
     *
     * @param tableName
     * @param colsAndDataType
     *            カラム名 データ型 [オプション],,,,,
     * @return
     */
    public final static String createTable(String tableName, String... colsAndDataType) {
        return CREATE + SPACE + TABLE + SPACE + tableName + SPACE + createCols(colsAndDataType);
    }

    /**
     * ドロップ文　テーブルを消す
     *
     * @param tableName
     * @return
     */
    public final static String dropTable(String... tableName) {
        return DROP + SPACE + TABLE + SPACE + MyArray.join(tableName, COMMA);
    }

    /**
     * mysqldumpバックアップ用sqlファイルを出力する
     *
     * @param user
     * @param pass
     * @param dbName
     * @param fileName
     * @return
     */
    public final static String backup(String user, String pass, String dbName, String fileName) {
        return "mysqldump -u" + user + " -p" + pass + " " + dbName + " > " + fileName;
    }

    /**
     * 復元もしくはインポート
     *
     * @param user
     * @param pass
     * @param dbName
     * @param fileName
     * @return
     */
    public final static String restore(String user, String pass, String dbName, String fileName) {
        return "mysql -u" + user + " -p" + pass + " " + dbName + " < " + fileName;
    }

    /**
     * インサート文を作って返す
     *
     * @param tableName
     *            テーブル名
     * @param cols
     *            (c1,c2,c3)な文字列
     * @param values
     *            (?,?,?)な文字列<br>
     *            prepareを使わない場合は文字列は''で囲む
     * @return
     */
    public final static String insert(String tableName, String cols, String values) {
        return INSERT + SPACE + INTO + SPACE + tableName + cols + SPACE + VALUES + values;
    }

    /**
     * インサート文を作って返す
     *
     * @param tableName
     *            テーブル名
     * @param cols
     *            ["c1","c2","c3"]な文字列配列
     * @param values
     *            ["?","?","?"]な文字列配列<br>
     *            prepareを使わない場合は文字列は''で囲む
     * @return
     */
    public final static String insert(String tableName, String[] cols, String[] values) {
        return insert(tableName, createCols(cols), createCols(values));
    }

    /**
     * インサート文を作って返す
     *
     * @param tableName
     *            テーブル名
     * @param cols
     *            ["c1","c2","c3"]な文字列配列
     * @return
     */
    public final static String insert(String tableName, String[] cols) {
        return insert(tableName, createCols(cols), createHatenaValues(cols.length));
    }

    /**
     * update文作る
     *
     * @param tableName
     * @param set
     *            SET句
     * @param where
     *            WHERE句
     * @return
     */
    public final static String update(String tableName, String set, String where) {
        return UPDATE + SPACE + tableName + SPACE + SET + SPACE + set + SPACE + WHERE + SPACE + where;
    }

    /**
     * update文作る
     *
     * @param tableName
     * @param set
     *            SET句
     * @return
     */
    public final static String update(String tableName, String set) {
        return UPDATE + SPACE + tableName + SPACE + SET + SPACE + set;
    }

    /**
     * update文作る
     *
     * @param tableName
     * @param where
     *            WHERE句
     * @return
     */
    public final static String update(String tableName, String col, String val, String where) {
        return UPDATE + SPACE + SET + SPACE + createSet(col, val) + SPACE + WHERE + SPACE + where;
    }

    /**
     * update文作る
     *
     * @param tableName
     * @param where
     *            WHERE句
     * @return
     */
    public final static String update(String tableName, String[] cols, String[] vals, String where) {
        return UPDATE + SPACE + tableName + SPACE + SET + SPACE + createSet(cols, vals) + SPACE + WHERE + SPACE + where;
    }

    /**
     * update文作る
     *
     * @param tableName
     * @param where
     *            WHERE句
     * @return
     */
    public final static String update(String tableName, String[] cols, String where) {
        return UPDATE + SPACE + tableName + SPACE + SET + SPACE + createSet(cols) + SPACE + WHERE + SPACE + where;
    }

    /**
     * update文作る
     *
     * @param tableName
     * @param where
     *            WHERE句
     * @return
     */
    public final static String update(String tableName, String[] cols, String[] vals) {
        return UPDATE + SPACE + tableName + SPACE + SET + SPACE + createSet(cols, vals);
    }

    /**
     * update文作る
     *
     * @param tableName
     * @param where
     *            WHERE句
     * @return
     */
    public final static String update(String tableName, String[] cols) {
        return UPDATE + SPACE + tableName + SPACE + SET + SPACE + createSet(cols);
    }

    /**
     * col=valな文字列
     *
     * @param col
     * @param val
     *            省略した場合col=?
     * @return
     */
    public final static String createSet(String col, String val) {
        return col + EQUAL + val;
    }

    /**
     * col=?
     *
     * @param col
     * @return
     */
    public final static String createSet(String col) {
        return createSet(col, HATENA);
    }

    /**
     * cols=values,,,
     *
     * @param cols
     * @param values
     * @return
     */
    public final static String createSet(String[] cols, String[] values) {
        return MyArray.join(MyArray.join(cols, values, EQUAL), COMMA);
    }

    /**
     * cols=?,,,
     *
     * @param cols
     * @param values
     * @return
     */
    public final static String createSet(String[] cols) {
        return MyArray.join(cols, EQUAL + HATENA + COMMA) + EQUAL + HATENA;
    }

    /**
     * SELECT文作る
     *
     * @param tableName
     * @param cols
     * @param where
     * @return
     */
    public final static String select(String tableName, String cols, String where) {
        return SELECT + SPACE + cols + SPACE + FROM + SPACE + tableName + SPACE + WHERE + SPACE + where;
    }

    /**
     * select
     *
     * @param tableName
     * @param cols
     *            配列
     * @param where
     * @return
     */
    public final static String select(String tableName, String[] cols, String where) {
        return SELECT + SPACE + createCols(false, cols) + SPACE + FROM + SPACE + tableName + SPACE + WHERE + SPACE + where;
    }

    /**
     * SELECT文作る
     *
     * @param tableName
     * @param cols
     * @return
     */
    public final static String select(String tableName, String cols) {
        return SELECT + SPACE + cols + SPACE + FROM + SPACE + tableName;
    }

    /**
     * select
     *
     * @param tableName
     * @param cols
     *            配列
     * @return
     */
    public final static String select(String tableName, String[] cols) {
        return SELECT + SPACE + createCols(false, cols) + SPACE + FROM + SPACE + tableName;
    }

    /**
     * DELETE文作る
     *
     * @param tableName
     * @param where
     * @return
     */
    public final static String delete(String tableName, String where) {
        return DELETE + SPACE + FROM + SPACE + tableName + SPACE + WHERE + SPACE + where;
    }

    /**
     * UNIQUE()
     *
     * @param cols
     * @return
     */
    public final static String unique(String... cols) {
        return UNIQUE + createCols(cols);
    }

    /**
     * primary key()
     *
     * @param cols
     * @return
     */
    public final static String primary(String... cols) {
        return PRIMARY_KEY + createCols(cols);
    }

    /**
     * (a,b,c)な感じにする
     *
     * @param cols
     *            "a","b","c"もしくは配列["a","b","c"]
     * @return
     */
    public final static String createCols(String... cols) {
        return START_KAKKO + MyArray.join(cols, COMMA) + END_KAKKO;
    }

    /**
     * カッコなしパターン (a,b,c)な感じにする
     *
     * @param cols
     *            "a","b","c"もしくは配列["a","b","c"]
     * @return
     */
    public final static String createCols(boolean dummy, String... cols) {
        return MyArray.join(cols, COMMA);
    }

    /**
     * i個の(?,?)つくる
     *
     * @param i
     * @return
     */
    public final static String createHatenaValues(int i) {
        StringBuffer sb = new StringBuffer();
        sb.append(START_KAKKO);
        while (--i >= 0) {
            sb.append(HATENA);
            if (i > 0) {
                sb.append(COMMA);
            }
        }
        return sb.append(END_KAKKO).toString();
    }

    /**
     * i個の?,?つくる
     *
     * @param i
     * @return
     */
    public final static String createHatenaValues(boolean dummy, int i) {
        StringBuffer sb = new StringBuffer();
        while (--i >= 0) {
            sb.append(HATENA);
            if (i > 0) {
                sb.append(COMMA);
            }
        }
        return sb.toString();
    }

    /**
     * limit
     *
     * @param start
     * @param size
     * @return
     */
    public static String limit(int start, int size) {
        return SPACE + LIMIT + SPACE + start + COMMA + size;
    }

    /**
     * limit
     *
     * @param size
     * @return
     */
    public static String limit(int size) {
        return SPACE + LIMIT + SPACE + size;
    }

    /**
     * order by
     *
     * @param cols
     *            ソートするカラムとオプション
     * @return
     */
    public static String orderBy(String cols) {
        return SPACE + ORDER + SPACE + BY + SPACE + cols;
    }

    /**
     * group by
     *
     * @param cols
     * @return
     */
    public static String groupBy(String cols) {
        return SPACE + GROUP + SPACE + BY + SPACE + cols;
    }

    /**
     * id like value
     *
     * @param id
     * @param value
     * @return
     */
    public static String like(String id, String value) {
        return SPACE + id + SPACE + LIKE + SPACE + value;
    }

    /**
     * id like ?
     *
     * @param id
     * @return
     */
    public static String like(String id) {
        return SPACE + id + SPACE + LIKE + SPACE + HATENA;
    }

}
