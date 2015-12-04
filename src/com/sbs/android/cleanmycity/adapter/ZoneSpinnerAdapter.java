package com.sbs.android.cleanmycity.adapter;

import java.util.List;

import com.sbs.android.cleanmycity.R;
import com.sbs.android.cleanmycity.model.DTO;
import com.sbs.android.cleanmycity.model.ZoneDTO;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ZoneSpinnerAdapter implements  SpinnerAdapter{

	private ZoneDTO zoneDTO;
	private Context context;
	private LayoutInflater vi;
	
	
	public ZoneSpinnerAdapter(Context context, ZoneDTO dto)
	{
		this.context = context;
		this.zoneDTO = dto;
		vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		if(this.zoneDTO != null)
		{
			return zoneDTO.zones.size();
		}
		else
		{
			return 0;
		}
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return zoneDTO.zones.get(position).name;
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
		convertView  = vi.inflate(R.layout.simple_spinner_item, null);
		TextView textView = (TextView) convertView.findViewById(R.id.text1);
		textView.setText(zoneDTO.zones.get(position).name);
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
		convertView  = vi.inflate(R.layout.spinner_layout, null);
		CheckedTextView textView = (CheckedTextView) convertView.findViewById(R.id.text1);
		textView.setText(zoneDTO.zones.get(position).name);
		return convertView;
	}

}
