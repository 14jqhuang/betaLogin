package login;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class LoginMethod extends JFrame
{
	JTabbedPane jtp=new JTabbedPane();
	
	public LoginMethod()
	{
		jtp.add("Main Interface",new Flow());
		jtp.add("Add new User",new AddAccount());
		jtp.add("Delete User",new DeleteAcc());
		jtp.add("Correct User",new CorrectAcc());
		jtp.add("Query User",new QueryAcc());
		add(jtp);
		setVisible(true);
		setBounds(800,0,600,500);
		setIconImage(new ImageIcon("E:/images/penguin.png").getImage());
	}
	
	public static void main(String[] args) 
	{
			LoginMethod LM=new LoginMethod();
	}

}
