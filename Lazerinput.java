package login;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Lazerinput
{
	DataBaseconnection dbc= new DataBaseconnection();
	ResultSet res;
	public Lazerinput(String user, String pass)
	{
		res=dbc.executeQuery("select * from lazer where account='"+user+"'");
		try {
			if (!res.next())
			{
				dbc.executeUpdate("insert into lazer values('"+user+"','"+pass+"')");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
