import javax.swing.*;
import java.awt.*;

public class MyLabel extends JLabel {
    public MyLabel(int x, int y, int width, int height, String text) {
        setText(text);
        setBounds(x, y, width, height);
        setFont(new Font("宋体", Font.PLAIN, 20));
        setVisible(true);
    }
}
