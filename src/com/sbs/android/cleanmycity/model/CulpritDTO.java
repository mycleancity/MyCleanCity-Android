package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class CulpritDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<CulpritDTO>> {

		public ArrayList<CulpritDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<CulpritDTO> feeds = new ArrayList<CulpritDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				CulpritDTO feed = DataHandler.GSON.fromJson(o, CulpritDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	public ArrayList<CulpritDTO> culprits;
	public CulpritDTO culprit;
	
	public String fullname;
	public String contactEmail;
	public String contactNo;
	public String longi;
	public String lat;
	public String address;
	public String description;
	public int photo;
	public String youtubelink;
	public boolean pubName;
	public boolean repeat_offender;
	public int commentCount;
	public String create_date;
	public int status;
	public String statusDisplay;
	public int slaTotalDays;
	public int slaLeftoverDays;
	public int ID;
	public UserDTO user;
	public CulpritCategoryDTO category; 
	
	
	
	

}
