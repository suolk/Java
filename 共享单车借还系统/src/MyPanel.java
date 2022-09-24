import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyPanel extends JPanel {
    public MyPanel(int width, int height) {
        setLayout(null);
        setSize(width, height);
        setVisible(true);
    }

    int flag = 0, SitQuantity;
    ArrayList<Integer> sit;
    MyDb mydb = new MyDb();
    Image img;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        img = new ImageIcon("img/StartBg.jpg").getImage();
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        if (flag == 1) {
            img = new ImageIcon("img/Pay.png").getImage();
            g.drawImage(img, 190, 20, 200, 250, this);
        }
    }

    public void update(Graphics g) {
        this.paint(g);
    }

    public void setFlag(int a) {
        flag = a;
    }

    public void setSit(ArrayList<Integer> s) {
        sit = s;
    }

    public void setSitQuantity(int a) {
        SitQuantity = a;
    }
}