package pst.ServerSocketJava;

import java.util.HashMap;
import java.util.Map;

import pst.ServerSocketAndroid.UsbHandler;

/**
 * Create By：Pst on 2018/11/1 0001 14:21
 * DescriBe:连接池管理
 */
public class ServerManager {
    private ServerManager() {

    }

    private static final ServerManager cm = new ServerManager();

    public static ServerManager getManager() {
        return cm;
    }

    private static Map<String, OnClickListener> listenerList = new HashMap<>();

    public void setOnClicklistener(String userAddrss, OnClickListener listener) {
        if (listenerList.containsKey(userAddrss)) {
            listenerList.get(userAddrss).onClosed();
            listenerList.remove(userAddrss);
            listenerList.put(userAddrss, listener);
        } else {
            listenerList.put(userAddrss, listener);
            System.out.println(userAddrss+"  链接成功"+"  连接数量："+listenerList.size());
        }

    }


    public boolean publish(final String out) {
        if (listenerList.size() == 0) {
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (OnClickListener listener : listenerList.values()) {
                    listener.onSend(out);
                }
            }
        }).start();
        return true;
    }

    public void closeAllClient() {
        for (OnClickListener listener : listenerList.values()) {
            listener.onClosed();
            listenerList.remove(listener);
        }
    }

    public void remove(String address) {
        System.out.println(address+"断开连接");
        listenerList.remove(address);
    }

    public interface OnClickListener {
        void onSend(String msg);

        void onClosed();
    }
}
