package login;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;

class DesignButton extends JButton {
    private static final long serialVersionUID = 1965063150601339314L;
    Image image = new ImageIcon("F:/login.png").getImage();
    public DesignButton(String text) 
    {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false); // 这一句非常重要, 否则父类还会绘制按钮的区域.  
        setFocusable(false);
        setBorderPainted(false);
        this.setPreferredSize(new Dimension(image.getWidth(this), image
                .getHeight(this)));
    }
    protected void paintComponent(Graphics g) {
        g.drawImage(image,
                0,
                0,
                125,30,
                null);
    }
}