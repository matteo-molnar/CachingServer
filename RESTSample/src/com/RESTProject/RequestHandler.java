package com.RESTProject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Path("/")

public class RequestHandler
{
	JSONObject obj; 
	 @Path("")
	 @GET
	 @Produces("application/json")
	 public Response requestHandler(@Context UriInfo info) throws JSONException, UnirestException 
	 {
		String type = info.getQueryParameters().getFirst("type");
		String data = info.getQueryParameters().getFirst("data");
		obj = new JSONObject(); 
		
		if(type.equals("all")){
			return Response.status(200).entity(AllRequest().getBody().toString()).build();
		}
		
		//This part is working (i think)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		else if(type.equals("ace")){
			return Response.status(200).entity(AceRequest(data).getBody().toString()).build();
		}
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		else if(type.equals("archive")){
			return Response.status(200).entity(ArchiveRequest().getBody().toString()).build();
		}
		
		else if(type.equals("embed")){
			return Response.status(200).entity(EmbedRequest().getBody().toString()).build();
		}
		
		else if(type.equals("images")){
			return Response.status(200).entity(ImagesRequest().getBody().toString()).build();
		}
		
		else if(type.equals("weather")){
			return Response.status(200).entity(WeatherRequest().getBody().toString()).build();
		}
		//This part is working (I think)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		else if(type.equals("locations")){
			return Response.status(200).entity(LocationRequest().getBody().toString()).build();
		}
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		else{
			String response = "Invalid URL";
			return Response.status(404).entity(response).build();
		}
	 }

	 private HttpResponse<JsonNode> AllRequest(/*String[] parameters*/){
		//TODO: Implement this
		return null;
	 }
	 private HttpResponse<JsonNode> ArchiveRequest(/*String[] parameters*/){
		//TODO: Implement this
		return null;
	 }
	 
	 private HttpResponse<JsonNode> EmbedRequest(/*String[] parameters*/){
		//TODO: Implement this
		return null;
	 }
	 
	 private HttpResponse<JsonNode> Request(/*String[] parameters*/){
		//TODO: Implement this
		return null;
	 }
	 
	 private HttpResponse<JsonNode> ImagesRequest(/*String[] parameters*/){
		//TODO: Implement this
		return null;
	 }
	 
	 private HttpResponse<JsonNode> WeatherRequest(/*String[] parameters*/){
		//TODO: Implement this
		return null;
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
