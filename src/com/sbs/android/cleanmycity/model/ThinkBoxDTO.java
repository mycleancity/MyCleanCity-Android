package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class ThinkBoxDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<ThinkBoxDTO>> {

		public ArrayList<ThinkBoxDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<ThinkBoxDTO> feeds = new ArrayList<ThinkBoxDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				ThinkBoxDTO feed = DataHandler.GSON.fromJson(o, ThinkBoxDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	//{"thinkBox":{"contactName":"fong","contactEmail":"Hello@gmail.com","contactNo":"01232932323","title":"Hello","description":"This is story",
	//"feasibility":1,"supportCount":0,"commentCount":0,"category":{"name":"CategoryA","ID":1},
	//"create_date":"2015-05-07 16:12:22","user":{"name":"Admin","email":"admin@gmail.com","ID":1},"ID":1}}
	
	public ArrayList<ThinkBoxDTO> thinkBoxes;
	public ThinkBoxDTO thinkBox;
	
	public String statusDisplay;
	public int status;
	public String contactName;
	public String contactEmail;
	public String contactNo;
	public String title;
	public String description;
	public int feasibility;
	public int supportCount;
	public int commentCount;
	public String create_date;
	public int photo;
	public ZoneDTO zone;
	public int kickoffCount;
	public boolean supported;
	public int ID;
	public ThinkBoxCategoryDTO category;
	public UserDTO user;		
	

}