package login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class AccPass 
{
	List list=new ArrayList();
	DataBaseconnection dbc = new DataBaseconnection();
	ResultSet res;
	String temp1 = null;
	public AccPass()
	{
		/*
		 * 初始化对即将要自动切换账号的集合
		 */
		list.clear();
		res=dbc.executeQuery("select * from stuacc where account!=''");
		try 
		{
			while (res.next())
			{
				list.add(res.getString(3));
			}
		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
		//get the original code source
		try {
			URL url =new URL("http://192.168.31.4:8080/");

			HttpURLConnection hurl = (HttpURLConnection) url.openConnection();

			BufferedReader br = new BufferedReader(new InputStreamReader(hurl.getInputStream()));

			String temp;

			StringBuffer sb= new StringBuffer();
			while ((temp=br.readLine())!=null)
			{
				sb.append(temp);
			}
			temp1 = sb.toString();
			
		}
		catch(Exception e){e.printStackTrace();}
		
		
		while (temp1.contains("Password"))
		{
			//判断用户名与密码是否正确
			try {
				URL url =new URL("http://192.168.31.4:8080/");

				HttpURLConnection hurl = (HttpURLConnection) url.openConnection();

				BufferedReader br = new BufferedReader(new InputStreamReader(hurl.getInputStream()));

				String temp;

				StringBuffer sb= new StringBuffer();
				while ((temp=br.readLine())!=null)
				{
					sb.append(temp);
				}
				temp1 = sb.toString();
				
			}
			catch(Exception e){e.printStackTrace();}

		}
	}
	
//	public static void main(String[] args) {
//		new AccPass();
//	}
}
