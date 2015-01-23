package myclass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import myclass.database.MyQuery;

public class IH31DBCustomer extends MyQuery {

	private int id=0;
	@Override
	protected void setPreparedSql(PreparedStatement ps) {
		try {
			ps.setInt(1, this.id);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	public IH31DBCustomer setId(int id) {
		this.id = id;
		return this;
	}
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

}
