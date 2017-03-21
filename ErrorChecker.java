package com.RESTProject;

import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ErrorChecker {

	public String[] checkMapErrors(String id) throws UnirestException{
		HttpResponse<JsonNode> temp = Unirest.get("http://api.auroras.live/v1/?type=locations").asJson();
		JSONArray jsonArray = temp.getBody().getArray();

		String latitude = null;
		String longitude = null;

		for(int i = 0; i < jsonArray.length(); i++) {
			if(id.equals(jsonArray.getJSONObject(i).getString("id"))){
				latitude = jsonArray.getJSONObject(i).getString("lat");
				longitude = jsonArray.getJSONObject(i).getString("long");
			}
		}

		if(latitude == null || longitude == null)
			return null;
		String[] a = {latitude, longitude};
		return a;
	}
	
	public Response errorResponse(){
		String response = "Invalid URL";
		return Response.status(404).entity(response).build();
	}
}
