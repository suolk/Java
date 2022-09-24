import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;


public class MyDb {
    Connection conn = null;
    Statement stat = null;
    String sql = null;
    ResultSet rs = null;

    void RegDb() {//注册驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Reg Done");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void ConDb() {//创建链接
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bikesharing?characterEncoding=UTF-8", "root", "123456");
            stat = conn.createStatement();
            System.out.println("Con Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int InsertUser(String UsrPhoneNum, String UsrPassword) {
        RegDb();
        ConDb();
        sql = "insert into user values('" + UsrPhoneNum + "','" + UsrPassword + "',0,0)";
        int ret = 0;
        try {
            ret = stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void InsertBorrow(String UsrPhoneNum, int BikeNum, long Time) throws SQLException {
        RegDb();
        ConDb();
        sql = "insert into borrow values (" + BikeNum + ",'" + UsrPhoneNum + "'," + Time + ")";
        stat.executeUpdate(sql);
    }

    String GetUsrPassword(String UsrPhoneNum) throws SQLException {
        RegDb();
        ConDb();
        String ret = "";
        sql = "select * from user where PhoneNum='" + UsrPhoneNum + "'";
        rs = stat.executeQuery(sql);
        if (rs.next())
            ret = rs.getString("Password");
        return ret;
    }

    public int GetBorrowed(String UsrPhoneNum) throws SQLException {
        RegDb();
        ConDb();
        int ret = 0;
        sql = "select * from user where PhoneNum='" + UsrPhoneNum + "'";
        rs = stat.executeQuery(sql);
        if (rs.next())
            ret = rs.getInt("borrowed");
        return ret;
    }

    public int GetBikeNum(String UsrPhoneNum) throws SQLException {
        RegDb();
        ConDb();
        int ret = 0;
        sql = "select * from borrow where PhoneNum='" + UsrPhoneNum + "'";
        rs = stat.executeQuery(sql);
        if (rs.next())
            ret = rs.getInt("bno");
        return ret;

    }

    public long GetBorrowTime(int borrowBikeNum) throws SQLException {
        RegDb();
        ConDb();
        long ret = 0;
        sql = "select * from borrow where bno=" + borrowBikeNum;
        rs = stat.executeQuery(sql);
        if (rs.next())
            ret = rs.getLong("BorrowTime");
        return ret;
    }

    public int GetUsrBills(String UsrPhoneNum) throws SQLException {
        RegDb();
        ConDb();
        int ret = 0;
        sql = "select * from user where PhoneNum='" + UsrPhoneNum + "'";
        rs = stat.executeQuery(sql);
        if (rs.next())
            ret = rs.getInt("bills");
        return ret;
    }

    Integer[] GetBikesList(String Address) throws SQLException {//获取当前位置的可选车辆
        RegDb();
        ConDb();
        ArrayList<Integer> ret = new ArrayList<>();
        int BPno = 0;
        sql = "select * from bikeplace where address='" + Address + "'";
        rs = stat.executeQuery(sql);
        if (rs.next())
            BPno = rs.getInt("bpno");
        sql = "select * from bike where bpno=" + BPno;
        rs = stat.executeQuery(sql);
        while (rs.next()) {
            ret.add(rs.getInt("bno"));
        }
        return ret.toArray(new Integer[0]);
    }

    public boolean CheckPos(String UsrAddress) throws SQLException {
        RegDb();
        ConDb();
        sql = "select * from bikeplace where address='" + UsrAddress + "'";
        rs = stat.executeQuery(sql);
        if (rs.next())
            return true;
        return false;

    }

    public void SetBikeAddress(int BorrowBikeNum, String UsrAddress) throws SQLException {
        RegDb();
        ConDb();
        if (Objects.equals(UsrAddress, "")) {
            sql = "update bike set bpno=-1 where bno=" + BorrowBikeNum;
            stat.executeUpdate(sql);
            return;
        }
        int BPno = 0;
        sql = "select * from bikeplace where address='" + UsrAddress + "'";
        rs = stat.executeQuery(sql);
        if (rs.next())
            BPno = rs.getInt("bpno");
        sql = "update bike set bpno=" + BPno + " where bno=" + BorrowBikeNum;
        stat.executeUpdate(sql);
    }

    public void SetUsrBorrowed(String UsrPhoneNum, int situation) throws SQLException {
        RegDb();
        ConDb();
        sql = "update user set borrowed=" + situation + " where PhoneNum='" + UsrPhoneNum + "'";
        stat.executeUpdate(sql);
    }

    public void SetUsrBills(long Bills, String UsrPhoneNum) throws SQLException {
        RegDb();
        ConDb();
        sql = "update user set bills=" + Bills + " where PhoneNum='" + UsrPhoneNum + "'";
        stat.executeUpdate(sql);
    }

    public void DeleteBorrow(String UsrPhoneNum) throws SQLException {
        RegDb();
        ConDb();
        sql = "delete from borrow where PhoneNum='" + UsrPhoneNum + "'";
        stat.executeUpdate(sql);
    }

}
