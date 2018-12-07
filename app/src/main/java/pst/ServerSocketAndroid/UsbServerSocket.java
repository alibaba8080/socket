package pst.ServerSocketAndroid;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Create By：Pst on 2018/11/1 0001 14:13
 * DescriBe:
 */
public class UsbServerSocket extends Thread {

    private static UsbServerSocket ourInstance;
    private static final Object mLock = new Object();
    private final int port = 18100;
    private static ServerSocket server;
    private boolean isExit = false;
    public static boolean isClose = true;

    public static UsbServerSocket getInstance() {
        if (ourInstance == null) {
            synchronized (mLock) {
                if (ourInstance == null) {
                    ourInstance = new UsbServerSocket();
                }
            }
        }
        return ourInstance;
    }

    private UsbServerSocket() {
        try {
            server = new ServerSocket(port);
            UsbHandler.getInstance().sendMsg("启动服务成功" + "port:" + port, UsbHandler.STATUS_SERVER_SUCCESS);
        } catch (IOException e) {
            System.out.println();
            UsbHandler.getInstance().sendMsg("启动服务失败，错误原因：" + e.getMessage(), UsbHandler.STATUS_CONNECT_ERROR);
        }
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            if (isExit) {
                UsbHandler.getInstance().sendMsg("服务关闭", UsbHandler.STATUS_SERVER_CLOSED);
                break;
            }
            try {
                System.out.println("等待手机的连接... ... ");
                if (server == null) {
                    break;
                }
                Socket socket = server.accept();
                new UsbClient(socket).start();
            } catch (IOException e) {
                e.printStackTrace();
                UsbHandler.getInstance().sendMsg("服务关闭" + e.getMessage(), UsbHandler.STATUS_SERVER_CLOSED);
                isClose=true;
                ourInstance = null;
            }
        }
    }

    @Override
    public synchronized void start() {
        if (this.isAlive()){
            UsbHandler.getInstance().sendMsg("服务器已经启动", UsbHandler.STATUS_CONNECT_SUCCESS);
            return;
        }
        isClose = false;
        super.start();
    }

    public void stopServer() {
        this.isExit = false;
        UsbManager.getManager().closeAllClient();
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
