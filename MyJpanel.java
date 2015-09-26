package login;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MyJpanel extends JPanel{

	@Override
	protected void paintComponent(Graphics g) {
		g.setFont(new Font("Arial",Font.BOLD,20));
		g.drawString("^_^Welcome to use^_^", 500, 470);
	}

	@Override
	protected void paintChildren(Graphics g) {
		super.paintChildren(g);
	}

	@Override
	protected void paintBorder(Graphics g) {
		super.paintBorder(g);
	}

	
	
}
