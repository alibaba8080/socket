package pst.server_socket;

import java.util.HashMap;
import java.util.Map;


/**
 * Create By：Pst on 2018/11/1 0001 14:21
 * DescriBe:连接池管理
 */
public class USBanager {
    private USBanager() {

    }

    private static final USBanager cm = new USBanager();

    public static USBanager getManager() {
        return cm;
    }

    Map<String, UsbClient> clientMap = new HashMap<>();

    public void add(UsbClient usbClient) {
        String address = usbClient.getAddress();
        if(clientMap.containsKey(address)){
            clientMap.get(address).cloes();
            clientMap.remove(address);
        }
        clientMap.put(address, usbClient);
//        UsbHandler.getInstance().sendMsg("当前连接数量：" + clientMap.size(),UsbHandler.STATUS_CONNECT_SUCCESS);
    }

    public void remove(String address) {
        clientMap.remove(address);
    }

    public boolean publish(final String out,final int type) {
        if(clientMap.size()==0){
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (UsbClient usbClient : clientMap.values()) {
                    usbClient.sendMsg(out,type);
                }
            }
        }).start();
        return true;
    }
    public void closeAllClient() {
        for (UsbClient usbClient : clientMap.values()) {
            usbClient.cloes();
        }
    }
}
