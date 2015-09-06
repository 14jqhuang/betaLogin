package login;
import java.awt.*;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
public class JButtonTest extends JFrame{
private MyJButton btn,btn_2;
private MyJButton btn_3,btn_4,btn_5;
public JButtonTest(){
super();
setTitle("JButtonTest");
setSize(500,500);
getContentPane().setLayout(null);
getContentPane().setBackground(Color.black);
btn=new MyJButton();
btn_2=new MyJButton();
btn_3=new MyJButton();
btn_4=new MyJButton();
btn_5=new MyJButton();
btn.setBounds(30,50,128,49);
btn_2.setBounds(200,50,128,49);
btn_3.setBounds(30,120,128,49);
btn_4.setBounds(200,120,128,49);
btn_5.setBounds(30,200,128,49);
getContentPane().add(btn);
getContentPane().add(btn_2);
getContentPane().add(btn_3);
getContentPane().add(btn_4);
getContentPane().add(btn_5);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setVisible(true);
setLocationRelativeTo(null);

}
public static void main(String[] args){
new JButtonTest();
}
}
class MyJButton extends JButton{
private Font font;
private String str;
public MyJButton(){
super();
setMargin(new Insets(0,0,0,0));//设置边距
setContentAreaFilled(false);//不绘制按钮区域
setBorderPainted(false);//不绘制边框
setIcon(new ImageIcon("images/button1.png"));//设置默认图片
setRolloverIcon(new ImageIcon("images/button2.png"));//设置鼠标经过图片
setPressedIcon(new ImageIcon("images/button3.png"));//设置鼠标按下图片
}
}