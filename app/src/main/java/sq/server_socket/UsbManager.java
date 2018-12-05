package sq.server_socket;

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

    Map<String, UsbClient> clientMap = new HashMap<>();

    public void add(UsbClient usbClient) {
        String address = usbClient.getAddress();
        if(clientMap.containsKey(address)){
            clientMap.get(address).setFlag(false);
            clientMap.remove(address);
        }
        clientMap.put(address, usbClient);
        UsbHandler.getInstance().sendMsg("当前连接数量：" + clientMap.size(),UsbHandler.STATUS_CONNECT_SUCCESS);
    }

    public void remove(String address) {
        clientMap.remove(address);
    }

    public void publish(String out) {
        for (UsbClient usbClient : clientMap.values()) {
            usbClient.sendMsg(out,1);
        }
    }
}
