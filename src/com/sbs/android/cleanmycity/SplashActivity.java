package com.sbs.android.cleanmycity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//getActionBar().hide();
		setContentView(R.layout.activity_splash);
		
		Handler handler=new Handler();

		final Runnable r = new Runnable()
		{
		    public void run() 
		    {
		    	Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			startActivity(intent);
    			finish();
		    }
		};

		handler.postDelayed(r, 200);
	}
	
}