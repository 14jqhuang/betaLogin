package login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.TimerTask;

	//自动切换Login部分
public class AutoLogin extends TimerTask
{
	Flow flow;
	ResultSet res;
	DataBaseconnection dbc = new DataBaseconnection();
	public AutoLogin(Flow flow)
	{
		this.flow = flow;
	}
		public void run()
		{
				flow.index=(String) flow.list.get(0);
				res=dbc.executeQuery("select * from StuAcc where id='"+flow.index+"'");//Pay attention to the blank(空格)
				try 
				{
					while (res.next())
				  {		
						flow.acc=res.getString(1);
						flow.pass=res.getString(2);
						System.out.println(flow.acc);
				  }
					new Login(flow.acc,flow.pass);
				}
				
				catch (Exception e) 
				{System.out.println("W");}
				
				//get the original code source
				try {
					URL url =new URL("http://192.168.31.4:8080/");

					HttpURLConnection hurl = (HttpURLConnection) url.openConnection();

					BufferedReader br = new BufferedReader(new InputStreamReader(hurl.getInputStream()));

					String temp = null;

					StringBuffer sb= new StringBuffer();
					while ((temp=br.readLine())!=null)
					{
						sb.append(temp);
					}
					flow.temp1 = sb.toString();
					
				}
				catch(Exception e){e.printStackTrace();}
				
				
				while (flow.temp1.contains("Password"))
				{
					//判断用户名与密码是否正确
					flow.list.remove(flow.index);
					flow.index=(String) flow.list.get(0);
					res=dbc.executeQuery("select * from StuAcc where id='"+flow.index+"'");//Pay attention to the blank(空格)
					try 
					{
						while (res.next())
					  {		
							flow.acc=res.getString(1);
							flow.pass=res.getString(2);
							System.out.println(flow.acc);
					  }
						new Login(flow.acc,flow.pass);
						//Flush the web page
						Thread.sleep(3000);
						
					}catch (Exception ex){ex.printStackTrace();}
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
						flow.temp1 = sb.toString();
						
					}
					catch(Exception e){e.printStackTrace();}

				}
		}
}
