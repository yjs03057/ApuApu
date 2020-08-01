package org.androidtown.dbproject.Activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidtown.dbproject.ConnectDB;
import org.androidtown.dbproject.R;


public class ItemAddActivity extends Activity
{
    ArrayAdapter main_adapter;
    ArrayAdapter sub_adapter;
    String cloth_name;
    String choice_main;
    String choice_sub;
    int cnt;

    private ConnectDB DB;

    protected void onCreate(Bundle savedInstanceState) {
        cnt = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);
        DB = new ConnectDB(this);

        ImageButton btn = (ImageButton) findViewById(R.id.button2);

        TextView text1 = (TextView) findViewById(R.id.textView1);
        TextView text3 = (TextView) findViewById(R.id.main_category);
        TextView text4 = (TextView) findViewById(R.id.sub_category);

        final EditText edit1 = (EditText) findViewById(R.id.editText1);

        Button btn2 = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edit1.getText().toString().length()!=0)
                {
                    cloth_name = edit1.getText().toString();
                    cnt++;
                }

                if(cnt==3)
                {
                    if(cloth_name.equals("")||choice_sub.equals("")){
                        //worring event
                    }else{
                        DB.Insert_User_Cloth(cloth_name,choice_sub);
                        finish();
                    }
                }
            }
        });

        Spinner main_cate = (Spinner) findViewById(R.id.spinner_main_category);
        final Spinner sub_cate = (Spinner) findViewById(R.id.spinner_sub_category);
        main_adapter = ArrayAdapter.createFromResource(this, R.array.main_category,
                android.R.layout.simple_spinner_dropdown_item);
        main_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        main_cate.setAdapter(main_adapter);

        main_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(main_adapter.getItem(position).equals("상의"))
                {
                    choice_main = "상의";
                    sub_adapter = ArrayAdapter.createFromResource(ItemAddActivity.this, R.array.sub_category1,
                            android.R.layout.simple_spinner_item);
                    sub_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sub_cate.setAdapter(sub_adapter);

                    sub_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_sub = sub_adapter.getItem(position).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    cnt += 2;
                }
                else if(main_adapter.getItem(position).equals("하의"))
                {
                    choice_main = "하의";
                    sub_adapter = ArrayAdapter.createFromResource(ItemAddActivity.this, R.array.sub_category2,
                            android.R.layout.simple_spinner_item);
                    sub_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sub_cate.setAdapter(sub_adapter);

                    sub_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_sub = sub_adapter.getItem(position).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    cnt += 2;
                }
                else if(main_adapter.getItem(position).equals("아우터"))
                {
                    choice_main = "아우터";
                    sub_adapter = ArrayAdapter.createFromResource(ItemAddActivity.this, R.array.sub_category3,
                            android.R.layout.simple_spinner_item);
                    sub_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sub_cate.setAdapter(sub_adapter);

                    sub_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_sub = sub_adapter.getItem(position).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    cnt += 2;
                }
                else if(main_adapter.getItem(position).equals("기타"))
                {
                    choice_main = "기타";
                    sub_adapter = ArrayAdapter.createFromResource(ItemAddActivity.this, R.array.sub_category4,
                            android.R.layout.simple_spinner_item);
                    sub_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sub_cate.setAdapter(sub_adapter);

                    sub_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_sub = sub_adapter.getItem(position).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    cnt += 2;
                }
                else
                {
                    sub_adapter = ArrayAdapter.createFromResource(ItemAddActivity.this, R.array.empty_category,
                            android.R.layout.simple_spinner_item);
                    sub_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sub_cate.setAdapter(sub_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}