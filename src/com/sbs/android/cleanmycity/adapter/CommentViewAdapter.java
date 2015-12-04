package com.sbs.android.cleanmycity.adapter;

import java.util.Date;
import java.util.List;

import com.sbs.android.cleanmycity.R;
import com.sbs.android.cleanmycity.model.CommentDTO;
import com.sbs.android.cleanmycity.model.DTO;
import com.sbs.android.cleanmycity.utils.Constants;
import com.sbs.android.cleanmycity.utils.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class CommentViewAdapter extends BaseAdapter {

	private Context context;
	private List<DTO> commentList;
	private LayoutInflater vi;
	private boolean isFacebokComment;
	private AdapterCallback callback;
	 
	public CommentViewAdapter(Context context, List<DTO> list, AdapterCallback adapterCallback) {
		  this.context = context;
		  this.commentList = list;
		  this.callback = adapterCallback;
		  vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
		 
		 
	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		
		
		
		
		if(position == 0)
		  {
			  
			  if(callback.checkStatus("", null))
			  {
				  rowView = vi.inflate(R.layout.emptyview, null);
			  }
			  else
			  {
				//Create custom load previous message button
				  rowView = vi.inflate(R.layout.previousmessage, null);
				  final Button previousMessageButton = (Button) rowView.findViewById(R.id.previousMessageButton);
				  previousMessageButton.setText("  Load more comments  ");
				  previousMessageButton.setOnClickListener(new View.OnClickListener() {
				        public void onClick(View view) {
				        	previousMessageButton.setText("  Loading comments..  ");
				        	callback.callTask("", null);;
				        }
				   });
			  }
		  }
		else
		{
			DTO dto = commentList.get(position - 1);
			rowView  = vi.inflate(R.layout.comment_list_item, parent, false);
			
			final CommentDTO commentDTO = (CommentDTO) dto;// commentList.get(position - 1);
			 final TextView userLabel = (TextView) rowView.findViewById(R.id.userLabel);
			 final TextView timeLabel = (TextView) rowView.findViewById(R.id.timeLabel);
			 final TextView detailLabel = (TextView) rowView.findViewById(R.id.detailLabel);
		
			 
		    //Populate Data
			 userLabel.setText(commentDTO.user.name);
			 
			 Date feedDate = Util.convertStringToDate(Util.convertToLocalDate(commentDTO.date), Constants.serverDateFormat);
			 timeLabel.setText(Util.getRelativeLocalDateTime(feedDate.getTime()));
			 detailLabel.setText(commentDTO.story);	
			 
		}
		 
		
		return rowView;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentList.size() + 1;
		
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		
		return commentList.get(position - 1);
		
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}