package animation_2D;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class myPanel extends JPanel implements ActionListener{

    final int panel_width = 400;
    final int panel_height = 400;
    Image enemy;
    Image background;
    Timer timer;
    int xVelocity = 3;
    int yVelocity = 2;
    int x = 0;
    int y = 0;

    myPanel(){
        this.setPreferredSize(new Dimension(panel_width, panel_height));
        this.setBackground(Color.BLACK);
        enemy = new ImageIcon("C:\\Users\\OneTen\\Desktop\\Animation\\src\\animation_2D\\bot.png").getImage();
        background = new ImageIcon("C:\\Users\\OneTen\\Desktop\\Animation\\src\\animation_2D\\background.png").getImage();
        timer = new Timer (10, this);
        timer.start();


    }

    public void paint(Graphics g){

        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(background,0, 0, null);
        g2D.drawImage(enemy, x, y, null);
    }

    public void actionPerformed(ActionEvent e){
        if (x >= panel_width - enemy.getWidth(null) || x < 0){
            xVelocity = xVelocity * -1;
        }
        x  = x + xVelocity;

        if (y >= panel_height - enemy.getHeight(null) || y < 0){
            yVelocity = yVelocity * -1;
        }
        y  = y + yVelocity;
        repaint();
    }
}
