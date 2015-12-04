package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class ReportCategoryDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<ReportCategoryDTO>> {

		public ArrayList<ReportCategoryDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<ReportCategoryDTO> feeds = new ArrayList<ReportCategoryDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				ReportCategoryDTO feed = DataHandler.GSON.fromJson(o, ReportCategoryDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	
	//public String cat_name;
	
	//{"categories":[{"name":"Buld Garbage","ID":1},{"name":"Domestic Garbage","ID":2}]}
	public ArrayList<ReportCategoryDTO> categories;
	public String name;
	public int ID;
	
	

}
