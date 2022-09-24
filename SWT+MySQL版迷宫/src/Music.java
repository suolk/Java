import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Music {//音乐类

    public void st_tr(Shell win) {
        final Player[] player = {null};
        final Runnable refresh = new Runnable() {
            public void run() {//第一层嵌套将作为showWhile的参数
                final Runnable Run = new Runnable() {
                    public void run() {//第二层为音乐播放
                        File file = new File("src/m_bgm.mp3");
                        try {
                            player[0] = new Player(new FileInputStream(file));
                        } catch (FileNotFoundException | JavaLayerException e) {
                            e.printStackTrace();
                        }
                        try {
                            System.out.println(1);
                            player[0].play();
                            System.out.println(2);
                        } catch (JavaLayerException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(123);
                        player[0].close();
                    }
                };
                Thread thread = new Thread(Run);
                thread.start();
            }
        };
        BusyIndicator.showWhile(win.getDisplay(), refresh);//确保音乐播放不影响其他线程
    }
}
