package com.example.choihyesun.realtimechatting;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by choihyesun on 16. 8. 10..
 * 회원가입 클래스
 */
public class JoinActivity extends Activity{

    EditText studentNumEdit;
    EditText nameEdit;
    EditText idEdit;
    EditText passwordEdit;

    Button joinBtn;

    MySqliteDBHelper myDBHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        studentNumEdit = (EditText) findViewById(R.id.studentNumberEdit);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        idEdit = (EditText) findViewById(R.id.idEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);

        joinBtn = (Button) findViewById(R.id.joinBtn);

        myDBHelper = new MySqliteDBHelper(this);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentNumber = studentNumEdit.getText().toString();
                String name = nameEdit.getText().toString();
                String userId = idEdit.getText().toString();
                String pswd = passwordEdit.getText().toString();

                // 유효성 검사
                if(!(studentNumber.trim().length() > 0)){
                    Toast.makeText(getApplicationContext(), "학번 정보가 비어있습니다", Toast.LENGTH_SHORT).show();
                    return; // 함수 호출 전으로 돌아감
                }

                if(!(name.trim().length() > 0)){
                    Toast.makeText(getApplicationContext(), "이름 정보가 비어있습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(userId.trim().length() > 0)){
                    Toast.makeText(getApplicationContext(), "ID 정보가 비어있습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(pswd.trim().length() > 0)){
                    Toast.makeText(getApplicationContext(), "비밀번호 정보가 비어있습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                // DB Insert
                db = myDBHelper.getWritableDatabase();
                db.execSQL("INSERT INTO userTBL VALUES (" + studentNumber + ", '" + name + "', '" + userId + "', '" + pswd + "');");
                Log.i("chat", "학번 " + studentNumber +", 이름 "+ name +", ID " + userId +", 비밀번호 "+ pswd);
                db.close();

//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
