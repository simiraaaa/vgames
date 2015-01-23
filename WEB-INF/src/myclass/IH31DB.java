package myclass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import myclass.database.MyQuery;


public class IH31DB extends MyQuery{

	private Object[] prepareObjects =null;

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
	public IH31DB setPrepareObjects(Object... os) {
		this.prepareObjects=(os==null? new Object[]{null}:os);
		return this;
	}
	/**
	 * SQL文を実行する
	 * @param sql SQL文
	 * @param primary SELECT文で実行したあとMAPに格納するキー
	 * @param fields キーに紐づいたデータ
	 * @return boolean
	 */
	public boolean action(String sql, String primary,String... fields) {
		boolean isTrue =false;
		Connection con= null;
		boolean isSelect=!"".equals(primary);

		try {
			con= new IH31DBconnection().open();
			if(isSelect){
				isTrue=exe(con.prepareStatement(sql), primary, fields);
			}else {
				isTrue=exe(con.prepareStatement(sql));
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}finally{
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}

		return isTrue;
	}


	/**
	 * SQLを実行する
	 * @param sql SELECT以外のSQL文
	 * @return true or false
	 */
	public boolean action(String sql) {
		return action(sql,"");
	}

}
