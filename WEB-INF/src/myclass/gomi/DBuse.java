package myclass.gomi;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class DBuse {
	//フィールド
	private Connection con = null;		// DB接続用
	private Statement stmt = null;		// DB接続口
	private ResultSet rs = null;				// SQLの実行結果格納用
	private int cnt = 0;						// SQL実行カウンタ
	private String host = "localhost";	// サーバ名称(後々は)
	private String dbName = null;	// データベース名称
	// 接続URLの作成
	private String dbUrl = "jdbc:mysql://" + this.host + "/" + this.dbName+"?useUnicode=true&characterEncoding=UTF-8";
	private String dbUser = "root";	// DB接続ユーザ
	private String dbPw = "root";		// DB接続パスワード
	private StringBuffer sql=null;
	private StringBuffer errMes = null;
	private String table=null;

	private StringBuffer errSql = null;

// 連想配列(Hash)の準備
//		１レコード分の情報を格納する連想配列
	private HashMap<String,String> map = null;
	//連想配列を格納する配列
	private ArrayList<HashMap> list=null;
	//データの行数を格納する変数
	public int length;
	private String driver="org.gjt.mm.mysql.Driver";
//			"oracle.jdbc.driver.OracleDriver"


	//コンストラクタ
	public DBuse() {

	}
	/**
	 * いろいろ設定を一気にする
	 * @param v ホスト,DB(ORACLEはSID),user,pass,tableの順番
	 * 前から順番に判定し、後ろは省略可能
	 * 前を省略するときは""
	 * ("","",user,pass,table) とか(host,DB)とか("",DB,user)とか
	 */
	public DBuse setAll(String... v) {
		if(!"".equals(v[0]))this.host=v[0];
		if(v.length>=2){
			if(!"".equals(v[1]))this.dbName=v[1];
		}
		if(v.length>=3){
			if(!"".equals(v[2]))this.dbUser=v[2];
		}
		if(v.length>=4){
			if(!"".equals(v[3]))this.dbPw=v[3];
		}
		if(v.length>=5){
			if(!"".equals(v[4]))this.table=v[4];
		}
		setMysqlUrl();
		return this;
	}
	/**
	 * Tableの名前をセット
	 * @param v セットする名前
	 */
	public DBuse setTableName(String v) {
		this.table=v;
		return this;
	}
	/**
	 * 使うDB(SID)の名前をセット
	 * @param v セットする名前
	 */
	public DBuse setDBName(String v) {
		this.dbName=v;
		setMysqlUrl();
		return this;
	}
	/**
	 * 使うHOSTの名前をセット
	 * @param v セットする名前
	 */
	public DBuse setHost(String v) {
		this.host=v;
		setMysqlUrl();
		return this;
	}
	/**
	 * MySQL版URLをセット(ついでにdriverもセット)
	 */
	public DBuse setMysqlUrl() {
		this.driver="org.gjt.mm.mysql.Driver";
		this.dbUrl = "jdbc:mysql://" + this.host + "/" + this.dbName+"?useUnicode=true&characterEncoding=UTF-8";
		return this;
	}
	/**
	 * Oracle版URLをセット
	 */
	public DBuse setOracleUrl() {
		this.driver="oracle.jdbc.driver.OracleDriver";
		this.dbUrl = "jdbc:oracle:thin:@"+this.host+this.dbName;
		return this;
	}
	/**
	 * 使うUserをセット
	 * @param v セットする名前
	 */
	public DBuse setUser(String v) {
		this.dbUser=v;
		return this;
	}
	/**
	 * 使うパスワードをセット
	 * @param v セットする名前
	 */
	public DBuse setPass(String v) {
		this.dbPw=v;
		return this;
	}
	/**
	 * SQL文を全部書いてセット
	 * @param v セットするSQL文
	 */
	public DBuse setSql(String v) {
		this.sql=new StringBuffer(v);
		return this;
	}
	/**
	 * "select * from "+vをsqlに格納
	 * @param v テーブル名(設定してあれば省略可)
	 */
	public DBuse setSelectAll(String v) {
		this.sql=new StringBuffer("select * from "+v);
		return this;
	}
	/**
	 * select * from tableをsqlに格納(tableが設定してある時引数いらない)
	 */
	public DBuse setSelectAll() {
		this.sql=new StringBuffer("select * from "+this.table);
		return this;
	}
	/**
	 * Delete文をつくる(テーブルを設定してある場合)
	 * @param w 条件(whereはいらない)
	 */
	public DBuse setDelete(String w) {
		this.sql=new StringBuffer("delete");
		addFrom(this.table);
		addWhere(w);
		return this;
	}
	/**
	 * Delete文をつくる
	 * @param f テーブル名(from はいらない)
	 * @param w 条件(whereはいらない)
	 */
	public DBuse setDelete(String f,String w) {
		this.sql=new StringBuffer("delete");
		addFrom(f);
		addWhere(w);
		return this;
	}
	/**
	 * 現在のSQL文の最後に" where "+vを追加
	 * @param v セットする条件(条件のみ)
	 */
	public DBuse addWhere(String v) {
		this.sql.append(" where "+v);
		return this;
	}
	/**
	 * 現在のSQL文の最後に" from "+vを追加
	 * @param v セットする条件(条件のみ)
	 */
	public DBuse addFrom(String v) {
		this.sql.append(" from "+v);
		return this;
	}

	/**
	 * UPDATE文を変数sqlに格納します。
	 * @param f テーブル名(先に設定してある場合""で省略可)ここで設定しても先に設定したtableは上書きされない
	 * @param w 条件(""で省略可)
	 * @param v 配列にフィールド名,入れるデータを交互に格納(データは'で囲まない)
	 */
	public DBuse setUpdate(String f,String w,String... v) {
		this.sql=new StringBuffer("update ");
		if(!"".equals(f))this.sql.append(f+" set ");
		else this.sql.append(this.table+" set ");
		for(int i=0;i<v.length;i+=2){
			this.sql.append(v[i]+"='"+v[i+1]+"'");
			if(i+2<v.length)this.sql.append(",");
		}
		if(!"".equals(w))addWhere(w);
		return this;
	}


	/**
	 * INSERT文を変数sqlに格納します。(すべてのフィールドにデータ入れる版)
	 * @param f テーブル名(先に設定してある場合""で省略可)ここで設定したテーブル名はここでしか使わない。
	 * @param v 入れるデータをフィールドの順番で
	 */
	public DBuse setInsertAll(String f,String... v) {
		this.sql=new StringBuffer("insert into ");
		if(!"".equals(f))this.sql.append(f+" values (");
		else this.sql.append(this.table+" values (");
		for(int i=0;i<v.length;i++){
			this.sql.append("'"+v[i]+"'");
			if(i+1<v.length)this.sql.append(",");
			else this.sql.append(")");
		}
		return this;
	}





/**
 * MySQLでSQL文を実行します。
 * @param sql sql文(先頭には絶対に空白を入れない。これは先頭の文字でselect or update or delete or insertを判別するため)
 * @param fname 取り出したいフィールド名（select以外省略可）
 * @return エラーだった場合falseを返す
 */
	public boolean exeSql(String... fname) throws SQLException{
		boolean flg = true;	//成功＝true
		this.list=new ArrayList();
		this.length=0;
		//pattern=false=select,true=update or delete
		boolean updateflag=("s".equals(this.sql.substring(0, 1))||"".equals(this.sql.substring(0, 1)))?false:true;
		try{
			// ドライバクラスのロード
			Class.forName(this.driver);
			// データベースへの接続
			this.con = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPw);

			//SQLの実行口の取得
			this.stmt = this.con.createStatement();
			if(updateflag){
				this.stmt.executeUpdate(this.sql.toString());
			}else{
				this.rs = this.stmt.executeQuery(this.sql.toString());
				// 次がある間は繰り返す
				while( this.rs.next() ){
					//this.rsList.add( this.rs.getString("info") );
					//this.rsList.add( this.rs.getString("uname") );
					this.map = new HashMap<String,String>();
					for(int i=0; i < fname.length; i++) {
						this.map.put(fname[i], this.rs.getString(fname[i]));
					}
					// １件分のデータ（HashMap)をArrayListの格納
					this.list.add(this.map);
					this.length++;

					//	ほかにもフィールドが数値型の場合(例えば在庫数など)
					//	this.rs.getInt(”count”)
					// 等と書く
				}
			}
		}catch(ClassNotFoundException e){
			// Driver系エラー発生時
			this.errMes = new StringBuffer();
			this.errMes.append( "Driver:" + e.getMessage() );
		}catch(SQLException e){
			// SQL系エラー発生時
			this.errMes = new StringBuffer();
			this.errSql = new StringBuffer();
			this.errSql.append( "SQL:" + this.sql + "" );
			this.errMes.append( e.getMessage() );
		}catch(Exception e){
			// その他エラー発生時
			this.errMes = new StringBuffer();
			this.errMes.append( e.getMessage() );
			flg = false;
		}finally{
			// エラーの場合はthis.stmt/this.rs/this.con共にnullがセットされる
			if( this.rs != null ){
				this.rs.close();
			}
			if( this.stmt != null ){
				this.stmt.close();
			}
			if( this.con != null ){
				this.con.close();
			}
		}
		return flg;
	}

	/*
	 * ＜もうこれは使わない＞
	 */
	public boolean updateTable(String sql, String... fname) throws SQLException{
		boolean flg = true;	//成功＝true
		try{
			// ドライバクラスのロード
			Class.forName("com.mysql.jdbc.Driver");
			// データベースへの接続
			this.con = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPw);

			//SQLの実行口の取得
			this.stmt = this.con.createStatement();
			this.rs = this.stmt.executeQuery(sql);
			// 次がある間は繰り返す
			while( this.rs.next() ){
				//this.rsList.add( this.rs.getString("info") );
				//this.rsList.add( this.rs.getString("uname") );
				this.map = new HashMap<String,String>();
				for(int i=0; i < fname.length; i++) {
					this.map.put(fname[i], this.rs.getString(fname[i]));
				}
				// １件分のデータ（HashMap)をArrayListの格納
				this.list.add(this.map);

				//	ほかにもフィールドが数値型の場合(例えば在庫数など)
				//	this.rs.getInt(”count”)
				// 等と書く
			}
		}catch(ClassNotFoundException e){
			// Driver系エラー発生時
			this.errMes = new StringBuffer();
			this.errMes.append( "Driver:" + e.getMessage() );
		}catch(SQLException e){
			// SQL系エラー発生時
			this.errMes = new StringBuffer();
			this.errSql = new StringBuffer();
			this.errSql.append( "SQL:" + this.sql + "" );
			this.errMes.append( e.getMessage() );
		}catch(Exception e){
			// その他エラー発生時
			this.errMes = new StringBuffer();
			this.errMes.append( e.getMessage() );
			flg = false;
		}finally{
			// エラーの場合はthis.stmt/this.rs/this.con共にnullがセットされる
			if( this.rs != null ){
				this.rs.close();
			}
			if( this.stmt != null ){
				this.stmt.close();
			}
			if( this.con != null ){
				this.con.close();
			}
		}
		return flg;
	}

	/**
	 * エラーメッセージ取得
	 * @return エラーメッセージ
	 */
	public StringBuffer getErrerMessage() {
			return this.errSql.append("、エラーメッセージ："+this.errMes);
	}
	/**
	 * オブジェクトごとセレクト文の結果を返す
	 * @return セレクト文の結果
	 */
	public ArrayList<HashMap> getList() {
		return this.list;
	}
	/**
	 * フィールド単位で結果を返す
	 * @return フィールド
	 */
	public String getField(int i,String s) {
		return (String)this.list.get(i).get(s);
	}

	/**
	 * 変数sqlの現在の中身を返す
	 * @return sql.toString()
	 */
	public String getSql() {
		return this.sql.toString();
	}
	/**
	 * 現在設定中のtable返す
	 * @return table
	 */
	public String getTableName() {
		return this.table;
	}
	/**
	 * 現在設定中のhost返す
	 * @return host
	 */
	public String getHost() {
		return this.host;
	}
	/**
	 * 現在設定中のDB返す
	 * @return DB
	 */
	public String getDB() {
		return this.dbName;
	}
	/**
	 * 現在設定中のuser返す
	 * @return user
	 */
	public String getUser() {
		return this.dbUser;
	}
	/**
	 * 現在設定中のpass返す
	 * @return pass
	 */
	public String getPass() {
		return this.dbPw;
	}
	/**
	 * 現在設定中のURL返す
	 * @return URL
	 */
	public String getUrl() {
		return this.dbUrl;
	}
	/**
	 * 現在設定中のDRIVER返す
	 * @return DRIVER
	 */
	public String getDriver() {
		return this.driver;
	}

}
