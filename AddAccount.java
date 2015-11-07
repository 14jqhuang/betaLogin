package login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

public class AddAccount extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panel1,panel2;
	JLabel label1,label2,label3,label4;
	JTextField addtext1;
	JPasswordField addtext2;
	JButton addbutton;
	int clicknumber=0;
	DataBaseconnection dbc=new DataBaseconnection();
	ResultSet res,res1;
	int id=0;//自增
	
	public AddAccount()
	{
		panel1= new JPanel();
		panel2= new JPanel();
		
		panel1.setPreferredSize(new Dimension(500,200));
		panel2.setPreferredSize(new Dimension(400,100));
		label1=new JLabel("账号");
		label2=new JLabel("密码");
		label3=new JLabel(new ImageIcon("E:/images/qq4.gif"));
		label4=new JLabel("亲！！！添加账号前请先登出^_^",JLabel.CENTER);
		addtext1=new JTextField(10);
		addtext2=new JPasswordField(10);
		addbutton=new JButton("添加");
		
		panel1.add(label3);
		panel2.setLayout(new GridLayout(3,2));
	
		panel2.add(label1);panel2.add(label2);panel2.add(addtext1);
		panel2.add(addtext2);panel2.add(addbutton);
		
		add(panel1,BorderLayout.NORTH);add(label4);add(panel2,BorderLayout.SOUTH);
		setVisible(true);
		setBounds(200,200,400,400);
		addbutton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand()=="添加")
		{
			//First to logout if login
			int judgeflow = new JudgeLogin().judge(addtext1.getText(),addtext2.getText());
			if (judgeflow==1)
			{
				res1=dbc.executeQuery("select * from stuacc where account='"+addtext1.getText()+"'");
				try 
				{
					//Check whether there exists acounnt
					if (res1.next())
					{
						JOptionPane.showMessageDialog(null, addtext1.getText()+"账号已经存在 ");
					}

					else 
					{
						res=dbc.executeQuery("select * from stuacc");
						try 
						{
							while (res.next())
							{
								//遍历到最后
								id=Integer.parseInt(res.getString(3));
							}
							dbc.executeUpdate("insert into stuacc values('"+addtext1.getText()+"',"
									+ "'"+addtext2.getText()+"','"+(id+1)+"','')");

							JOptionPane.showMessageDialog(null,"添加成功！","已保存",JOptionPane.OK_OPTION);
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
			//添加后清空
			addtext1.setText("");addtext2.setText("");
		}
	}
}
