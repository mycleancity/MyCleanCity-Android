package com.sbs.android.cleanmycity;

import java.io.File;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.ReportDTO;
import com.sbs.android.cleanmycity.model.ResultDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Constants;
import com.sbs.android.cleanmycity.utils.Util;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ComplaintDetailActivity  extends Activity{

	//private ScrollView scrollView;
	private ImageView feedImageView;
	private TextView refLabel;
	private TextView refValueLabel;
	private TextView titleValueLabel;
	private TextView descValueLabel;
	private TextView categoryValueLabel;
	private TextView addressValueLabel;
	private TextView nameValueLabel;
	private TextView dateValueLabel;
	
	private ImageView commentImageView;
	private ImageView shareImageView;
	private TextView commentLabel;
	private TextView shareLabel;
	
	private TextView statusValueLabel;
	private TextView dayLeftValueLabel;
	private RelativeLayout feedStatusView;
	private RoundCornerProgressBar progressLabel;
	private TextView statusTextLabel;
	private TextView statusTextValueLabel;
	
	private Button deleteButton;
	
	private GoogleMap map;
	
	private ProgressDialog pd;
	
	@Override
	protected void onResume()
	{
		super.onResume();
		commentLabel.setText(AppCache.selectedReportDTO.commentCount + " Comments");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complaint_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Complaint Details");
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		feedImageView = (ImageView) findViewById(R.id.feedImageView);
		refLabel = (TextView) findViewById(R.id.refLabel);
		refValueLabel = (TextView) findViewById(R.id.refValueLabel);
		titleValueLabel = (TextView) findViewById(R.id.titleValueLabel);
		descValueLabel = (TextView) findViewById(R.id.descValueLabel);
		categoryValueLabel = (TextView) findViewById(R.id.categoryValueLabel);
		addressValueLabel = (TextView) findViewById(R.id.addressValueLabel);
		nameValueLabel = (TextView) findViewById(R.id.nameValueLabel);
		dateValueLabel = (TextView) findViewById(R.id.dateValueLabel);
		commentImageView = (ImageView) findViewById(R.id.commentImageView);
		commentLabel = (TextView) findViewById(R.id.commentLabel);
		shareImageView = (ImageView) findViewById(R.id.shareImageView);
		shareLabel = (TextView) findViewById(R.id.shareLabel);
		statusValueLabel = (TextView) findViewById(R.id.statusValueLabel);
		progressLabel = (RoundCornerProgressBar) findViewById(R.id.progressLabel);
		dayLeftValueLabel = (TextView) findViewById(R.id.dayLeftValueLabel);
		feedStatusView = (RelativeLayout) findViewById(R.id.feedStatusView);
		statusTextLabel = (TextView) findViewById(R.id.statusTextLabel);
		statusTextValueLabel = (TextView) findViewById(R.id.statusTextValueLabel);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		
		commentImageView.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent intent = new Intent(ComplaintDetailActivity.this, CommentActivity.class);
    			intent.putExtra("type", Constants.COMPLAINT);
    			startActivity(intent);
    		}
    	});

    	commentLabel.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			Intent intent = new Intent(ComplaintDetailActivity.this, CommentActivity.class);
    			intent.putExtra("type", Constants.COMPLAINT);
    			startActivity(intent);
    		}
    	});
    	
    	shareImageView.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, Constants.DOMAIN_URL + "/complaint?id="+AppCache.selectedReportDTO.ID);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent, "Share"));
    		}
    	});

    	shareLabel.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, Constants.SERVER_URL + "/complaint?id="+AppCache.selectedReportDTO.ID);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent, "Share"));
    		}
    	});
    	
    	deleteButton.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			AlertDialog.Builder dialogResult = new AlertDialog.Builder(ComplaintDetailActivity.this);
				dialogResult.setTitle("Delete Complaint");
				dialogResult.setMessage("Are you sure you want to delete this complaint?");
				dialogResult.setIcon(null);
				dialogResult.setPositiveButton("Yes", new DialogInterface.OnClickListener() {	
					public void onClick(DialogInterface dialogResult, int id) {
						dialogResult.dismiss();
						pd = ProgressDialog.show(ComplaintDetailActivity.this,"","Please wait...",true, false);
						callDeleteComplaint();
					}
				});
				dialogResult.setNegativeButton("No", new DialogInterface.OnClickListener() {	
					public void onClick(DialogInterface dialogResult, int id) {
						dialogResult.dismiss();
					}
				});
				dialogResult.show();
    		}
    	});
		
		if(AppCache.selectedReportDTO != null)
		{
			
			if(AppCache.selectedReportDTO.ref == null)
			{
				refLabel.setVisibility(View.GONE);
				refValueLabel.setVisibility(View.GONE);
			}
			else
			{
				refValueLabel.setText(AppCache.selectedReportDTO.ref);
			}
				
			
			titleValueLabel.setText(AppCache.selectedReportDTO.title);
			descValueLabel.setText(AppCache.selectedReportDTO.description);
			categoryValueLabel.setText(AppCache.selectedReportDTO.category.name.toUpperCase());
			
			
			nameValueLabel.setText(AppCache.selectedReportDTO.fullname); 
			dateValueLabel.setText(AppCache.selectedReportDTO.create_date); 
			
			commentLabel.setText(AppCache.selectedReportDTO.commentCount + " Comments");
			
			Picasso.with(this)
			  .load(Constants.IMAGE_URL + "/400/" + AppCache.selectedReportDTO.photo)
			  .into(feedImageView);
			
			if(AppCache.selectedReportDTO.lat != null && AppCache.selectedReportDTO.lat.length() > 0
					&& AppCache.selectedReportDTO.longi != null && AppCache.selectedReportDTO.longi.length() > 0)
			{
				LatLng markerPosition = new LatLng(Double.parseDouble(AppCache.selectedReportDTO.lat), Double.parseDouble(AppCache.selectedReportDTO.longi));
				Marker marker = map.addMarker(new MarkerOptions().position(markerPosition));
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 16);//14
		        map.animateCamera(cameraUpdate);
			}
			
			if(AppCache.selectedReportDTO.address != null && AppCache.selectedReportDTO.address.length() > 0)
			{
				addressValueLabel.setText(AppCache.selectedReportDTO.address); 
			}
			else
			{
				addressValueLabel.setText("N/A"); 
			}
			
			if(!Util.isCanDelete(AppCache.selectedReportDTO.status, AppCache.selectedReportDTO.user.ID))
			{
				deleteButton.setVisibility(View.GONE);
			}
			
			if(Util.showStatusSection(AppCache.selectedReportDTO.status))
			{
				statusTextLabel.setVisibility(View.GONE);
				statusTextValueLabel.setVisibility(View.GONE);
				
				statusValueLabel.setText(Util.getStatusLabel(AppCache.selectedReportDTO.status));
				if(AppCache.selectedReportDTO.slaLeftoverDays <= 0)
				{
					progressLabel.setProgress(1.0f);
					progressLabel.setProgressColor(getResources().getColor(R.color.red));
					dayLeftValueLabel.setText("Overdue "+(-1 * AppCache.selectedReportDTO.slaLeftoverDays)+" days");
				}
				else
				{
					float progress = (float)(1.0f - (float)((float)AppCache.selectedReportDTO.slaLeftoverDays/(float)AppCache.selectedReportDTO.slaTotalDays));
					Util.log("progress: "+progress);
					progressLabel.setProgress(progress);
					dayLeftValueLabel.setText(AppCache.selectedReportDTO.slaLeftoverDays + " days to go");
				}
			}
			else
			{
				feedStatusView.setVisibility(View.GONE);
				statusTextValueLabel.setText(Util.getStatusLabel(AppCache.selectedReportDTO.status));
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
	
	private void callDeleteComplaint()
	{
		AppAPI.deleteFeedback(AppCache.selectedReportDTO.ID, 
				new DataHandler<ResultDTO>(ResultDTO.class) {

					
			
					@Override
					public void onSuccess(ResultDTO data) {
						
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						if(data != null && data.status != null && data.status.equalsIgnoreCase("OK"))
						{
							ContextThemeWrapper ctw = new ContextThemeWrapper(ComplaintDetailActivity.this, android.R.style.Theme_Holo_Light_Dialog );
							AlertDialog.Builder dialogResult = new AlertDialog.Builder(ctw);
							dialogResult.setTitle("Deleted");
							dialogResult.setIcon(R.drawable.ic_launcher);
							dialogResult.setMessage("Complaint deleted.");
							dialogResult.setNeutralButton("Ok", new DialogInterface.OnClickListener() {	
								public void onClick(DialogInterface dialogResult, int id) {
					        	  dialogResult.dismiss();
					        	  
					        	  Intent intent = new Intent();
					        	  intent.putExtra("id", AppCache.selectedReportDTO.ID);
					        	  setResult(RESULT_OK, intent);
					        	  finish();
								}
							});
							dialogResult.show();
						}
						else
						{
							Util.showAlertDialog(ComplaintDetailActivity.this, "Error", "Failed to delete complaint, kindly try again.");
						}
						
					}

					@Override
					public void onFailure(ResultDTO data) {
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						Util.showAlertDialog(ComplaintDetailActivity.this, "Error", "Failed to delete complaint, kindly try again.");
					}
			});
	}
}
