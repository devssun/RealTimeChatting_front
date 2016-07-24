package com.example.choihyesun.realtimechatting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<MyItem> chatList = new ArrayList<MyItem>();
    CustomAdapter adapter;
    MyItem vo;

    // today Date
    String currentDate;
    Calendar today;
    int amFm;

//    TextView chatContentTxt;
    EditText sendMsgEdit;
    Button sendMsgBtn;
    WebSocketClient mWebSocketClient;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendMsgEdit = (EditText) findViewById(R.id.sendMsgEdit);
        sendMsgBtn = (Button) findViewById(R.id.sendBtn);

        // 시간 뿌리기
        today = Calendar.getInstance();
        amFm = today.get(Calendar.HOUR_OF_DAY);

        if(amFm < 12)
            currentDate = "오전 " + (today.get(Calendar.HOUR_OF_DAY) + ":" + today.get(Calendar.MINUTE));
        if(amFm > 13)
            currentDate = "오후 " + (today.get(Calendar.HOUR_OF_DAY) + ":" + today.get(Calendar.MINUTE));

        adapter = new CustomAdapter(this, R.layout.layout_list_row, chatList);

        backPressCloseHandler = new BackPressCloseHandler(this);

        URI uri = null;

        try {
            uri = new URI("ws://192.168.25.44:8889");
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.listView);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); // 새로운 리스트뷰로 자동 스크롤
        listView.setAdapter(adapter);
        listView.setSelection(adapter.getCount() - 1);

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
            }

            @Override
            public void onMessage(String s) {
                // 메세지 뿌리는 곳
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vo = new MyItem(message, currentDate);
                        chatList.add(vo);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        try{
            mWebSocketClient.connect();
        }catch (Exception e){
            e.printStackTrace();
        }

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    public void sendMessage(){
        try{
            mWebSocketClient.send(sendMsgEdit.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        sendMsgEdit.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
