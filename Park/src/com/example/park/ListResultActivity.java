package com.example.park;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.TextView;

public class ListResultActivity extends Activity {
	TextView nameText;
	TextView sumText;
	TextView serviceText;
	TextView totalText;
	TextView telText;
	TextView payText;
	TextView addText;
	TextView realcarText;
	TextView realmotoText;
	String []data;
	Button mBtn;
	String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_result);
		
			
			Bundle bun =  this.getIntent().getExtras();
//			final String message = bun.getString("id"); 
			message = bun.getString("id"); 
			System.out.print(message);
			
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
//				                 back = responseBody;
//				                 System.out.print(back);
				                 data = responseBody.split("#");
				                 System.out.print(data[1]);
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
					if(data.length==11){
						nameText.setText(data[1]);
						totalText.setText("總車位數量:"+data[10]);
						sumText.setText("停車場介紹:"+data[2]);
						serviceText.setText("服務時間:"+data[6]+"-"+data[7]);
						payText.setText("停車費用:"+data[5]);
						addText.setText("停車場地址:"+data[3]);
						telText.setText("停車場電話:"+data[4]);
					}else{
						nameText.setText(data[1]);
						totalText.setText("總車位數量:"+data[9]);
						sumText.setText("停車場介紹:"+data[2]);
						serviceText.setText("服務時間:"+data[5]+"-"+data[6]);
						payText.setText("停車費用:"+data[4]);
						addText.setText("停車場地址:"+data[3]);
						telText.setText("停車場電話:--");
					}
//					nameText.setText(data[1]);
//					nameText.setText(data[1]);
					super.onPostExecute(result);
				}
				
			}.execute();
			
			findViews();
	}
	
	private void findViews(){
		nameText = (TextView)findViewById(R.id.name);
		totalText = (TextView)findViewById(R.id.total);
		sumText = (TextView)findViewById(R.id.sumText);
		serviceText = (TextView)findViewById(R.id.service);
		payText = (TextView)findViewById(R.id.pay);
		addText = (TextView)findViewById(R.id.address);
		telText = (TextView)findViewById(R.id.tel);
		mBtn = (Button)findViewById(R.id.button1);
//		realcarText = (TextView)findViewById(R.id.realcarText);
//		realmotoText = (TextView)findViewById(R.id.realmotoText);
		mBtn.setOnClickListener(btnListener);
	}
	
	private Button.OnClickListener btnListener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(ListResultActivity.this, ShowParkActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("search", message);
	    	intent.putExtras(bundle);
			startActivity(intent);
		}
		
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_result, menu);
		return true;
	}

}
