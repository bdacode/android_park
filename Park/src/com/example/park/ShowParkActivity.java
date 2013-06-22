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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class ShowParkActivity extends Activity {
	String[] target = new String[2];
	TextView moto; 
	TextView car;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_park);
		
		Bundle bun =  this.getIntent().getExtras();
		final String message = bun.getString("search"); 
		
		new AsyncTask<Void,Void,Void>(){
			String back = null;
			ArrayList<String> arr = new ArrayList<String>();

			@Override
			protected Void doInBackground(Void... arg0) {
				String url = "http://its.taipei.gov.tw/atis_index/data/get.aspx?xml=allavailable";
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
			                 String responseBody = EntityUtils.toString(resEntity,"big5"); //�o�̭n�[�W�s�X
			                 Document doc = Jsoup.parse(responseBody);
			     			 Elements links = doc.select("DATA>PARK");
			     			 
			     			 for(Element e:links){
//			     				System.out.println(e.text());
			     				System.out.println(e.text());
			     				arr.add(e.text().toString());
			     			 }
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
				try{
				for(int i=0;i<arr.size();i++){
					if(Integer.parseInt(message) == Integer.parseInt(arr.get(i).substring(0, 3))){
						String t[];
						t = arr.get(i).split(" ");
						if(t[1].equals("-9")){
							target[0] = "�ثe�T������:--";
						}else{
							target[0] = "�ثe�T������:"+t[1];
						}
						if(t[2].equals("-9")){
							target[1] = "�ثe��������:--";
						}else{
							target[1] = "�ثe��������:"+t[2];
						}
	     			}
				}
				}catch(Exception e){
					final AlertDialog alertDialog = getAlertDialog("�o�O�@�ӹ�ܮ�","�п��......");
					alertDialog.show();
					e.printStackTrace();
				}
				
				if(target[0] == null||target[1]==null){
//					moto.setText("�d�L���");
//					car.setText("�d�L���");
					final AlertDialog alertDialog = getAlertDialog("�䤣��z��J��������ID����T��","�Цb�������T���Φa�Ϥ����s�T�{�A���");
					alertDialog.show();
					moto.setText("�d�L���");
					car.setText("�d�L���");
				}
				
				moto.setText(target[1]);
				car.setText(target[0]);
				super.onPostExecute(result);
			}
			
		}.execute();
		
		moto = (TextView)findViewById(R.id.MOTO);
		car = (TextView)findViewById(R.id.CAR);
		
	}
	
	private AlertDialog getAlertDialog(String title,String message){
		        //���ͤ@��Builder����
		        Builder builder = new AlertDialog.Builder(ShowParkActivity.this);
		        //�]�wDialog�����D
		        builder.setTitle(title);
		        //�]�wDialog�����e
		        builder.setMessage(message);
		        //�]�wPositive���s���
		        builder.setPositiveButton("���s�d��", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		                //���U���s����ܧ���
//		                Toast.makeText(ShowParkActivity.this, "�z���UOK���s", Toast.LENGTH_SHORT).show();
		            	Intent intent = new Intent();
		            	intent.setClass(ShowParkActivity.this, ParkActivity.class);
		            	startActivity(intent);
		            }
		        });
		        //�]�wNegative���s���
//		        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//		            @Override
//		            public void onClick(DialogInterface dialog, int which) {
//		                //���U���s����ܧ���
//		                Toast.makeText(ShowParkActivity.this, "�z���UCancel���s", Toast.LENGTH_SHORT).show();
//		            }
//		        });
		        //�Q��Builder����إ�AlertDialog
		        return builder.create();
		    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_park, menu);
		return true;
	}

}
