import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.*;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Display dis = new Display();
        Shell win = new Shell(dis);

        final boolean[] login = {false};
        final Boolean[] GameStart = {false};
        final char[] ch = new char[1];
        final int[][][] maze = {new int[50][50]};
        final int[] px = {1};
        final int[] py = {0};
        final int[] Click = {0};
        int[][] dire = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}}; // 搜索路径时的方向数组
        int Wall = 1;
        Maze mz = new Maze();
        player pl = new player();
        DB db = new DB();

        win.setText("迷宫");
        win.setSize(800, 600);
        win.setImage(new Image(dis, "src/ico.png"));

        Text name = new Text(win, SWT.SINGLE | SWT.BORDER);
        Canvas canvas = new Canvas(win, SWT.BORDER);
        canvas.setBackgroundImage(new Image(dis, "src/p_BG.png"));
        canvas.setBounds(0, 0, 800, 600);
        canvas.setFocus();
        GC gc = new GC(canvas);

        new Music().st_tr(win);//播放背景音乐
        setCenter(win);//将窗口设置到屏幕正中间
        win.open();

        //添加窗口关闭事件监听，防止叉掉了但是音乐还在播放的灵异事件发生
        win.addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent e) {
                System.exit(0);
            }
        });

        //展示规则
        gc.setBackground(new Color(dis, 187, 239, 253));
        gc.setForeground(new Color(dis, 0, 0, 0));
        gc.setFont(new Font(dis, "等线", 15, SWT.NONE));
        gc.drawString("游戏中使用w、a、s、d(对应上下左右)来进行人物移动", 220, 240);
        gc.drawString("按o即可作弊画出最佳路线", 220, 270);
        gc.drawString("如果跳出了输入法请按shift键", 220, 300);
        gc.drawString("(按任意键继续)", 220, 330);

        name.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((!GameStart[0]) && e.character == '\r') {
                    pl.setName(name.getText());
                    pl.cnt = 0;
                    GameStart[0] = true;
                    px[0] = 1;
                    py[0] = 0;
                    gc.setBackground(new Color(dis, 187, 239, 253));
                    gc.fillRectangle(0, 30, 250, 60);
                    g_s(gc, mz, maze[0], dire, dis, Wall);
                    canvas.setFocus();
                    name.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        canvas.addKeyListener(new KeyListener() {//键盘监听
            @Override
            public void keyPressed(KeyEvent e) {
                ch[0] = e.character;
//                System.out.println(ch);
                if (!login[0]) {
                    login[0] = true;
                    gc.setBackground(new Color(dis, 187, 239, 253));
                    gc.fillRectangle(215, 230, 500, 150);
                    gc.setFont(new Font(dis, "等线", 15, SWT.NONE));
                    gc.setForeground(new Color(dis, 0, 0, 0));
                    gc.drawString("用户名：", 0, 30);
                    gc.drawString("(用户名长度不得超过10个字符)", 0, 60);
                    name.setBounds(80, 30, 100, 20);
                    name.setTextLimit(10);
                    name.setEditable(true);
                    name.setVisible(true);
                    name.setFocus();
                } else if (px[0] == 33 && py[0] == 34 && Click[0] == 0) { // 到出口了
                    pl.setBest_score();
                    gc.setBackground(new Color(dis, 187, 239, 253));
                    gc.fillRectangle(210, 30, 500, 500);
                    gc.setForeground(new Color(dis, 0, 0, 0));
                    gc.setFont(new Font(dis, "Times New Roman Italic", 60, SWT.None));
                    if (!pl.cheat)
                        gc.drawString("YOU WIN !", 240, 200); // 通关成功
                    else
                        gc.drawString("CHEATER !", 240, 200);
                    gc.setFont(new Font(dis, "等线", 20, SWT.NONE));
                    gc.drawString("(按任意键继续)", 300, 350);
                    ++Click[0];
                } else if (px[0] == 33 && py[0] == 34 && Click[0] == 1) {
                    MessageBox msb = new MessageBox(win, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
                    msb.setMessage("再来一局？");//询问是否再来一局
                    if (msb.open() == SWT.YES) {
                        Click[0] = 0;
                        px[0] = 1;
                        py[0] = 0;
                        pl.cheat = false;
                        pl.cnt = 0;
                        g_s(gc, mz, maze[0], dire, dis, Wall);
                    } else {
                        try {
                            db.CreateDatabase();
                            db.UpdateDatabase(pl);
                            gc.setBackground(new Color(dis, 187, 239, 253));
                            gc.setFont(new Font(dis, "等线", 20, SWT.NONE));
                            gc.setForeground(new Color(dis, 0, 0, 0));
                            gc.fillRectangle(210, 30, 500, 500);
                            gc.drawString("排名     编号     玩家           步数", 350, 20);
                            gc.drawString("你的排名：", 20, 40);
                            db.GetRank(pl.Name, gc, dis);
                            gc.setFont(new Font(dis, "等线", 15, SWT.NONE));
                            gc.drawString("(按任意键结束)", 460, 440);
                        } catch (Exception ep) {
                            ep.printStackTrace();
                            System.out.println("normally EndGame failed");
                        }
                        px[0] = py[0] = -1;
                    }
                } else if (px[0] == -1 && py[0] == -1) {
                    System.exit(0);
                } else if ((ch[0] == 'o' || ch[0] == 'O') && GameStart[0]) {
                    mz.Find_Path(px[0], py[0], maze[0], dire);
                    System.out.println("Find Path success");
                    MessageBox msb = new MessageBox(win, SWT.OK | SWT.ICON_QUESTION);
                    if (pl.cheat) {
                        msb.setMessage("不会有人作弊了还迷路吧");
                    } else {
                        msb.setMessage("不会吧不会吧不会有人玩这么小的迷宫都要作弊吧");
                    }
                    pl.cheat = true;
                    msb.open();
                    mz.draw_path(gc, maze[0], px[0], py[0], dis);
                } else if ((ch[0] == 'a' || ch[0] == 'A') && maze[0][px[0]][py[0] - 1] != Wall && GameStart[0]) {
                    mz.draw_player(gc, px[0], py[0], px[0], py[0] - 1, dis);
                    pl.StepCount();
                    --py[0];
                } else if ((ch[0] == 's' || ch[0] == 'S') && maze[0][px[0] + 1][py[0]] != Wall && GameStart[0]) {
                    mz.draw_player(gc, px[0], py[0], px[0] + 1, py[0], dis);
                    pl.StepCount();
                    ++px[0];
                } else if ((ch[0] == 'd' || ch[0] == 'D') && maze[0][px[0]][py[0] + 1] != Wall && GameStart[0]) {
                    mz.draw_player(gc, px[0], py[0], px[0], py[0] + 1, dis);
                    pl.StepCount();
                    ++py[0];
                } else if ((ch[0] == 'W' || ch[0] == 'w') && maze[0][px[0] - 1][py[0]] != Wall && GameStart[0]) {
                    mz.draw_player(gc, px[0], py[0], px[0] - 1, py[0], dis);
                    pl.StepCount();
                    --px[0];
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        while (!win.isDisposed()) {//确保窗口不会一闪而过
            if (!dis.readAndDispatch()) {
                dis.sleep();
            }
        }
        dis.dispose();
    }

    public static void setCenter(Shell win) {
        win.setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().width - win.getBounds().width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - win.getBounds().height) / 2);
    }

    public static void g_s(GC gc, Maze mz, int[][] maze, int[][] dire, Display dis, int Wall) {
        mz.Create_Maze(maze);
        System.out.println("Create Maze success");
        mz.draw_maze(gc, maze, Wall, dis);
        System.out.println("Draw Maze success");

    }
}

