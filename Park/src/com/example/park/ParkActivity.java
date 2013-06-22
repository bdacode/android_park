package com.example.park;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ParkActivity extends Activity {
	EditText mEditText;
	Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_park);
		findViews();
		mButton.setOnClickListener(btnListener);
	}
	
	private Button.OnClickListener btnListener = new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("search", mEditText.getText().toString());
			intent.putExtras(bundle);
			intent.setClass(ParkActivity.this, ShowParkActivity.class);
			startActivity(intent);
		}
	};
	
	private void findViews(){
		mEditText = (EditText)findViewById(R.id.editText1);
		mButton = (Button)findViewById(R.id.button1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.park, menu);
		return true;
	}

}
