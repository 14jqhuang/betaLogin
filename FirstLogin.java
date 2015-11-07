package login;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLogin 
{
	/*
	 * 判断是否有默认的登陆账号,并且字段名不能为某些特殊字符，如default等
	 */
	
	DataBaseconnection dbc = new DataBaseconnection();
	ResultSet res4;
	public FirstLogin()
	{
		res4=dbc.executeQuery("select * from stuacc where defaultacc='yes'");
		try 
		{
			while (res4.next())
			{
				String username=res4.getString(1);
				String password=res4.getString(2);
				new Login(username,password);
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}
