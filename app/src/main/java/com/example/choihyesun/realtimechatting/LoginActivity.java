package com.example.choihyesun.realtimechatting;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by choihyesun on 16. 7. 25..
 * 로그인 클래스
 */
public class LoginActivity extends Activity{

    EditText editNickname;
    EditText editPassword;
    Button btnLogin;
    Button btnJoinUS;

    private String userId;
    private String pswd;

    MySqliteDBHelper myDBHelper;
    SQLiteDatabase db;

    UserDTO userDTO;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.loginBtn);
        btnJoinUS = (Button) findViewById(R.id.joinUSBtn);
        editNickname = (EditText) findViewById(R.id.nicknameEdit);
        editPassword = (EditText) findViewById(R.id.passwordEdit);

        myDBHelper = new MySqliteDBHelper(this);
        userDTO = new UserDTO();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userId = editNickname.getText().toString();
                pswd = editPassword.getText().toString();

                // DB Select
                db = myDBHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM userTBL where userId = '"+ userId +"' AND pswd = '"+ pswd + "';", null);

                while (cursor.moveToNext()){
                    userDTO.setStudentNumber(Integer.parseInt(cursor.getString(0)));
                    userDTO.setName(cursor.getString(1));
                    userDTO.setUserId(cursor.getString(2));
                    userDTO.setPswd(cursor.getString(3));
                }

                cursor.close();
                db.close();

                // 입력된 userId와 db의 userId가 동일하면 데이터 전송하고 intent 넘김
                if(userId.equals(userDTO.getUserId())){
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("userId", userDTO.getUserId());
                    intent.putExtra("pswd", userDTO.getPswd());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "등록되지않은 회원입니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i("chat", "ID " + userDTO.getUserId() +", 비밀번호 "+ userDTO.getPswd());

            }
        });

        btnJoinUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }
}
