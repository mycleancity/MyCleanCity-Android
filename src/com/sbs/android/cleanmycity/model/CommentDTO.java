package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class CommentDTO  extends ErrorDTO implements DTO {

	public static class Deserializer implements
	JsonDeserializer<ArrayList<CommentDTO>> {

		public ArrayList<CommentDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<CommentDTO> feeds = new ArrayList<CommentDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				CommentDTO feed = DataHandler.GSON.fromJson(o, CommentDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	public ArrayList<CommentDTO> comments;
	public CommentDTO comment;
	public UserDTO user;
	public String story;
	public String date;
}
