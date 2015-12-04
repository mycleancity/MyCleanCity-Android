package com.sbs.android.cleanmycity;

import java.io.File;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.sbs.android.cleanmycity.adapter.ZoneSpinnerAdapter;
import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.ReportCategoryDTO;
import com.sbs.android.cleanmycity.model.ReportDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxCategoryDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxDTO;
import com.sbs.android.cleanmycity.model.ZoneDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Util;

public class ThinkBoxActivity extends Activity implements  SpinnerAdapter {

	
	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private Location lastLocation = null;
	
	private EditText nameEditText;
	private EditText emailEditText;
	private EditText mobileEditText;
	private EditText titleEditText;
	private EditText detailEditText;
	private EditText addressEditText;
	private Spinner thinkBoxCategorySpinner;
	private Spinner feasibilitySpinner;
	private Spinner zoneSpinner;
	private Button submitButton;
	
	
	private ImageView thinkBoxPhotoImage;
	
	private boolean isWarningDone = false;
	private String mCurrentPhotoPath;
	private final int CAMERA_CAPTURE = 1;
	private final int GET_CONTENT = 2;
	private Bitmap bitmap;
	
	private ProgressDialog pd;
	
	private ThinkBoxCategoryDTO thinkBoxCategoryDTO;
	private ZoneDTO zoneDTO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thinkbox);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		addressEditText = (EditText) findViewById(R.id.addressEditText);
		
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		mobileEditText = (EditText) findViewById(R.id.mobileEditText);
		titleEditText = (EditText) findViewById(R.id.titleEditText);
		detailEditText = (EditText) findViewById(R.id.detailEditText);
		thinkBoxCategorySpinner = (Spinner) findViewById(R.id.thinkBoxCategorySpinner);
		feasibilitySpinner = (Spinner) findViewById(R.id.feasibilitySpinner);
		zoneSpinner = (Spinner) findViewById(R.id.zoneSpinner);
		thinkBoxPhotoImage = (ImageView) findViewById(R.id.thinkBoxPhotoImage);
		submitButton = (Button) findViewById(R.id.submitButton);
		
		
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
		
		
		
        
		thinkBoxPhotoImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				try
				{
					String option[] = new String[]{"Camera", "Gallery"};
					
					AlertDialog.Builder dialogResult = new AlertDialog.Builder(ThinkBoxActivity.this);
					dialogResult.setTitle("Take photo from..");
					dialogResult.setIcon(null);
					dialogResult.setItems(option, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int index) {
							if(index == 0)
							{
								Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								mCurrentPhotoPath = Environment.getExternalStorageDirectory().toString() + "/mycleancity.jpg";
								captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCurrentPhotoPath)));
					            startActivityForResult(captureIntent, CAMERA_CAPTURE);
							}
							else if(index == 1)
							{
								Intent captureIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
								mCurrentPhotoPath = Environment.getExternalStorageDirectory().toString() + "/mycleancity.jpg";
								captureIntent.setType("image/*");
								captureIntent.putExtra("return-data", true);
					            startActivityForResult(captureIntent, GET_CONTENT);
							}

						} });
					dialogResult.setNeutralButton("Close", new DialogInterface.OnClickListener() {	
						public void onClick(DialogInterface dialogResult, int id) {
							dialogResult.dismiss();
						}
					});
					dialogResult.show();
					
					
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
        
        
        submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(nameEditText.getText() == null || nameEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ThinkBoxActivity.this, "Full Name", "Full name is required for submission.");
				}
				else if(emailEditText.getText() == null || emailEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ThinkBoxActivity.this, "Email Address", "Email address is required for submission.");
				}
				else if(mobileEditText.getText() == null || mobileEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ThinkBoxActivity.this, "Mobile Number", "Mobile number is required for submission.");
				}
				else if(titleEditText.getText() == null || titleEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ThinkBoxActivity.this, "ThinkBox Title", "ThinkBox Title is required for submission.");
				}
				else if(detailEditText.getText() == null || detailEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ThinkBoxActivity.this, "ThinkBox Detail", "ThinkBox detail is required for submission.");
				}
				else if(bitmap == null)
				{
					Util.showAlertDialog(ThinkBoxActivity.this, "ThinkBox Photo", "ThinkBox campaign photo is required for submission.");
				}
				else
				{
					pd = ProgressDialog.show(ThinkBoxActivity.this,"","Please wait...",true, false);
					callSubmitThinkBox(nameEditText.getText().toString(), emailEditText.getText().toString(), mobileEditText.getText().toString(), 
							titleEditText.getText().toString(), detailEditText.getText().toString(), 
							thinkBoxCategoryDTO.categories.get(thinkBoxCategorySpinner.getSelectedItemPosition()).ID,
							feasibilitySpinner.getSelectedItemPosition() + 1,
							zoneDTO.zones.get(zoneSpinner.getSelectedItemPosition()).ID
							);
				}
			}
		});
        
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		        this, R.array.feasibility_arrays, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_layout);        
		feasibilitySpinner.setAdapter(adapter);
        
        //Spinner
        if(AppCache.getString(AppCache.THINKBOX_CATEGORY_PREF).length() > 0)
        {
        	Util.log("AppCache.getString(AppCache.THINKBOX_CATEGORY_PREF) = "+AppCache.getString(AppCache.THINKBOX_CATEGORY_PREF));
        	
        	Gson gson = new Gson();
        	thinkBoxCategoryDTO = gson.fromJson(AppCache.getString(AppCache.THINKBOX_CATEGORY_PREF), ThinkBoxCategoryDTO.class);
        	if(thinkBoxCategoryDTO != null && thinkBoxCategoryDTO.categories != null)
        	{
        		//String[] category = AppCache.getString(AppCache.COMPLAINT_CATEGORY_PREF).split(",");
        	}
        	thinkBoxCategorySpinner.setAdapter(this);
        }
        
        if(AppCache.getString(AppCache.ZONE_PREF).length() > 0)
        {
        	Util.log("AppCache.getString(AppCache.ZONE_PREF) = "+AppCache.getString(AppCache.ZONE_PREF));
        	
        	Gson gson = new Gson();
        	zoneDTO = gson.fromJson(AppCache.getString(AppCache.ZONE_PREF), ZoneDTO.class);
        	if(zoneDTO != null && zoneDTO.zones != null)
        	{
        		zoneSpinner.setAdapter(new ZoneSpinnerAdapter(this, zoneDTO));
        	}
        	
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
		
	    if(requestCode == CAMERA_CAPTURE)
	    {
	    	if (resultCode == RESULT_OK) 
	    	{
	    		
	    		int targetW = thinkBoxPhotoImage.getWidth();
	    	    int targetH = thinkBoxPhotoImage.getHeight();

	    	    // Get the dimensions of the bitmap
	    	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    	    
	    	    mCurrentPhotoPath = Environment.getExternalStorageDirectory().toString() + "/mycleancity.jpg";
	    		
	    	    //new
	    	    int rotate = 0;
	    	    try
	    	    {
	    	    	File imageFile = new File(mCurrentPhotoPath);
		    	    ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
	                int orientation = exif.getAttributeInt(
	                        ExifInterface.TAG_ORIENTATION,
	                        ExifInterface.ORIENTATION_NORMAL);

	                switch (orientation) {
	                case ExifInterface.ORIENTATION_ROTATE_270:
	                    rotate = 270;
	                    break;
	                case ExifInterface.ORIENTATION_ROTATE_180:
	                    rotate = 180;
	                    break;
	                case ExifInterface.ORIENTATION_ROTATE_90:
	                    rotate = 90;
	                    break;
	                }
	    	    }
	    	    catch(Exception ex)
	    	    {
	    	    	ex.printStackTrace();
	    	    }
	    	    
                
                
	    	    bmOptions.inJustDecodeBounds = false;
	    	    bmOptions.inSampleSize = 2;
	    	    bmOptions.inPurgeable = true;
	    	    

	    	    bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    	    
	    	    Matrix matrix = new Matrix();
	    	    matrix.postRotate(rotate);
	    	    bitmap =  Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	    	
	    	
	    	    thinkBoxPhotoImage.setImageBitmap(bitmap);
	    	    
	    	    /*Picasso.with(ComplaintActivity.this).load(new File(mCurrentPhotoPath)).resize(50, 50)
	    	    .centerCrop().into(complaintPhotoImage);*/
	    	   
	    	}
	    }
	    else if(requestCode == GET_CONTENT)
	    {
	    	if (resultCode == RESULT_OK) 
	    	{	
	    		try
	    		{
	    			int targetW = thinkBoxPhotoImage.getWidth();
		    	    int targetH = thinkBoxPhotoImage.getHeight();

		    	    
		    	    Uri uri = data.getData();
		    	    
		    	    int rotate = 0;
		    	    try
		    	    {
		    	    	File imageFile = new File(Util.getRealPathFromURI(uri, this));
			    	    ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
		                int orientation = exif.getAttributeInt(
		                        ExifInterface.TAG_ORIENTATION,
		                        ExifInterface.ORIENTATION_NORMAL);

		                switch (orientation) {
		                case ExifInterface.ORIENTATION_ROTATE_270:
		                    rotate = 270;
		                    break;
		                case ExifInterface.ORIENTATION_ROTATE_180:
		                    rotate = 180;
		                    break;
		                case ExifInterface.ORIENTATION_ROTATE_90:
		                    rotate = 90;
		                    break;
		                }
		    	    }
		    	    catch(Exception ex)
		    	    {
		    	    	ex.printStackTrace();
		    	    }
		    	    
		    	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	                bmOptions.inJustDecodeBounds = false;
		    	    bmOptions.inSampleSize = 2;
		    	    bmOptions.inPurgeable = true;
		    	    bitmap = BitmapFactory.decodeFile(Util.getRealPathFromURI(uri, this), bmOptions);
		    	    
		    	    Matrix matrix = new Matrix();
		    	    matrix.postRotate(rotate);
		    	    bitmap =  Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		    	
		    	
		    	    thinkBoxPhotoImage.setImageBitmap(bitmap);
		    	    /*Picasso.with(ComplaintActivity.this).load(uri).resize(50, 50)
		    	    .centerCrop().into(complaintPhotoImage);*/
		    	    
		    		
	    		}
	    		catch (Exception e) {
					// TODO: handle exception
	    			e.printStackTrace();
	    			
				}

	    	}
	    	
	    }
	    
	    
	}
	
	
	
	
	private void callSubmitThinkBox(String fullname, String email, String mobileNo, String title, String description, int category, int feasibility, int zone)
	{
		AppCache.putString(AppCache.FULLNAME_PREF, fullname);
		AppCache.putString(AppCache.MOBILENO_PREF, mobileNo);
		
		AppAPI.postThinkBoxJson(fullname, email, mobileNo, title, description, category, bitmap, feasibility, zone,
				new DataHandler<ThinkBoxDTO>(ThinkBoxDTO.class) {

					
			
					@Override
					public void onSuccess(ThinkBoxDTO data) {
						
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						if(data != null && data.thinkBox != null)
						{
							//Util.showAlertDialog(ComplaintActivity.this, "Completed", "Complaint submitted. Thanks for your submission.");
							ContextThemeWrapper ctw = new ContextThemeWrapper(ThinkBoxActivity.this, android.R.style.Theme_Holo_Light_Dialog );
							AlertDialog.Builder dialogResult = new AlertDialog.Builder(ctw);
							dialogResult.setTitle("Completed");
							dialogResult.setIcon(R.drawable.ic_launcher);
							dialogResult.setMessage("ThinkBox submitted. Thanks for your submission.");
							dialogResult.setNeutralButton("Ok", new DialogInterface.OnClickListener() {	
								public void onClick(DialogInterface dialogResult, int id) {
					        	  dialogResult.dismiss();
					        	  finish();
								}
							});
							dialogResult.show();
						}
						else
						{
							Util.showAlertDialog(ThinkBoxActivity.this, "Error", "Submission failed, kindly try again.");
						}
						
					}

					@Override
					public void onFailure(ThinkBoxDTO data) {
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						Util.showAlertDialog(ThinkBoxActivity.this, "Error", "Submission failed, kindly try again.");
					}
			});
	}
	
	
	


	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return thinkBoxCategoryDTO.categories.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return thinkBoxCategoryDTO.categories.get(position).name;
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView  = vi.inflate(R.layout.simple_spinner_item, null);
		TextView textView = (TextView) convertView.findViewById(R.id.text1);
		textView.setText(thinkBoxCategoryDTO.categories.get(position).name);
		return convertView;
	}


	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView  = vi.inflate(R.layout.spinner_layout, null);
		CheckedTextView textView = (CheckedTextView) convertView.findViewById(R.id.text1);
		textView.setText(thinkBoxCategoryDTO.categories.get(position).name);
		return convertView;
	}
	
}