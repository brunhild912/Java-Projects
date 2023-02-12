package animation_2D;

import javax.swing.*;

public class myFrame extends JFrame{
    myPanel panel;

    myFrame(){
        panel = new myPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }
}
