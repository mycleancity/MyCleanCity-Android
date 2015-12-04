package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class UserDTO extends ErrorDTO implements DTO{

	public static class Deserializer implements
	JsonDeserializer<ArrayList<UserDTO>> {

		public ArrayList<UserDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<UserDTO> feeds = new ArrayList<UserDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				UserDTO feed = DataHandler.GSON.fromJson(o, UserDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	//"user_id":"51","user_zone":""
	//public String user_id;
	//public String user_zone;
	
	
	//NEW PARAM
	//{"user":{"name":"fong huang yee","email":"fong1@gmail.com","ID":4}}
	public String name;
	public String email;
	public int ID;
}
