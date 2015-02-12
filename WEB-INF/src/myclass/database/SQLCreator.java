package myclass.database;

import myclass.util.Compare;

/**
 * setしたデータをもとに簡単なSQL文を作る
 */
public class SQLCreator {
    private String table, whr, option;

    private String[] columns;

    public SQLCreator() {}

    public SQLCreator(String tableName) {
        setTable(tableName);
    }

    public SQLCreator(String tableName, String... cols) {
        setTable(tableName).setColumns(cols);
    }

    public SQLCreator setOption(String op) {
        option = op;
        return this;
    }

    /**
     * 使用するテーブルをセット<br>
     * select文の場合は複数のテーブルをスペース区切りでセットできます。
     *
     * @param tableName
     * @return
     */
    public final SQLCreator setTable(String tableName) {
        table = tableName;
        return this;
    }

    public final String getTable() {
        return table;
    }

    /**
     * UPDATE SELECT INSERTで使用するカラムをセット SELECT の場合はtableが複数ある場合 table.col [as
     * name]という感じで
     *
     * @param cols
     * @return
     */
    public final SQLCreator setColumns(String... cols) {
        columns = cols;
        return this;
    }

    public final String[] getColumns() {
        return columns;
    }

    /**
     * where文をセットします。<br>
     * prepareStatementで?を使用する場合、最後に追加されるものと考えてください。 <br>
     * where文を使用しない場合nullか""をセットしてください。
     *
     * @param where
     * @return
     */
    public final SQLCreator setWhere(String where) {
        whr = where;
        return this;
    }

    public final String getWhere() {
        return whr;
    }

    /**
     * insert文発行<br>
     * valuesを指定しなかった場合すべて?になります
     *
     * @param values
     * @return
     */
    public final String insert(String... values) {
        return SQLforMySQL.insert(table, columns, values);
    }

    /**
     * insert
     *
     * @return
     */
    public final String insert() {
        return SQLforMySQL.insert(table, columns);
    }

    /**
     * delete文を発行<br>
     * whereは必ずセットしてください
     *
     * @return
     */
    public final String delete() {
        checkOption();
        return SQLforMySQL.delete(table, whr) + option;
    }

    /**
     * update
     *
     * @param values
     * @return
     */
    public final String update(String... values) {
        checkOption();
        return Compare.isEmpty(whr) ? SQLforMySQL.update(table, columns, values) + option : SQLforMySQL.update(table, columns, values, whr) + option;
    }

    /**
     * update
     *
     * @return
     */
    public final String update() {
        checkOption();
        return Compare.isEmpty(whr) ? SQLforMySQL.update(table, columns) + option : SQLforMySQL.update(table, columns, whr) + option;
    }

    /**
     * select
     *
     * @return
     */
    public final String select() {
        checkOption();
        return Compare.isEmpty(whr) ? SQLforMySQL.select(table, columns) + option : SQLforMySQL.select(table, columns, whr) + option;
    }

    private void checkOption() {
        if (option == null) {
            option = "";
        }
    }

    private void checkWhere() {
        if (whr == null) {
            whr = "";
        }
    }

    /**
     * order byを追加
     *
     * @param cols
     * @return
     */
    public SQLCreator addOrderBy(String cols) {
        checkOption();
        option += SQLforMySQL.orderBy(cols);
        return this;
    }

    /**
     * group by 追加
     */
    public SQLCreator addGroupBy(String cols) {
        checkOption();
        option += SQLforMySQL.groupBy(cols);
        return this;
    }

    /**
     * limit追加
     *
     * @param start
     * @param size
     * @return
     */
    public SQLCreator addLimit(int start, int size) {
        checkOption();
        option += SQLforMySQL.limit(start, size);
        return this;
    }

    /**
     * limit追加
     *
     * @param size
     * @return
     */
    public SQLCreator addLimit(int size) {
        checkOption();
        option += SQLforMySQL.limit(size);
        return this;
    }

    /**
     * likeをwhereのあとに追加<br>
     * id like value
     *
     * @param id
     * @param value
     * @return
     */
    public SQLCreator addLike(String id, String value) {
        checkWhere();
        whr += SQLforMySQL.like(id, value);
        return this;
    }

    /**
     * likeをwhereのあとに追加<br>
     * id like ?
     *
     * @param id
     * @return
     */
    public SQLCreator addLike(String id) {
        checkWhere();
        whr += SQLforMySQL.like(id);
        return this;
    }

}
