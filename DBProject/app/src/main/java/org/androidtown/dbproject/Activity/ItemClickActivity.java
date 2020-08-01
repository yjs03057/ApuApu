package org.androidtown.dbproject.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.androidtown.dbproject.ConnectDB;
import org.androidtown.dbproject.Fragment.Fragment1;
import org.androidtown.dbproject.R;

public class ItemClickActivity extends Activity
{
    private ConnectDB DB;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_click);
        DB = new ConnectDB(this);

        TextView clothname = (TextView) findViewById(R.id.textView1);
        TextView main_cate = (TextView) findViewById(R.id.textView2);
        TextView sub_cate = (TextView) findViewById(R.id.textView3);
        Button button = (Button) findViewById(R.id.button);

        ImageButton btn = (ImageButton) findViewById(R.id.button2);

        final Intent intent = getIntent();
        clothname.setText("옷 이름: " + intent.getStringExtra("옷 이름"));
        main_cate.setText("대분류: " + intent.getStringExtra("대분류"));
        sub_cate.setText("소분류: " + intent.getStringExtra("소분류"));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(ItemClickActivity.this);

                dlg.setTitle("목록에서 삭제");
                dlg.setMessage("삭제 하시겠습니까?");
                dlg.setCancelable(false);
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DB.Delete_User_Cloth(intent.getStringExtra("옷 이름"));
                        finish();
                    }
                });
                dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = dlg.create();
                dialog.show();
            }
        });
    }
}