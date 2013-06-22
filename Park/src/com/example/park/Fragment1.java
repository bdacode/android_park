package com.example.park;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class Fragment1 extends Fragment {
	 ImageButton searchButton;
//	 EditText searchText;

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    
	        
	 }
	 
	 private Button.OnClickListener btnListener = new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Fragment1.this.getActivity(), SearchActivity.class);
				startActivity(intent);
			}
	};
	 
	 private void setListeners(){
		 searchButton.setOnClickListener(btnListener);
	 }
	 

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	     Bundle savedInstanceState) {
	     // TODO Auto-generated method stub
		 View v = inflater.inflate(R.layout.activity_fragment1, container, false);
		 searchButton = (ImageButton)v.findViewById(R.id.imageButton);
//		 searchText = (EditText)v.findViewById(R.id.editText1);
		 
		 setListeners();
//	     return super.onCreateView(inflater, container, savedInstanceState);
	     return v;
	 }

}
