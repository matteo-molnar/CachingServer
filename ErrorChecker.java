package com.RESTProject;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.Headers;
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
	
	public String encodeSpaces(String raw){
		String temp = raw.replaceAll(" ", "%20");
		return temp;
	}
	
	public int checkArchiveErrors(JSONObject obj){
		if(!obj.has("statusCode"))
			return 0;
		return obj.getInt("statusCode");
	}
	
	public boolean checkImageErrors(Headers hd) throws UnirestException{
		String contentType = hd.getFirst("Content-Type");
		System.out.println(contentType);
		if(contentType.equals("application/json"))
			return true;
		
		return false;
	}
	
	public Response errorResponse(String module, String code){
		JSONObject obj = new JSONObject();
		String response = "Invalid URL";
		obj.put("Module", module);
		obj.put("Error code", code);
		obj.put("Error", response);
		return Response.status(Integer.parseInt(code)).entity(obj.toString()).build();
	}
}
