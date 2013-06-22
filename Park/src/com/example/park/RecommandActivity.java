package com.example.park;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class RecommandActivity extends Activity {
	String[] allData;
	String[] listData;
	ListView list;
	
	private LocationManager locationMgr;
	private String provider;
	double lng ;
	 double lat;

	@Override
	protected void onStart() {
	    super.onStart();

	    if (initLocationProvider()) {
	        whereAmI();
	    }else{
	    }
	}

	private boolean initLocationProvider() {
		 locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		 if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			  provider = LocationManager.GPS_PROVIDER;
			  return true;
			 }
		 return false;
	}
	
	private void whereAmI(){
		 //取得上次已知的位置
		 Location location = locationMgr.getLastKnownLocation(provider);
		 //GPS Listener
		 locationMgr.addGpsStatusListener(gpsListener);
		 
		//經度
		  lng = location.getLongitude();
		  //緯度
		  lat = location.getLatitude();
		//Location Listener
		  int minTime = 5000;//ms
		  int minDist = 5;//meter
		  locationMgr.requestLocationUpdates(provider, minTime, minDist, locationListener);
		 }
	
	GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
	    @Override
	    public void onGpsStatusChanged(int event) {
	        switch (event) {
	            case GpsStatus.GPS_EVENT_STARTED:
	            Toast.makeText(RecommandActivity.this, "GPS_EVENT_STARTED", Toast.LENGTH_SHORT).show();
	            break;
	            case GpsStatus.GPS_EVENT_STOPPED:
	            Toast.makeText(RecommandActivity.this, "GPS_EVENT_STOPPED", Toast.LENGTH_SHORT).show();
	            break;
	            case GpsStatus.GPS_EVENT_FIRST_FIX:
	            Toast.makeText(RecommandActivity.this, "GPS_EVENT_FIRST_FIX", Toast.LENGTH_SHORT).show();
	            break;
	            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
	            break;
	       }
	    }
	};
	
	LocationListener locationListener = new LocationListener(){
		 @Override
		 public void onLocationChanged(Location location) {
		  updateWithNewLocation(location);
		 }

		 @Override
		 public void onProviderDisabled(String provider) {
		  updateWithNewLocation(null);
		 }

		 @Override
		 public void onProviderEnabled(String provider) {

		 }

		 @Override
		 public void onStatusChanged(String provider, int status, Bundle extras) {
		  switch (status) {
		      case LocationProvider.OUT_OF_SERVICE:
		      Toast.makeText(RecommandActivity.this, "Status Changed: Out of Service", Toast.LENGTH_SHORT).show();
		      break;
		      case LocationProvider.TEMPORARILY_UNAVAILABLE:
		      Toast.makeText(RecommandActivity.this, "Status Changed: Temporarily Unavailable", Toast.LENGTH_SHORT).show();
		      break;
		      case LocationProvider.AVAILABLE:
		      Toast.makeText(RecommandActivity.this, "Status Changed: Available", Toast.LENGTH_SHORT).show();
		      break;
		  }
		 }

		};
		
		private void updateWithNewLocation(Location location) {
			 String where = "";
			 if (location != null) {
			   lng = location.getLongitude();
			  //緯度
			   lat = location.getLatitude();
			  //速度
			 }
			}
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommand);
		
		new AsyncTask<Void,Void,Void>(){
			String back = null;

			@Override
			protected Void doInBackground(Void... arg0) {
				String url = "http://140.116.96.88/searchRecommand.php";
				try{
					HttpClient httpclient = new DefaultHttpClient();
			    	 
			         try {
			        	 
			             HttpPost httpPost = new HttpPost(url);
			  
//			             List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//			             nameValuePairs.add(new BasicNameValuePair("PARKID",message));
//			             httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
			  
			             System.out.println("executing request " + httpPost.getRequestLine());
			             HttpResponse response = httpclient.execute(httpPost);
			             HttpEntity resEntity = response.getEntity();
			  
			             System.out.println("----------------------------------------");
			             System.out.println(response.getStatusLine());
			             if (resEntity != null) {
			                 String responseBody = EntityUtils.toString(resEntity,"big5"); //這裡要加上編碼
			                 back = responseBody;
			                 allData = back.split("#");
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
				String []t = null; 
				listData = new String[8];
				int count = 0;
				double distance[] = new double[allData.length];
				HashMap<Double,String> g = new HashMap<Double,String>();
				
				for(String temp:allData){
					t = temp.split(",");
					double y = Double.parseDouble(t[3]);
					double x = Double.parseDouble(t[2]);
					
					distance[count] = Math.sqrt(Math.pow((lng-x),2)+Math.pow((lat-y),2));
					g.put(distance[count], "查詢編號:"+t[0]+" "+t[1]);
					count++;
				}
				
				Arrays.sort(distance);
				
				for(int i=0;i<8;i++){
					listData[i] = g.get(distance[i]);
				}
				
//				for(String temp:allData){
//					t = temp.split(",");
//					String te = "查詢編號:"+t[0]+" "+t[1];
//					listData[count] = te;
//					count++;
//				}
				list.setAdapter(new ArrayAdapter<String>(RecommandActivity.this, android.R.layout.simple_list_item_1, listData));
				super.onPostExecute(result);
			}
			
		}.execute();
		
		list = (ListView)findViewById(R.id.listView1);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent();
		    	Bundle bundle = new Bundle();
		    	String[] tem = new String[8];
		    	
		    	for(int i=0;i<listData.length;i++){
		    		tem[i] = listData[i].substring(5,(listData[i].indexOf(" ")));
		    	}
		    	bundle.putString("id", tem[position]);
		    	intent.putExtras(bundle);
		    	intent.setClass(RecommandActivity.this, ListResultActivity.class);
		    	startActivity(intent);
				
			}
		});

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recommand, menu);
		return true;
	}

}
