package com.RESTProject;

import java.io.IOException;
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
	public Response retrieveFromCache(String[] params){
		//Retrieve a response from local cache and return it
		//Should NOT make another call to Auroras.live
		
		return null;
	}
	
	public void storeInCache(Response rsp) throws IOException{
		//store a response in local cache
	}
	
	public boolean checkNoCaching(String c){
		if(c == null)
			return true; //TODO: This should return false once caching is implemented
		if(c.equals("true"))
			return true;
		return true;
	}
}

