package com.android.hospital.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.hospital.ui.activity.LocalTempEmrActivity;
import com.android.hospital.ui.activity.R;

/**
 * Created by WHL on 2015/2/9.
 */
public class EmrFragment extends Fragment {
    
    private ListView mListView;
    
    private String[] items={"电子胃镜知情同意书","病程记录","入院记录"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emr_list,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView= (ListView) getView().findViewById(R.id.list_view);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,items);
        mListView.setAdapter(adapter);
        
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), LocalTempEmrActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
}
