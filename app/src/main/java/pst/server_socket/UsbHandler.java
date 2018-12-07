package pst.server_socket;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import pst.constant.USBApplication;

/**
 * Create By：Pst on 2018/11/2 0002 10:41
 * DescriBe:消息发送
 */
public class
UsbHandler extends Handler {
    public final static int ACTION_DOWN_PRISONER = 0001;//下载犯人数据
    public final static int ACTION_UPLOAD_DATA  = 0002;//上传消息
    public final static int ACTION_CONNECT_CLOSED = 0003;//关闭连接
    public final static int ACTION_DOWN_CONFIG = 0004;// 轮次配置/干警信息
    public final static int STATUS_CONNECT_SUCCESS = 0005;//连接成功
    public final static int STATUS_CONNECT_ERROR = 0006;//连接失败
    public final static int STATUS_SERVER_SUCCESS = 0007;//服务器开启
    public final static int STATUS_SERVER_CLOSED = 0000;//服务器关闭

    private static final UsbHandler ourInstance = new UsbHandler();
    private com.jx.call.server_socket.UsbListener listener;

    public static UsbHandler getInstance() {
        return ourInstance;
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String msgInfo = (String) msg.obj;

        switch (msg.what) {
            case STATUS_CONNECT_SUCCESS:
                printLog(msgInfo);
                toast(msgInfo);
                if (null != listener) {
                    listener.onConnect(msgInfo);
                }
                break;
            case ACTION_CONNECT_CLOSED:
                printLog(msgInfo);
                if (null != listener) {
                    listener.onDisconnect(msgInfo);
                }
                break;
            case STATUS_CONNECT_ERROR:
                printLog(msgInfo);
                toast(msgInfo);
                if (null != listener) {
                    listener.onError(msgInfo);
                }
                break;
            case ACTION_UPLOAD_DATA:
                if (null != listener) {
                    listener.onUpLoad(msgInfo);
                }
                break;
            case ACTION_DOWN_CONFIG:
                if (null != listener) {
                    listener.onDownLoad(msgInfo);
                }
                break;
            case ACTION_DOWN_PRISONER:
                if (null != listener) {
                    listener.onDownData(msgInfo);
                }
                break;
            case STATUS_SERVER_SUCCESS:
                printLog(msgInfo);
//                showMsg(msgInfo);
                break;
            case STATUS_SERVER_CLOSED:
                printLog(msgInfo);
//                showMsg(msgInfo);
                break;
        }


    }

    protected void sendMsg(String msgInfo, int msgFlag) {
        if (null == msgFlag + "") {
            msgFlag = STATUS_CONNECT_SUCCESS;
        }
        Message handlerMsg = new Message();
        handlerMsg.what = msgFlag;
        handlerMsg.obj = msgInfo;
        this.sendMessage(handlerMsg);
    }

    public void setListener(com.jx.call.server_socket.UsbListener listener) {
        this.listener = listener;
    }
    public void closeMessage() {
        this.removeCallbacksAndMessages(null);
        this.listener = null;
    }
    private void toast(String msgInfo) {
        Toast.makeText(USBApplication.context, msgInfo + "", Toast.LENGTH_SHORT).show();
    }

    private void printLog(String msg) {
        Log.e("USB_HANDER", msg);
    }
}
