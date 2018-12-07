package pst.server_socket;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Create By：Pst on 2018/11/1 0001 14:21
 * DescriBe:连接的客户端
 */
public class UsbClient extends Thread {
    private Socket socket;
    private final String address;
    private volatile boolean flag = true;
    PrintWriter printWriter = null;
    private InputStream inputStream;
    private int MSG_MODE = UsbHandler.STATUS_CONNECT_ERROR;

    public UsbClient(Socket socket) {
        this.socket = socket;
        address = socket.getInetAddress().toString();
        UsbHandler.getInstance().sendMsg("连接成功，连接的手机为：" + address, UsbHandler.STATUS_CONNECT_SUCCESS);
    }

    public String getAddress() {
        return address;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        super.run();
        try {
            // 单线程索锁
            synchronized (this) {
                // 放进到Map中保存
                USBanager.getManager().add(UsbClient.this);
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
                    UsbHandler.getInstance().sendMsg(msg + "", MSG_MODE);
                    msg = "";
                }

            }
        } catch (Exception e) {
            System.out.println("错误信息为：" + e.getMessage());
        } finally {
            synchronized (this) {
                UsbHandler.getInstance().sendMsg("关闭链接：" + address, UsbHandler.ACTION_CONNECT_CLOSED);
                USBanager.getManager().remove(address);
                cloes();
            }
        }
    }

    public boolean sendMsg(String msg, int type) {
        MSG_MODE = type;
        try {
            if (printWriter == null) {
                printWriter = new PrintWriter(socket.getOutputStream(), true);
            }
            printWriter.println(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cloes() {
        try {
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
        return true;
    }
}
