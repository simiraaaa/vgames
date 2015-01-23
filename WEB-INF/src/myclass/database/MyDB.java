package myclass.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class MyDB extends MyQuery {

	/**
	 * 汎用データベースクラス
	 */
	public MyDB() {
	}
	/**
	 * １回SQL文を実行して、con.close()する
	 * @param con Connection
	 * @param sql SQL文
	 * @param primary mapの主キーにしたいフィールド名
	 * @param fields mapにセットしたいフィールド
	 * @return true or false
	 */
	public boolean action(Connection con, String sql, String primary,String... fields) {
		boolean isTrue =false;
		boolean isSelect=!"".equals(primary);

		try {
			if(isSelect){
				isTrue=exe(con.prepareStatement(sql),primary,fields);
			}else{
				isTrue=exe(con.prepareStatement(sql));
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}finally{
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					System.out.println("エラーが出たSQL文\n"+sql);
				}

			}
		}
		return isTrue;
	}

	/**
	 * 複数回SQL文を実行して、con.close()する（SELECT以外）
	 * @param con Connection
	 * @param sqls SELECT以外のSQL文を実行する順番の配列
	 * @return true or false
	 */
	public boolean action(Connection con, String[] sqls) {
		boolean isTrue =false;
		String sql=null;
		try {

			for(int i =0,len=sqls.length;i<len;){
				sql=sqls[i];
				exe(con.prepareStatement(sqls[i]));
			}
			isTrue=true;
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}finally{
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					System.out.println("エラーが出たSQL文\n"+sql);
				}

			}
		}
		return isTrue;
	}


	/**
	 * １回SQL文を実行して、con.close()する
	 * @param con Connection
	 * @param sql SQL文
	 * @return true or false
	 */
	public boolean action(Connection con,String sql) {
		return action(con, sql,"");
	}

	private Object[] prepareObjects=null;
	@Override
	protected void setPreparedSql(PreparedStatement ps) {
		if(this.prepareObjects==null)return;
		try {
			for(int i=0,len=this.prepareObjects.length;i<len;++i){
				Object o=this.prepareObjects[i];
				ps.setObject(i+1, (o==null || o.equals("") ? "null": o));
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	/**
	 * PreparedStatementの?に入れる
	 */
	public MyDB setPrepareObjects(Object... os) {
		this.prepareObjects=(os==null? new Object[]{null}:os);
		return this;
	}

}
