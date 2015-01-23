package myclass.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabaseConnection {

	private final static String
	LOCAL_HOST="localhost",
	NAME = "XE",
	DRIVER="org.gjt.mm.mysql.Driver";
	/**
	 * コネクションを返します。
	 * @param host host名
	 * @param name	データベース名
	 * @param user	ユーザ名
	 * @param pass パスワード
	 * @return Connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public final static Connection open(String host, String name, String user, String pass) throws SQLException, ClassNotFoundException {
		// ドライバクラスのロード
		Class.forName(DRIVER);
		return DriverManager.getConnection(getURL(host, name), user, pass);
	}

	/**
	 * ローカルホストでコネクションを返す
	 * @param name XEとか
	 * @param user user
	 * @param pass password
	 * @return Connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public final static Connection open(String name, String user, String pass) throws SQLException, ClassNotFoundException {
		return open(LOCAL_HOST, name, user, pass);
	}
	/**
	 * ローカルホストXEでコネクションを返す
	 * @param user user
	 * @param pass password
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public final static Connection open(String user, String pass) throws SQLException, ClassNotFoundException {
		return open(NAME,user, pass);
	}

    private final static String getURL(String host, String name) {
		return "jdbc:mysql://" + host + "/" + name+"?useUnicode=true&characterEncoding=UTF-8";
	}
}
