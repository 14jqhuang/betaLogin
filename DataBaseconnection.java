package login;
//数据库的连接
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseconnection {
	
	Connection con = null;
	Statement sql = null;
	ResultSet rs = null;

	public DataBaseconnection() {

		// 将可能出现异常的语句放入try块中.
		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载驱动
		} catch (ClassNotFoundException e) {
			System.out.println(e);//catch块处理异常.
		}
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/abcd","root","578178"); // 连接数据库

		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	// 查询
	public ResultSet executeQuery(String sql_s) {
		try {
			sql = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = sql.executeQuery(sql_s);
		} catch (SQLException e) {
			System.out.println(e);
		}
		return rs;
	}

	// update操作
	public int executeUpdate(String sql_s) {

		int rs = 0;
		try {
			sql = con.createStatement();
			rs = sql.executeUpdate(sql_s);
		} catch (SQLException e) {
			System.out.println(e);
		}

		return rs;
	}

	// 关闭数据库
	public void close() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
