package com.example.park;

import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment2 extends ListFragment {
	
	String[] presidents = { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };
	String[] allData;
	
	HashMap<String,String> nameToid = new HashMap<String,String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        new AsyncTask<Void,Void,Void>(){
			String back = null;

			@Override
			protected Void doInBackground(Void... arg0) {
				String url = "http://140.116.96.88/search.php";
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
				String []listdata = new String[allData.length];
				int count = 0;
				for(String temp:allData){
					t = temp.split(",");
					listdata[count] = "查詢編號:"+t[0]+" "+t[1];
					count++;
				}
				 setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listdata));
				super.onPostExecute(result);
			}
			
		}.execute();

//        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, allData));
    }

    public void onListItemClick(ListView parent, View v, int position, long id) {
    	Intent intent = new Intent();
    	Bundle bundle = new Bundle();
    	String[] tem = new String[allData.length];
    	
    	for(int i=0;i<allData.length;i++){
    		tem[i] = allData[i].substring(0,(allData[i].indexOf(",")));
    	}
    	bundle.putString("id", tem[position]);
    	intent.putExtras(bundle);
    	intent.setClass(Fragment2.this.getActivity(), ListResultActivity.class);
    	startActivity(intent);
//        Toast.makeText(getActivity(), "您選擇項目是 : " + allData[position], Toast.LENGTH_SHORT).show();
    }


	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        return super.onCreateView(inflater, container, savedInstanceState);
	    }
}
