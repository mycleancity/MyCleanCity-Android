package com.sbs.android.cleanmycity;

import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.RegisterResultDTO;
import com.sbs.android.cleanmycity.model.ResultDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity  extends Activity {

	private ProgressDialog pd;
	private EditText emailEditText;
	private EditText passwordEditText;
	private Button loginButton;
	private TextView registerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().hide();
		setContentView(R.layout.activity_login);
		
		AppCache.init(getApplicationContext());
		
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		loginButton = (Button) findViewById(R.id.loginButton);
		registerButton = (TextView) findViewById(R.id.registerButton);
		
		registerButton.setPaintFlags(registerButton.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(emailEditText.getText() == null || emailEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(LoginActivity.this, "Email Address", "Email address is required for login");
				}
				else if(passwordEditText.getText() == null || passwordEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(LoginActivity.this, "Password", "Password is required for login");
				}
				else
				{
					pd = ProgressDialog.show(LoginActivity.this,"","Please wait...",true, false);
					callEmailLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
				}
			}
		});
		
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		    	startActivity(intent);
		    	
			}
		});
		
		if(AppCache.getString(AppCache.EMAIL_ADDRESS_PREF) != null 
				&& AppCache.getString(AppCache.EMAIL_ADDRESS_PREF).length() > 0)
		{
			//callEmailLogin(AppCache.getString(AppCache.EMAIL_ADDRESS_PREF), AppCache.getString(AppCache.PASSWORD_PREF));
			Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
	    	startActivity(intent);
	    	finish();
		}
	}
	
	private void callEmailLogin(final String username, final String password)
	{
		AppAPI.authenticate(username, password,
				new DataHandler<RegisterResultDTO>(RegisterResultDTO.class) {

					@Override
					public void onSuccess(RegisterResultDTO data) {
						
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						if(data.user != null)
						{
														
							AppCache.saveUserData(data.user.name, username, password, data.user.ID);
							
							Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
					    	startActivity(intent);
					    	finish();
						}
						else
						{
							Util.showAlertDialog(LoginActivity.this, "Error", "Login failed, kindly try again.");
						}
						
					}

					@Override
					public void onFailure(RegisterResultDTO data) {
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						Util.showAlertDialog(LoginActivity.this, "Error", "Login failed, kindly try again.");
					}
			});
	}
	
}
