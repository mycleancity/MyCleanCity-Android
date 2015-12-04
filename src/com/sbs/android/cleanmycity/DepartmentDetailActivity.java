package com.sbs.android.cleanmycity;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

import com.sbs.android.cleanmycity.adapter.DepartmentDetailListingAdapter;
import com.sbs.android.cleanmycity.adapter.ListingViewAdapter;
import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.CouncillorDetailDTO;
import com.sbs.android.cleanmycity.model.CulpritDTO;
import com.sbs.android.cleanmycity.model.DTO;
import com.sbs.android.cleanmycity.model.DepartmentReportDTO;
import com.sbs.android.cleanmycity.model.ReportDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxSupportDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Constants;
import com.sbs.android.cleanmycity.utils.Util;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class DepartmentDetailActivity  extends Activity{

	private ListView listView;
	private DepartmentDetailListingAdapter listListAdapter;
	public ArrayList<DTO> departmentDetailsList;
	
	private CircularProgressBar circleLoadingView;
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Department Details");
		
		circleLoadingView = (CircularProgressBar) findViewById(R.id.feedLoadingView);
		circleLoadingView.setVisibility(View.VISIBLE);
        
        listView = (ListView) findViewById(R.id.department_list);
        
        departmentDetailsList = new ArrayList<DTO>();
        listListAdapter = new DepartmentDetailListingAdapter(this, departmentDetailsList);
        listView.setAdapter(listListAdapter);
        
        
       getDepartmentDetails();
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
	
	
	
	private void getDepartmentDetails()
	{
		AppAPI.getDepartmentDetails(AppCache.selectedDepartmentDTO.DepartmentID, 
				new DataHandler<DepartmentReportDTO>(DepartmentReportDTO.class) {
			
					@Override
					public void onSuccess(DepartmentReportDTO data) {
						
						if(data != null && data.results != null)
						{
							departmentDetailsList.add(AppCache.selectedDepartmentDTO);
							for(DepartmentReportDTO reportDTO : data.results)
							{
								Util.log("reportDTO " + reportDTO.Month);
								departmentDetailsList.add(reportDTO);
							}
						}
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listListAdapter.notifyDataSetChanged();
						
					}

					@Override
					public void onFailure(DepartmentReportDTO data) {
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
					}
			});
	}
	
	
}
