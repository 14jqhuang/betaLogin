package login;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class CorrectAcc extends JPanel implements ActionListener
{
	JLabel corr,toCorr,orilabel,oripassword;
	JTextField corrText,acc;
	JPasswordField jp;
	JButton sure1,sure2,back;
	JPanel npanel,spanel;
	DataBaseconnection dbc=new DataBaseconnection();
	ResultSet res;
	String id="middle";//记录账号位置
	
	public CorrectAcc()
	{
		npanel=new JPanel();
		spanel=new JPanel();
		
		//实例化
		corr=new JLabel("请输入您要修改的账号 :",JLabel.CENTER);
		toCorr=new JLabel("请修改，然后点击确定即可：",JLabel.CENTER);
		orilabel=new JLabel("账号 :",JLabel.CENTER);oripassword=new JLabel("密码 :",JLabel.CENTER);
		corrText=new JTextField(10);acc=new JTextField(10);
		jp=new JPasswordField(10);
		sure1=new JButton("确定");sure2=new JButton("确定修改");
		
		//add to contailor
		npanel.add(corr);npanel.add(corrText);npanel.add(sure1);
		add(npanel);
		add(toCorr);
		spanel.setLayout(new GridLayout(2,2));
		spanel.add(orilabel);spanel.add(acc);
		spanel.add(oripassword);spanel.add(jp);
		
		//添加监听器
		sure1.addActionListener(this);sure2.addActionListener(this);
		
		add(spanel);
		add(sure2);
		
		setLayout(new GridLayout(4,1));
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource()==sure1)
		{
			res=dbc.executeQuery("select * from stuacc where account='"+corrText.getText()+"'");
			try {
				if (res.next())
				{
						id=res.getString(3);
						acc.setText(res.getString(1));jp.setText(res.getString(2));
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
		
		else if (e.getSource()==sure2)
		{
			System.out.println(id);
			dbc.executeUpdate("update stuacc set account='"+acc.getText()+"',password='"+jp.getText()+"' where id='"+id+"'");
			JOptionPane.showMessageDialog(null,"修改成功！","已保存",JOptionPane.OK_OPTION);
			corrText.setText("");acc.setText("");jp.setText("");
		}
	}

	
	
}
