package org.androidtown.dbproject.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.androidtown.dbproject.Activity.ItemClickActivity;
import org.androidtown.dbproject.Adapter.TabPagerAdapter;
import org.androidtown.dbproject.ConnectDB;
import org.androidtown.dbproject.Object.Cloth;
import org.androidtown.dbproject.Object.User;
import org.androidtown.dbproject.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Fragment5 extends Fragment {
    ListView listview;
    private ArrayList<String> Cloth_Name_list;
    private ArrayAdapter adapter;

    private ConnectDB DB;

    private Handler adapter_handler=null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_fragment5, null);

        listview = (ListView) view.findViewById(R.id.listView);

        DB = new ConnectDB(getContext());
        ReFresh();


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Cloth cloth = DB.Select_User_Cloth_Name(Cloth_Name_list.get(position));
                Intent intent = new Intent(getContext(), ItemClickActivity.class);
                intent.putExtra("옷 이름", cloth.getName());
                intent.putExtra("대분류", cloth.getMain_category());
                intent.putExtra("소분류", cloth.getSub_category());

                startActivity(intent);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("목록에서 삭제");

                alertDialogBuilder.setMessage("삭제하시겠습니까?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(position >= 0 && position < Cloth_Name_list.size())
                        {
                            DB.Delete_User_Cloth(Cloth_Name_list.get(position));
                            Cloth_Name_list.remove(position);
                            adapter.notifyDataSetChanged();
                            send_handler_message();
                        }
                    }
                })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

                return true;
            }
        });

        return view;
    }

    public void setHandler(Handler adpater_handler){
        adapter_handler = adpater_handler;
    }

    public void ReFresh(){
        Cloth_Name_list = new ArrayList<String>();
        ArrayList<Cloth> list = DB.Select_User_Cloth(User.MainUser_Id,"기타");
        for(Cloth cloth : list){
            Cloth_Name_list.add(cloth.getName());
        }

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, Cloth_Name_list);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void send_handler_message(){
        if(adapter_handler!=null) {
            Message message = adapter_handler.obtainMessage();
            message.what = TabPagerAdapter.OTHER;
            adapter_handler.sendMessage(message);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ReFresh();
    }
};