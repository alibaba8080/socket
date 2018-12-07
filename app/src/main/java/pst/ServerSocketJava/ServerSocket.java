package pst.ServerSocketJava;

import java.io.IOException;
import java.net.Socket;

/**
 * Create By：Pst on 2018/11/1 0001 14:13
 * DescriBe:
 */
public class ServerSocket extends Thread {

    private static ServerSocket ourInstance;
    private static final Object mLock = new Object();
    private final int port = 18100;
    private static java.net.ServerSocket server;
    private boolean isExit = false;
    public static boolean isClose = true;

    public static ServerSocket getInstance() {
        if (ourInstance == null) {
            synchronized (mLock) {
                if (ourInstance == null) {
                    ourInstance = new ServerSocket();
                }
            }
        }
        return ourInstance;
    }

    private ServerSocket() {
        try {
            server = new java.net.ServerSocket(port);
            System.out.println("启动服务成功" + "port:" + port);
        } catch (IOException e) {
            System.out.println("启动服务失败，错误原因：" + e.getMessage());
        }
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            if (isExit) {
                System.out.println("服务关闭");
                break;
            }
            try {
                System.out.println("等待手机的连接... ... ");
                if (server == null) {
                    break;
                }
                Socket socket = server.accept();
                new ServerClient(socket).start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("服务关闭" + e.getMessage());

                isClose=true;
                ourInstance = null;
            }
        }
    }

    @Override
    public synchronized void start() {
        if (this.isAlive()){
            System.out.println("服务器已经启动");
            return;
        }
        isClose = false;
        super.start();
    }

    public void stopServer() {
        this.isExit = false;
        ServerManager.getManager().closeAllClient();
        if (server != null) {
            try {
                server.close();
                server = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
