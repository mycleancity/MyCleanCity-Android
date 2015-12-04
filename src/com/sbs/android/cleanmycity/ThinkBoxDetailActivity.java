package com.sbs.android.cleanmycity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.sbs.android.cleanmycity.adapter.ZoneSpinnerAdapter;
import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.ReportDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxSupportDTO;
import com.sbs.android.cleanmycity.model.ZoneDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Constants;
import com.sbs.android.cleanmycity.utils.Util;
import com.squareup.picasso.Picasso;

public class ThinkBoxDetailActivity extends Activity{

	//private ScrollView scrollView;
	private ImageView feedImageView;
	private TextView titleValueLabel;
	private TextView descValueLabel;
	private TextView categoryValueLabel;
	private TextView feasibilityValueLabel;
	private TextView zoneValueLabel;
	private TextView nameValueLabel;
	private TextView dateValueLabel;
	
	private ImageView commentImageView;
	private ImageView shareImageView;
	private TextView commentLabel;
	private TextView shareLabel;
	
	private TextView statusValueLabel;
	private TextView supporterValueLabel;
	private RelativeLayout feedStatusView;
	private RoundCornerProgressBar progressLabel;
	private TextView statusTextLabel;
	private TextView statusTextValueLabel;
	
	private TextView preApproveStatusLabel;
	private TextView preApproveStatusValue;
	
	private RelativeLayout feedSupportView;
	private TextView supporterTitle;
	private EditText nameEditText;
	private EditText emailEditText;
	private EditText mobileEditText; 
	
	private Button submitButton;
	private Button suppoterButton;
	
	private ProgressDialog pd;
	
	@Override
	protected void onResume()
	{
		super.onResume();
		commentLabel.setText(AppCache.selectedThinkBoxDTO.commentCount + " Comments");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thinkbox_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("ThinkBox Details");
		
		feedImageView = (ImageView) findViewById(R.id.feedImageView);
		titleValueLabel = (TextView) findViewById(R.id.titleValueLabel);
		descValueLabel = (TextView) findViewById(R.id.descValueLabel);
		categoryValueLabel = (TextView) findViewById(R.id.categoryValueLabel);
		feasibilityValueLabel = (TextView) findViewById(R.id.feasibilityValueLabel);
		zoneValueLabel = (TextView) findViewById(R.id.zoneValueLabel);
		nameValueLabel = (TextView) findViewById(R.id.nameValueLabel);
		dateValueLabel = (TextView) findViewById(R.id.dateValueLabel);
		commentImageView = (ImageView) findViewById(R.id.commentImageView);
		commentLabel = (TextView) findViewById(R.id.commentLabel);
		shareImageView = (ImageView) findViewById(R.id.shareImageView);
		shareLabel = (TextView) findViewById(R.id.shareLabel);
		statusValueLabel = (TextView) findViewById(R.id.statusValueLabel);
		progressLabel = (RoundCornerProgressBar) findViewById(R.id.progressLabel);
		supporterValueLabel = (TextView) findViewById(R.id.supporterValueLabel);
		feedStatusView = (RelativeLayout) findViewById(R.id.feedStatusView);
		statusTextLabel = (TextView) findViewById(R.id.statusTextLabel);
		statusTextValueLabel = (TextView) findViewById(R.id.statusTextValueLabel);
		submitButton = (Button) findViewById(R.id.submitButton);
		suppoterButton = (Button) findViewById(R.id.suppoterButton);
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		mobileEditText = (EditText) findViewById(R.id.mobileEditText);
		feedSupportView = (RelativeLayout) findViewById(R.id.feedSupportView);
		feedStatusView = (RelativeLayout) findViewById(R.id.feedStatusView);
		preApproveStatusLabel = (TextView) findViewById(R.id.preApproveStatusLabel); 
		preApproveStatusValue = (TextView) findViewById(R.id.preApproveStatusValue); 
		
		
		
		if(AppCache.getString(AppCache.FULLNAME_PREF).length() > 0)
		{
			nameEditText.setText(AppCache.getString(AppCache.FULLNAME_PREF));
		}
		if(AppCache.getString(AppCache.EMAIL_ADDRESS_PREF).length() > 0)
		{
			emailEditText.setText(AppCache.getString(AppCache.EMAIL_ADDRESS_PREF));
		}
		if(AppCache.getString(AppCache.MOBILENO_PREF).length() > 0)
		{
			mobileEditText.setText(AppCache.getString(AppCache.MOBILENO_PREF));
		}
		
		commentImageView.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent intent = new Intent(ThinkBoxDetailActivity.this, CommentActivity.class);
    			intent.putExtra("type", Constants.THINKBOX);
    			startActivity(intent);
    		}
    	});

    	commentLabel.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			Intent intent = new Intent(ThinkBoxDetailActivity.this, CommentActivity.class);
    			intent.putExtra("type", Constants.THINKBOX);
    			startActivity(intent);
    		}
    	});
    	
    	shareImageView.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, Constants.SERVER_URL + "/thinkbox?id="+AppCache.selectedThinkBoxDTO.ID);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent, "Share"));
                
    			
    		}
    	});

    	shareLabel.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, Constants.SERVER_URL + "/thinkbox?id="+AppCache.selectedThinkBoxDTO.ID);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent, "Share"));
    		}
    	});
    	
    	suppoterButton.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			Intent intent = new Intent(ThinkBoxDetailActivity.this, ListingActivity.class);
				intent.putExtra("type", Constants.THINKBOX_SUPPORT);
		    	startActivity(intent);
    		}
    	});
    	
    	submitButton.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			if(nameEditText.getText() == null || nameEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ThinkBoxDetailActivity.this, "Full Name", "Full name is required for submission.");
				}
				else if(emailEditText.getText() == null || emailEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ThinkBoxDetailActivity.this, "Email Address", "Email address is required for submission.");
				}
				else if(mobileEditText.getText() == null || mobileEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ThinkBoxDetailActivity.this, "Mobile Number", "Mobile number is required for submission.");
				}
				else
				{
					callSupportThinkBox(nameEditText.getText().toString(), emailEditText.getText().toString(), mobileEditText.getText().toString());
				}
    		}
    	});
		
		if(AppCache.selectedThinkBoxDTO != null)
		{
			titleValueLabel.setText(AppCache.selectedThinkBoxDTO.title);
			descValueLabel.setText(AppCache.selectedThinkBoxDTO.description);
			categoryValueLabel.setText(AppCache.selectedThinkBoxDTO.category.name.toUpperCase());
			
			
			nameValueLabel.setText(AppCache.selectedThinkBoxDTO.contactName); 
			dateValueLabel.setText(AppCache.selectedThinkBoxDTO.create_date); 
			
			commentLabel.setText(AppCache.selectedThinkBoxDTO.commentCount + " Comments");
			
			if(AppCache.selectedThinkBoxDTO.photo > 0)
			{
				Picasso.with(this)
				  .load(Constants.IMAGE_URL + "/400/" + AppCache.selectedThinkBoxDTO.photo)
				  .into(feedImageView);
			}
			else
			{
				feedImageView.setVisibility(View.GONE);
			}
			
			if(AppCache.selectedThinkBoxDTO.supported)
			{
				feedSupportView.setVisibility(View.GONE);
			}
			
			
			feasibilityValueLabel.setText(getResources().getStringArray(R.array.feasibility_arrays)[AppCache.selectedThinkBoxDTO.feasibility - 1]);
			
			if(AppCache.selectedThinkBoxDTO.zone != null)
			zoneValueLabel.setText(AppCache.selectedThinkBoxDTO.zone.name); 
			
			
			
			statusValueLabel.setText("TOTAL SIGNATURE: "+AppCache.selectedThinkBoxDTO.supportCount);
			supporterValueLabel.setText("SIGNATURE REQUIRED: "+AppCache.selectedThinkBoxDTO.kickoffCount);
			
			float progress = (float)AppCache.selectedThinkBoxDTO.supportCount/(float)AppCache.selectedThinkBoxDTO.kickoffCount;
			Util.log("progress: "+progress);
			progressLabel.setProgress(progress);
			
			//Check Status
			if(AppCache.selectedThinkBoxDTO.status < 1) //Pending Moderation
			{
				feedStatusView.setVisibility(View.GONE);
				feedSupportView.setVisibility(View.GONE);
				preApproveStatusValue.setText(AppCache.selectedThinkBoxDTO.statusDisplay);
			}
			else
			{
				preApproveStatusValue.setText(AppCache.selectedThinkBoxDTO.statusDisplay);
			}
			
			
		}
		else
		{
			finish();
		}
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        //NavUtils.navigateUpFromSameTask(this);
	    	finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	private void callSupportThinkBox(String fullname, String email, String mobileNo)
	{
		AppCache.putString(AppCache.FULLNAME_PREF, fullname);
		AppCache.putString(AppCache.MOBILENO_PREF, mobileNo);
		
		AppAPI.supportThinkBoxIdea(fullname, email, mobileNo, AppCache.selectedThinkBoxDTO.ID, 
				new DataHandler<ThinkBoxSupportDTO>(ThinkBoxSupportDTO.class) {

							@Override
					public void onSuccess(ThinkBoxSupportDTO data) {
						
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						if(data != null && data.support != null)
						{
							feedSupportView.setVisibility(View.GONE);
							
							AppCache.selectedThinkBoxDTO.supportCount = AppCache.selectedThinkBoxDTO.supportCount + 1;
							AppCache.selectedThinkBoxDTO.supported = true;
							
							statusValueLabel.setText("TOTAL SIGNATURE: "+AppCache.selectedThinkBoxDTO.supportCount);
							float progress = (float)AppCache.selectedThinkBoxDTO.supportCount/(float)AppCache.selectedThinkBoxDTO.kickoffCount;
							Util.log("progress: "+progress);
							progressLabel.setProgress(progress);
							
							//Util.showAlertDialog(ComplaintActivity.this, "Completed", "Complaint submitted. Thanks for your submission.");
							ContextThemeWrapper ctw = new ContextThemeWrapper(ThinkBoxDetailActivity.this, android.R.style.Theme_Holo_Light_Dialog );
							AlertDialog.Builder dialogResult = new AlertDialog.Builder(ctw);
							dialogResult.setTitle("Completed");
							dialogResult.setIcon(R.drawable.ic_launcher);
							dialogResult.setMessage("Support submitted. Thanks for your submission.");
							dialogResult.setNeutralButton("Ok", new DialogInterface.OnClickListener() {	
								public void onClick(DialogInterface dialogResult, int id) {
					        	  dialogResult.dismiss();
					        	 
								}
							});
							dialogResult.show();
						}
						else
						{
							Util.showAlertDialog(ThinkBoxDetailActivity.this, "Error", "Submission failed, kindly try again.");
						}
						
					}

					@Override
					public void onFailure(ThinkBoxSupportDTO data) {
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						Util.showAlertDialog(ThinkBoxDetailActivity.this, "Error", "Submission failed, kindly try again.");
					}
			});
	}
}