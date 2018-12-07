package pst.ServerSocketAndroid;

import java.util.Date;

import pst.ServerSocketAndroid.UsbManager;
import pst.ServerSocketAndroid.UsbServerSocket;

public class ServerSocketTest {
    public static void main(String[]args){
        // 开启服务器
        UsbServerSocket.getInstance().start();
//        ServerSocket.getInstance().stopServer();
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
