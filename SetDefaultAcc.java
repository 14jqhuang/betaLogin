package login;

import java.sql.ResultSet;
//自动登陆模块
public class SetDefaultAcc implements Runnable
{
	//单独的线程
	DataBaseconnection dbc=new DataBaseconnection();
	ResultSet res;
	String pass=null;
	
	public SetDefaultAcc(String temp)
	{
		this.pass=temp;
	}
		

	public void run()
	{
		res=dbc.executeQuery("select * from stuacc where defaultacc='yes'");
		try
		{
			if (res.next())
			{
				String acc=res.getString(1);
				dbc.executeUpdate("update stuacc set defaultacc='' where account='"+acc+"'");
				dbc.executeUpdate("update stuacc set defaultacc='yes' where account='"+pass+"'");
			}
			else 
			{
				dbc.executeUpdate("update stuacc set defaultacc='yes' where account='"+pass+"'");
		
			}
		}
		catch(Exception e){}
	}

}
