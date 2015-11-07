package login;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class OuterClass implements ActionListener
{
	//引入图形界面组件
	Flow flow;
	ResultSet res,res1,res3;
	ScheduledExecutorService ser1= Executors.newScheduledThreadPool(1);//自动切换账号
	DataBaseconnection dbc = new DataBaseconnection();
	
	public OuterClass(Flow flow)
	{
		this.flow=flow;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand()=="Summit")
		{
			//判断用户名与密码是否正确，再登陆
			int judgeflow = new JudgeLogin().judge((String)flow.user.getSelectedItem(),flow.pw.getText());
				//确保用户名和密码输入正确
				if (judgeflow==1)
				{
					//保留输入历史的同时，保存至数据库
					new Lazerinput((String) flow.user.getSelectedItem(),flow.pw.getText());
					res1=dbc.executeQuery("select * from stuacc where account='"+(String) flow.user.getSelectedItem()+"'");
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
									flow.id=Integer.parseInt(res.getString(3));
								}
								dbc.executeUpdate("insert into stuacc values('"+(String) flow.user.getSelectedItem()+"',"
										+ "'"+flow.pw.getText()+"','"+(flow.id+1)+"','')");
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
				else{JOptionPane.showMessageDialog(null,"亲，你还没登出哦^_^,请先登出吧,这样就能愉快地玩耍啦！！！","未登出",JOptionPane.ERROR_MESSAGE);}
			} 
		
		else if(e.getSource()==flow.trip)//自动切换账号按钮
		{
			/*
			 * 初始化对即将要自动切换账号的集合
			 */
			flow.list.clear();
			res3=dbc.executeQuery("select * from stuacc where account!=''");
			try 
			{
				while (res3.next())
				{
					flow.list.add(res3.getString(3));
				}
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
			flow.trip.setEnabled(false);
			ser1.scheduleAtFixedRate(new TaskSelection(flow),0,10000,TimeUnit.MILLISECONDS);
		}
		
		else if (e.getActionCommand()=="Logout")
		{
				if (flow.list.size()!=flow.list.size())
				{
				res=dbc.executeQuery("select * from stuacc where account='"+flow.l5.getText()+"'");
				try 
				{
					while(res.next())
					{
						int media=(Integer.parseInt(res.getString(3))-1);
						flow.addList.add(media);
						flow.addList.addAll(flow.list);
						flow.list.clear();
						flow.list.addAll(flow.addList);
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
				flow.la4.setText("You hava logged out");
				flow.la5.setText("");
				flow.l5.setText("");flow.l6.setText("");flow.l7.setText("");//清空
		}
		
		else if (e.getSource()==flow.setDefault)
		{
			SetDefaultAcc sDA=new SetDefaultAcc(flow.l5.getText());
			Thread t = new Thread(sDA);
			t.start();
		}
		
		else if(e.getActionCommand()=="Exit")
		{
			System.exit(0);
		}
		
		else if (e.getSource()==flow.clear)
		{
			new Dialog();
		}
	}
}