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


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ShowFragResultActivity extends Activity {
	TextView textView;
	TextView sumText;
	TextView addText;
	TextView telText;
	TextView payText;
	TextView sserText;
	TextView eserText;
	TextView totalText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_frag_result);
		
		Bundle bun =  this.getIntent().getExtras();
		final String message = bun.getString("search"); 
		
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
				textView.setText(back);
				super.onPostExecute(result);
			}
			
		}.execute();
		
		textView = (TextView)findViewById(R.id.textView1);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_frag_result, menu);
		return true;
	}

}
