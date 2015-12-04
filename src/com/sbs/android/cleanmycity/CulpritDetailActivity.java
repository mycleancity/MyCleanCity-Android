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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.ResultDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Constants;
import com.sbs.android.cleanmycity.utils.Util;
import com.squareup.picasso.Picasso;

public class CulpritDetailActivity extends Activity{

	//private ScrollView scrollView;
	private ImageView feedImageView;
	private TextView descValueLabel;
	private TextView categoryValueLabel;
	private TextView addressValueLabel;
	private TextView nameValueLabel;
	private TextView dateValueLabel;
	private TextView statusValueLabel;
	
	private TextView youtubeValueLabel;
	private TextView repeatValueLabel;
	
	private ImageView commentImageView;
	private ImageView shareImageView;
	private TextView commentLabel;
	private TextView shareLabel;
	
	private TextView statusLabel;
	private Button deleteButton;
	
	private GoogleMap map;
	private ProgressDialog pd;
	
	@Override
	protected void onResume()
	{
		super.onResume();
		commentLabel.setText(AppCache.selectedCulpritDTO.commentCount + " Comments");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_culprit_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Culprit Details");
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		feedImageView = (ImageView) findViewById(R.id.feedImageView);
		descValueLabel = (TextView) findViewById(R.id.descValueLabel);
		categoryValueLabel = (TextView) findViewById(R.id.categoryValueLabel);
		addressValueLabel = (TextView) findViewById(R.id.addressValueLabel);
		nameValueLabel = (TextView) findViewById(R.id.nameValueLabel);
		dateValueLabel = (TextView) findViewById(R.id.dateValueLabel);
		youtubeValueLabel = (TextView) findViewById(R.id.youtubeValueLabel);
		repeatValueLabel = (TextView) findViewById(R.id.repeatValueLabel);
		statusValueLabel = (TextView) findViewById(R.id.statusValueLabel);
		commentImageView = (ImageView) findViewById(R.id.commentImageView);
		commentLabel = (TextView) findViewById(R.id.commentLabel);
		shareImageView = (ImageView) findViewById(R.id.shareImageView);
		shareLabel = (TextView) findViewById(R.id.shareLabel);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		
		//Temp Remove
		//statusLabel = (TextView) findViewById(R.id.statusLabel);
		//statusLabel.setVisibility(View.GONE);
		//statusValueLabel.setVisibility(View.GONE);
		
		commentImageView.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent intent = new Intent(CulpritDetailActivity.this, CommentActivity.class);
    			intent.putExtra("type", Constants.CULPRIT);
    			startActivity(intent);
    		}
    	});

    	commentLabel.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			Intent intent = new Intent(CulpritDetailActivity.this, CommentActivity.class);
    			intent.putExtra("type", Constants.CULPRIT);
    			startActivity(intent);
    		}
    	});
    	
    	shareImageView.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, Constants.SERVER_URL + "/culprit?id="+AppCache.selectedCulpritDTO.ID);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent, "Share"));
    		}
    	});

    	shareLabel.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, Constants.DOMAIN_URL + "/culprit?id="+AppCache.selectedCulpritDTO.ID);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(intent, "Share"));
    		}
    	});
    	
    	deleteButton.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			AlertDialog.Builder dialogResult = new AlertDialog.Builder(CulpritDetailActivity.this);
				dialogResult.setTitle("Delete Culprit Report");
				dialogResult.setMessage("Are you sure you want to delete this culprit report?");
				dialogResult.setIcon(null);
				dialogResult.setPositiveButton("Yes", new DialogInterface.OnClickListener() {	
					public void onClick(DialogInterface dialogResult, int id) {
						dialogResult.dismiss();
						pd = ProgressDialog.show(CulpritDetailActivity.this,"","Please wait...",true, false);
						callDeleteCulprit();
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
		
		
		if(AppCache.selectedCulpritDTO != null)
		{
			descValueLabel.setText(AppCache.selectedCulpritDTO.description);
			categoryValueLabel.setText(AppCache.selectedCulpritDTO.category.name.toUpperCase());
			
			
			dateValueLabel.setText(AppCache.selectedCulpritDTO.create_date); 
			
			//statusValueLabel.setText(Util.getStatusLabel(AppCache.selectedCulpritDTO.status));
			statusValueLabel.setText(AppCache.selectedCulpritDTO.statusDisplay);
			commentLabel.setText(AppCache.selectedCulpritDTO.commentCount + " Comments");
			
			
			if(AppCache.selectedCulpritDTO.repeat_offender)
			{
				repeatValueLabel.setText("Yes"); 
			}
			else
			{
				repeatValueLabel.setText("No"); 
			}
			
			
			if(AppCache.selectedCulpritDTO.pubName)
			{
				nameValueLabel.setText(AppCache.selectedCulpritDTO.fullname); 
			}
			else
			{
				nameValueLabel.setText("N/A"); 
			}
			
			if(AppCache.selectedCulpritDTO.address != null && AppCache.selectedCulpritDTO.address.length() > 0)
			{
				addressValueLabel.setText(AppCache.selectedCulpritDTO.address); 
			}
			else
			{
				addressValueLabel.setText("N/A"); 
			}
			
			if(AppCache.selectedCulpritDTO.youtubelink != null && AppCache.selectedCulpritDTO.youtubelink.length() > 0)
			{
				youtubeValueLabel.setText(AppCache.selectedCulpritDTO.youtubelink); 
			}
			else
			{
				youtubeValueLabel.setText("N/A"); 
			}
			
			if(!Util.isCanDelete(AppCache.selectedCulpritDTO.status, AppCache.selectedCulpritDTO.user.ID))
			{
				deleteButton.setVisibility(View.GONE);
			}
			
			Picasso.with(this)
			  .load(Constants.IMAGE_URL + "/400/"+ AppCache.selectedCulpritDTO.photo)
			  .into(feedImageView);
			
			if(AppCache.selectedCulpritDTO.lat != null && AppCache.selectedCulpritDTO.lat.length() > 0
					&& AppCache.selectedCulpritDTO.longi != null && AppCache.selectedCulpritDTO.longi.length() > 0)
			{
				LatLng markerPosition = new LatLng(Double.parseDouble(AppCache.selectedCulpritDTO.lat), Double.parseDouble(AppCache.selectedCulpritDTO.longi));
				Marker marker = map.addMarker(new MarkerOptions().position(markerPosition));
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 16);//14
		        map.animateCamera(cameraUpdate);
			}
			else
			{
				
			}
			
		}
		else
		{
			
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
	
	
	private void callDeleteCulprit()
	{
		AppAPI.deleteCulprit(AppCache.selectedCulpritDTO.ID, 
				new DataHandler<ResultDTO>(ResultDTO.class) {

					
			
					@Override
					public void onSuccess(ResultDTO data) {
						
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						if(data != null && data.status != null && data.status.equalsIgnoreCase("OK"))
						{
							ContextThemeWrapper ctw = new ContextThemeWrapper(CulpritDetailActivity.this, android.R.style.Theme_Holo_Light_Dialog );
							AlertDialog.Builder dialogResult = new AlertDialog.Builder(ctw);
							dialogResult.setTitle("Deleted");
							dialogResult.setIcon(R.drawable.ic_launcher);
							dialogResult.setMessage("Culprit report deleted.");
							dialogResult.setNeutralButton("Ok", new DialogInterface.OnClickListener() {	
								public void onClick(DialogInterface dialogResult, int id) {
					        	  dialogResult.dismiss();
					        	  
					        	  Intent intent = new Intent();
					        	  intent.putExtra("id", AppCache.selectedCulpritDTO.ID);
					        	  setResult(RESULT_OK, intent);
					        	  
					        	  finish();
								}
							});
							dialogResult.show();
						}
						else
						{
							Util.showAlertDialog(CulpritDetailActivity.this, "Error", "Failed to delete culprit report, kindly try again.");
						}
						
					}

					@Override
					public void onFailure(ResultDTO data) {
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						Util.showAlertDialog(CulpritDetailActivity.this, "Error", "Failed to delete culprit report, kindly try again.");
					}
			});
	}
}
