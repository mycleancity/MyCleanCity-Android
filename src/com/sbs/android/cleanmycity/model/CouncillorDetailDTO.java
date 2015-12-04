package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class CouncillorDetailDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<CouncillorDetailDTO>> {

		public ArrayList<CouncillorDetailDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<CouncillorDetailDTO> feeds = new ArrayList<CouncillorDetailDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				CouncillorDetailDTO feed = DataHandler.GSON.fromJson(o, CouncillorDetailDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	public ArrayList<ReportDTO> complaints;
	public ArrayList<CulpritDTO> culprits;
	public ArrayList<ThinkBoxDTO> thinkBoxes;
	public ArrayList<MapDTO> maps;
	
	public ArrayList<CouncillorDetailDTO> results;
	public CouncillorDetailDTO councillor;
	public ZoneDTO zone;
	
	public int ID;
	public String name;
	public String email;
	public String mobile;
	public int photo;
	
	
}