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
public class RequestHandler
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
		String forecast = info.getQueryParameters().getFirst("forecast");
		String action = info.getQueryParameters().getFirst("action");
		String start = info.getQueryParameters().getFirst("start");
		String end = info.getQueryParameters().getFirst("end");
		String image = info.getQueryParameters().getFirst("image");
		
		obj = new JSONObject(); 
		arr = new JSONArray();
		
		if(type.equals("all")){
			return Response.status(200).entity(AllRequest().getBody().toString()).build();
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
			return Response.status(200).entity(MapRequest().getBody().toString()).build();
		}
		
		else if(type.equals("images")){
			return Response.status(200).entity(ImagesRequest().getBody().toString()).build();
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
	 }
	 
	 private HttpResponse<JsonNode> AllRequest(/*String[] parameters*/){
		//TODO: Implement this
		return null;
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
	 
	 private HttpResponse<JsonNode> MapRequest(/*String[] parameters*/){
		//TODO: Implement this
		return null;
	 }
	 
	 private HttpResponse<JsonNode> ImagesRequest(/*String[] parameters*/){
		//TODO: Implement this
		return null;
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
