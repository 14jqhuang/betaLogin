package login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Flow extends JPanel implements FocusListener,ItemListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String acc,pass,temp1,index;
	int id=0;
	int clicknum=0;
	int closePane=0;
	DataBaseconnection dbc=new DataBaseconnection();
	ResultSet res;
	ResultSetMetaData rsmd;
	JPanel npanel,mpanel,spanel;
	JButton button,button1,exit,trip,setDefault,clear;
	JLabel la1,la2,la3,la4,la5,la6,la7;//la7——specialized for account
	JComboBox<String> user;
	JTextField t4;
	JPasswordField pw;
	URL url;
	JLabel l1,l2,l3,l4,l5,l6,l7;
	Random random=new Random();
	List list=new ArrayList();
	List addList=new ArrayList();
	int[] string=new int[4];
	//键值对(账号——密码)
	Map<String,String> map;
	ScheduledExecutorService ser4= Executors.newScheduledThreadPool(2);//流量监控
	
	public Flow()
	{
		npanel=new JPanel();
		spanel=new JPanel();
		mpanel=new JPanel();
		
		button=new DesignButton("Summit");
		
		button1=new JButton("Logout");
		trip=new JButton("自动切换账号");
		exit=new JButton("Exit");
		setDefault=new JButton("设置为默认账号");
		clear=new JButton("清空输入历史");
		
		la1=new JLabel("Username : ",JLabel.RIGHT);
		la2=new JLabel("Password : ",JLabel.RIGHT);
		la3=new JLabel("将当前账号设置为默认登陆账号，以便下次自动登陆轻松上网",JLabel.CENTER);
		la3.setFont(new Font("宋体",Font.BOLD,14));
		la4= new JLabel("",JLabel.CENTER);la4.setFont(new Font("宋体",Font.BOLD,20));
		la5= new JLabel("",JLabel.CENTER);la5.setFont(new Font("宋体",Font.BOLD,14));
		la6= new JLabel("",JLabel.CENTER);la6.setFont(new Font("宋体",Font.BOLD,19));
		la7= new JLabel("",JLabel.CENTER);la7.setFont(new Font("宋体",Font.BOLD,25));
		
		//账号密码对应
		map = new HashMap<String,String>();
		user=new JComboBox<String>();
		user.setEditable(true);
		user.addFocusListener(this);
		user.addItemListener(this);
		pw=new JPasswordField(10);
		pw.addFocusListener(this);
		//获取输入历史
		res=dbc.executeQuery("select * from lazer");
		try {
			while (res.next())
			{
				user.addItem(res.getString(1));
				map.put(res.getString(1),res.getString(2));
			}	pw.setText(map.get(user.getItemAt(0)));
		} catch (SQLException e) {e.printStackTrace();}
		l5=new JLabel("",JLabel.CENTER);
		l5.setBackground(Color.cyan);
		l6=new JLabel("",JLabel.CENTER);
		l7=new JLabel("800",JLabel.CENTER);
		t4=new JTextField(10);
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
		spanel.add(la3);spanel.add(setDefault);spanel.add(la6);spanel.add(la7);spanel.add(clear);
		
		add(mpanel,BorderLayout.NORTH);add(npanel,BorderLayout.CENTER);add(spanel,BorderLayout.SOUTH);
		
		setLayout(new GridLayout(3,1));
		setVisible(true);
		setBounds(800,0,500,500);
		setBackground(Color.cyan);
		
		button.addActionListener(new OuterClass(this));
		button1.addActionListener(new OuterClass(this));
		exit.addActionListener(new OuterClass(this));
		trip.addActionListener(new OuterClass(this));
		setDefault.addActionListener(new OuterClass(this));
		clear.addActionListener(new OuterClass(this));
		//设置有默认登陆账号后，下次会自动登陆
		new FirstLogin();
		//运行程序后同时启动定时器
		ser4.scheduleAtFixedRate(new Update(this),0,1000,TimeUnit.MILLISECONDS);//动态显示
	}
	
	//实现Tab切换文本框
	public void focusGained(FocusEvent e) {}
	public void focusLost(FocusEvent e) {}
	
	//选择账号并显示密码
	public void itemStateChanged(ItemEvent e) 
	{
		if (e.getStateChange()==ItemEvent.SELECTED)
		{
			String acc = (String) user.getSelectedItem();
			pw.setText(map.get(acc));
		}
	}
}