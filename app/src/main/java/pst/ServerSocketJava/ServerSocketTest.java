package pst.ServerSocketJava;

import java.util.Date;

import pst.ServerSocketAndroid.UsbManager;
import pst.ServerSocketAndroid.UsbServerSocket;

public class ServerSocketTest {
    public static void main(String[]args){
        // 开启服务器
        ServerSocket.getInstance().start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    ServerManager.getManager().publish("51348//你好//"+new Date());
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
