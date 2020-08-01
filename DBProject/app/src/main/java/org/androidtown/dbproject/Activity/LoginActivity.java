package org.androidtown.dbproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.androidtown.dbproject.ConnectDB;
import org.androidtown.dbproject.Object.User;
import org.androidtown.dbproject.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class LoginActivity extends AppCompatActivity {

    private EditText Id_EditText = null;
    private EditText Pw_EditText = null;
    private Button Login_Button = null;
    private Button Regist_Button = null;
    private TextView Worring_TextView = null;

    private ConnectDB DB = null;
    private static final String DB_NAME = "apuapu.db";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            boolean bResult = isCheckDB(getApplicationContext());	// DB가 있는지?
            Log.d("MiniApp", "DB Check="+bResult);
            if(!bResult){	// DB가 없으면 복사
                copyDB(getApplicationContext());
            }else{

            }
        } catch (Exception e) {

        }

        DB = new ConnectDB(this);

        Id_EditText = findViewById(R.id.login_id_edittext);
        Pw_EditText = findViewById(R.id.login_pw_edittext);
        Login_Button = findViewById(R.id.login_button);
        Regist_Button = findViewById(R.id.regist_button);
        Worring_TextView = findViewById(R.id.login_worring);

        Login_Button.setOnClickListener(Login_Button_ClickListener);
        Regist_Button.setOnClickListener(Regist_Button_ClickListener);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    // 로그인 버튼 클릭 이벤트
    private View.OnClickListener Login_Button_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Login()) {
                if(Initial_Setting()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), Initial_SettingActivity.class);
                    startActivityForResult(intent,3000);
                }
            }
        }
    };

    // 회원가입 버튼 클릭 이벤트Select_User_Cloth
    private View.OnClickListener Regist_Button_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), RegistActivity.class);
            startActivity(intent);
        }
    };

    private boolean Login() {

        String id = String.valueOf(Id_EditText.getText());
        String pw = String.valueOf(Pw_EditText.getText());

        if (id.equals("") || pw.equals("")) {
            Worring_TextView.setText("입력이 올바르지 않습니다.");
            return false;
        } else {
            User user = null;
            user = DB.Select_User(id, pw);

            if (user == null) {
                Worring_TextView.setText("입력한 정보가 일치하지 않습니다.");
                return false;
            } else {
                Id_EditText.setText("");
                Pw_EditText.setText("");
                Worring_TextView.setText("");
                User.setMainUser(user);
                return true;
            }

        }
    }

    private boolean Initial_Setting(){

        return !DB.Select_User_Cloth(User.MainUser_Id).isEmpty();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                // MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case 3000:
                    if(data.getBooleanExtra("result", false)) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    }
            }
        }
    }

    // DB가 있나 체크하기
    public boolean isCheckDB(Context mContext){
        String filePath = "/data/data/" + mContext.getPackageName() + "/databases/" + DB_NAME;
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }

        return false;

    }

    // DB를 복사하기
    // assets의 /db/xxxx.db 파일을 설치된 프로그램의 내부 DB공간으로 복사하기
    public void copyDB(Context mContext){
        Log.d("MiniApp", "copyDB");
        AssetManager manager = mContext.getAssets();
        String folderPath = "/data/data/" + mContext.getPackageName() + "/databases";
        String filePath = "/data/data/" + mContext.getPackageName() + "/databases/" + DB_NAME;
        File folder = new File(folderPath);
        File file = new File(filePath);

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            InputStream is = manager.open("db/" + DB_NAME);
            BufferedInputStream bis = new BufferedInputStream(is);

            if (folder.exists()) {
            }else{
                folder.mkdirs();
            }


            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();

            bos.close();
            fos.close();
            bis.close();
            is.close();

        } catch (IOException e) {
            Log.e("ErrorMessage : ", e.getMessage());
        }
    }
}
