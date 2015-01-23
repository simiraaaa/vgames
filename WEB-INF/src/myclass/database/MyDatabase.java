package myclass.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 汎用データベースクラス
 *
 */
public class MyDatabase extends MyQuery{

    private Connection con = null;

    public Connection getCon() {
        return con;
    }

    public final MyDatabase setConnection(Connection conn) {
        con = conn;
        return this;
    }

    public final MyDatabase setConnection(String host, String name, String user,
            String pass) {
        try {
            con = MySQLDatabaseConnection.open(host, name, user, pass);
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return this;
    }

    public final MyDatabase setConnection(String name, String user, String pass) {
        try {
            con = MySQLDatabaseConnection.open(name, user, pass);
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return this;
    }

    public final MyDatabase setConnection(String user, String pass) {
        try {
            con = MySQLDatabaseConnection.open(user, pass);
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return this;
    }

    public MyDatabase(Connection conn) {
        con = conn;
    }

    public MyDatabase() {
        // TODO 自動生成されたコンストラクター・スタブ
    }

    public MyDatabase(String host, String name, String user, String pass) {
        setConnection(host, name, user, pass);
    }

    public MyDatabase(String name, String user, String pass) {
        setConnection(name, user, pass);
    }

    public MyDatabase(String user, String pass) {
        setConnection(user, pass);
    }

    /**
     * sql文を実行する<BR>
     * SELECT以外
     *
     * @param sql
     * @return
     */
    public final boolean exe(String sql) {
        return exe(con, sql);
    }

    /**
     * SELECT実行
     *
     * @param sql
     * @param primary
     * @param fields
     * @return
     */
    public final boolean exe(String sql, String primary, String... fields) {
        return exe(con, sql, primary, fields);
    }

    public final MyDatabase close() {
        try {
            con.close();
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            System.out.println("Connectionクローズに失敗");
            e.printStackTrace();
        }
        this.list = null;
        this.map = null;
        return this;
    }

    private Object[] prepareObjects = null;

    @Override
    protected void setPreparedSql(PreparedStatement ps) {
        if (this.prepareObjects == null)
            return;
        try {
            for (int i = 0, len = this.prepareObjects.length; i < len; ++i) {
                Object o = this.prepareObjects[i];
                ps.setObject(i + 1, (o == null || o.equals("") ? "null" : o));
            }
        } catch (SQLException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } finally {
            this.prepareObjects = null;
        }
    }

    /**
     * PreparedStatementの?に入れる
     */
    public final MyDatabase setPrepareObjects(Object... os) {
        this.prepareObjects = (os == null ? new Object[] { null } : os);
        return this;
    }


}
