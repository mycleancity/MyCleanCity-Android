package com.sbs.android.cleanmycity.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sbs.android.cleanmycity.R;
import com.sbs.android.cleanmycity.component.AppImageView;
import com.sbs.android.cleanmycity.model.CouncillorDetailDTO;
import com.sbs.android.cleanmycity.model.CulpritDTO;
import com.sbs.android.cleanmycity.model.DTO;
import com.sbs.android.cleanmycity.model.DepartmentReportDTO;
import com.sbs.android.cleanmycity.model.ReportDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxDTO;
import com.sbs.android.cleanmycity.model.ThinkBoxSupportDTO;
import com.sbs.android.cleanmycity.utils.Constants;
import com.sbs.android.cleanmycity.utils.Util;
import com.squareup.picasso.Picasso;

public class DepartmentDetailListingAdapter extends BaseAdapter {

	private Context context;
	private List<DTO> feedList;
	private LayoutInflater vi;
	 
	public DepartmentDetailListingAdapter(Context context, List<DTO> list) {
		  this.context = context;
		  this.feedList = list;
		  
		  vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
		 
		 
	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		
		final int index = position;
		DTO dto = feedList.get(position);
		
		
		final DepartmentReportDTO departmentReportDTO = (DepartmentReportDTO) feedList.get(position);
		
		if(position == 0)
        {
        	if (rowView == null || rowView.getTag().toString().equalsIgnoreCase("STATUS")) 
        	{
    			rowView  = vi.inflate(R.layout.department_details_list_item, null);
    			rowView.setTag("HEAD");
    	    }
        	
        	final TextView departmentLabel = (TextView) rowView.findViewById(R.id.departmentLabel);
        	final TextView departmentHeadValue = (TextView) rowView.findViewById(R.id.departmentHeadValue);
        	
        	final AppImageView imageView = (AppImageView) rowView.findViewById(R.id.departmentImage);
        	
        	if(departmentReportDTO.Photo > 0)
        	{
        		Picasso.with(context)
                .load(Constants.IMAGE_URL + "/400/" + departmentReportDTO.Photo)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_launcher)
                .resize(200, 200)
                .centerCrop()
                .into(imageView);
        	}
        	else
        	{
        		imageView.setImageResource(R.drawable.ic_launcher);
        	}
        	
        	departmentLabel.setText(departmentReportDTO.DepartmentName);
        	departmentHeadValue.setText(""+departmentReportDTO.Head);
        	
        	
        }
		else
		{
			if (rowView == null || rowView.getTag().toString().equalsIgnoreCase("HEAD")) 
        	{
    			rowView  = vi.inflate(R.layout.department_status_list_item, null);
    			rowView.setTag("STATUS");
    	    }
        	
        	final TextView statusLabel = (TextView) rowView.findViewById(R.id.statusLabel);
        	
        	final TextView receivedValueLabel = (TextView) rowView.findViewById(R.id.receivedValueLabel);
        	final TextView resolvedValueLabel = (TextView) rowView.findViewById(R.id.resolvedValueLabel);
        	final TextView progressValueLabel = (TextView) rowView.findViewById(R.id.progressValueLabel);
        	final TextView delayedValueLabel = (TextView) rowView.findViewById(R.id.delayedValueLabel);
        	
        	statusLabel.setText("Month : "+departmentReportDTO.Month);
        	
        	receivedValueLabel.setText("Total Received : "+departmentReportDTO.Received); 
        	resolvedValueLabel.setText("Total Resolved : "+departmentReportDTO.Resolved); 
        	progressValueLabel.setText("In Progress : "+departmentReportDTO.InProgress); 
        	delayedValueLabel.setText("Delayed : "+String.format("%.0f", departmentReportDTO.Delayed) + "%"); 
		}
        
            
		  
		return rowView;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return feedList.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return feedList.get(position);
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
}
