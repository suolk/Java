public class test {
    public static void main(String[] args) {

    }

    {/*public static void CreateDatabase() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        //获取连接
        String url = "jdbc:mysql://localhost:3306/maze_player?serverTimezone=GMT&useSSL=false&characterEncoding=utf8";

        try {
            Connection conn = DriverManager.getConnection(url, "root", "1548712503");
            //创建命令
            Statement stat = conn.createStatement();
            //若不存在则建表
            stat.executeUpdate("create table if not exists test(num int primary key auto_increment,name varchar(20),step int)");
            stat.close();
            conn.close();
            System.out.println("build success");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("build failed");
        }
    }

    public static void UpdateDatabase(player pl) throws SQLException {
        int result = 0;
        String url = "jdbc:mysql://localhost:3306/maze_player?serverTimezone=GMT&useSSL=false&characterEncoding=utf8";
        Connection conn = DriverManager.getConnection(url, "root", "1548712503");
        Statement stat = conn.createStatement();
        try {
            result = stat.executeUpdate("update test set step=" + pl.best_score + " where name='" + pl.Name + "'");
            if (result != 0)
                System.out.println("update success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("update failed");
        }
        if (result == 0) {
            try {
                stat.executeUpdate("insert into test(name,step)value ('" + pl.Name + "'," + pl.best_score + ")");
                System.out.println("insert success");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("insert failed");
            }
        }
        stat.close();
        conn.close();
    }

    public static void GetRank(String name) {
        String url = "jdbc:mysql://localhost:3306/maze_player?serverTimezone=GMT&useSSL=false&characterEncoding=utf8";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url, "root", "1548712503");
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stat.executeQuery("select num,step,name,(@rowNum:=@rowNum+1) as rowNo " +
                    "from test, " +
                    "(select (@rowNum :=0) ) b " +
                    "order by test.step ");
            while (rs.next()) {
                String temp = rs.getString("name");
                int r = rs.getInt("rowNo");
                int num = rs.getInt("num");
                int step = rs.getInt("step");
                System.out.println(r + num + " " + " " + name + " " + step);
            }
            System.out.println("rank found");
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

    public static void PrintDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/maze_player?serverTimezone=GMT&useSSL=false&characterEncoding=utf8";
        Connection conn = DriverManager.getConnection(url, "root", "1548712503");
        Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stat.executeQuery("select *from test");
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
    }*/
    }


}
