package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class ReportDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<ReportDTO>> {

		public ArrayList<ReportDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<ReportDTO> feeds = new ArrayList<ReportDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				ReportDTO feed = DataHandler.GSON.fromJson(o, ReportDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	public ArrayList<ReportDTO> complaints;
	public ReportDTO complaint;
	
	public String fullname;
	public String contactEmail;
	public String contactNo;
	public String title;
	public String description;
	public String address;
	public int photo;
	public int commentCount;
	public String create_date;
	public int status;
	public int slaTotalDays;
	public int slaLeftoverDays;
	public String longi;
	public String lat;
	public int ID;
	public String ref;
	public ReportCategoryDTO category;
	public UserDTO user;		
	

}
