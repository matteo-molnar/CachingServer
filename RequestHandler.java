package com.RESTProject;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.HttpHeaders;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

@Path("/")
public class RequestHandler<T>
{
	 JSONObject obj; 
	 JSONArray arr;
	 @Path("")
	 @GET
	 @Produces({ "application/json","image/jpeg"})
	 public Response requestHandler(@Context UriInfo info) throws JSONException, UnirestException 
	 {
		String type = info.getQueryParameters().getFirst("type");
		String data = info.getQueryParameters().getFirst("data");
		String latitude = info.getQueryParameters().getFirst("lat");
		String longitude = info.getQueryParameters().getFirst("long");
		
		//Used by weather "module" and "all" module
		String forecast = info.getQueryParameters().getFirst("forecast");
		String action = info.getQueryParameters().getFirst("action");
		String start = info.getQueryParameters().getFirst("start");
		String end = info.getQueryParameters().getFirst("end");
		String image = info.getQueryParameters().getFirst("image");
		
		//The following seven are used by the "all" module
		String ace = info.getQueryParameters().getFirst("ace");
		String archive = info.getQueryParameters().getFirst("archive");
		String images = info.getQueryParameters().getFirst("images");
		String probability = info.getQueryParameters().getFirst("probability");
		String threeday = info.getQueryParameters().getFirst("threeday");
		String twentysevenday = info.getQueryParameters().getFirst("twentysevenday");
		String weather = info.getQueryParameters().getFirst("weather");
		String[] allModule = {ace, archive, forecast, images, probability, threeday, twentysevenday, weather, latitude, longitude};
		
		//Used by "map" module (Google maps API)
		String id = info.getQueryParameters().getFirst("id");
		
		obj = new JSONObject(); 
		arr = new JSONArray();
		
		if(type.equals("all")){
			//working
			return Response.status(200).entity(AllRequest(allModule).getBody().toString()).build();
		}
		
		else if(type.equals("ace")){
			//Working
			return Response.status(200).entity(AceRequest(data).getBody().toString()).build();
		}
		
		else if(type.equals("archive")){
			//Working
			return Response.status(200).entity(ArchiveRequest(action, start, end).getBody().toString()).build();
		}
		
		else if(type.equals("embed")){
			//Working
			return Response.status(200).entity(EmbedRequest(image, latitude, longitude).getBody()).header(HttpHeaders.CONTENT_TYPE, "image/jpeg").build();
		}
		
		else if(type.equals("map")){
			//working (very glitchy)
			return Response.status(200).entity(MapRequest(id).getBody()).header(HttpHeaders.CONTENT_TYPE, "image/jpeg").build();
		}
		
		else if(type.equals("images")){
			//working
			if(action != null)
				return Response.status(200).entity(ImagesRequestJson(action).getBody().toString()).build();
			if(image != null)
				return Response.status(200).entity(ImageRequestBinary(image).getBody()).header(HttpHeaders.CONTENT_TYPE, "image/jpeg").build();
		}
		
		else if(type.equals("weather")){
			//Working
			return Response.status(200).entity(WeatherRequest(latitude, longitude, forecast).getBody().toString()).build();
		}
		
		else if(type.equals("locations")){
			//Working
			return Response.status(200).entity(LocationRequest().getBody().toString()).build();
		}
	
		else{
			String response = "Invalid URL";
			return Response.status(404).entity(response).build();
		}
		//Should not reach here
		return null;
	 }
	 
	 private HttpResponse<JsonNode> AllRequest(String[] parameters) throws UnirestException{
		//{ace, archive, forecast, images, probability, threeday, twentysevenday, weather, latitude, longitude}
		String url = "http://api.auroras.live/v1/?type=all";
		if(parameters[0] != null){
			url += "&ace=";
			url += parameters[0];
		}
		if(parameters[1] != null){
			url += "&archive=";
			url += parameters[1];
		}
		if(parameters[2] != null){
			url += "&forecast=";
			url += parameters[2];
		}
		if(parameters[3] != null){
			url += "&images=";
			url += parameters[3];
		}
		if(parameters[4] != null){
			url += "&probability=";
			url += parameters[4];
		}
		if(parameters[5] != null){
			url += "&threeday=";
			url += parameters[5];
		}
		if(parameters[6] != null){
			url += "&twentysevenday=";
			url += parameters[7];
		}
		if(parameters[7] != null){
			url += "&weather=";
			url += parameters[7];
		}
		
		url += "&lat=";
		url += parameters[8];
			
		url += "&long=";
		url += parameters[9];
		
		HttpResponse<JsonNode> response = Unirest.get(url).asJson();
		obj = response.getBody().getObject();
		String att = "Powered by Auroras.live";
		obj.put("Attribution", att);
		return response;
	 }
	 
	 private HttpResponse<JsonNode> ArchiveRequest(String action, String start, String end) throws UnirestException{
		String url = "http://api.auroras.live/v1/?type=archive";
		url += "&action=";
		url += action;
		if(action.equals("search")){
			url += "&start=";
			url += start;
			
			url += "&end=";
			url += end;
		}
		HttpResponse<JsonNode> response = Unirest.get(url).asJson();
		obj = response.getBody().getObject();
		String att = "Powered by Auroras.live";
		obj.put("Attribution", att);
		return response;
	 }
	 
	 private HttpResponse<InputStream> EmbedRequest(String image, String latitude, String longitude) throws UnirestException{
		String url = "http://api.auroras.live/v1/?type=embed";
		url += "&image=";
		url += image;
		
		url += "&lat=";
		url += latitude;
			
		url += "&long=";
		url += longitude;
		
		HttpRequest request = Unirest.get(url);
		request.header("Accept", "image/jpeg");
		HttpResponse<InputStream> response = request.asBinary();
		
		return response;
	 }
	 
	 private HttpResponse<InputStream> MapRequest(String id) throws UnirestException{
		HttpResponse<JsonNode> temp = Unirest.get("http://api.auroras.live/v1/?type=locations").asJson();
		JSONArray jsonArray = temp.getBody().getArray();

		String latitude = null;
		String longitude = null;
		
		for(int i = 0; i < jsonArray.length(); i++){
			if(id.equals(jsonArray.getJSONObject(i).getString("id"))){
				latitude = jsonArray.getJSONObject(i).getString("lat");
				longitude = jsonArray.getJSONObject(i).getString("long");
			}
		}
		String url = "https://maps.googleapis.com/maps/api/staticmap?";
		url += "center=";
		url += latitude + "," + longitude;
		
		System.out.println(latitude + "," + longitude);
		url += "&zoom=10";
		url += "&size=600x300";
		
		url += "&markers=color:red%7Clabel:S%7C";
		url += latitude + "," + longitude;
				
		
		HttpRequest request = Unirest.get(url);
		request.header("Accept", "image/jpeg");
		HttpResponse<InputStream> response = request.asBinary();
		
		return response;
	 }
	 
	 private HttpResponse<JsonNode> ImagesRequestJson(String action) throws UnirestException{
		String url = "http://api.auroras.live/v1/?type=images";
		url += "&action=";
		url += action;
		HttpResponse<JsonNode> response = Unirest.get(url).asJson();
		obj = response.getBody().getObject();
		String att = "Powered by Auroras.live";
		obj.put("Attribution", att);
		return response;
	 }
	 
	 private HttpResponse<InputStream> ImageRequestBinary(String image) throws UnirestException{
		 String url = "http://api.auroras.live/v1/?type=images";		
		 url += "&image=";
		 url += image;
		 HttpRequest request = Unirest.get(url);
		 request.header("Accept", "image/jpeg");
		 HttpResponse<InputStream> response = request.asBinary();
		 return response;
	 }
	 
	 
	 private HttpResponse<JsonNode> WeatherRequest(String latitude, String longitude, String forecast) throws UnirestException{
		String url = "http://api.auroras.live/v1/?type=weather";
		url += "&lat=";
		url += latitude;
		
		url += "&long=";
		url += longitude;
		
		if(forecast != null){
			url += "&forecast=";
			url += forecast;
		}
		HttpResponse<JsonNode> response = Unirest.get(url).asJson();
		obj = response.getBody().getObject();
		String att = "Powered by Auroras.live";
		obj.put("Attribution", att);
		return response;
	 }
	 
	 private HttpResponse<JsonNode> AceRequest(String data) throws UnirestException{
		String url = "http://api.auroras.live/v1/?type=ace";
		if(data != null){
			url += "&data=";
			url += data;
		}
		HttpResponse<JsonNode> response = Unirest.get(url).asJson();
		obj = response.getBody().getObject();
		String att = "Powered by Auroras.live";
		obj.put("Attribution", att);
		return response;
	 }
	 
	 private HttpResponse<JsonNode> LocationRequest() throws UnirestException{
		 HttpResponse<JsonNode> response = Unirest.get("http://api.auroras.live/v1/?type=locations").asJson();
		 JSONArray jsonArray = response.getBody().getArray();
		 obj = new JSONObject();
		 String att = "Powered by Auroras.live";
		 obj.put("Attribution", att);
		 jsonArray.put(jsonArray.length(), obj);
		 
		 return response;
	 }
}
