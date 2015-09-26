package login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;


public class Flow extends JPanel implements ActionListener,FocusListener,ItemListener
{
	String acc,pass;
	int id=0;
	int clicknum=0;
	int closePane=0;
	DataBaseconnection dbc=new DataBaseconnection();
	ResultSet res,res1,res2,res3,res4,res5,res6,res7;
	ResultSetMetaData rsmd;
	JPanel npanel,mpanel,spanel;
	JButton button,button1,exit,trip,setDefault;
	JLabel la1,la2,la3,la4,la5,la6,la7;
	JComboBox<String> user;
	JTextField t4,t5;
	JPasswordField pw;
	URL url;
	JLabel l1,l2,l3,l4,l5,l6,l7;
	Random random=new Random();
	List list=new ArrayList();
	List addList=new ArrayList();
	int[] string=new int[4];
	ScheduledExecutorService ser1= Executors.newScheduledThreadPool(1);//自动切换账号
	ScheduledExecutorService ser3= Executors.newScheduledThreadPool(1);//AutoLogout
	ScheduledExecutorService ser4= Executors.newScheduledThreadPool(2);//流量监控
	ScheduledExecutorService ser5= Executors.newScheduledThreadPool(1);//AutoLogin
//	ScheduledExecutorService ser2= Executors.newScheduledThreadPool(1);//FlowDetect
	
	public Flow()
	{
		npanel=new JPanel();
		spanel=new JPanel();
		mpanel=new JPanel();
		
		button=new JButton("Summit");
		button1=new JButton("Logout");
		trip=new JButton("自动切换账号");
		exit=new JButton("Exit");
		setDefault=new JButton("设置为默认账号");

		la1=new JLabel("账号: ",JLabel.RIGHT);
		la2=new JLabel("密码: ",JLabel.RIGHT);
		la3=new JLabel("将当前账号设置为默认登陆账号，以便下次自动登陆轻松上网",JLabel.CENTER);
		la3.setFont(new Font("宋体",Font.BOLD,14));
		la4= new JLabel("",JLabel.CENTER);la4.setFont(new Font("宋体",Font.BOLD,20));
		la5= new JLabel("",JLabel.CENTER);la5.setFont(new Font("宋体",Font.BOLD,14));
		la6= new JLabel("",JLabel.CENTER);la6.setFont(new Font("宋体",Font.BOLD,20));
		la7= new JLabel("本机所使用的流量 ：",JLabel.CENTER);la6.setFont(new Font("宋体",Font.BOLD,20));
		
		//获取输入历史
		user=new JComboBox<String>();
		user.setEditable(true);
		res5=dbc.executeQuery("select * from lazer");
		user.addItem("  ");
		try {
			while (res5.next())
			{
				user.addItem(res5.getString(1));
			}	
		} catch (SQLException e) {e.printStackTrace();}

		user.addFocusListener(this);
		user.addItemListener(this);
		pw=new JPasswordField(10);
		pw.addFocusListener(this);
		//
		l5=new JLabel("",JLabel.CENTER);
		l5.setBackground(Color.cyan);
		l6=new JLabel("",JLabel.CENTER);
		l7=new JLabel("800",JLabel.CENTER);
		t4=new JTextField(10);
		t5=new JTextField(10);
		//设置默认的流量额度
		t4.setText("800");
		
		l1=new JLabel("Username:",JLabel.RIGHT);
		l2=new JLabel("  Used(M)   :",JLabel.RIGHT);
		l3=new JLabel("  Total(M)  :",JLabel.RIGHT);
		l4=new JLabel("设置额定流量(M) :",JLabel.RIGHT);
		init();
	}
	public void init()
	{
		npanel.add(la1);npanel.add(user);npanel.add(l1);npanel.add(l5);
		npanel.add(la2);npanel.add(pw);npanel.add(l2);npanel.add(l6);
		npanel.add(button);npanel.add(button1);npanel.add(l3);
		npanel.add(l7);npanel.add(exit);npanel.add(trip);npanel.add(l4);
		npanel.add(t4);
		npanel.setLayout(new GridLayout(4,4));
		mpanel.add(la4);mpanel.add(la5);
		mpanel.setLayout(new GridLayout(2,1));
		spanel.add(la3);spanel.add(setDefault);spanel.add(la6);
		spanel.add(la7);spanel.add(t5);
		
		add(mpanel,BorderLayout.NORTH);add(npanel,BorderLayout.CENTER);add(spanel,BorderLayout.SOUTH);
		
		setLayout(new GridLayout(3,1));
		setVisible(true);
		setBounds(800,0,500,500);
		setBackground(Color.cyan);
		
		button.addActionListener(this);
		button1.addActionListener(this);
		exit.addActionListener(this);
		trip.addActionListener(this);
		setDefault.addActionListener(this);
		/*
		 * 判断是否有默认的登陆账号,并且字段名不能为某些特殊字符，如default等
		 */
		res4=dbc.executeQuery("select * from stuacc where defaultacc='yes'");
		try 
		{
			while (res4.next())
			{
				String username=res4.getString(1);
				String password=res4.getString(2);
				new Login(username,password);
			}
			ser4.scheduleAtFixedRate(new Update(),0,1000,TimeUnit.MILLISECONDS);//动态显示
			ser4.scheduleAtFixedRate(new FlowDetect(),0,2000,TimeUnit.MILLISECONDS);//动态显示
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	//实现Tab切换文本框
	public void focusGained(FocusEvent e) {}
	public void focusLost(FocusEvent e) {}
	
	//选择账号并显示密码
	public void itemStateChanged(ItemEvent e) 
	{
		if (e.getStateChange()==ItemEvent.SELECTED)
		{
			res6=dbc.executeQuery("select * from lazer where account='"+user.getSelectedItem()+"'");
			try 
			{
				if (res6.next())
				{
					pw.setText(res6.getString(2));
				}
				else{pw.setText("");};
			} catch (SQLException e1) {e1.printStackTrace();}
		}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand()=="Summit")
		{
			new Login((String) user.getSelectedItem(),pw.getText());
			
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
				String temp1 = sb.toString();
				if (temp1.contains("Invalid"))
				{
					JOptionPane.showMessageDialog(null,"Invalid username&&||password","用户名或密码错误",JOptionPane.ERROR_MESSAGE);
				}
				//确保用户名和密码输入正确
				else
				{//保留输入历史的同时，保存至数据库
					new Lazerinput((String) user.getSelectedItem(),pw.getText());
					res1=dbc.executeQuery("select * from stuacc where account='"+(String) user.getSelectedItem()+"'");
					try 
					{
						//Check whether there exists acounnt
						if (!res1.next())
						{
							res=dbc.executeQuery("select * from stuacc");
							try 
							{
								while (res.next())
								{
									//遍历到最后
									id=Integer.parseInt(res.getString(3));
								}
								dbc.executeUpdate("insert into stuacc values('"+(String) user.getSelectedItem()+"',"
										+ "'"+pw.getText()+"','"+(id+1)+"','')");
							} 
							catch (SQLException e1)
							{e1.printStackTrace();}
						}
					} 
					catch (HeadlessException e2) 
						{e2.printStackTrace();}
					 
					catch (SQLException e2)
					{e2.printStackTrace();}
				}
			} 
			catch (HeadlessException e1) {
				e1.printStackTrace();
			} 
			catch (MalformedURLException e1) {
				e1.printStackTrace();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}
		
		else if(e.getSource()==trip)//自动切换账号按钮
		{
			/*
			 * 初始化对即将要自动切换账号的集合
			 */
			list.clear();
			res3=dbc.executeQuery("select * from stuacc where account!=''");
			try 
			{
				while (res3.next())
				{
					list.add(res3.getString(3));
				}
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
			trip.setEnabled(false);
			ser1.scheduleAtFixedRate(new TaskSelection(),0,10000,TimeUnit.MILLISECONDS);
		}
		
		else if (e.getActionCommand()=="Logout")
		{
				if (list.size()!=list.size())
				{
				res=dbc.executeQuery("select * from stuacc where account='"+l5.getText()+"'");
				try 
				{
					while(res.next())
					{
						int media=(Integer.parseInt(res.getString(3))-1);
						addList.add(media);
						addList.addAll(list);
						list.clear();
						list.addAll(addList);
					}
					
				} 
				catch (NumberFormatException e1) 
				{
					e1.printStackTrace();
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
				}
				new Logout();
				//添加logout之前本机所产生的流量
				dbc.executeUpdate("update stuacc set flowdetect='"+t5.getText()+"' where account='"+l5.getText()+"'");
				la4.setText("You hava logged out");
				la5.setText("");
				l5.setText("");l6.setText("");t5.setText("");//清空
		}
		
		else if (e.getSource()==setDefault)
		{
			SetDefaultAcc sDA=new SetDefaultAcc(l5.getText());
			Thread t = new Thread(sDA);
			t.start();
		}
		
		else if(e.getActionCommand()=="Exit")
		{
			System.exit(0);
		}
		
	}

//自动切换Logout部分类
class AutoLogout extends TimerTask
	{	
	public void run()
	{			
				//Logout
				new Logout();
				res1=dbc.executeQuery("select * from stuacc where account='"+l5.getText()+"'");
				try 
				{
					while (res1.next())
					{
						list.remove(res1.getString(3));
					}
				}
				catch (SQLException e2) 
				{
					e2.printStackTrace();
				}
	}
	}

//自动切换Login部分
class AutoLogin extends TimerTask
{
	public void run()
	{
			String index=(String) list.get(0);
			res=dbc.executeQuery("select * from StuAcc where id='"+index+"'");//Pay attention to the blank(空格)
			try 
			{
				while (res.next())
			  {		
					acc=res.getString(1);
					pass=res.getString(2);
			  }
				new Login(acc,pass);
			}
			catch (Exception e) 
			{System.out.println("W");}
	}
}

//实时监控流量类
class Update extends TimerTask
{
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
			String acc=sb.substring(2896,2922);
			String stacc=sb.substring(2550,2580);
			
			int numm=stacc.indexOf(">");
			int num=stacc.indexOf("<");
			String accou=stacc.substring(numm+1,num);
			
			int num1 = sb.indexOf("Used bytes");
			String account= sb.substring(num1,num1+10);
			
			String regEx="[^0-9]";   
			Pattern p = Pattern.compile(regEx);   
			Matcher m = p.matcher(acc);   
			String mrp=m.replaceAll("").trim();
			double usedflow=Double.parseDouble(mrp);
			String mused=df.format(usedflow/(1024*1024));
			
			l5.setText(accou);l6.setText(mused);
			//用户联网状态
			if (account.equals("Used bytes"))
			{
				la4.setText(accou+"------Connected------");
			}
			else 
			{
				la4.setText("You have logged out");
				la5.setText("");
			}
			
			//判断流量是否超过指定额度流量	
			if (Double.parseDouble(l6.getText())>=Double.parseDouble(t4.getText()))
			{
				la5.setText("Red Warning :"+accou+"的账号已经超过额定流量,您还可使用 ："+df.format((800.00-Double.parseDouble(mused)))+"M");
			}
			else {la5.setText(accou+"账号流量在指定范围内，请放心使用");}
			//显示默认登陆账号
			res=dbc.executeQuery("select * from stuacc where defaultacc='yes'");
			if (res.next())
			{
				la6.setText("当前的默认的自动登陆账号为 ： "+res.getString(1));
			}
			else {la6.setText("");}
		}	
		catch (Exception e)
		{}
		}
}
//本机流量监控
class FlowDetect extends TimerTask
{
	public void run() 
	{
		DecimalFormat df = new DecimalFormat("#0.00");
		try {
			URL url = new URL("http://192.168.31.4:8080/quota?url=http%3A%2F%2Fso.csdn.net%2Fso%2Fsearch%2Fs.do%3Fref%3Dtoolbar%26q%3Dphp%25E4%25B8%258Ehtml%26ref%3Dtoolbar%26q%3Dphp%25E4%25B8%258Ehtml");
			//necessary,flush web response.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
			String line;
			String flow = null;
			StringBuffer sb = new StringBuffer();
			while ((line=br.readLine())!=null)
			{
				sb.append(line);
			}
			//使用正则表达式提取数字!!!!!!!!!!!!!!!!!!!!!!!!!!!(维护性能差,550变为553时，会出错)!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			String temp= sb.substring(550,570);
			int num = temp.indexOf("<");
			String temp1 = temp.substring(0,num);
			String regEx="[^0-9]";   
			Pattern p = Pattern.compile(regEx);   
			Matcher m = p.matcher(temp1);   
			String mrp=m.replaceAll("").trim();
			//从数据库中取出logout前的本机流量数据
			res7 = dbc.executeQuery("select * from stuacc where account='"+l5.getText()+"'");
			try {
				while (res7.next())
				{
					flow = res7.getString(5);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			double d = Double.parseDouble(flow)+Double.parseDouble(mrp)/(1024*1024);
			String temp2 = df.format(d);
			t5.setText(temp2);
		}
		//捕获SocketException异常
		catch (SocketException se){System.out.println("Empty");}
		catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		finally{
//			try{
//				if (br!=null) br.close();
//			}
//			catch (IOException e){e.printStackTrace();}
//		}
	}
}


//定时器
class TaskSelection extends TimerTask
{
	public void run()
	{
		 try 
		 {
			 /*
			  * 防止多次弹窗
			  */
			 if (list.size()==0&&closePane==0)
			 {
				 try 
				 {
						la4.setText("");la5.setText("流量群已全爆完，等着挨揍吧，少年");
						l5.setText("");l6.setText("");
					 	closePane+=1;
						trip.setEnabled(false);
						ser1.shutdownNow();
//						pw.setText("");
						}
				 catch (Exception e) 
				 {
					e.printStackTrace();
				 }
			 	}
			if ((Double.parseDouble(l6.getText())>=Double.parseDouble(t4.getText()))&&list.size()!=0)
			{
				ser5.schedule(new AutoLogout(),0,TimeUnit.MILLISECONDS);
				Thread.sleep(5000);
				ser3.schedule(new AutoLogin(),0,TimeUnit.MILLISECONDS);//缓冲Logout
			}
		 }
		 catch (Exception e)  { }
	}
	}
}