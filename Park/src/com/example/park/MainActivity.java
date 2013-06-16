package com.example.park;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	private ImageButton infoButton;
	private ImageButton parkButton;
	private ImageButton mapButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setViews();
		setListeners();
		
	}
	
	protected void setViews(){
		infoButton = (ImageButton)findViewById(R.id.parkButton);
		parkButton = (ImageButton)findViewById(R.id.infoButton);
		mapButton = (ImageButton)findViewById(R.id.mapButton);
	}
	
	protected void setListeners(){
		infoButton.setOnClickListener(infoListener);
		parkButton.setOnClickListener(parkListener);
		mapButton.setOnClickListener(mapListener);
	}
	
	private ImageButton.OnClickListener infoListener = new ImageButton.OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, InfoFragmentActivity.class);
			startActivity(intent);
		}
		
	};
	
	private ImageButton.OnClickListener parkListener = new ImageButton.OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ParkActivity.class);
			startActivity(intent);
		}
		
	};
	
	private ImageButton.OnClickListener mapListener = new ImageButton.OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, MapActivity.class);
			startActivity(intent);
		}
		
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
