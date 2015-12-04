package com.sbs.android.cleanmycity;

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

import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.RegisterResultDTO;
import com.sbs.android.cleanmycity.model.ResultDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Util;

public class RegisterActivity extends Activity {

	private ProgressDialog pd;
	private EditText emailEditText;
	private EditText passwordEditText;
	private EditText passwordEditText2;
	private EditText fullnameEditText;
	private TextView backToLoginButton;
	private Button registerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().hide();
		setContentView(R.layout.activity_register);
		
		AppCache.init(getApplicationContext());
		
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		passwordEditText2 = (EditText) findViewById(R.id.passwordEditText2);
		fullnameEditText = (EditText) findViewById(R.id.fullnameEditText);
		backToLoginButton = (TextView) findViewById(R.id.backToLoginButton);
		registerButton = (Button) findViewById(R.id.registerButton);
		
		backToLoginButton.setPaintFlags(registerButton.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
		
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(fullnameEditText.getText() == null || fullnameEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(RegisterActivity.this, "Fullname", "Fullname is required for register");
				}
				else if(emailEditText.getText() == null || emailEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(RegisterActivity.this, "Email Address", "Email address is required for register");
				}
				else if(passwordEditText.getText() == null || passwordEditText.getText().toString().length() == 0
						|| passwordEditText2.getText() == null || passwordEditText2.getText().toString().length() == 0
						|| !passwordEditText.getText().toString().equalsIgnoreCase(passwordEditText2.getText().toString()))
				{
					Util.showAlertDialog(RegisterActivity.this, "Password", "Password doesn't match. Kindly try again.");
				}
				else
				{
					pd = ProgressDialog.show(RegisterActivity.this,"","Please wait...",true, false);
					callRegister(fullnameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString());
				}
			}
		});
		
		backToLoginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	
	private void callRegister(final String fullname, final String username, final String password)
	{
		AppAPI.register(username, password,fullname,
				new DataHandler<RegisterResultDTO>(RegisterResultDTO.class) {

					@Override
					public void onSuccess(RegisterResultDTO data) {
						
						
						if(data.user != null)
						{
							callEmailLogin(username, password);
						}
						else
						{
							if(pd != null)
			            	{
			            		try{ pd.dismiss(); } catch (Exception ex){}
			            	}
							
							Util.showAlertDialog(RegisterActivity.this, "Error", "Register failed, kindly try again.");
						}
						
					}

					@Override
					public void onFailure(RegisterResultDTO data) {
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						Util.showAlertDialog(RegisterActivity.this, "Error", "Register failed, kindly try again.");
					}
			});
		
		
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
							
							Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
					    	startActivity(intent);
					    	finish();
						}
						else
						{
							Util.showAlertDialog(RegisterActivity.this, "Error", "Login failed, kindly try again.");
						}
						
					}

					@Override
					public void onFailure(RegisterResultDTO data) {
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						Util.showAlertDialog(RegisterActivity.this, "Error", "Login failed, kindly try again.");
					}
			});
	}
	
}