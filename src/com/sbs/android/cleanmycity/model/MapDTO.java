package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class MapDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<MapDTO>> {

		public ArrayList<MapDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<MapDTO> feeds = new ArrayList<MapDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				MapDTO feed = DataHandler.GSON.fromJson(o, MapDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	public ArrayList<MapDTO> maps;
	
	public String latitude;
	public String longitude;
	public int status;		
	

}