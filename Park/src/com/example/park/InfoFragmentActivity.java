package com.example.park;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;

public class InfoFragmentActivity extends FragmentActivity {
	 private TabHost mTabHost;
	 private TabManager mTabManager;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_fragment);
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);
        
        mTabHost.setCurrentTab(0);//設定一開始就跳到第一個分頁
        mTabManager.addTab(
            mTabHost.newTabSpec("Fragment1").setIndicator("搜尋停車場"),
            Fragment1.class, null);
        mTabManager.addTab(
            mTabHost.newTabSpec("Fragment2").setIndicator("停車場列表"),
            Fragment2.class, null);
        
        DisplayMetrics dm = new DisplayMetrics();   
        getWindowManager().getDefaultDisplay().getMetrics(dm); //先取得螢幕解析度  
        int screenWidth = dm.widthPixels;   //取得螢幕的寬
           
           
        TabWidget tabWidget = mTabHost.getTabWidget();   //取得tab的物件
        int count = tabWidget.getChildCount();   //取得tab的分頁有幾個
        if (count > 0) {   
            for (int i = 0; i < count; i++) {   
                tabWidget.getChildTabViewAt(i)
                      .setMinimumWidth((screenWidth)/3);//設定每一個分頁最小的寬度   
            }   
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}
	
	class TabManager implements TabHost.OnTabChangeListener {
	    private final FragmentActivity mActivity;
	    private final TabHost mTabHost;
	    private final int mContainerId;
	    private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
	    TabInfo mLastTab;

	     final class TabInfo {
	        private final String tag;
	        private final Class<?> clss;
	        private final Bundle args;
	        private Fragment fragment;

	        TabInfo(String _tag, Class<?> _class, Bundle _args) {
	            tag = _tag;
	            clss = _class;
	            args = _args;
	        }
	    }

	     class DummyTabFactory implements TabHost.TabContentFactory {
	        private final Context mContext;

	        public DummyTabFactory(Context context) {
	            mContext = context;
	        }

	        @Override
	        public View createTabContent(String tag) {
	            View v = new View(mContext);
	            v.setMinimumWidth(0);
	            v.setMinimumHeight(0);
	            return v;
	        }
	    }

	    public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
	        mActivity = activity;
	        mTabHost = tabHost;
	        mContainerId = containerId;
	        mTabHost.setOnTabChangedListener(this);
	    }

	    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
	        tabSpec.setContent(new DummyTabFactory(mActivity));
	        String tag = tabSpec.getTag();

	        TabInfo info = new TabInfo(tag, clss, args);

	        info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
	        if (info.fragment != null && !info.fragment.isDetached()) {
	            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
	            ft.detach(info.fragment);
	            ft.commit();
	        }

	        mTabs.put(tag, info);
	        mTabHost.addTab(tabSpec);
	    }

	    @Override
	    public void onTabChanged(String tabId) {
	     TabInfo newTab = mTabs.get(tabId);

	        if (mLastTab != newTab) {
	            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
	            if (mLastTab != null) {
	                if (mLastTab.fragment != null) {
	                    ft.detach(mLastTab.fragment);
	                }
	            }

	            if (newTab != null) {
	                newTab.fragment = Fragment.instantiate(mActivity,
	                        newTab.clss.getName(), newTab.args);
	                ft.add(mContainerId, newTab.fragment, newTab.tag);
	                if (newTab.fragment == null) {
	                    ft.detach(mLastTab.fragment);
	                } else {
	                    mActivity.getSupportFragmentManager().popBackStack();
	                    ft.replace(mContainerId, newTab.fragment);
	                    ft.attach(newTab.fragment);
	                }
	            }
	            
	            mLastTab = newTab;
	            ft.commit();
	            mActivity.getSupportFragmentManager().executePendingTransactions();
	        }
	    
	    }
	}

}
