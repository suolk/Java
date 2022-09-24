import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import java.sql.*;
import java.util.Objects;

class DB {

    public void CreateDatabase() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        //获取连接
        String url = "jdbc:mysql://localhost:3306/maze_player?serverTimezone=GMT&useSSL=false&characterEncoding=utf8";

        try {
            Connection conn = DriverManager.getConnection(url, "root", "1548712503");
            //创建命令
            Statement stat = conn.createStatement();
            //若不存在则建库
            stat.executeUpdate("create table if not exists player(num int primary key auto_increment,name varchar(20),step int)");
            stat.close();
            conn.close();
            System.out.println("build success");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("build failed");
        }
    }

    public void UpdateDatabase(player pl) throws SQLException {
        int result = 0;
        String url = "jdbc:mysql://localhost:3306/maze_player?serverTimezone=GMT&useSSL=false&characterEncoding=utf8";
        Connection conn = DriverManager.getConnection(url, "root", "1548712503");
        Statement stat = conn.createStatement();
        ResultSet rs = null;
        try {
            rs = stat.executeQuery("select * from player where name='" + pl.Name + "'");
            if (rs.next()) {
                int temp = rs.getInt("step");
                if (temp < pl.best_score) {
                    pl.best_score = temp;
                }
                result = stat.executeUpdate("update player set step=" + pl.best_score + " where name='" + pl.Name + "'");
                System.out.println("update success");
            } else {
                stat.executeUpdate("insert into player(name,step)value ('" + pl.Name + "'," + pl.best_score + ")");
                System.out.println("insert success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("update failed or query failed");
        }
        assert rs != null;
        rs.close();
        stat.close();
        conn.close();
    }

    public void GetRank(String name, GC gc, Display dis) {
        String url = "jdbc:mysql://localhost:3306/maze_player?serverTimezone=GMT&useSSL=false&characterEncoding=utf8";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url, "root", "1548712503");
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stat.executeQuery("select num,step,name,(@rowNum:=@rowNum+1) as rowNo " +
                    "from player, " +
                    "(select (@rowNum :=0) ) b " +
                    "order by player.step ");
            gc.setBackground(new Color(dis, 187, 239, 253));
            gc.setForeground(new Color(dis, 0, 0, 0));
            gc.setFont(new Font(dis, "等线", 20, SWT.NONE));
            int cnt = 0, pl_rank = 0;
            while (rs.next()) {
                String temp = rs.getString("name");
                ++cnt;
                if (Objects.equals(temp, name)) {
                    pl_rank = rs.getInt("rowNo");
                    String s = pl_rank + "";
                    gc.drawString(s, 150, 40);
                }
                if (cnt <= 10) {
                    int r = rs.getInt("rowNo");
                    int num = rs.getInt("num");
                    int step = rs.getInt("step");
//                System.out.println(r + " " + num + " " + temp + " " + step);
                    StringBuilder s = new StringBuilder();
                    s.append(r);
                    while (s.length() < 14) {
                        s.append(" ");
                    }
                    s.append(num);
                    while (s.length() < 25) {
                        s.append(" ");
                    }
                    s.append(temp);
                    while (s.length() < 40) {
                        s.append(" ");
                    }
                    s.append(step);
                    gc.drawString(s.toString(), 350, 20 + 40 * cnt);
                }
                if (pl_rank != 0 && cnt > 15) {
                    break;
                }
            }
            System.out.println("GetRank success");
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("GetRank failed");
        }

        try {
            assert rs != null;
            rs.close();
            stat.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PrintDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/maze_player?serverTimezone=GMT&useSSL=false&characterEncoding=utf8";
        Connection conn = DriverManager.getConnection(url, "root", "1548712503");
        Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stat.executeQuery("select *from player");
        try {
            System.out.println("----print start----");
            while (rs.next()) {
                int num = rs.getInt("num");
                int step = rs.getInt("step");
                String name = rs.getString("name");
                System.out.println(num + " " + " " + name + " " + step);
            }
            System.out.println("-----print end-----");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("print failed");
        }
    }

}
