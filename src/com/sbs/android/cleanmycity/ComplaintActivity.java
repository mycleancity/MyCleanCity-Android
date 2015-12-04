package com.sbs.android.cleanmycity;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.sbs.android.cleanmycity.MenuActivity.ImageAdapter;
import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.ReportCategoryDTO;
import com.sbs.android.cleanmycity.model.ReportDTO;
import com.sbs.android.cleanmycity.model.ResultDTO;
import com.sbs.android.cleanmycity.model.SubmitResultDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Util;
import com.squareup.picasso.Picasso;

public class ComplaintActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, 
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, SpinnerAdapter {

	
	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private Location lastLocation = null;
	
	private EditText nameEditText;
	private EditText emailEditText;
	private EditText mobileEditText;
	private EditText titleEditText;
	private EditText detailEditText;
	private EditText addressEditText;
	private Spinner complaintCategorySpinner;
	private Button submitButton;
	
	
	
	private ImageView complaintPhotoImage;
	
	private boolean isWarningDone = false;
	private String mCurrentPhotoPath;
	private final int CAMERA_CAPTURE = 1;
	private final int GET_CONTENT = 2;
	private Bitmap bitmap;
	
	private ProgressDialog pd;
	
	private ReportCategoryDTO reportCategoryDTO;
	
	private boolean isDraft = false;
	private double draftLongtitude = 0.0;
	private double draftLatitude = 0.0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complaint);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		addressEditText = (EditText) findViewById(R.id.addressEditText);
		
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		mobileEditText = (EditText) findViewById(R.id.mobileEditText);
		titleEditText = (EditText) findViewById(R.id.titleEditText);
		detailEditText = (EditText) findViewById(R.id.detailEditText);
		complaintCategorySpinner = (Spinner) findViewById(R.id.complaintCategorySpinner);
		complaintPhotoImage = (ImageView) findViewById(R.id.complaintPhotoImage);
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
		
		
		mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(5000);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(5000);
        
        complaintPhotoImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				try
				{
					String option[] = new String[]{"Camera", "Gallery"};
					
					AlertDialog.Builder dialogResult = new AlertDialog.Builder(ComplaintActivity.this);
					dialogResult.setTitle("Take photo from..");
					dialogResult.setIcon(null);
					dialogResult.setItems(option, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int index) {
							if(index == 0)
							{
								Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								mCurrentPhotoPath = Environment.getExternalStorageDirectory().toString() + "/mycleancity_complaint.jpg";
								captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCurrentPhotoPath)));
					            startActivityForResult(captureIntent, CAMERA_CAPTURE);
							}
							else if(index == 1)
							{
								Intent captureIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
								mCurrentPhotoPath = Environment.getExternalStorageDirectory().toString() + "/mycleancity_complaint.jpg";
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
					Util.showAlertDialog(ComplaintActivity.this, "Full Name", "Full name is required for submission.");
				}
				else if(emailEditText.getText() == null || emailEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ComplaintActivity.this, "Email Address", "Email address is required for submission.");
				}
				else if(mobileEditText.getText() == null || mobileEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ComplaintActivity.this, "Mobile Number", "Mobile number is required for submission.");
				}
				else if(titleEditText.getText() == null || titleEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ComplaintActivity.this, "Complaint Title", "Complaint Title is required for submission.");
				}
				else if(detailEditText.getText() == null || detailEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ComplaintActivity.this, "Complaint Detail", "Complaint detail is required for submission.");
				}
				else if(bitmap == null)
				{
					Util.showAlertDialog(ComplaintActivity.this, "Photo", "Photo is required for submission.");
				}
				else if(addressEditText.getText() == null || addressEditText.getText().toString().length() == 0)
				{
					Util.showAlertDialog(ComplaintActivity.this, "Address", "Address is required for submission.");
				}
				else if(lastLocation == null)
				{
					Util.showAlertDialog(ComplaintActivity.this, "Location", "Kindly enable the location services on your phone.");
				}
				else
				{
					pd = ProgressDialog.show(ComplaintActivity.this,"","Please wait...",true, false);
					callSubmitComplaint(nameEditText.getText().toString(), emailEditText.getText().toString(), mobileEditText.getText().toString(), 
							titleEditText.getText().toString(), detailEditText.getText().toString(), 
							reportCategoryDTO.categories.get(complaintCategorySpinner.getSelectedItemPosition()).ID,
							
							addressEditText.getText().toString());
				}
			}
		});
        
        
        
        
        
        
        
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
        	Util.showAlertDialog(this, "Location", "Kindly enable the location services on your phone.");
        }
        
        
        //Spinner
        if(AppCache.getString(AppCache.COMPLAINT_CATEGORY_PREF).length() > 0)
        {
        	Util.log("AppCache.getString(AppCache.COMPLAINT_CATEGORY_PREF) = "+AppCache.getString(AppCache.COMPLAINT_CATEGORY_PREF));
        	
        	Gson gson = new Gson();
        	reportCategoryDTO = gson.fromJson(AppCache.getString(AppCache.COMPLAINT_CATEGORY_PREF), ReportCategoryDTO.class);
        	if(reportCategoryDTO != null && reportCategoryDTO.categories != null)
        	{
        		//String[] category = AppCache.getString(AppCache.COMPLAINT_CATEGORY_PREF).split(",");
        	}
        	
        	complaintCategorySpinner.setAdapter(this);
        	
        	//complaintCategorySpinner.setAdapter(spinnerArrayAdapter);
        }
                
        
        if(AppCache.getString(AppCache.COMPLAINT_DRAFT_PREF) != null && AppCache.getString(AppCache.COMPLAINT_DRAFT_PREF).length() > 0)
        {
        	ContextThemeWrapper ctw = new ContextThemeWrapper(ComplaintActivity.this, android.R.style.Theme_Holo_Light_Dialog );
			AlertDialog.Builder dialogResult = new AlertDialog.Builder(ctw);
			dialogResult.setTitle("Info");
			dialogResult.setIcon(R.drawable.ic_launcher);
			dialogResult.setMessage("You have a complaint draft, do you want to open it?");
			dialogResult.setPositiveButton("Yes", new DialogInterface.OnClickListener() {	
				public void onClick(DialogInterface dialogResult, int id) {
	        	  dialogResult.dismiss();
	        	  
	        	    try {
	        	    	
	        	    	isDraft = true;
	        	    	
	        	    	JSONObject object = new JSONObject(AppCache.getString(AppCache.COMPLAINT_DRAFT_PREF));
	        	    	
	        	    	addressEditText.setText(object.getString("address"));
	        	    	nameEditText.setText(object.getString("fullname"));
	        	    	emailEditText.setText(object.getString("email"));
	        	    	mobileEditText.setText(object.getString("mobileNo"));
	        	    	titleEditText.setText(object.getString("title"));
	        	    	detailEditText.setText(object.getString("description"));
	        	    	
	        	    	Util.log("category selected : "+object.getInt("category"));
	        	    	complaintCategorySpinner.setSelection(object.getInt("category"));
	        	    	
	        	    	draftLatitude = object.getDouble("latitude");
	        	    	draftLongtitude = object.getDouble("longtitude");
	        	    	
	        	    	int targetW = complaintPhotoImage.getWidth();
	    	    	    int targetH = complaintPhotoImage.getHeight();
	    	    	    // Get the dimensions of the bitmap
	    	    	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    	    	    String draftPhotoPath = Environment.getExternalStorageDirectory().toString() + "/mycleancity_complaint.jpg";
	    	    		int rotate = 0;
	    	    	    try
	    	    	    {
	    	    	    	File imageFile = new File(draftPhotoPath);
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
	    	    	    bmOptions.inSampleSize = 8;
	    	    	    bmOptions.inPurgeable = true;
	    	    	    bitmap = BitmapFactory.decodeFile(draftPhotoPath, bmOptions);
	    	    	    Matrix matrix = new Matrix();
	    	    	    matrix.postRotate(rotate);
	    	    	    bitmap =  Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	    	    	    complaintPhotoImage.setImageBitmap(bitmap);
	    	    	    
	    	    	    AppCache.putString(AppCache.COMPLAINT_DRAFT_PREF, "");
	    	    	    
				    } catch (JSONException e) {
				        // Handle impossible error
				        e.printStackTrace();
				        isDraft = false;
				    }
				    
				}
			});
			dialogResult.setNegativeButton("No", new DialogInterface.OnClickListener() {	
				public void onClick(DialogInterface dialogResult, int id) {
				AppCache.putString(AppCache.COMPLAINT_DRAFT_PREF, "");
	        	  dialogResult.dismiss();
				}
			});
			dialogResult.show();
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
	    		
	    		int targetW = complaintPhotoImage.getWidth();
	    	    int targetH = complaintPhotoImage.getHeight();

	    	    // Get the dimensions of the bitmap
	    	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    	    
	    	    mCurrentPhotoPath = Environment.getExternalStorageDirectory().toString() + "/mycleancity_complaint.jpg";
	    		
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
	    	    bmOptions.inSampleSize = 8;
	    	    bmOptions.inPurgeable = true;
	    	    

	    	    bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    	    
	    	    Matrix matrix = new Matrix();
	    	    matrix.postRotate(rotate);
	    	    bitmap =  Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	    	
	    	
	    	    complaintPhotoImage.setImageBitmap(bitmap);
	    	    
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
	    			int targetW = complaintPhotoImage.getWidth();
		    	    int targetH = complaintPhotoImage.getHeight();

		    	    
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
		    	    bmOptions.inSampleSize = 8;
		    	    bmOptions.inPurgeable = true;
		    	    bitmap = BitmapFactory.decodeFile(Util.getRealPathFromURI(uri, this), bmOptions);
		    	    
		    	    Matrix matrix = new Matrix();
		    	    matrix.postRotate(rotate);
		    	    bitmap =  Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		    	
		    	
		    	    complaintPhotoImage.setImageBitmap(bitmap);
		    	    
		    		
	    		}
	    		catch (Exception e) {
					// TODO: handle exception
	    			e.printStackTrace();
	    			
				}

	    	}
	    	
	    }
	    
	    
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		lastLocation = location;
		
		if(!isDraft)
		{
			(new GetAddressTask(this)).execute(lastLocation);
		}
		
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
		Util.log("Connection faled");
		if(!isWarningDone)
		{
			isWarningDone = true;
			Util.showAlertDialog(this, "Location", "Kindly enable the location services on your phone.");
		}
			
		mLocationClient.connect();
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Util.log("Location connected..");
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
		
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Util.log("Location disconnected..");
	}
	
	@Override
	public void onDestroy() {
        // If the client is connected
        if (mLocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
        	mLocationClient.removeLocationUpdates(this);
        }
        /*
         * After disconnect() is called, the client is
         * considered "dead".
         */
        mLocationClient.disconnect();
        super.onStop();
    }
	
	
	private void callSubmitComplaint(final String fullname, final String email, final String mobileNo, final String title, final String description, final int category, final String address)
	{
		AppCache.putString(AppCache.FULLNAME_PREF, fullname);
		AppCache.putString(AppCache.MOBILENO_PREF, mobileNo);
		
		AppAPI.postFeedbackJson(/*fullname*/ "", email, mobileNo, title, description, category, bitmap, address, isDraft? ""+draftLongtitude : ""+lastLocation.getLongitude(),
				isDraft? ""+draftLatitude: ""+lastLocation.getLatitude(), 
				new DataHandler<ReportDTO>(ReportDTO.class) {

					
			
					@Override
					public void onSuccess(ReportDTO data) {
						
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						if(data != null && data.complaint != null)
						{
							//Util.showAlertDialog(ComplaintActivity.this, "Completed", "Complaint submitted. Thanks for your submission.");
							ContextThemeWrapper ctw = new ContextThemeWrapper(ComplaintActivity.this, android.R.style.Theme_Holo_Light_Dialog );
							AlertDialog.Builder dialogResult = new AlertDialog.Builder(ctw);
							dialogResult.setTitle("Completed");
							dialogResult.setIcon(R.drawable.ic_launcher);
							dialogResult.setMessage("Complaint submitted. Thanks for your submission.");
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
							//Util.showAlertDialog(ComplaintActivity.this, "Error", "Submission failed, kindly try again.");
							
							ContextThemeWrapper ctw = new ContextThemeWrapper(ComplaintActivity.this, android.R.style.Theme_Holo_Light_Dialog );
							AlertDialog.Builder dialogResult = new AlertDialog.Builder(ctw);
							dialogResult.setTitle("Error");
							dialogResult.setIcon(R.drawable.ic_launcher);
							dialogResult.setMessage("Submission failed. Do you want to save it as draft and submit later?");
							dialogResult.setPositiveButton("Yes", new DialogInterface.OnClickListener() {	
								public void onClick(DialogInterface dialogResult, int id) {
					        	  dialogResult.dismiss();
					        	  
					        	  JSONObject object = new JSONObject();
								    try {
								    	
								    	Util.log("category saved : "+category);
								    	
								        object.put("fullname", fullname);
								        object.put("email", email);
								        object.put("mobileNo", mobileNo);
								        object.put("title", title);
								        object.put("description", description);
								        object.put("category", complaintCategorySpinner.getSelectedItemPosition());
								        object.put("address", address);
								        object.put("longtitude", isDraft? draftLongtitude : lastLocation.getLongitude() );
								        object.put("latitude", isDraft? draftLatitude: lastLocation.getLatitude());
								        
								        AppCache.putString(AppCache.COMPLAINT_DRAFT_PREF, object.toString());
								        
								        ContextThemeWrapper ctw = new ContextThemeWrapper(ComplaintActivity.this, android.R.style.Theme_Holo_Light_Dialog );
										AlertDialog.Builder dialogResult2 = new AlertDialog.Builder(ctw);
										dialogResult2.setTitle("Completed");
										dialogResult2.setIcon(R.drawable.ic_launcher);
										dialogResult2.setMessage("Complaint saved as draft.");
										dialogResult2.setNeutralButton("Ok", new DialogInterface.OnClickListener() {	
											public void onClick(DialogInterface dialogResult, int id) {
								        	  dialogResult.dismiss();
								        	  finish();
											}
										});
										dialogResult2.show();
								        
								    } catch (JSONException e) {
								        // Handle impossible error
								        e.printStackTrace();
								        Util.showAlertDialog(ComplaintActivity.this, "Error", "Failed to save as draft, kindly try again.");
								    }
								    
								}
							});
							dialogResult.setNegativeButton("No", new DialogInterface.OnClickListener() {	
								public void onClick(DialogInterface dialogResult, int id) {
					        	  dialogResult.dismiss();
								}
							});
							dialogResult.show();
							
							
						    
							
							
						}
						
					}

					@Override
					public void onFailure(ReportDTO data) {
						if(pd != null)
		            	{
		            		try{ pd.dismiss(); } catch (Exception ex){}
		            	}
						
						//Util.showAlertDialog(ComplaintActivity.this, "Error", "Submission failed, kindly try again.");
						
						ContextThemeWrapper ctw = new ContextThemeWrapper(ComplaintActivity.this, android.R.style.Theme_Holo_Light_Dialog );
						AlertDialog.Builder dialogResult = new AlertDialog.Builder(ctw);
						dialogResult.setTitle("Error");
						dialogResult.setIcon(R.drawable.ic_launcher);
						dialogResult.setMessage("Submission failed. Do you want to save it as draft and submit later?");
						dialogResult.setPositiveButton("Yes", new DialogInterface.OnClickListener() {	
							public void onClick(DialogInterface dialogResult, int id) {
				        	  dialogResult.dismiss();
				        	  
				        	  JSONObject object = new JSONObject();
							    try {
							    	Util.log("category saved : "+category);
							    	
							        object.put("fullname", fullname);
							        object.put("email", email);
							        object.put("mobileNo", mobileNo);
							        object.put("title", title);
							        object.put("description", description);
							        object.put("category", complaintCategorySpinner.getSelectedItemPosition());
							        object.put("address", address);
							        object.put("longtitude", isDraft? draftLongtitude : lastLocation.getLongitude() );
							        object.put("latitude", isDraft? draftLatitude: lastLocation.getLatitude());
							        
							        AppCache.putString(AppCache.COMPLAINT_DRAFT_PREF, object.toString());
							        
							        ContextThemeWrapper ctw = new ContextThemeWrapper(ComplaintActivity.this, android.R.style.Theme_Holo_Light_Dialog );
									AlertDialog.Builder dialogResult2 = new AlertDialog.Builder(ctw);
									dialogResult2.setTitle("Completed");
									dialogResult2.setIcon(R.drawable.ic_launcher);
									dialogResult2.setMessage("Complaint saved as draft.");
									dialogResult2.setNeutralButton("Ok", new DialogInterface.OnClickListener() {	
										public void onClick(DialogInterface dialogResult, int id) {
							        	  dialogResult.dismiss();
							        	  finish();
										}
									});
									dialogResult2.show();
							        
							    } catch (JSONException e) {
							        // Handle impossible error
							        e.printStackTrace();
							        Util.showAlertDialog(ComplaintActivity.this, "Error", "Failed to save as draft, kindly try again.");
							    }
							    
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
	}
	
	
	private class GetAddressTask extends
	AsyncTask<Location, Void, String> {
		Context mContext;
		public GetAddressTask(Context context) {
			super();
			mContext = context;
		}
		@Override
		protected String doInBackground(Location... params) {
			// TODO Auto-generated method stub
			String address = "";
			try {
				
				Location currentLocation = params[0];
				
		        Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
		        
		        List<Address> addresses = geo.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
		        if (addresses.isEmpty()) {
		            
		        	Util.log("No address found");
		        }
		        else {
		            if (addresses.size() > 0) {
		            	
		            	
		            	for(int i = 0; i < addresses.get(0).getMaxAddressLineIndex() ; i ++)
		            	{
		            		if(i == addresses.get(0).getMaxAddressLineIndex() - 1)
		            		{
		            			address = address +  addresses.get(0).getAddressLine(i);
		            		}
		            		else
		            		{
		            			address = address +  addresses.get(0).getAddressLine(i) + ", ";
		            		}
		            	}
		            	
		            	Util.log("Address found = "+address);
		            	
		            	if(address.length() > 0 && addressEditText.getText().toString().length() == 0)
		            	{
		            		if(!isDraft)
		            		{
		            			addressEditText.setText(address);
		            		}
		            	}
		            }
		        }
		    }
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
			return address;
		}
		
		@Override
        protected void onPostExecute(String address) {
            
            addressEditText.setText(address);
        }
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
		return reportCategoryDTO.categories.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return reportCategoryDTO.categories.get(position).name;
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
		textView.setText(reportCategoryDTO.categories.get(position).name);
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
		textView.setText(reportCategoryDTO.categories.get(position).name);
		return convertView;
	}
	
}


