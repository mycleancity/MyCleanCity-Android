package com.sbs.android.cleanmycity.model;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sbs.android.cleanmycity.http.DataHandler;

public class DepartmentReportDTO extends ErrorDTO implements DTO {
	
	public static class Deserializer implements
	JsonDeserializer<ArrayList<DepartmentReportDTO>> {

		public ArrayList<DepartmentReportDTO> deserialize(JsonElement json, Type typeOfT,

		JsonDeserializationContext context) throws JsonParseException {
			ArrayList<DepartmentReportDTO> feeds = new ArrayList<DepartmentReportDTO>();
			JsonArray array = json.getAsJsonArray();
			for (JsonElement o : array) {
				DepartmentReportDTO feed = DataHandler.GSON.fromJson(o, DepartmentReportDTO.class);
				feeds.add(feed);
			}
			return feeds;

		}

	}
	
	//{"results":[{"Delayed":100.0000,"Resolved":1,"Head":"Head","Received":1,"InProgress":4,"DepartmentName":"My Department","DepartmentID":1}]}
	
	
	public ArrayList<DepartmentReportDTO> results;
	
	public String Head;
	public String DepartmentName;
	public int Resolved;
	public int Received;
	public int InProgress;
	public int DepartmentID;
	public int Photo;
	public double Delayed;
	public String Month;
}

