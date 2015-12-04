package com.sbs.android.cleanmycity.adapter;

import java.util.Date;
import java.util.List;

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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListingViewAdapter extends BaseAdapter {

	private Context context;
	private List<DTO> feedList;
	private LayoutInflater vi;
	 
	public ListingViewAdapter(Context context, List<DTO> list) {
		  this.context = context;
		  this.feedList = list;
		  
		  vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
		 
		 
	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		
		final int index = position;
		DTO dto = feedList.get(position);
		
		if(dto instanceof ReportDTO)
        {
        	
        	if (rowView == null) 
        	{
    			rowView  = vi.inflate(R.layout.report_list_item, null);
    			//rowView.setTag("FACEBOOK-INSTAGRAM");
    	    }
        	//Util.log(rowView.getTag().toString());
        	
        	final ReportDTO reportDTO = (ReportDTO) feedList.get(position);

        	final TextView titleLabel = (TextView) rowView.findViewById(R.id.titleLabel);
        	final TextView detailLabel = (TextView) rowView.findViewById(R.id.detailLabel);
        	final TextView categoryLabel = (TextView) rowView.findViewById(R.id.categoryLabel);
        	final TextView timeLabel = (TextView) rowView.findViewById(R.id.timeLabel);
        	
        	final AppImageView imageView = (AppImageView) rowView.findViewById(R.id.reportImage);
        	
        	//imageView.loadImage(Constants.IMAGE_URL + reportDTO.image);
        	
        	Util.log("photo url : "+Constants.IMAGE_URL + "/400/" + reportDTO.photo);
        	
        	Picasso.with(context)
            .load(Constants.IMAGE_URL + "/400/" + reportDTO.photo)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.ic_launcher)
            .resize(200, 200)
            .centerCrop()
            .into(imageView);
        	
        	Date feedDate = Util.convertStringToDate(Util.convertToLocalDate(reportDTO.create_date), Constants.serverDateFormat);
        	
        	titleLabel.setText(reportDTO.title);
        	detailLabel.setText(reportDTO.description);
        	timeLabel.setText(Util.getRelativeLocalDateTime(feedDate.getTime())); //2015-01-22 16:58:03
        	
        	categoryLabel.setText(reportDTO.category.name.toUpperCase());
        	
        }
        else if(dto instanceof CulpritDTO)
        {
        	
        	if (rowView == null) 
        	{
    			rowView  = vi.inflate(R.layout.culprit_list_item, null);
    			
    	    }
        	
        	
        	final CulpritDTO culpritDTO = (CulpritDTO) feedList.get(position);

        	final TextView detailLabel = (TextView) rowView.findViewById(R.id.detailLabel);
        	final TextView categoryLabel = (TextView) rowView.findViewById(R.id.categoryLabel);
        	final TextView timeLabel = (TextView) rowView.findViewById(R.id.timeLabel);
        	
        	final AppImageView imageView = (AppImageView) rowView.findViewById(R.id.reportImage);
        	
        	
        	
        	Picasso.with(context)
            .load(Constants.IMAGE_URL + "/400/" + culpritDTO.photo)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.ic_launcher)
            .resize(200, 200)
            .centerCrop()
            .into(imageView);
        	
        	Date feedDate = Util.convertStringToDate(Util.convertToLocalDate(culpritDTO.create_date), Constants.serverDateFormat);
        	
        	detailLabel.setText(culpritDTO.description);
        	timeLabel.setText(Util.getRelativeLocalDateTime(feedDate.getTime())); //2015-01-22 16:58:03
        	
        	categoryLabel.setText(culpritDTO.category.name.toUpperCase());
        	
        }
        else if(dto instanceof ThinkBoxDTO)
        {
        	
        	if (rowView == null) 
        	{
    			rowView  = vi.inflate(R.layout.thinkbox_list_item, null);
    			//rowView.setTag("FACEBOOK-INSTAGRAM");
    	    }
        	//Util.log(rowView.getTag().toString());
        	
        	final ThinkBoxDTO thinkBoxDTO = (ThinkBoxDTO) feedList.get(position);

        	final TextView titleLabel = (TextView) rowView.findViewById(R.id.titleLabel);
        	final TextView detailLabel = (TextView) rowView.findViewById(R.id.detailLabel);
        	final TextView categoryLabel = (TextView) rowView.findViewById(R.id.categoryLabel);
        	final TextView timeLabel = (TextView) rowView.findViewById(R.id.timeLabel);
        	
        	final AppImageView imageView = (AppImageView) rowView.findViewById(R.id.reportImage);
        	
        	//imageView.loadImage(Constants.IMAGE_URL + reportDTO.image);
        	
        	if(thinkBoxDTO.photo > 0)
        	{
        		Picasso.with(context)
                .load(Constants.IMAGE_URL + "/400/" + thinkBoxDTO.photo)
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
        	
        	
        	
        	Date feedDate = Util.convertStringToDate(Util.convertToLocalDate(thinkBoxDTO.create_date), Constants.serverDateFormat);
        	
        	titleLabel.setText(thinkBoxDTO.title);
        	detailLabel.setText(thinkBoxDTO.description);
        	timeLabel.setText(Util.getRelativeLocalDateTime(feedDate.getTime())); //2015-01-22 16:58:03
        	
        	categoryLabel.setText(thinkBoxDTO.category.name.toUpperCase());
        	
        }
        else if(dto instanceof ThinkBoxSupportDTO)
        {
        	
        	if (rowView == null) 
        	{
    			rowView  = vi.inflate(R.layout.supporter_list_item, null);
    			
    	    }
        	
        	final ThinkBoxSupportDTO thinkBoxSupportDTO = (ThinkBoxSupportDTO) feedList.get(position);

        	final TextView userLabel = (TextView) rowView.findViewById(R.id.userLabel);
        	final TextView timeLabel = (TextView) rowView.findViewById(R.id.timeLabel);
        	
        	
        	Date feedDate = Util.convertStringToDate(Util.convertToLocalDate(thinkBoxSupportDTO.create_date), Constants.serverDateFormat);
        	userLabel.setText(thinkBoxSupportDTO.contactName);
        	timeLabel.setText(Util.getRelativeLocalDateTime(feedDate.getTime())); //2015-01-22 16:58:03
        	
        }
        else if(dto instanceof DepartmentReportDTO)
        {
        	
        	if (rowView == null) 
        	{
    			rowView  = vi.inflate(R.layout.department_list_item, null);
    			
    	    }
        	
        	final DepartmentReportDTO departmentReportDTO = (DepartmentReportDTO) feedList.get(position);

        	final TextView departmentLabel = (TextView) rowView.findViewById(R.id.departmentLabel);
        	final TextView departmentHeadValue = (TextView) rowView.findViewById(R.id.departmentHeadValue);
        	
        	final TextView receivedValueLabel = (TextView) rowView.findViewById(R.id.receivedValueLabel);
        	final TextView resolvedValueLabel = (TextView) rowView.findViewById(R.id.resolvedValueLabel);
        	final TextView progressValueLabel = (TextView) rowView.findViewById(R.id.progressValueLabel);
        	final TextView delayedValueLabel = (TextView) rowView.findViewById(R.id.delayedValueLabel);
        	
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
        	
        	receivedValueLabel.setText("Total Received : "+departmentReportDTO.Received); 
        	resolvedValueLabel.setText("Total Resolved : "+departmentReportDTO.Resolved); 
        	progressValueLabel.setText("In Progress : "+departmentReportDTO.InProgress); 
        	delayedValueLabel.setText("Delayed : "+String.format("%.0f", departmentReportDTO.Delayed) + "%"); 
        	
        }
        else if(dto instanceof CouncillorDetailDTO)
        {
        	
        	if (rowView == null) 
        	{
    			rowView  = vi.inflate(R.layout.councillor_list_item, null);
    			
    	    }
        	
        	final CouncillorDetailDTO councillorDetailDTO = (CouncillorDetailDTO) feedList.get(position);

        	final TextView councillorLabel = (TextView) rowView.findViewById(R.id.councillorLabel);
        	final TextView councillorNameValue = (TextView) rowView.findViewById(R.id.councillorNameValue);
        	final TextView councillorEmailValue = (TextView) rowView.findViewById(R.id.councillorEmailValue);
        	
        	final AppImageView imageView = (AppImageView) rowView.findViewById(R.id.councillorImage);
        	
        	if(councillorDetailDTO.photo > 0)
        	{
        		Picasso.with(context)
                .load(Constants.IMAGE_URL + "/400/" + councillorDetailDTO.photo)
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
        	
        	councillorLabel.setText(councillorDetailDTO.zone.name);
        	councillorNameValue.setText(councillorDetailDTO.name);
        	councillorEmailValue.setText(councillorDetailDTO.email);
        	
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
