package com.sbs.android.cleanmycity;

import java.util.ArrayList;
import java.util.Collections;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.sbs.android.cleanmycity.adapter.AdapterCallback;
import com.sbs.android.cleanmycity.adapter.CommentViewAdapter;
import com.sbs.android.cleanmycity.adapter.ListingViewAdapter;
import com.sbs.android.cleanmycity.http.AppAPI;
import com.sbs.android.cleanmycity.http.DataHandler;
import com.sbs.android.cleanmycity.model.CommentDTO;
import com.sbs.android.cleanmycity.model.CulpritDTO;
import com.sbs.android.cleanmycity.model.DTO;
import com.sbs.android.cleanmycity.model.ReportDTO;
import com.sbs.android.cleanmycity.utils.AppCache;
import com.sbs.android.cleanmycity.utils.Constants;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class CommentActivity  extends Activity implements AdapterCallback{

	private ListView listView;
	private CommentViewAdapter listAdapter;
	public ArrayList<DTO> commentList;
	private boolean loadingMore = true;	
	private boolean noMoreData = false;	
	private Button messageSend;
	private EditText commentInput;
    
	private CircularProgressBar circleLoadingView;
	
    private int scrollLastVisibleItem = 0;
    private boolean animationStarted =  false;
    private boolean didScrollAfterStart =  false;
    
    private String lastDate;
    private int pageSize = 10;
    
    private String type = Constants.COMPLAINT;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			type = extras.getString("type");
		}
		
		
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Comment");
		
		circleLoadingView = (CircularProgressBar) findViewById(R.id.commentLoadingView);
		circleLoadingView.setVisibility(View.VISIBLE);
        
		messageSend = (Button) findViewById(R.id.messageSend);
		commentInput = (EditText) findViewById(R.id.commentInput);
		
        listView = (ListView) findViewById(R.id.comment_list);
        
        commentList = new ArrayList<DTO>();
        listAdapter = new CommentViewAdapter(this, commentList, this);
        listView.setAdapter(listAdapter);
        
        messageSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(commentInput.getText() != null && commentInput.getText().length() > 0)
				{
					if(type.equalsIgnoreCase(Constants.COMPLAINT))
			        {
						createFeedbackComment(commentInput.getText().toString());
			        }
					else if(type.equalsIgnoreCase(Constants.THINKBOX))
			        {
						createThinkBoxComment(commentInput.getText().toString());
			        }
			        else
			        {
			        	createCulpritComment(commentInput.getText().toString());
			        }
					
				}
			}
		});
        
        lastDate = "";
        if(type.equalsIgnoreCase(Constants.COMPLAINT))
        {
        	getFeedbackComment(true);
        }
        else if(type.equalsIgnoreCase(Constants.THINKBOX))
        {
        	getThinkBoxComment(true);
        }
        else
        {
        	getCulpritComment(true);
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
	
	private void getComment()
	{
		for(int i = 0; i < pageSize+1; i ++)
		{
			CommentDTO dto = new CommentDTO();
			commentList.add(dto);
		}
		//noMoreData = true;
		loadingMore = false;
		circleLoadingView.setVisibility(View.INVISIBLE);
		listView.setVisibility(View.VISIBLE);
		listAdapter.notifyDataSetChanged();
	}
	
	private void getFeedbackComment(final boolean clearAll)
	{
		AppAPI.getFeedbackComment(lastDate, pageSize,AppCache.selectedReportDTO.ID,
				new DataHandler<CommentDTO>(CommentDTO.class) {

			
			
					@Override
					public void onSuccess(CommentDTO data) {
						loadingMore = false;
						if(clearAll)
						{
							commentList.clear();
						}
						
						if(data != null && data.comments != null  && data.comments.size() > 0)
						{
							if(data.comments.size() < pageSize)
							{
								noMoreData = true;
							}
							
							
							
							
							for(CommentDTO commentDTO : data.comments)
							{
								commentList.add(0,commentDTO);
							}
							
							lastDate = ((CommentDTO)(commentList.get(0))).date;
						}
						else
						{
							noMoreData = true;
						}
						
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listAdapter.notifyDataSetChanged();
						
						
					}

					@Override
					public void onFailure(CommentDTO data) {
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						
					}
			});
	}
	
	private void getCulpritComment(final boolean clearAll)
	{
		AppAPI.getCulpritComment(lastDate, pageSize,AppCache.selectedCulpritDTO.ID,
				new DataHandler<CommentDTO>(CommentDTO.class) {

			
			
					@Override
					public void onSuccess(CommentDTO data) {
						loadingMore = false;
						if(clearAll)
						{
							commentList.clear();
						}
						
						if(data != null && data.comments != null && data.comments.size() > 0)
						{
							if(data.comments.size() < pageSize)
							{
								noMoreData = true;
							}
							
							for(CommentDTO commentDTO : data.comments)
							{
								commentList.add(0,commentDTO);
							}
							
							lastDate = ((CommentDTO)(commentList.get(0))).date;
						}
						else
						{
							noMoreData = true;
						}
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listAdapter.notifyDataSetChanged();
						
						
					}

					@Override
					public void onFailure(CommentDTO data) {
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						
					}
			});
	}
	
	private void getThinkBoxComment(final boolean clearAll)
	{
		AppAPI.getThinkBoxComment(lastDate, pageSize,AppCache.selectedThinkBoxDTO.ID,
				new DataHandler<CommentDTO>(CommentDTO.class) {

			
			
					@Override
					public void onSuccess(CommentDTO data) {
						loadingMore = false;
						if(clearAll)
						{
							commentList.clear();
						}
						
						if(data != null && data.comments != null  && data.comments.size() > 0)
						{
							if(data.comments.size() < pageSize)
							{
								noMoreData = true;
							}
							
							
							
							
							for(CommentDTO commentDTO : data.comments)
							{
								commentList.add(0,commentDTO);
							}
							
							lastDate = ((CommentDTO)(commentList.get(0))).date;
						}
						else
						{
							noMoreData = true;
						}
						
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listAdapter.notifyDataSetChanged();
						
						
					}

					@Override
					public void onFailure(CommentDTO data) {
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						
					}
			});
	}
	
	private void createFeedbackComment(String comment)
	{
		messageSend.setEnabled(false);
		AppAPI.createFeedbackComment(comment, AppCache.selectedReportDTO.ID,
				new DataHandler<CommentDTO>(CommentDTO.class) {

			
			
					@Override
					public void onSuccess(CommentDTO data) {
						loadingMore = false;
						
						messageSend.setEnabled(true);
						if(data != null && data.comment != null)
						{
							commentList.add(data.comment);
							AppCache.selectedReportDTO.commentCount = AppCache.selectedReportDTO.commentCount + 1;
						}
						
						commentInput.setText("");
						
						InputMethodManager imm = (InputMethodManager)getSystemService(
							      Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(commentInput.getWindowToken(), 0);
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listAdapter.notifyDataSetChanged();
						
						
					}

					@Override
					public void onFailure(CommentDTO data) {
						
						messageSend.setEnabled(true);
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						
					}
			});
	}
	
	private void createCulpritComment(String comment)
	{
		messageSend.setEnabled(false);
		AppAPI.createCulpritComment(comment, AppCache.selectedCulpritDTO.ID,
				new DataHandler<CommentDTO>(CommentDTO.class) {

			
			
					@Override
					public void onSuccess(CommentDTO data) {
						loadingMore = false;
						
						messageSend.setEnabled(true);
						if(data != null && data.comment != null)
						{
							commentList.add(data.comment);
							AppCache.selectedCulpritDTO.commentCount = AppCache.selectedCulpritDTO.commentCount + 1;
						}
						
						commentInput.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(
							      Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(commentInput.getWindowToken(), 0);
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listAdapter.notifyDataSetChanged();
						
						
					}

					@Override
					public void onFailure(CommentDTO data) {
						
						messageSend.setEnabled(true);
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						
					}
			});
	}
	
	private void createThinkBoxComment(String comment)
	{
		messageSend.setEnabled(false);
		AppAPI.createThinkBoxComment(comment, AppCache.selectedThinkBoxDTO.ID,
				new DataHandler<CommentDTO>(CommentDTO.class) {

			
			
					@Override
					public void onSuccess(CommentDTO data) {
						loadingMore = false;
						
						messageSend.setEnabled(true);
						if(data != null && data.comment != null)
						{
							commentList.add(data.comment);
							AppCache.selectedThinkBoxDTO.commentCount = AppCache.selectedThinkBoxDTO.commentCount + 1;
						}
						
						commentInput.setText("");
						
						InputMethodManager imm = (InputMethodManager)getSystemService(
							      Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(commentInput.getWindowToken(), 0);
						
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						listAdapter.notifyDataSetChanged();
						
						
					}

					@Override
					public void onFailure(CommentDTO data) {
						
						messageSend.setEnabled(true);
						circleLoadingView.setVisibility(View.INVISIBLE);
						listView.setVisibility(View.VISIBLE);
						
						
					}
			});
	}
	
	

	@Override
	public void callTask(String task, Object[] parameter) {
		// TODO Auto-generated method stub
		if(!loadingMore)
		{
			loadingMore = true;
			//retrieveFacebookComments(false, afterID);
			//getComment();
			//getFeedbackComment(false);
			
			if(type.equalsIgnoreCase(Constants.COMPLAINT))
	        {
	        	getFeedbackComment(false);
	        	//getComment();
	        }
			else if(type.equalsIgnoreCase(Constants.THINKBOX))
	        {
	        	getThinkBoxComment(false);
	        	//getComment();
	        }
	        else
	        {
	        	getCulpritComment(false);
	        	
	        }
		}
	}

	@Override
	public boolean checkStatus(String task, String[] parameter) {
		// TODO Auto-generated method stub
		if(noMoreData == false)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	

}