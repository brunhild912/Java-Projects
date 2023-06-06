import javax.swing.*;

public class swing extends JFrame{
    public static void main(String[] args){
        JFrame frame = new JFrame();
        JButton button = new JButton("Flip");
        frame.getContentPane().add(button);

        frame.setSize(300,300);
        frame.setVisible(true);
    }
}
