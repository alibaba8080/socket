package pst.test;

import java.util.Date;

import pst.server_socket.UsbManager;
import pst.server_socket.UsbServerSocket;

public class ServerSocketTest {
    public static void main(String[]args){
        // 开启服务器
        UsbServerSocket.getInstance().start();
//        UsbServerSocket.getInstance().stopServer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    UsbManager.getManager().publish("51348//你好//"+new Date());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
