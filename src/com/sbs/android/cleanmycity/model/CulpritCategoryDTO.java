package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class CulpritCategoryDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<CulpritCategoryDTO>> {

		public ArrayList<CulpritCategoryDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<CulpritCategoryDTO> feeds = new ArrayList<CulpritCategoryDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				CulpritCategoryDTO feed = DataHandler.GSON.fromJson(o, CulpritCategoryDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	//public String cat_name;
	//{"categories":[{"name":"Loan Shark Advertisement","ID":1},{"name":"Illegal Dumping","ID":2}]}
	
	public ArrayList<CulpritCategoryDTO> categories;
	public String name;
	public int ID;
}
