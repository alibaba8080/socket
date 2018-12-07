package pst.ServerSocketJava;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import pst.ServerSocketAndroid.UsbHandler;


/**
 * Create By：Pst on 2018/11/1 0001 14:21
 * DescriBe:连接的客户端
 */
public class ServerClient extends Thread implements ServerManager.OnClickListener {
    private Socket socket;
    private final String address;
    private volatile boolean flag = true;
    PrintWriter printWriter = null;
    private InputStream inputStream;
    private int MSG_MODE = UsbHandler.STATUS_CONNECT_ERROR;

    public ServerClient(Socket socket) {
        this.socket = socket;
        address = socket.getInetAddress().toString();
    }

    @Override
    public void run() {
        super.run();
        try {
            // 单线程索锁
            synchronized (this) {
                // 放进到Map中保存
                ServerManager.getManager().setOnClicklistener(address,this);
            }
            // 定义输入流
            inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int len;
            String msg = "";
            while (flag && (len = inputStream.read(buffer)) != -1) {
                String input = new String(buffer, 0, len);
                msg += input;
                if (msg.endsWith("\n")) {
                    ServerManager.getManager().publish(msg);
                    msg="";
                }

            }
        } catch (Exception e) {
            System.out.println("错误信息为：" + e.getMessage());
        } finally {
            synchronized (this) {
                ServerManager.getManager().remove(address);
            }
        }
    }

    @Override
    public void onSend(String msg) {
        try {
            if (printWriter == null) {
                printWriter = new PrintWriter(socket.getOutputStream(), true);
            }
            printWriter.println(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosed() {
        try {
            flag=false;
            if (inputStream != null) {
                inputStream.close();
            }
            if (printWriter != null) {
                printWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            printWriter = null;
            inputStream = null;
            socket = null;
        }
    }
}
