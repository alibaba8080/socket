package pst.test_socketchat;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pst.ServerSocketJava.ServerManager;
import pst.constant.Constant;

public class CharActivity extends AppCompatActivity {

    private RecyclerView rv;
    private EditText et;
    private Button btn;
    private Socket socket;
    private ArrayList<MyBean> list;
    private MyAdapter adapter;
    private Handler handler;
    private OutputStream outputStream;
    private InputStream inputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.char_layout);

        rv = (RecyclerView) findViewById(R.id.rv);
        et = (EditText) findViewById(R.id.et);
        btn = (Button) findViewById(R.id.btn);
        list = new ArrayList<>();
        adapter = new MyAdapter(this);

        handler = new MyHandler();
        startChart();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String data = et.getText().toString();

                if (outputStream == null) {
                    Toast.makeText(CharActivity.this, "未连接", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");    //设置日期格式
                            outputStream.write((socket.getLocalPort() + "//" + data + "//" + df.format(new Date()) + "\n").getBytes("utf-8"));
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                et.setText("");
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //
                int localPort = socket.getLocalPort();
                if (list.size() > 8) {
                    list.clear();
                }
                String[] split = ((String) msg.obj).split("//");
                if (split[0].equals(localPort + "")) {
                    MyBean bean = new MyBean(split[1], 1, split[2], "我：");
                    list.add(bean);
                } else {
                    MyBean bean = new MyBean(split[1], 2, split[2], ("来自：" + split[0]));
                    list.add(bean);
                }

                // 向适配器set数据
                adapter.setData(list);
                rv.setAdapter(adapter);
                LinearLayoutManager manager = new LinearLayoutManager(CharActivity.this, LinearLayoutManager.VERTICAL, false);
                rv.setLayoutManager(manager);
            }
        }
    }

    private void startChart() {
        if (socket == null) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {

                        try {
                            socket = new Socket(Constant.SOCKET_ADDRESS, 18100);
                            outputStream = socket.getOutputStream();
                            inputStream = socket.getInputStream();
                            byte[] buffer = new byte[1024];
                            int len;
                            String msg = "";
                            while ((len = inputStream.read(buffer)) != -1) {
                                String input = new String(buffer, 0, len);
                                msg += input;
                                if (msg.endsWith("\n")) {
                                    String data = new String(buffer, 0, len);
                                    Message message = Message.obtain();
                                    message.what = 1;
                                    message.obj = data;
                                    handler.sendMessage(message);
                                    msg = "";
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }

            }).start();
            return;
        }
    }
}
