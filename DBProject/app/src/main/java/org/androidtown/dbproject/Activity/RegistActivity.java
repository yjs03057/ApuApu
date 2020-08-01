package org.androidtown.dbproject.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.androidtown.dbproject.ConnectDB;
import org.androidtown.dbproject.Object.User;
import org.androidtown.dbproject.R;

public class RegistActivity extends AppCompatActivity {

    private final int NONE = 0;
    private final int MEN = 1;
    private final int WOMEN = 2;

    private int press_status = NONE;

    private EditText Id_EditText =null;
    private EditText Pw_EditText =null;
    private Button Select_Sex_Men_Button =null;
    private Button Select_Sex_Women_Button =null;
    private Button Submit_Button =null;
    private TextView Worring_TextView =null;

    private ConnectDB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        DB=new ConnectDB(this);

        Id_EditText = findViewById(R.id.regist_id_edittext);
        Pw_EditText = findViewById(R.id.regist_pw_edittext);
        Select_Sex_Men_Button= findViewById(R.id.regist_men_button);
        Select_Sex_Women_Button = findViewById(R.id.regist_women_button);
        Submit_Button = findViewById(R.id.submit_button);
        Worring_TextView = findViewById(R.id.regist_worring_textview);

        Select_Sex_Men_Button.setOnClickListener(Select_Sex_Men_Button_ClickListener);
        Select_Sex_Women_Button.setOnClickListener(Select_Sex_Women_Button_ClickListener);
        Submit_Button.setOnClickListener(Select_Submit_Button_ClickListener);

    }

    View.OnClickListener Select_Sex_Men_Button_ClickListener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Select_Sex_Women_Button.setBackgroundResource(R.drawable.choice_button);
            Select_Sex_Men_Button.setBackgroundResource(R.drawable.choice_button_pre);

            press_status = MEN;
        }
    };

    View.OnClickListener Select_Sex_Women_Button_ClickListener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Select_Sex_Men_Button.setBackgroundResource(R.drawable.choice_button);
            Select_Sex_Women_Button.setBackgroundResource(R.drawable.choice_button_pre);

            press_status = WOMEN;
        }
    };

    View.OnClickListener Select_Submit_Button_ClickListener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Submit()){
                finish();
            }
        }
    };

    private boolean Submit(){

        String id = String.valueOf(Id_EditText.getText());
        String pw = String.valueOf(Pw_EditText.getText());

        if(id.equals("")||id.equals("")){
            Worring_TextView.setText("입력이 올바르지 않습니다.");
            return false;
        }
        else if(press_status==NONE){
            Worring_TextView.setText("성별을 선택해 주십시오.");
            return false;
        }
        else{
            Worring_TextView.setText("");
            String sex;
            if(press_status==MEN){
                sex = "남자";
            }
            else{
                sex = "여자";
            }

            DB.Insert_User(id,pw,sex);
            return true;
        }
    }
}
