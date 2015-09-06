package login;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.layout.Grid;

public class QueryAcc extends JPanel implements ActionListener
{
	int clicktimes=0;
	DataBaseconnection dbc=new DataBaseconnection();
	ResultSet res,res1;
	ResultSetMetaData rsmd;
	DefaultTableModel mo1=new DefaultTableModel(new String[]{"账号","密码"},0);
	DefaultTableModel mo2=new DefaultTableModel(new String[]{"账号","密码"},0);
	JTextField Qtext;
	JLabel Qlabel;
	JButton Qbu1,Qbu2;
	JPanel qjp2,qjp3;
	JTable Qt1,Qt2;
	JScrollPane jsp1,jsp2;
	
	public QueryAcc()
	{
		Qtext=new JTextField(10);
		Qlabel=new JLabel("请输入您要查询的账号");
		Qbu1=new JButton("显示所有");Qbu2=new JButton("单个查询");
		qjp2=new JPanel();qjp3=new JPanel();
		Qt1=new JTable(mo1);Qt2=new JTable(mo2);
		jsp1=new JScrollPane(Qt1);jsp2=new JScrollPane(Qt2);

		qjp2.add(Qlabel);qjp2.add(Qtext);qjp2.add(Qbu2);
		qjp3.add(jsp1);qjp3.add(jsp2);
		
		qjp2.setLayout(new GridLayout(1,3));
		qjp3.setLayout(new GridLayout(1,2));
		//注册监听器
		Qbu1.addActionListener(this);Qbu2.addActionListener(this);
		
		add(Qbu1);add(qjp2);add(qjp3);
		setLayout(new GridLayout(3,1));
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand()=="单个查询")
		{
			mo1.setRowCount(0);
			res=dbc.executeQuery("select * from stuacc where account='"+Qtext.getText()+"'");
			try 
			{
				if (res.next())
				{
					Vector currow=new Vector();
					
						currow.addElement(res.getString(1));
						currow.addElement(res.getString(2));
						mo1.addRow(currow);
						Qtext.setText("");
				}
				
				else 
				{
					JOptionPane.showMessageDialog(null,"Sorry!暂无该账号的信息，请确认好后重新输入","Wrong Usernumber",JOptionPane.ERROR_MESSAGE);
				}
				}
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
		}
		
		else if (e.getActionCommand()=="显示所有")
		{
			clicktimes+=1;
			
			if (clicktimes%2==1)
			{
				try 
				{
					res1=dbc.executeQuery("select * from stuacc");
					rsmd=res1.getMetaData();
					while (res1.next())
					{
						Vector currow=new Vector();
						for (int i=1;i<=rsmd.getColumnCount();i++)
						{
							currow.addElement(res1.getString(1));
							currow.addElement(res1.getString(2));
						}
						mo2.addRow(currow);
					}
				}
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
			}
			else
			{
				mo2.setRowCount(0);
			}
		}
	}

}
