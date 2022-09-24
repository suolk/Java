import javax.swing.*;

public class MyTextField extends JTextField {
    public MyTextField(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
        setEditable(true);
    }
}
