package com.example.park;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends Activity {
	 Button searchButton;
	 EditText searchText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		 searchButton = (Button)findViewById(R.id.searchbutton);
		 searchText = (EditText)findViewById(R.id.searchtext);
		 
		 searchText.setFocusableInTouchMode(true);
		 searchText.setFocusable(true);
		 
		 setListeners();
	}
	
	private void setListeners(){
		 searchButton.setOnClickListener(btnListener);
	 }
	
	 private Button.OnClickListener btnListener = new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("search", searchText.getText().toString());
				intent.putExtras(bundle);
				intent.setClass(SearchActivity.this, ShowFragResultActivity.class);
				startActivity(intent);
			}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
