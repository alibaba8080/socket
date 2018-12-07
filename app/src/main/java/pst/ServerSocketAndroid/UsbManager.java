package pst.ServerSocketAndroid;

import java.util.HashMap;
import java.util.Map;

/**
 * Create By：Pst on 2018/11/1 0001 14:21
 * DescriBe:连接池管理
 */
public class UsbManager {
    private UsbManager() {

    }

    private static final UsbManager cm = new UsbManager();

    public static UsbManager getManager() {
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
            UsbHandler.getInstance().sendMsg(userAddrss+"  链接成功"+"  连接数量："+listenerList.size(), UsbHandler.STATUS_CONNECT_SUCCESS);
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
        listenerList.remove(address);
    }

    public interface OnClickListener {
        void onSend(String msg);

        void onClosed();
    }
}
