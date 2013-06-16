package com.example.park;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment2 extends ListFragment {

	String[] presidents = { "�@��", "�G��", "�T��", "�|��", "����", "����", "�C��", "�K��", "�E��", "�Q��", "�Q�@��", "�Q�G��" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, presidents));
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
        Toast.makeText(getActivity(), "�z��ܶ��جO : " + presidents[position], Toast.LENGTH_SHORT).show();
    }


	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        return super.onCreateView(inflater, container, savedInstanceState);
	    }
}
