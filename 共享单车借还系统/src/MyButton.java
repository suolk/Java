import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyButton extends JButton {
    MyActionListener myActionListener = new MyActionListener();
    public static int flag = 0;

    public MyButton(int x, int y, int width, int height, String text, String command) {
        setFont(new Font("宋体", Font.PLAIN, 20));
        setBounds(x, y, width, height);
        setText(text);
        setActionCommand(command);
        addActionListener(myActionListener);
        setVisible(true);
    }

    static class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("1")) //退出按钮
                System.exit(0);
            else {//2 注册   3 登录   4 登录/注册界面确定按键   5 还车   6 支付   7 选车
                String tmp = e.getActionCommand().toString();
                flag = tmp.charAt(0) - 48;
            }
        }
    }

}
