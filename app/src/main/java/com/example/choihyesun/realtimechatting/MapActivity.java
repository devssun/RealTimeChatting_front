package com.example.choihyesun.realtimechatting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by choihyesun on 16. 8. 29..
 * Map FragmentActivity class
 */
public class MapActivity extends FragmentActivity{

    GoogleMap googleMap;
    GpsInfo gpsInfo;
    WebSocketClient mWebSocketClient;
    Intent intent;
    UserDTO userDTO;

    String gpsinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        intent = getIntent();
        userDTO = (UserDTO) intent.getSerializableExtra("user");

        gpsInfo = new GpsInfo(MapActivity.this);
        if(gpsInfo.isGetLocation()){
            // gps 정보 가져올 수 있을 때
            double latitude = gpsInfo.getLatitude();
            double longitude = gpsInfo.getLongitude();
            gpsinfo = userDTO.getUserId() + ";" + latitude + ";" + longitude;

            // 서버 연결
            URI uri = null;

            try {
                uri = new URI("ws://192.168.0.151:8890");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 서버에 메시지 전송하는 소스
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
                            // split으로 ; 기준으로 나눠 배열 삽입
                            String[] gpsinfo = message.split(";");
                            String userid = gpsinfo[0];
                            double lat = new Double(gpsinfo[1]);
                            double lng = new Double(gpsinfo[2]);

                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lng))
                                    .title(userid)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_menu_mylocation)));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
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
//                Thread.sleep(1000);
                TimerTask myTask = new TimerTask() {
                    @Override
                    public void run() {
                        sendMessage(gpsinfo);
                    }
                };

                Timer timer = new Timer();
                timer.schedule(myTask, 1000, 50000); // 1초 후 실행하고 5초마다 갱신

            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            // gps 정보 가져올 수 없을 때
            gpsInfo.showSettingsAlert();
        }
    }

    // 아이디와 메세지를 보내는 메소드
    public void sendMessage(String gpsinfo){
        try{
            mWebSocketClient.send(gpsinfo);
        }catch (Exception e){
            e.printStackTrace();
        }
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
}
