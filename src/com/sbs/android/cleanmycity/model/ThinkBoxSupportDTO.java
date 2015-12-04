package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class ThinkBoxSupportDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<ThinkBoxSupportDTO>> {

		public ArrayList<ThinkBoxSupportDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<ThinkBoxSupportDTO> feeds = new ArrayList<ThinkBoxSupportDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				ThinkBoxSupportDTO feed = DataHandler.GSON.fromJson(o, ThinkBoxSupportDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	//{"support":{"contactName":"fong","contactEmail":"Hello@gmail.com","contactNo":"01232932323","create_date":"2015-05-07 18:12:43","ID":1}}
	
	public ArrayList<ThinkBoxSupportDTO> supports;
	public ThinkBoxSupportDTO support;
	
	public String contactName;
	public String contactEmail;
	public String contactNo;
	
	public String create_date;
	
	public int ID;
	
	

}