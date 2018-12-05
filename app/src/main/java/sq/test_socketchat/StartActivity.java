package sq.test_socketchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import sq.constant.Constant;
import sq.server_socket.UsbHandler;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startServer;
    private Button shutServer;
    private Button startClient1;
    private Button startClient2;
    private Button startClient3;
    private EditText inputMsg;
    private Button sendMsg;



    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
       UsbHandler usbHandler= UsbHandler.getInstance();
        startServer = (Button) findViewById(R.id.start_server);
        shutServer = (Button) findViewById(R.id.shut_server);
        startClient1 = (Button) findViewById(R.id.start_client1);
        startClient2 = (Button) findViewById(R.id.start_client2);
        startClient3 = (Button) findViewById(R.id.start_client3);
        startServer.setOnClickListener(this);
        shutServer.setOnClickListener(this);
        startClient1.setOnClickListener(this);
        startClient2.setOnClickListener(this);
        startClient3.setOnClickListener(this);
        inputMsg = (EditText) findViewById(R.id.input_msg);
        sendMsg = (Button) findViewById(R.id.send_msg);
        sendMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_server:

                break;
            case R.id.shut_server:

                break;
            case R.id.start_client1:
                setStartClient();
                break;
            case R.id.start_client2:
                setStartClient();
                break;
            case R.id.start_client3:
                setStartClient();
                break;
            case R.id.send_msg:

                break;

        }

    }

    private void setStartClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   Socket socket = new Socket(Constant.SOCKET_ADDRESS, 18100);
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        String data = new String(buffer, 0, len);
                        // 发到主线程中 收到的数据
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = data;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
