package myclass.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDatabaseConnection {


	private final static String
	LOCAL_HOST="localhost",
	XE = "XE",
	DRIVER="oracle.jdbc.driver.OracleDriver";
	/**
	 * コネクションを返します。
	 * @param host host名
	 * @param sid	データベース名
	 * @param user	ユーザ名
	 * @param pass パスワード
	 * @return Connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public final static Connection open(String host, String sid, String user, String pass) throws SQLException, ClassNotFoundException {
		// ドライバクラスのロード
		Class.forName(DRIVER);
		return DriverManager.getConnection(getURL(host, sid), user, pass);
	}

	/**
	 * ローカルホストでコネクションを返す
	 * @param sid XEとか
	 * @param user user
	 * @param pass password
	 * @return Connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
    public final static Connection open(String sid, String user, String pass) throws SQLException, ClassNotFoundException {
		return open(LOCAL_HOST, sid, user, pass);
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
		return open(XE,user, pass);
	}

    private final static String getURL(String host, String sid) {
		return "jdbc:oracle:thin:@"+host+":1521:"+sid;
	}
}
