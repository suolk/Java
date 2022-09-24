import javax.swing.*;

public class MyFrame extends JFrame {
    public MyFrame(int width, int height, String title) {
        setTitle(title);
        setLayout(null);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
