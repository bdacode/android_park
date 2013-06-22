package com.example.park;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ShowFragResultActivity extends Activity {
	TextView nameText;
	TextView sumText;
	TextView addText;
	TextView telText;
	TextView payText;
	TextView serText;
	TextView totalText;
	Button mBtn;
	String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_frag_result);
		
		Bundle bun =  this.getIntent().getExtras();
//		final String message = bun.getString("search"); 
		message = bun.getString("search"); 
		
		new AsyncTask<Void,Void,Void>(){
			String back = null;

			@Override
			protected Void doInBackground(Void... arg0) {
				String url = "http://140.116.96.88/index.php";
				try{
					HttpClient httpclient = new DefaultHttpClient();
			    	 
			         try {
			        	 
			             HttpPost httpPost = new HttpPost(url);
			  
			             List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			             nameValuePairs.add(new BasicNameValuePair("PARKID",message));
			             httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
			  
			             System.out.println("executing request " + httpPost.getRequestLine());
			             HttpResponse response = httpclient.execute(httpPost);
			             HttpEntity resEntity = response.getEntity();
			  
			             System.out.println("----------------------------------------");
			             System.out.println(response.getStatusLine());
			             if (resEntity != null) {
			                 String responseBody = EntityUtils.toString(resEntity,"big5"); //這裡要加上編碼
			                 back = responseBody;
			                 System.out.print(back);
			             }
			             
			         } 
			         catch (Exception e) {
			             System.out.println(e);
			         }
					
				}catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}
			
			protected void onPostExecute(Void result){
//				textView.setText(back);
				String data[];
				data = back.split("#");
				if(data.length==11){
					nameText.setText(data[1]);
					totalText.setText("總車位數量:"+data[10]);
					sumText.setText("停車場介紹:"+data[2]);
					serText.setText("服務時間:"+data[6]+"-"+data[7]);
					payText.setText("停車費用:"+data[5]);
					addText.setText("停車場地址:"+data[3]);
					telText.setText("停車場電話:"+data[4]);
				}else{
					nameText.setText(data[1]);
					totalText.setText("總車位數量:"+data[9]);
					sumText.setText("停車場介紹:"+data[2]);
					serText.setText("服務時間:"+data[5]+"-"+data[6]);
					payText.setText("停車費用:"+data[4]);
					addText.setText("停車場地址:"+data[3]);
					telText.setText("停車場電話:--");
				}
				mBtn.setOnClickListener(btnListener);
				super.onPostExecute(result);
			}
			
		}.execute();
		
		nameText = (TextView)findViewById(R.id.name);
		sumText = (TextView)findViewById(R.id.sumText);
		 addText = (TextView)findViewById(R.id.address);
		 telText = (TextView)findViewById(R.id.tel);
		 payText = (TextView)findViewById(R.id.pay);
		 serText = (TextView)findViewById(R.id.service);
		 totalText = (TextView)findViewById(R.id.total);
		mBtn = (Button)findViewById(R.id.button1);
	}
	
	private Button.OnClickListener btnListener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(ShowFragResultActivity.this, ShowParkActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("search", message);
	    	intent.putExtras(bundle);
			startActivity(intent);
		}
		
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_frag_result, menu);
		return true;
	}

}
