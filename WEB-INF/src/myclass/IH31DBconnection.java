package myclass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class IH31DBconnection{
	public IH31DBconnection() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	private String host="localhost";
	private String dbName = "XE";
	private String dbUrl ="jdbc:oracle:thin:@"+this.host+":1521:"+this.dbName;
	private String dbUser = "IH31";
	private String dbPw = "pass";
	private String driver="oracle.jdbc.driver.OracleDriver";
	public Connection open() throws SQLException, ClassNotFoundException {
		// ドライバクラスのロード
		Class.forName(this.driver);
		return DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPw);
	}

}
