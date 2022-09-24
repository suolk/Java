import javax.swing.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class Main {
    //数据库变量用于调用数据库
    static MyDb mydb = new MyDb();
    static String UsrPhoneNum, UsrPassword, UsrAddress;
    static int Borrowed, BorrowBikeNum, UsrBills;

    public static void main(String[] args) throws SQLException {
        //创建窗口和画布
        MyFrame myFrame = new MyFrame(600, 400, "共享单车借还系统");
        MyPanel myPanel = new MyPanel(600, 400);
        MyButton exit = new MyButton(500, 10, 75, 30, "退出", "1");
        //将画布加入窗口
        myFrame.add(myPanel);
        myPanel.add(exit);
        //开始界面
        PageStart(myPanel);
        //登录/注册界面
        PageLogin(myPanel);
        //获取用户所在地址
        GetAddress();
        //检查用户是否借车
        Borrowed = mydb.GetBorrowed(UsrPhoneNum);
        if (Borrowed != 0) {
            //获取所借车辆编号
            BorrowBikeNum = mydb.GetBikeNum(UsrPhoneNum);
            //还车界面
            PageReturn(myPanel);
        }
        //借车界面
        PageBorrow(myPanel, myFrame);
        //重绘
        myPanel.repaint();
    }

    static void PageBorrow(MyPanel myPanel, MyFrame myFrame) throws SQLException {//借车界面
        UsrBills = mydb.GetUsrBills(UsrPhoneNum);
        if (UsrBills != 0) {//付款界面,别真扫，二维码真的
            //展示收款码等待用户支付
            myPanel.setFlag(1);
            MyLabel myLabel = new MyLabel(205, 270, 400, 30, "您还有" + UsrBills + "元未付清");
            MyButton BPay = new MyButton(230, 300, 120, 30, "支付完成", "6");
            myPanel.add(myLabel);
            myPanel.add(BPay);
            myPanel.setFlag(1);
            myPanel.repaint();
            do {//检查支付
                Thread.onSpinWait();
                if (MyButton.flag == 6) {
                    if (GetPayState())
                        break;
                    else
                        MyButton.flag = 0;
                }
            } while (true);
            //支付完成
            JOptionPane.showMessageDialog(null, "支付完成！");
            myPanel.remove(myLabel);
            myPanel.remove(BPay);
            myPanel.setFlag(0);
            myPanel.repaint();
            UsrBills = 0;
            mydb.SetUsrBills(0, UsrPhoneNum);
        }
        GetAddress();//获取用户所在地
        int ChosenBikeNum = -1;
        MyButton BSelected = new MyButton(260, 180, 80, 30, "确定", "7");
        Integer[] BikeList = mydb.GetBikesList(UsrAddress);
        JComboBox<Integer> Bike = new JComboBox<>(BikeList);//获取当前区域存在的自行车编号
        Bike.setBounds(250, 120, 100, 20);
        myPanel.add(Bike);
        myPanel.add(BSelected);
        myFrame.setVisible(true);
        myPanel.repaint();
        do {//等待用户选车
            Thread.onSpinWait();
        } while (MyButton.flag != 7);
        //用户选车完成，更新数据库
        ChosenBikeNum = (int) Bike.getSelectedItem();
        mydb.SetBikeAddress(ChosenBikeNum, "");
        mydb.SetUsrBorrowed(UsrPhoneNum, 1);
        mydb.InsertBorrow(UsrPhoneNum, ChosenBikeNum, new Date().getTime());
        myPanel.remove(BSelected);
        myPanel.remove(Bike);
    }

    static void PageReturn(MyPanel myPanel) throws SQLException {//还车界面
        MyButton BReturn = new MyButton(240, 150, 75, 30, "还车", "5");
        myPanel.add(BReturn);
        do {
            Thread.onSpinWait();
            GetAddress();
            long tmp = mydb.GetBorrowTime(BorrowBikeNum);
            tmp = (long) (((new Date().getTime() - tmp) / 60000) * 0.2);//计算借车费用，按0.2元/分钟收费
            //还车完成，更新数据库
            if (!mydb.CheckPos(UsrAddress)) {//检查当前位置是否可还车
                MyButton.flag = 0;
                JOptionPane.showMessageDialog(null, "您不在还车区域！");
                continue;
            }
            mydb.SetBikeAddress(BorrowBikeNum, UsrAddress);
            mydb.SetUsrBorrowed(UsrPhoneNum, 0);
            JOptionPane.showMessageDialog(null, "感谢用车，本次借车需支付" + tmp + "元！");
            mydb.SetUsrBills(tmp, UsrPhoneNum);
            mydb.DeleteBorrow(UsrPhoneNum);
            break;
        } while (MyButton.flag != 5);
        myPanel.remove(BReturn);
    }

    static void PageStart(MyPanel myPanel) {//初始界面
        MyButton BRegister = new MyButton(240, 100, 75, 30, "注册", "2");
        MyButton BLogin = new MyButton(240, 170, 75, 30, "登录", "3");
        myPanel.add(BRegister);
        myPanel.add(BLogin);
        myPanel.repaint();
        do {
            Thread.onSpinWait();
        } while (MyButton.flag == 0);
        myPanel.remove(BRegister);
        myPanel.remove(BLogin);
        myPanel.repaint();
    }

    static void PageLogin(MyPanel myPanel) throws SQLException {//登录/注册界面
        int BFlag = MyButton.flag;
        MyTextField TUser = new MyTextField(100, 100, 240, 30);
        MyTextField TPassword = new MyTextField(100, 150, 240, 30);
        MyLabel LPhoneNum = new MyLabel(30, 100, 100, 30, "手机号:");
        MyLabel LPassword = new MyLabel(30, 150, 100, 30, "密码:");
        MyButton BYes = new MyButton(200, 200, 75, 30, "确定", "4");
        myPanel.add(TUser);
        myPanel.add(TPassword);
        myPanel.add(LPhoneNum);
        myPanel.add(LPassword);
        myPanel.add(BYes);
        myPanel.repaint();
        while (true) {//等待用户完成输入
            do {
                Thread.onSpinWait();
            } while (MyButton.flag < 4);
            Thread.onSpinWait();
            UsrPhoneNum = TUser.getText();
            UsrPassword = TPassword.getText();
            int LenN = UsrPhoneNum.length();
            int LenP = UsrPassword.length();
            if (LenN == 0 || LenN > 20 || LenP == 0 || LenP > 20) {
                JOptionPane.showMessageDialog(null, "输入不得为空白且不允许超过20个字符！");
            } else if (BFlag == 2) {
                if (mydb.InsertUser(UsrPhoneNum, UsrPassword) == 0)
                    JOptionPane.showMessageDialog(null, "该用户已注册！");
                else
                    break;
            } else if (BFlag == 3) {
                String TmpPw = mydb.GetUsrPassword(UsrPhoneNum);
                if (Objects.equals(TmpPw, ""))
                    JOptionPane.showMessageDialog(null, "该用户不存在！");
                else if (!Objects.equals(TmpPw, UsrPassword))
                    JOptionPane.showMessageDialog(null, "密码错误！");
                else
                    break;
            }
            MyButton.flag = BFlag;
            TUser.setText("");
            TPassword.setText("");
            myPanel.repaint();
        }
        myPanel.remove(TUser);
        myPanel.remove(TPassword);
        myPanel.remove(LPhoneNum);
        myPanel.remove(LPassword);
        myPanel.remove(BYes);
        myPanel.repaint();
    }

    static void GetAddress() {
        //获取地址，没接口，假的
        UsrAddress = "address1";
    }

    static boolean GetPayState() {
        //获取支付状态，没接口，假的
        return true;
    }
}

