package com.sbs.android.cleanmycity;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.sbs.android.cleanmycity.MenuActivity.ImageAdapter;
import com.sbs.android.cleanmycity.adapter.ListingViewAdapter;
import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.CouncillorDetailDTO;
import com.sbs.android.cleanmycity.model.CulpritDTO;
import com.sbs.android.cleanmycity.model.DTO;
import com.sbs.android.cleanmycity.model.DepartmentReportDTO;
import com.sbs.android.cleanmycity.model.ReportDTO;
import com.sbs.android.cleanmycity.model.ResultDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxSupportDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Constants;
import com.sbs.android.cleanmycity.utils.Util;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class ListingActivity extends Activity implements OnRefreshListener {

	private ListView listView;
	public ArrayList<DTO> tempFeedList;
	private ListingViewAdapter listListAdapter;
	public ArrayList<DTO> feedList;
	private boolean loadingMore = true;	
	private boolean noMoreData = false;
	
	private CircularProgressBar circleLoadingView;
	private SmoothProgressBar bottomProgressBar;
	private Button tapTopButton;
	private PullToRefreshLayout mPullToRefreshLayout;
	
    //Detect Scroll Up
    private int scrollLastVisibleItem = 0;
    private boolean animationStarted =  false;
    private boolean didScrollAfterStart =  false;
    
    private int lastPage;
    private int pageSize = 10;
    private int zoneID = -1;
    
    private String type = Constants.COMPLAINT;
    
    //StartActivityForResult 
    final int COMPLAINT_RESULT = 1;
    final int CULPRIT_RESULT = 2;
    
    
    @Override
	protected void onResume()
	{
		super.onResume();
		
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listing);
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			type = extras.getString("type");
			if(extras.containsKey("zone"))
			{
				zoneID = extras.getInt("zone");
			}
		}
		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if(type.equalsIgnoreCase(Constants.COMPLAINT))
		{
			getActionBar().setTitle("Resident Complaints");
			pageSize = 10;
		}
		else if(type.equalsIgnoreCase(Constants.THINKBOX))
		{
			getActionBar().setTitle("Resident ThinkBox");
			pageSize = 10;
		}
		else if(type.equalsIgnoreCase(Constants.THINKBOX_SUPPORT))
		{
			getActionBar().setTitle("Supporter");
			pageSize = 50;
		}
		else if(type.equalsIgnoreCase(Constants.DEPARTMENT_REPORT))
		{
			getActionBar().setTitle("Report Card");
			pageSize = 100;
		}
		else if(type.equalsIgnoreCase(Constants.COUNCILLOR))
		{
			getActionBar().setTitle("Councillor");
			pageSize = 100;
		}
		else
		{
			getActionBar().setTitle("Culprits");
			pageSize = 10;
		}
		
		circleLoadingView = (CircularProgressBar) findViewById(R.id.feedLoadingView);
		circleLoadingView.setVisibility(View.VISIBLE);
        
		
		
        listView = (ListView) findViewById(R.id.feed_list);
        tapTopButton = (Button) findViewById(R.id.btnTopTop);
        tapTopButton.setVisibility(View.INVISIBLE);
        tapTopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listView.setSelectionAfterHeaderView();
				tapTopButton.setVisibility(View.INVISIBLE);
			}
        });
        
        bottomProgressBar = (SmoothProgressBar) findViewById(R.id.bottomProgressBar);
        bottomProgressBar.setVisibility(View.INVISIBLE);
        bottomProgressBar.progressiveStop();
        
        feedList = new ArrayList<DTO>();
        listListAdapter = new ListingViewAdapter(this, feedList);
        listView.setAdapter(listListAdapter);
        
        
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);
        
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(type.equalsIgnoreCase(Constants.COMPLAINT))
				{
					AppCache.selectedReportDTO = (ReportDTO)feedList.get(position);
					Intent intent = new Intent(ListingActivity.this, ComplaintDetailActivity.class);
					startActivityForResult(intent, COMPLAINT_RESULT);
				}
				else if(type.equalsIgnoreCase(Constants.THINKBOX))
				{
					AppCache.selectedThinkBoxDTO = (ThinkBoxDTO)feedList.get(position);
					Intent intent = new Intent(ListingActivity.this, ThinkBoxDetailActivity.class);
			    	startActivity(intent);
				}
				else if(type.equalsIgnoreCase(Constants.CULPRIT))
				{
					AppCache.selectedCulpritDTO = (CulpritDTO)feedList.get(position);
					Intent intent = new Intent(ListingActivity.this, CulpritDetailActivity.class);
					startActivityForResult(intent, CULPRIT_RESULT);
				}
				else if(type.equalsIgnoreCase(Constants.COUNCILLOR))
				{
					AppCache.selectedCouncillorDTO = (CouncillorDetailDTO)feedList.get(position);
					Intent intent = new Intent(ListingActivity.this, CouncillorDetailActivity.class);
			    	startActivity(intent);
				}
				else if(type.equalsIgnoreCase(Constants.DEPARTMENT_REPORT))
				{
					AppCache.selectedDepartmentDTO = (DepartmentReportDTO)feedList.get(position);
					Intent intent = new Intent(ListingActivity.this, DepartmentDetailActivity.class);
			    	startActivity(intent);
				}
				
			}
		});
        
        
        
        listView.setOnScrollListener(new OnScrollListener() {
	        @Override
	        public void onScrollStateChanged(AbsListView view, 
	                int scrollState) {
	           if(scrollState == SCROLL_STATE_IDLE)
	        	{
	        		Animation fadeIn = new AlphaAnimation(1, 0);
	        		fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
	        		fadeIn.setDuration(2500);
	        		fadeIn.setAnimationListener(new AnimationListener() {
	        	        @Override
	        	        public void onAnimationEnd(Animation animation) {
	        	        	if(!didScrollAfterStart)
	        	        	{
	        	        		tapTopButton.setVisibility(View.INVISIBLE);
	        	        		animationStarted = false;
	        	        	}
	        	        	else
	        	        	{
	        	        		tapTopButton.setAlpha(1.0f);
	        	        		tapTopButton.setVisibility(View.VISIBLE);
	        	        		animationStarted = false;
	        	        	}
	        	        }

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
	        		});
	        		
	        		if(tapTopButton.getVisibility() == View.VISIBLE && !animationStarted)
	        		{
	        			didScrollAfterStart = false;
	        			animationStarted = true;
	        			tapTopButton.startAnimation(fadeIn);
	        		}
	        	}
	        }

	        
	        @Override
	        public void onScroll(AbsListView view, int firstVisibleItem, 
	                int visibleItemCount, int totalItemCount) {

	        	if (firstVisibleItem < scrollLastVisibleItem) {
        	        if(firstVisibleItem > 0)
        	        {
        	        	tapTopButton.setVisibility(View.VISIBLE);
        	        	tapTopButton.clearAnimation();
        	        	didScrollAfterStart = true;
        	        }
        	    }
        	    else if (firstVisibleItem > scrollLastVisibleItem) 
        	    {
        	    	tapTopButton.setVisibility(View.INVISIBLE);
        	    }
	        	scrollLastVisibleItem = firstVisibleItem;
	        	
	        	if(feedList.size() < pageSize || noMoreData)
	        		return;
	        	
	        	int lastInScreen = firstVisibleItem + visibleItemCount;
	        	
	        	if((lastInScreen == totalItemCount) && !loadingMore)
	    		{
	    			loadingMore = true;
	    			 if(type.equalsIgnoreCase(Constants.COMPLAINT))
	    			 {
	    				 getFeedback(false);
	    			 }
	    			 else if(type.equalsIgnoreCase(Constants.THINKBOX))
	    			 {
	    				 getThinkBox(false);
	    			 }
	    			 else if(type.equalsIgnoreCase(Constants.THINKBOX_SUPPORT))
	    			 {
	    				 getThinkBoxSupporter(false);
	    			 }
	    			 else if(type.equalsIgnoreCase(Constants.DEPARTMENT_REPORT))
	    			 {
	    				 getDepartmentReport(false);
	    			 }
	    			 else if(type.equalsIgnoreCase(Constants.COUNCILLOR))
	    			 {
	    				 getCouncillor(false);
	    			 }
	    			 else
	    			 {
	    				 getCulprit(false);
	    			 }
	    			
	    		}
	        }
	    });
	 
        
        lastPage = 1;
        if(type.equalsIgnoreCase(Constants.COMPLAINT))
        {
        	getFeedback(true);
        }
        else if(type.equalsIgnoreCase(Constants.THINKBOX))
        {
        	getThinkBox(true);
        }
        else if(type.equalsIgnoreCase(Constants.THINKBOX_SUPPORT))
		 {
			 getThinkBoxSupporter(true);
		 }
        else if(type.equalsIgnoreCase(Constants.DEPARTMENT_REPORT))
		 {
			 getDepartmentReport(true);
		 }
        else if(type.equalsIgnoreCase(Constants.COUNCILLOR))
		 {
			 getCouncillor(true);
		 }
        else
        {
        	getCulprit(true);
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   
		if(requestCode == COMPLAINT_RESULT && resultCode == RESULT_OK)
		{
			int complaintID = data.getIntExtra("id", -1);
			
			Util.log("Complaint ID deleted : "+complaintID);
			
    		if(complaintID > -1)
    		{
    			ReportDTO deletedReportDTO = null;
    			for(DTO dto : feedList)
				{
    				ReportDTO reportDTO = (ReportDTO) dto;
					if(reportDTO.ID == complaintID)
					{
						deletedReportDTO = reportDTO;
					}
				}
    			
    			if(deletedReportDTO != null)
    			{
    				feedList.remove(deletedReportDTO);
    				listListAdapter.notifyDataSetChanged();
    			}
    			
    		}
		}
		else if(requestCode == CULPRIT_RESULT && resultCode == RESULT_OK)
		{
			int culpritID = data.getIntExtra("id", -1);

			Util.log("Culprit ID deleted : "+culpritID);

			if(culpritID > -1)
			{
				CulpritDTO deletedCulpritDTO = null;
				for(DTO dto : feedList)
				{
					CulpritDTO culpritDTO = (CulpritDTO) dto;
					if(culpritDTO.ID == culpritID)
					{
						deletedCulpritDTO = culpritDTO;
					}
				}

				if(deletedCulpritDTO != null)
				{
					feedList.remove(deletedCulpritDTO);
					listListAdapter.notifyDataSetChanged();
				}

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
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		noMoreData = false;
		
		circleLoadingView.setVisibility(View.INVISIBLE);
		lastPage = 1;
		
		if(type.equalsIgnoreCase(Constants.COMPLAINT))
		{
			getFeedback(true);
		}
		else if(type.equalsIgnoreCase(Constants.THINKBOX))
		{
			getThinkBox(true);
		}
		else if(type.equalsIgnoreCase(Constants.THINKBOX_SUPPORT))
		 {
			 getThinkBoxSupporter(true);
		 }
		else if(type.equalsIgnoreCase(Constants.DEPARTMENT_REPORT))
		 {
			 getDepartmentReport(true);
		 }
		else if(type.equalsIgnoreCase(Constants.COUNCILLOR))
		 {
			 getCouncillor(true);
		 }
		else
		{
			getCulprit(true);
		}
			
	}
	
	private void getFeedback(final boolean clearAll)
	{
		AppAPI.getFeedback(lastPage, pageSize, zoneID,
				new DataHandler<ReportDTO>(ReportDTO.class) {

			
			
					@Override
					public void onSuccess(ReportDTO data) {
						
						loadingMore = false;
						if(clearAll)
						{
							feedList.clear();
						}
						
						if(data != null && data.complaints!= null)
						{
							if(data.complaints.size() < pageSize)
							{
								noMoreData = true;
							}
							
							for(ReportDTO reportDTO : data.complaints)
							{
								if(reportDTO.title != null && reportDTO.title.length() > 0
										&& reportDTO.description != null && reportDTO.description.length() > 0
										)
								feedList.add(reportDTO);
								
							}
							
							lastPage++;// + 1;
							
						}
						
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listListAdapter.notifyDataSetChanged();
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
					}

					@Override
					public void onFailure(ReportDTO data) {
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
						
						
					}
			});
	}
	
	private void getCulprit(final boolean clearAll)
	{
		AppAPI.getCulprit(lastPage, pageSize, zoneID,
				new DataHandler<CulpritDTO>(CulpritDTO.class) {

			
					@Override
					public void onSuccess(CulpritDTO data) {
						loadingMore = false;
						if(clearAll)
						{
							feedList.clear();
						}
					
						
						if(data != null && data.culprits!= null)
						{
							if(data.culprits.size() < pageSize)
							{
								noMoreData = true;
							}
							
							for(CulpritDTO culpritDTO : data.culprits)
							{
								if(culpritDTO.description != null && culpritDTO.description.length() > 0
										
										)
								{
								feedList.add(culpritDTO);
								}
							}
							
							lastPage++;// + 1;
							
						}
						
						
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listListAdapter.notifyDataSetChanged();
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
					}

					@Override
					public void onFailure(CulpritDTO data) {
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
						
						
					}
			});
	}
	
	
	private void getThinkBox(final boolean clearAll)
	{
		AppAPI.getThinkBox(lastPage, pageSize, zoneID,
				new DataHandler<ThinkBoxDTO>(ThinkBoxDTO.class) {

			
			
					@Override
					public void onSuccess(ThinkBoxDTO data) {
						
						loadingMore = false;
						if(clearAll)
						{
							feedList.clear();
						}
						
						if(data != null && data.thinkBoxes!= null)
						{
							if(data.thinkBoxes.size() < pageSize)
							{
								noMoreData = true;
							}
							
							for(ThinkBoxDTO thinkBoxDTO : data.thinkBoxes)
							{
								if(thinkBoxDTO.title != null && thinkBoxDTO.title.length() > 0
										&& thinkBoxDTO.description != null && thinkBoxDTO.description.length() > 0
										)
								feedList.add(thinkBoxDTO);
							}
							
							lastPage++;// + 1;
							
						}
						
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listListAdapter.notifyDataSetChanged();
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
					}

					@Override
					public void onFailure(ThinkBoxDTO data) {
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
						
						
					}
			});
	}
	

	private void getThinkBoxSupporter(final boolean clearAll)
	{
		AppAPI.getThinkBoxSupporter(lastPage, pageSize, AppCache.selectedThinkBoxDTO.ID,
				new DataHandler<ThinkBoxSupportDTO>(ThinkBoxSupportDTO.class) {

			
			
					@Override
					public void onSuccess(ThinkBoxSupportDTO data) {
						
						loadingMore = false;
						if(clearAll)
						{
							feedList.clear();
						}
						
						if(data != null && data.supports!= null)
						{
							if(data.supports.size() < pageSize)
							{
								noMoreData = true;
							}
							
							for(ThinkBoxSupportDTO thinkBoxSupportDTO : data.supports)
							{
								feedList.add(thinkBoxSupportDTO);
							}
							
							lastPage++;// + 1;
							
						}
						
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listListAdapter.notifyDataSetChanged();
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
					}

					@Override
					public void onFailure(ThinkBoxSupportDTO data) {
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
						
						
					}
			});
	}
	
	private void getDepartmentReport(final boolean clearAll)
	{
		AppAPI.getDepartmentResult(
				new DataHandler<DepartmentReportDTO>(DepartmentReportDTO.class) {

			
			
					@Override
					public void onSuccess(DepartmentReportDTO data) {
						
						loadingMore = false;
						if(clearAll)
						{
							feedList.clear();
						}
						
						if(data != null && data.results!= null)
						{
							if(data.results.size() < pageSize)
							{
								noMoreData = true;
							}
							
							for(DepartmentReportDTO departmentReportDTO : data.results)
							{
								feedList.add(departmentReportDTO);
							}
							
							lastPage++;// + 1;
							
						}
						
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listListAdapter.notifyDataSetChanged();
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
					}

					@Override
					public void onFailure(DepartmentReportDTO data) {
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
						
						
					}
			});
	}
	
	
	private void getCouncillor(final boolean clearAll)
	{
		AppAPI.getAllCouncillor(
				new DataHandler<CouncillorDetailDTO>(CouncillorDetailDTO.class) {

			
			
					@Override
					public void onSuccess(CouncillorDetailDTO data) {
						
						loadingMore = false;
						if(clearAll)
						{
							feedList.clear();
						}
						
						if(data != null && data.results!= null)
						{
							if(data.results.size() < pageSize)
							{
								noMoreData = true;
							}
							
							for(CouncillorDetailDTO councillorDetailDTO : data.results)
							{
								feedList.add(councillorDetailDTO);
							}
							
							lastPage++;// + 1;
							
						}
						
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listListAdapter.notifyDataSetChanged();
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
					}

					@Override
					public void onFailure(CouncillorDetailDTO data) {
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						bottomProgressBar.setVisibility(View.INVISIBLE);
						bottomProgressBar.progressiveStop();
						mPullToRefreshLayout.setRefreshComplete();
						
						
					}
			});
	}
}
