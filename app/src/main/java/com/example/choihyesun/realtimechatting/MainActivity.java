package com.example.choihyesun.realtimechatting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    private String nickname;
    private String password;

    Intent intent;

    UserDTO userDTO;

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

        // 어댑터 레이아웃 연결
        adapter = new CustomAdapter(this, R.layout.layout_list_row, chatList);

        backPressCloseHandler = new BackPressCloseHandler(this);
        userDTO = new UserDTO();

        // 서버 연결
        URI uri = null;

        try {
            uri = new URI("ws://192.168.0.151:8889");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // user 객체 가져오기
        intent = getIntent();
        userDTO = (UserDTO) intent.getSerializableExtra("user");

        // 리스트뷰와 어댑터 연결
        listView = (ListView) findViewById(R.id.listView);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); // 새로운 리스트뷰로 자동 스크롤
        listView.setAdapter(adapter);
        listView.setSelection(adapter.getCount() - 1);  // 자동으로 최신 리스트로 포커싱

        // 서버에 메시지 전송하는 소스
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                sendMessageOpen();
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
                sendMessageClose();
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

    // 아이디와 메세지를 보내는 메소드
    public void sendMessage(){
        try{
            mWebSocketClient.send(userDTO.getUserId() + ": " + sendMsgEdit.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        sendMsgEdit.setText("");
    }

    // 옵션 메뉴 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "채팅방");
        menu.add(0, 2, 0, "친구들 위치");
        return true;
    }

    // 메뉴 선택시 실행되는 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case 1:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user", userDTO);
                startActivity(intent);
                return true;
            case 2:
                intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("user", userDTO);
                startActivity(intent);
                return true;
        }

        return false;
    }

    // 채팅방 입장 시
    public void sendMessageOpen(){
        try{
            mWebSocketClient.send(userDTO.getUserId() + "님이 입장하셨습니다");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 채팅방 나갈 때
    public void sendMessageClose(){
        try{
            mWebSocketClient.send(userDTO.getUserId() + "님이 나가셨습니다");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
