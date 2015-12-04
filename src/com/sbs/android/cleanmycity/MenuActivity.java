package com.sbs.android.cleanmycity;



import java.util.ArrayList;

import com.google.gson.Gson;
import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.CouncillorDetailDTO;
import com.sbs.android.cleanmycity.model.CulpritCategoryDTO;
import com.sbs.android.cleanmycity.model.ReportCategoryDTO;
import com.sbs.android.cleanmycity.model.ReportDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxCategoryDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxDTO;
import com.sbs.android.cleanmycity.model.ZoneDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Constants;
import com.sbs.android.cleanmycity.utils.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		AppCache.init(getApplicationContext());
		
		GridView gridview = (GridView) findViewById(R.id.gridView);
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	       
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position == 0)
				{
					Intent intent = new Intent(MenuActivity.this, ComplaintActivity.class);
			    	startActivity(intent);
				}
				else if(position == 1)
				{
					Intent intent = new Intent(MenuActivity.this, ListingActivity.class);
					intent.putExtra("type", Constants.COMPLAINT);
			    	startActivity(intent);
				}
				else if(position == 2)
				{
					Intent intent = new Intent(MenuActivity.this, CulpritActivity.class);
			    	startActivity(intent);
				}
				else if(position == 3)
				{
					Intent intent = new Intent(MenuActivity.this, ListingActivity.class);
					intent.putExtra("type", Constants.CULPRIT);
			    	startActivity(intent);
				}
				else if(position == 4)
				{
					Intent intent = new Intent(MenuActivity.this, ThinkBoxActivity.class);
			    	startActivity(intent);
				}
				else if(position == 5)
				{
					Intent intent = new Intent(MenuActivity.this, ListingActivity.class);
					intent.putExtra("type", Constants.THINKBOX);
			    	startActivity(intent);
				}
				else if(position == 6)
				{
					Intent intent = new Intent(MenuActivity.this, ListingActivity.class);
					intent.putExtra("type", Constants.COUNCILLOR);
			    	startActivity(intent);
				}
				else if(position == 7)
				{
					Intent intent = new Intent(MenuActivity.this, ListingActivity.class);
					intent.putExtra("type", Constants.DEPARTMENT_REPORT);
			    	startActivity(intent);
				}
			}
	    });
	    
	    
	    getCulpritCategory();
	    getCategory();
	    getThinkBoxCategory();
	    getAllZone();
	    
	    //getTest();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			
			ContextThemeWrapper ctw = new ContextThemeWrapper(MenuActivity.this, android.R.style.Theme_Holo_Light_Dialog );
			AlertDialog.Builder dialogResult = new AlertDialog.Builder(ctw);
			dialogResult.setTitle("Logout");
			dialogResult.setIcon(R.drawable.ic_launcher);
			dialogResult.setMessage("Are you sure you want to logout?");
			dialogResult.setPositiveButton("Yes", new DialogInterface.OnClickListener() {	
				public void onClick(DialogInterface dialogResult, int id) {
	        	  dialogResult.dismiss();
	        	  AppCache.clearUserData();
					
					Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
			    	startActivity(intent);
			    	finish();
				}
			});
			dialogResult.setNegativeButton("No", new DialogInterface.OnClickListener() {	
				public void onClick(DialogInterface dialogResult, int id) {
	        	  dialogResult.dismiss();
	        	  
				}
			});
			dialogResult.show();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void getCulpritCategory()
	{
		AppAPI.getCulpritCategory(
				new DataHandler<CulpritCategoryDTO>(CulpritCategoryDTO.class) {

			
			
					@Override
					public void onSuccess(CulpritCategoryDTO data) {
						if(data != null && data.categories != null && data.categories.size() > 0)
						{
							for(CulpritCategoryDTO culpritCategoryDTO : data.categories)
							{
								Util.log("Culprit Category : "+culpritCategoryDTO.name + " : ID : "+culpritCategoryDTO.ID);
							}
							
							Gson gson = new Gson();
							String jsonRepresentation = gson.toJson(data);
							Util.log("category json: "+jsonRepresentation);
							AppCache.putString(AppCache.CULPRIT_CATEGORY_PREF, jsonRepresentation);
						}
					}

					@Override
					public void onFailure(CulpritCategoryDTO data) {
						
					}
			});
	}
	
	private void getThinkBoxCategory()
	{
		AppAPI.getThinkBoxCategory(
				new DataHandler<ThinkBoxCategoryDTO>(ThinkBoxCategoryDTO.class) {

			
			
					@Override
					public void onSuccess(ThinkBoxCategoryDTO data) {
						if(data != null && data.categories != null && data.categories.size() > 0)
						{
							for(ThinkBoxCategoryDTO thinkBoxCategoryDTO : data.categories)
							{
								Util.log("ThinkBox Category : "+thinkBoxCategoryDTO.name + " : ID : "+thinkBoxCategoryDTO.ID);
							}
							
							Gson gson = new Gson();
							String jsonRepresentation = gson.toJson(data);
							Util.log("category json: "+jsonRepresentation);
							AppCache.putString(AppCache.THINKBOX_CATEGORY_PREF, jsonRepresentation);
						}
					}

					@Override
					public void onFailure(ThinkBoxCategoryDTO data) {
						
					}
			});
	}
	
	
	private void getAllZone()
	{
		AppAPI.getAllZone(
				new DataHandler<ZoneDTO>(ZoneDTO.class) {

			
			
					@Override
					public void onSuccess(ZoneDTO data) {
						if(data != null && data.zones != null && data.zones.size() > 0)
						{
							for(ZoneDTO zoneDTO : data.zones)
							{
								Util.log("ThinkBox Category : "+zoneDTO.name + " : ID : "+zoneDTO.ID);
							}
							
							Gson gson = new Gson();
							String jsonRepresentation = gson.toJson(data);
							Util.log("category json: "+jsonRepresentation);
							AppCache.putString(AppCache.ZONE_PREF, jsonRepresentation);
						}
					}

					@Override
					public void onFailure(ZoneDTO data) {
						
					}
			});
	}
	
	
	private void getCategory()
	{
		AppAPI.getCategory(
				new DataHandler<ReportCategoryDTO>(ReportCategoryDTO.class) {

			
			
					@Override
					public void onSuccess(ReportCategoryDTO data) {
						if(data != null && data.categories != null && data.categories.size() > 0)
						{
							for(ReportCategoryDTO reportCategoryDTO : data.categories)
							{
								Util.log("Category : "+reportCategoryDTO.name + " : ID : "+reportCategoryDTO.ID);
							}
							
							Gson gson = new Gson();
							String jsonRepresentation = gson.toJson(data);
							Util.log("category json: "+jsonRepresentation);
							AppCache.putString(AppCache.COMPLAINT_CATEGORY_PREF, jsonRepresentation);
						}
						
						//tempString.append(reportCategoryDTO.cat_name).append(",");
						//AppCache.putString(AppCache.COMPLAINT_CATEGORY_PREF, tempString.toString());
					}

					@Override
					public void onFailure(ReportCategoryDTO data) {
						
					}
					
					
			});
	}
	
	
	
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return 8;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        
	    	LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	convertView = vi.inflate(R.layout.menu_list_item, parent, false);
	    	//convertView = vi.inflate(R.layout.menu_list_item, null);
	        
	    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
	    	convertView.setLayoutParams(new GridView.LayoutParams(params));
	    	//}
	    	
	    	ImageView menuImageView = (ImageView) convertView.findViewById(R.id.menuImageView); 
	    	TextView menuLabel = (TextView) convertView.findViewById(R.id.menuLabel); 
	    	
	    	if(position == 0)
	    	{
	    		menuImageView.setImageResource(R.drawable.ic_complaint);
	    		menuLabel.setText("Submit Complaint");
	    		menuImageView.setAlpha(1.0f);
	    		menuLabel.setAlpha(1.0f);
	    	}
	    	else if(position == 1)
	    	{
	    		menuImageView.setImageResource(R.drawable.ic_complaint_list);
	    		menuLabel.setText("Resident Complaints");
	    		menuImageView.setAlpha(1.0f);
	    		menuLabel.setAlpha(1.0f);
	    	}
	    	else if(position == 2)
	    	{
	    		menuImageView.setImageResource(R.drawable.ic_culprit);
	    		menuLabel.setText("Report a Culprit");
	    		menuImageView.setAlpha(1.0f);
	    		menuLabel.setAlpha(1.0f);
	    	}
	    	else if(position == 3)
	    	{
	    		menuImageView.setImageResource(R.drawable.ic_culprit_list);
	    		menuLabel.setText("View all Culprits");
	    		menuImageView.setAlpha(1.0f);
	    		menuLabel.setAlpha(1.0f);
	    	}
	    	
	    	else if(position == 4)
	    	{
	    		menuImageView.setImageResource(R.drawable.ic_thinkbox);
	    		menuImageView.setAlpha(1.0f);
	    		menuLabel.setText("Submit to Thinkbox");
	    		menuLabel.setAlpha(1.0f);
	    	}
	    	else if(position == 5)
	    	{
	    		menuImageView.setImageResource(R.drawable.ic_thinkbox_list);
	    		menuImageView.setAlpha(1.0f);
	    		menuLabel.setText("Resident Thinkbox");
	    		menuLabel.setAlpha(1.0f);
	    	}
	    	else if(position == 6)
	    	{
	    		menuImageView.setImageResource(R.drawable.ic_mycouncilor);
	    		menuImageView.setAlpha(1.0f);
	    		menuLabel.setText("My Councilor");
	    		menuLabel.setAlpha(1.0f);
	    	}
	    	else if(position == 7)
	    	{
	    		menuImageView.setImageResource(R.drawable.ic_reportcard);
	    		menuImageView.setAlpha(1.0f);
	    		menuLabel.setText("Report Card");
	    		menuLabel.setAlpha(1.0f);
	    	}
	    	 
	    	 
	    	 
	        return convertView;
	    }

	    
	}
	
	
	
	
}
