package com.example.park;

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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {
//	static final LatLng NKUT = new LatLng(23.979548, 120.696745);
	static final LatLng NKUT = new LatLng( 25.060989456314484,121.61851456536581 );
    private GoogleMap map;
    String eachPark[];
    Marker park[]= new Marker[164];
    private Marker markerMe;
    
    /** GPS */
    private LocationManager locationMgr;
    private String provider;

    @Override
    protected void onStart() {
        super.onStart();

        if (initLocationProvider()) {
            whereAmI();
        }else{
        }
    }

    @Override
    protected void onStop() {
        locationMgr.removeUpdates(locationListener);
        super.onStop();
    }
    
    private boolean initLocationProvider() {
    	 locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	 if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
    		  provider = LocationManager.GPS_PROVIDER;
    		  return true;
    	 }
    	 return false;
    }
    
    private void updateWithNewLocation(Location location) {
    	 if (location != null) {
    	  //經度
    	  double lng = location.getLongitude();
    	  //緯度
    	  double lat = location.getLatitude();
    	  //速度
    	  float speed = location.getSpeed();
    	  //時間
    	  long time = location.getTime();

    	  //"我"
    	  showMarkerMe(lat, lng);
    	  cameraFocusOnMe(lat, lng);

    	 }
    	}
    
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
    	      Toast.makeText(MapActivity.this, "Status Changed: Out of Service", Toast.LENGTH_SHORT).show();
    	      break;
    	      case LocationProvider.TEMPORARILY_UNAVAILABLE:
    	      Toast.makeText(MapActivity.this, "Status Changed: Temporarily Unavailable", Toast.LENGTH_SHORT).show();
    	      break;
    	      case LocationProvider.AVAILABLE:
    	      Toast.makeText(MapActivity.this, "Status Changed: Available", Toast.LENGTH_SHORT).show();
    	      break;
    	  }
    	 }

    	};
    
    private void showMarkerMe(double lat, double lng){
//    	 if (markerMe != null) {
//    	  markerMe.remove();
//    	 }

    	 MarkerOptions markerOpt = new MarkerOptions();
    	 markerOpt.position(new LatLng(lat, lng));
    	 markerOpt.title("目前位置");
    	 markerMe = map.addMarker(markerOpt);

    	 Toast.makeText(this, "lat:" + lat + ",lng:" + lng, Toast.LENGTH_SHORT).show();
    	}
    
    private void cameraFocusOnMe(double lat, double lng){
    	 CameraPosition camPosition = new CameraPosition.Builder()
    	    .target(new LatLng(lat, lng))
    	    .zoom(16)
    	    .build();

    	 map.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
    	}
    
    GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
                Toast.makeText(MapActivity.this, "GPS_EVENT_STARTED", Toast.LENGTH_SHORT).show();
                break;
                case GpsStatus.GPS_EVENT_STOPPED:
                Toast.makeText(MapActivity.this, "GPS_EVENT_STOPPED", Toast.LENGTH_SHORT).show();
                break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                Toast.makeText(MapActivity.this, "GPS_EVENT_FIRST_FIX", Toast.LENGTH_SHORT).show();
                break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                break;
           }
        }
    };
    
    private void whereAmI(){
    	 //取得上次已知的位置
    	 Location location = locationMgr.getLastKnownLocation(provider);
    	 updateWithNewLocation(location);

    	 //GPS Listener
    	 locationMgr.addGpsStatusListener(gpsListener);

    	 //Location Listener
    	 int minTime = 5000;//ms
    	 int minDist = 5;//meter
    	 locationMgr.requestLocationUpdates(provider, minTime, minDist, locationListener);
    	 }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
		new AsyncTask<Void,Void,Void>(){
			String back = null;

			@Override
			protected Void doInBackground(Void... arg0) {
				String url = "http://140.116.96.88/allsearch.php";
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
			                 eachPark = responseBody.split("&");
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
//				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//				
//				for(int i=0;i<eachPark.length;i++){
//					String data[];
//					data = eachPark[i].split("#");
//					Marker nkut = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data[4]), Double.parseDouble(data[3]))).title("南開科技大學").snippet("數位生活創意系"));
//				}
//				String data[];
//				data = eachPark[0].split("#");
				for(int i=0;i<eachPark.length;i++){
//					Marker nkut = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data[4]), Double.parseDouble(data[3]))).title("查詢邊號:"+data[0].toString()).snippet("停車場名稱:"+data[1]+"\n地址:"+data[2]));
					String data[];
					data = eachPark[i].split("#");
					Marker nkut = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data[4]), Double.parseDouble(data[3]))).title("查詢邊號:"+data[0].toString()+" "+data[1]).snippet("地址:"+data[2]));
//					park[i]  = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data[4]), Double.parseDouble(data[3]))).title("查詢邊號:"+data[0].toString()).snippet("停車場名稱:"+data[1]+"\n地址:"+data[2]));
				}
				super.onPostExecute(result);
			}
			
		}.execute();
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
//        Marker nkut = map.addMarker(new MarkerOptions().position(NKUT).title("南開科技大學").snippet("數位生活創意系"));
        
        // Move the camera instantly to NKUT with a zoom of 16.
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(NKUT, 16));
        map.setOnInfoWindowClickListener(mapListener);
//        map.setOnMarkerClickListener(mapListener);
//		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(data[4]), Double.parseDouble(data[3])), 16));
    }
    
    GoogleMap.OnInfoWindowClickListener mapListener =  new GoogleMap.OnInfoWindowClickListener(){


		@Override
		public void onInfoWindowClick(Marker arg0) {
			Intent intent = new Intent();
			intent.setClass(MapActivity.this, ParkActivity.class);
			startActivity(intent);
		}
    	
    };

}
