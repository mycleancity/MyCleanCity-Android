package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class ThinkBoxCategoryDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<ThinkBoxCategoryDTO>> {

		public ArrayList<ThinkBoxCategoryDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<ThinkBoxCategoryDTO> feeds = new ArrayList<ThinkBoxCategoryDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				ThinkBoxCategoryDTO feed = DataHandler.GSON.fromJson(o, ThinkBoxCategoryDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	public ArrayList<ThinkBoxCategoryDTO> categories;
	public String name;
	public int ID;
}