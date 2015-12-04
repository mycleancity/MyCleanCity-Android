package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class ZoneDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<ZoneDTO>> {

		public ArrayList<ZoneDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<ZoneDTO> feeds = new ArrayList<ZoneDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				ZoneDTO feed = DataHandler.GSON.fromJson(o, ZoneDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	//public String cat_name;
	//{"categories":[{"name":"Loan Shark Advertisement","ID":1},{"name":"Illegal Dumping","ID":2}]}
	
	public ArrayList<ZoneDTO> zones;
	public String name;
	public String zid;
	public int ID;
}