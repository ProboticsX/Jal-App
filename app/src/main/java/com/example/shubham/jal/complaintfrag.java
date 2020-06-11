package com.example.shubham.jal;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class complaintfrag extends Fragment {

    ListView list;

    public static String[]abc={"shubham","jain","cool","1","2","3"};

    public complaintfrag() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.fragment_complaintfrag, container, false);
        complaint_list adapter = new complaint_list(getActivity(),abc);

        list = v.findViewById(R.id.complaint_list2);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(l==0){
                    Intent intent=new Intent(getActivity(),wrongbill.class);
                    startActivity(intent);
                }
                if(l==1){
                    Intent intent=new Intent(getActivity(),watermeter.class);
                    startActivity(intent);
                }
                if(l==4){
                    Intent intent=new Intent(getActivity(),nowater.class);
                    startActivity(intent);
                }

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

}
