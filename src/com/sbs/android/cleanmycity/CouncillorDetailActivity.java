package com.sbs.android.cleanmycity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.CouncillorDetailDTO;
import com.sbs.android.cleanmycity.model.MapDTO;
import com.sbs.android.cleanmycity.model.ReportDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Constants;
import com.sbs.android.cleanmycity.utils.Util;
import com.squareup.picasso.Picasso;

public class CouncillorDetailActivity extends Activity{

	//private ScrollView scrollView;
	private ImageView feedImageView;
	private TextView nameValueLabel;
	private TextView zoneValueLabel;
	private TextView contactValueLabel;
	private TextView emailValueLabel;
	
	
	private ImageView callImageView;
	private TextView callLabel;
	private ImageView emailImageView;
	private TextView emailActionLabel;
	
	private Button complaintButton;
	private Button culpritButton;
	private Button thinkBoxButton;
	
	private RoundCornerProgressBar progressLabel;
	
	private CouncillorDetailDTO councillorDetailDTO;
	
	private GoogleMap map;
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_councillor_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Councillor Details");
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		feedImageView = (ImageView) findViewById(R.id.feedImageView);
		
		nameValueLabel = (TextView) findViewById(R.id.nameValueLabel);
		zoneValueLabel = (TextView) findViewById(R.id.zoneValueLabel);
		contactValueLabel = (TextView) findViewById(R.id.contactValueLabel);
		emailValueLabel = (TextView) findViewById(R.id.emailValueLabel);
		
		complaintButton = (Button) findViewById(R.id.complaintButton);
		culpritButton = (Button) findViewById(R.id.culpritButton);
		thinkBoxButton = (Button) findViewById(R.id.thinkBoxButton);
		
		callImageView = (ImageView) findViewById(R.id.callImageView);
		callLabel = (TextView) findViewById(R.id.callLabel);
		emailImageView = (ImageView) findViewById(R.id.emailImageView);
		emailActionLabel = (TextView) findViewById(R.id.emailActionLabel);
		
		
		callImageView.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			if(AppCache.selectedCouncillorDTO.mobile != null && AppCache.selectedCouncillorDTO.mobile.length() > 0)
    			{
    				Intent intent = new Intent(Intent.ACTION_DIAL);
        			intent.setData(Uri.parse("tel:"+AppCache.selectedCouncillorDTO.mobile));
        			startActivity(intent);
    			}
    		}
    	});

    	callLabel.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			if(AppCache.selectedCouncillorDTO.mobile != null && AppCache.selectedCouncillorDTO.mobile.length() > 0)
    			{
    				Intent intent = new Intent(Intent.ACTION_DIAL);
        			intent.setData(Uri.parse("tel:"+AppCache.selectedCouncillorDTO.mobile));
        			startActivity(intent);
    			}
    		}
    	});
    	
    	emailImageView.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			if(AppCache.selectedCouncillorDTO.email != null && AppCache.selectedCouncillorDTO.email.length() > 0)
    			{
    				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					  emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					  emailIntent.setType("vnd.android.cursor.item/email");
					  emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {AppCache.selectedCouncillorDTO.email});
					  emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
					  emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
					  
					  startActivity(Intent.createChooser(emailIntent, "Send mail using.."));
    			}
    		}
    	});

    	emailActionLabel.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			if(AppCache.selectedCouncillorDTO.email != null && AppCache.selectedCouncillorDTO.email.length() > 0)
    			{
    				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					  emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					  emailIntent.setType("vnd.android.cursor.item/email");
					  emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {AppCache.selectedCouncillorDTO.email});
					  emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
					  emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
					  
					  startActivity(Intent.createChooser(emailIntent, "Send mail using.."));
    			}
    		}
    	});
    	
    	complaintButton.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent intent = new Intent(CouncillorDetailActivity.this, ListingActivity.class);
				intent.putExtra("type", Constants.COMPLAINT);
				
				intent.putExtra("zone", AppCache.selectedCouncillorDTO.zone.ID);
		    	startActivity(intent);
    		}
    	});
    	
    	culpritButton.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent intent = new Intent(CouncillorDetailActivity.this, ListingActivity.class);
				intent.putExtra("type", Constants.CULPRIT);
				intent.putExtra("zone", AppCache.selectedCouncillorDTO.zone.ID);
		    	startActivity(intent);
    		}
    	});
    	
    	thinkBoxButton.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent intent = new Intent(CouncillorDetailActivity.this, ListingActivity.class);
				intent.putExtra("type", Constants.THINKBOX);
				intent.putExtra("zone", AppCache.selectedCouncillorDTO.zone.ID);
		    	startActivity(intent);
    		}
    	});
		
		if(AppCache.selectedCouncillorDTO != null)
		{
			nameValueLabel.setText(AppCache.selectedCouncillorDTO.name);
			zoneValueLabel.setText(AppCache.selectedCouncillorDTO.zone.name);
			contactValueLabel.setText(AppCache.selectedCouncillorDTO.mobile);
			emailValueLabel.setText(AppCache.selectedCouncillorDTO.email);
			
			Picasso.with(this)
			  .load(Constants.IMAGE_URL + "/400/" + AppCache.selectedCouncillorDTO.photo)
			  .into(feedImageView);
			
			getCouncillorDetails();
			
			
			
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
	
	
	private void getCouncillorDetails()
	{
		AppAPI.getCouncillorDetail(AppCache.selectedCouncillorDTO.ID, 0,
				new DataHandler<CouncillorDetailDTO>(CouncillorDetailDTO.class) {

					@Override
					public void onSuccess(CouncillorDetailDTO data) {
						
						councillorDetailDTO = data;
						
						if(councillorDetailDTO != null && councillorDetailDTO.maps != null)
						{
							LatLng markerPosition = null;
							for(MapDTO mapDTO : councillorDetailDTO.maps)
							{
								markerPosition = new LatLng(Double.parseDouble(mapDTO.latitude), Double.parseDouble(mapDTO.longitude));
								
								map.addMarker(new MarkerOptions().position(markerPosition).icon(BitmapDescriptorFactory.fromResource(Util.getMapFlagColor(mapDTO.status))));
								
							}
							
							if(markerPosition != null)
							{
								CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerPosition, 10);//14
						        map.animateCamera(cameraUpdate);
							}
							
						}
						
						
					}

					@Override
					public void onFailure(CouncillorDetailDTO data) {
						
						
						
					}
			});
	}
}
