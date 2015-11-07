package login;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dialog extends JFrame implements ActionListener
{

	JButton di1,di2;
	JPanel panel;
	JLabel label = new JLabel("Are you sure you want to delete input records? The operation can't be revoked!!!");
	DataBaseconnection dbc = new DataBaseconnection();
	public Dialog()
	{
		di1 =new JButton("Ok");
		di2 =new JButton("Cancal");
		di1.addActionListener(this);
		di2.addActionListener(this);
		panel = new JPanel();
		panel.add(di1);
		panel.add(di2);
		add(label,BorderLayout.NORTH);
		add(panel);
		setBounds(500,200,480,100);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==di1)
		{
			dbc.executeUpdate("delete from lazer");
			setVisible(false);
		}
		
		else if (e.getSource()==di2)
		{
			setVisible(false);
		}
	}
}
