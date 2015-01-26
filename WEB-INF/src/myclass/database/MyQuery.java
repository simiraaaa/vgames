package myclass.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class MyQuery{
    public MyQuery() {
        // TODO 自動生成されたコンストラクター・スタブ
    }

    protected HashMap<Object, HashMap<String, Object>> map = null;
    protected ArrayList<HashMap<String, Object>> list = null;

    /**
     * データベースにアクセスしSQL文を実行する（con.close()しない）
     *
     * @param con
     *            Connection
     * @param sql
     *            SQL文
     * @return
     * @throws SQLException
     */
    public final MyQuery exe(Connection con, String sql) throws SQLException {
        return exe(con, sql, "");
    }


    /**
     * データベースにアクセスしSQL文を実行する（con.close()しない）
     *
     * @param con
     *            Connection
     * @param sql
     *            SQL文
     * @param primary
     *            mapの主キーにしたいフィールド名
     * @param fields
     *            mapにセットしたいフィールド
     * @return
     * @throws SQLException
     */
    public final MyQuery exe(Connection con, String sql, String primary,
 String... fields) throws SQLException {
            return exe(con.prepareStatement(sql), primary, fields);
    }


    /**
     * 
     * @param ps
     *            実行するPreparedStatement
     * @param primary
     *            マップに保存するデータの主キーにしたい列名
     * @param fields
     *            マップに保存するデータの列名
     * @return 成功ならtrue
     * @throws SQLException
     */
    public final MyQuery exe(PreparedStatement ps, String primary, String... fields) throws SQLException {
        exeOnly(ps, primary, fields);
        close(ps);
        return this;
    }

    protected static void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.out.println("ps.close();時のエラー");
                e.printStackTrace();
            }
        }
    }

    public void exeOnly(PreparedStatement ps, String primary, String... fields) throws SQLException {

        boolean isArray = "[]".equals(primary);
        boolean isSelect = !"".equals(primary);
        ResultSet rs = null;
        setPreparedSql(ps);
        if (isSelect) {
            rs = ps.executeQuery();
            if (isArray) {
                this.list = getList(rs, fields);
            } else {
                this.map = getMap(rs, primary, fields);
            }
        }else {
            ps.executeUpdate();
        }

    }

    /**
     * Select以外のSQL文を実行する
     * 
     * @param ps実行するPreparedStatement
     * @return 成功ならtrue
     * @throws SQLException
     */
    public final MyQuery exe(PreparedStatement ps) throws SQLException {
        return exe(ps, "");
    }

    /**
     * ResultSetをmapに格納する(rs.closeする)
     *
     * @param primary
     *            mapの主キーにしたいフィールド名
     * @param fields
     *            mapにセットしたいフィールド
     * @return mp セットしたマップ
     * @throws SQLException
     */
    public final static HashMap<Object, HashMap<String, Object>> getMap(ResultSet rs,
            String primary, String... fields) throws SQLException {
        HashMap<Object, HashMap<String, Object>> mp = new HashMap<Object, HashMap<String, Object>>();
        int len = fields.length;
        while (rs.next()) {
            HashMap<String, Object> map2 = new HashMap<String, Object>();
            for (int j = 0; j < len;)
                map2.put(fields[j], rs.getObject(fields[j++]));
            mp.put(rs.getObject(primary), map2);
        }
        close(rs);
        return mp;
    }

    /**
     * ResultSetをlistに格納する(rs.closeする)
     *
     * @param fields
     *            mapにセットしたいフィールド
     * @return li セットしたマップ
     * @throws SQLException
     */
    public final static ArrayList<HashMap<String, Object>> getList(ResultSet rs,
            String... fields) throws SQLException {
        ArrayList<HashMap<String, Object>> li = new ArrayList<HashMap<String, Object>>();
        int len = fields.length;
        while (rs.next()) {
            HashMap<String, Object> map2 = new HashMap<String, Object>();
            for (int j = 0; j < len;)
                map2.put(fields[j], rs.getObject(fields[j++]));
            li.add(map2);
        }
        close(rs);
        return li;
    }

    private static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("rsクローズ時のエラー");
                e.printStackTrace();
            }
        }
    }

    public final ArrayList<HashMap<String, Object>> getList() {
        return list;
    }


    public final HashMap<Object, HashMap<String, Object>> getMap() {
        return this.map;
    }

    /**
     * exeメソッドの中で使う 何も処理しなければ setPreparedSql(PreparedStatement ps){}と書く
     *
     * @param ps
     */
    abstract protected void setPreparedSql(PreparedStatement ps);



}