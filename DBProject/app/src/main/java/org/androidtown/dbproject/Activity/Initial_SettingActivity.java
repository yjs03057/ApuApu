package org.androidtown.dbproject.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidtown.dbproject.ConnectDB;
import org.androidtown.dbproject.Object.User;
import org.androidtown.dbproject.R;

public class Initial_SettingActivity extends AppCompatActivity {

    private EditText Top_Name_EditText;
    private EditText Pants_Name_EditText;
    private Spinner Top_sub_category_Spinner;
    private Spinner Pants_sub_category_Spinner;
    private TextView Worring_TextView;
    private Button Submit_Button;

    private ArrayAdapter Top_sub_Adapter;
    private ArrayAdapter Pants_sub_Adapter;

    private ConnectDB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setting);

        DB = new ConnectDB(this);
        Top_Name_EditText = findViewById(R.id.initial_setting_top_edittext);
        Pants_Name_EditText = findViewById(R.id.initial_setting_pants_edittext);

        Top_sub_category_Spinner = findViewById(R.id.initial_setting_spinner_top_sub_category);
        Pants_sub_category_Spinner = findViewById(R.id.initial_setting_spinner_pants_sub_category);

        Worring_TextView = findViewById(R.id.initial_setting_worring_textview);

        Top_sub_Adapter = ArrayAdapter.createFromResource(this, R.array.sub_category1, android.R.layout.simple_spinner_dropdown_item);
        Top_sub_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Top_sub_category_Spinner.setAdapter(Top_sub_Adapter);

        Pants_sub_Adapter= ArrayAdapter.createFromResource(this, R.array.sub_category2, android.R.layout.simple_spinner_dropdown_item);
        Pants_sub_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Pants_sub_category_Spinner.setAdapter(Pants_sub_Adapter);

        Submit_Button = findViewById(R.id.initial_setting_submit_button);
        Submit_Button.setOnClickListener(Submit_Button_ClickListener);

    }

    private View.OnClickListener Submit_Button_ClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(setting()){
                Intent intent = new Intent();
                intent.putExtra("result",true);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    };

    private boolean setting(){

        String top = String.valueOf(Top_Name_EditText.getText());
        String pants = String.valueOf(Pants_Name_EditText.getText());
        String top_sub = Top_sub_category_Spinner.getSelectedItem().toString();
        String pants_sub = Pants_sub_category_Spinner.getSelectedItem().toString();

        if (top.equals("") || pants.equals("")) {
            Worring_TextView.setText("내용은 입력해주세요!");
            return false;
        } else {
            DB.Insert_User_Cloth(top,top_sub);
            DB.Insert_User_Cloth(pants,pants_sub);
            return true;

        }
    }
}
