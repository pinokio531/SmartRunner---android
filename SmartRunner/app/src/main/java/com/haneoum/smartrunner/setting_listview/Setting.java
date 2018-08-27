package com.haneoum.smartrunner.setting_listview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.haneoum.smartrunner.R;

public class Setting extends Fragment {

    //임승섭, 김성민

    public Setting() {
    }

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);

        final String[] list_menu = {"경고음 설정", "설명서", "회원정보"};

        ListView listView = (ListView) view.findViewById(R.id.list_menu);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_menu);

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                adapterView.getItemAtPosition(position);
                String c_list = list_menu[position];
                if (position == 0){
                    Intent intent;
                    intent = new Intent(getActivity(), Warning.class);
                    intent.putExtra("경고음 설정", c_list);
                    startActivity(intent);
                }
                else if (position == 1){
                    Intent intent;
                    intent = new Intent(getActivity(), Instruction.class);
                    intent.putExtra("설명서", c_list);
                    startActivity(intent);
                }
                else if (position == 2){
                    Intent intent;
                    intent = new Intent(getActivity(), Logout.class);
                    intent.putExtra("로그아웃", c_list);
                    startActivity(intent);
                }

            }
        });
        return view;
    }
}
