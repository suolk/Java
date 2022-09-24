import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Maze {
    int Blank = 0, Wall = 1, Path = 2, Wall_Num = 544;

    public void Create_Maze(int[][] maze) { // 原理是用最小生成树生成联通道路，这里用的是并查集+kruskal
        for (int i = 0; i <= 35; i++) {// 先将迷宫全部初始化为墙
            for (int j = 0; j <= 35; j++) {
                maze[i][j] = Wall;
            }
        }
        for (int i = 1; i < 35; i += 2) { // 再将必需的空格填入
            for (int j = 1; j < 35; j += 2) {
                maze[i][j] = Blank;
            }
        }
        List<Integer> Random_Sequence = new ArrayList<>();
        for (int i = 0; i < Wall_Num; i++) { // 设置一个数组用于生成随机序列
            Random_Sequence.add(i);
        }
        for (int i = 0; i < 5; ++i) {// 生成一个随机序列用于随机拆墙生成迷宫
            Collections.shuffle(Random_Sequence);
        }

        Merge_Find bcj = new Merge_Find(); // 调用并定义一个并查集类
        bcj.create(17 * 17);
        for (int i = 0; i < Wall_Num; i++) {
            if (bcj.Set_Quantity() <= 1) {// 只剩少于1个集合说明道路已全部联通，即迷宫已完成
                break;
            }
            int num, Horizon = 0, Vertical = 0, Rand_Wall_Num, x_Wall, y_Wall;
            if (Random_Sequence.get(i) < Wall_Num / 2) { // 序列中编号为0~Wall_Num/2-1的为横向墙
                Rand_Wall_Num = Random_Sequence.get(i);
                Horizon = 1;
                num = 16;
            } else {// 序列中编号为Wall_Num/2~Wall_Num的为纵向墙
                Rand_Wall_Num = Random_Sequence.get(i) - Wall_Num / 2;
                Vertical = 1;
                num = 17;
            }
            x_Wall = Rand_Wall_Num % num;
            y_Wall = Rand_Wall_Num / num;

            int x1 = x_Wall, y1 = y_Wall, x2 = x_Wall + Horizon, y2 = y_Wall + Vertical;
            if (bcj.Set_Union(y1 * 17 + x1, y2 * 17 + x2)) { // 合并了集合说明需要拆墙
                maze[y_Wall + y2 + 1][x_Wall + x2 + 1] = Blank;
            }
        }
        maze[33][34] = maze[1][0] = Blank;
    }

    public boolean Find_Path(int x, int y, int[][] maze, int[][] dire) { // 用dfs回溯法来搜索离开迷宫的最短路线

        if (x >= 0 && x <= 35 && y >= 0 && y <= 35 && maze[x][y] == Blank) { // 检测到空格
            maze[x][y] = Path; // 先标记为路线
            if (x == 33 && y == 34) { // 到达出口，说明路线正确，返回真
                return true;
            }
            for (int i = 0; i < 4; i++) { // dire数组分别代表了四个方向
                if (Find_Path(x + dire[i][0], y + dire[i][1], maze, dire)) { // 递归调用
                    return true; // 路线正确，返回真
                }
            }
            maze[x][y] = Blank; // 若四个方向都没有正确路线则说明这一格不是出迷宫的必经之路，重新标记为空格
        }
        return false; // 进行到这一步说明遇到了路线不正确，返回假
    }

    public void draw_maze(GC g, int[][] maze, int Wall, Display dis) {
        g.setBackground(new Color(dis, 187, 239, 253));
        g.fillRectangle(210, 30, 500, 500);
        g.setForeground(new Color(dis, 0, 0, 0));
        g.setLineWidth(2);
        for (int i = 0; i < 35; i += 2) {
            for (int j = 1; j < 35; j += 2) {
                if (maze[i][j] == Wall) {
                    g.drawLine(220 + (j - 1) * 14, 45 + i * 14, 220 + 14 * (j + 1), 45 + 14 * i); // 画横线
                }
            }
        }
        for (int i = 1; i < 35; i += 2) {
            for (int j = 0; j < 35; j += 2) {
                if (maze[i][j] == Wall) {
                    g.drawLine(220 + 14 * j, 45 + 14 * (i - 1), 220 + 14 * j, 45 + 14 * (i + 1)); // 画竖线
                }
            }
        }
        g.drawImage(new Image(dis, "src/player.png"), 0, 0, 220, 220, 212, 51, 15, 15);
    }

    public void draw_player(GC g, int fx, int fy, int x, int y, Display dis) {
        g.drawImage(new Image(dis, "src/player.png"), 0, 0, 220, 220, y * 14 + 212, x * 14 + 37, 15, 15);
        g.setBackground(new Color(dis, 187, 239, 253));
        g.fillRectangle(fy * 14 + 212, fx * 14 + 37, 15, 15);
    }

    public void draw_path(GC g, int[][] maze, int x, int y, Display dis) {
        g.setBackground(new Color(dis, 131, 94, 198));
        for (int i = 1; i < 35; i++) {
            for (int j = 1; j < 35; j++) {
                if (i == x && j == y) {
                    maze[i][j] = Blank;
                } else if (maze[i][j] == Path) {    //检测到路线则画出一个小方块覆盖当前格
                    g.fillRectangle(j * 14 + 212, i * 14 + 37, 15, 15);
                    maze[i][j] = Blank;             //同时记得恢复标记
                }
            }
        }
    }
}
