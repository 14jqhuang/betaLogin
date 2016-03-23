package login;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextPane;
//实时监控流量类
public class Update extends TimerTask
{
	Flow flow;
	ResultSet res;
	DataBaseconnection dbc = new DataBaseconnection();
	JTextPane jtp = new JTextPane();
	public Update(Flow flow)
	{
		this.flow=flow;
	}
	public void run() 
	{
		try { 
			DecimalFormat df = new DecimalFormat("#0.00");
			URL url = new URL("http://192.168.31.4:8080/");
			//打开URL 
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			//得到输入流，即获得了网页的内容 
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection
					.getInputStream()));
			String line;
			StringBuffer sb=new StringBuffer();
			while((line = reader.readLine()) != null)
			{
				sb.append(line);//asp页面打印的内容，注意是整个页面内容，包括HTML标签
			}
			String acc=sb.substring(2896,2922);//Used Bytes
			String stacc=sb.substring(2550,2580);//Account
			String total = sb.substring(3250,3280);//Total Flow

			int numm=stacc.indexOf(">");
			int num=stacc.indexOf("<");
			String accou=stacc.substring(numm+1,num);

			int num1 = sb.indexOf("Used bytes");
			String account= sb.substring(num1,num1+10);

			String regEx="[^0-9]";   
			Pattern p = Pattern.compile(regEx);   
			Matcher m1 = p.matcher(acc);
			Matcher m2 = p.matcher(total);
			String mrp1=m1.replaceAll("").trim();
			String mrp2=m2.replaceAll("").trim();
			double usedflow1=Double.parseDouble(mrp1);
			double usedflow2=Double.parseDouble(mrp2);
			String mused=df.format(usedflow1/(1024*1024));
			String mused1=df.format(usedflow2/(1024*1024));


			flow.l5.setText(accou);flow.l6.setText(mused);flow.l7.setText(mused1);
			//用户联网状态
			if (account.equals("Used bytes"))
			{
				flow.la4.setText(accou+"------Connected------");
			}
			else 
			{
				flow.la4.setText("You have logged out");
				flow.la5.setText("");
			}

			//判断流量是否超过指定额度流量	
			if (Double.parseDouble(flow.l6.getText())>=Double.parseDouble(flow.t4.getText()))
			{
				flow.la5.setForeground(Color.red);
				flow.la5.setText("Red Warning :"+accou+"的账号已经超过额定流量,您还可使用 ："+df.format(Double.parseDouble(mused1)-Double.parseDouble(mused))+"M");
			}
			else 
			{
				flow.la5.setForeground(Color.black);
				flow.la5.setText(accou+"账号流量在指定范围内，请放心使用");
			}
			//显示默认登陆账号
			res=dbc.executeQuery("select * from stuacc where defaultacc='yes'");
			if (res.next())
			{
				flow.la7.setForeground(Color.magenta);
				flow.la6.setText("当前的默认的自动登陆账号为 ： ");
				flow.la7.setText(res.getString(1));
			}
			else 
			{
				//flow.la6.setText("当前无默认登陆账号，亲，设置默认登陆账号下次可方便快速登陆哦！！！");
				flow.la7.setText("");}
		}	
		catch (Exception e)
		{}
	}
}
