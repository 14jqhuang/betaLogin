package login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class AddAccount extends JPanel implements ActionListener
{
	JPanel panel1,panel2;
	JLabel label1,label2,label3;
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
		addtext1=new JTextField(10);
		addtext2=new JPasswordField(10);
		addbutton=new JButton("添加");
		
		panel1.add(label3);
		panel2.setLayout(new GridLayout(3,2));
	
		panel2.add(label1);panel2.add(label2);panel2.add(addtext1);
		panel2.add(addtext2);panel2.add(addbutton);
		
		add(panel1,BorderLayout.NORTH);add(panel2,BorderLayout.CENTER);
		setVisible(true);
		setBounds(200,200,400,400);
		addbutton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand()=="添加")
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
				//添加后清空
				addtext1.setText("");addtext2.setText("");
	}
}
