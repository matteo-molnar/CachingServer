package com.RESTProject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.ws.rs.core.Response;

public class CacheController {
	
	//Pass in a bunch of query parameters for a single URL
	public boolean checkCache(String[] params){
		//check parameters to see if an existing query has been made
		//return true if yes or return false if no
		
		return false;
	}
	
	//Pass in a bunch of query parameters
	//To be called if and only if checkCache has returned true
	public Response retrieveFromCache(String[] params) throws IOException, ClassNotFoundException{
		//Retrieve a response from local cache and return it
		//Should NOT make another call to Auroras.live
		String fileName = "";
		for(int i = 0; i < params.length; i++){
			fileName += params[i];
		}
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object obj = ois.readObject();
		Response rsp = Response.status(200).entity(obj).build();
		
		return rsp;
	}
	
	public void storeInCache(Response rsp, String[] params) throws IOException{
		//store a response in local cache
		String fileName = "";
		for(int i = 0; i < params.length; i++){
			fileName += params[i];
		}
		//TODO: Needs a "caching period" identifier
		
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(rsp.getEntity());
		oos.flush();
	}
	
	public boolean checkNoCaching(String c){
		if(c == null)
			return true; //TODO: This should return false once caching is implemented
		if(c.equals("true"))
			return true;
		return true;
	}
}

