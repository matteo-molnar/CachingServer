package com.RESTProject;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.http.HttpHeaders;
import org.apache.tomcat.util.http.fileupload.IOUtils;

public class CacheController {
	TimerControl tc;
	
	public CacheController(){
		try {
			tc = new TimerControl();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Pass in a bunch of query parameters for a single URL
	public boolean checkCache(String[] params, boolean isImage) throws IOException{
		String fileName = "";
		for(int i = 0; i < params.length; i++){
			fileName += params[i];
		}
		
		File f = new File(fileName);
		if(f.exists() && tc.checkCachePeriodValid(fileName, isImage))
			return true;
		
		return false;
	}
	
	//Pass in a bunch of query parameters
	//To be called if and only if checkCache has returned true
	@Produces({ "application/json","image/jpeg"})
	public Response retrieveFromCache(String[] params, boolean isImage) throws IOException, ClassNotFoundException{
		//Retrieve a response from local cache and return it
		//Should NOT make another call to Auroras.live
		String fileName = "";
		for(int i = 0; i < params.length; i++){
			fileName += params[i];
		}
		
		if(isImage){
			fileName += ".jpeg";
			BufferedImage img = ImageIO.read(new File(fileName));
			Response rsp = Response.status(200).entity(img).header(HttpHeaders.CONTENT_TYPE, "image/jpeg").build();
			return rsp;
		}
		
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object obj = ois.readObject();
		Response rsp = Response.status(200).entity(obj).build();
		ois.close();
		return rsp;
	}
	
	public String replaceInvalidCharacters(String[] params){
		String fileName = "";
		for(int i = 0; i < params.length; i++){
			fileName += params[i];
		}
		String temp = fileName.replaceAll(":", "_");
		return temp;
	}
	
	public void storeInCache(Response rsp, String[] params) throws IOException, ClassNotFoundException{
		//store a response in local cache
		String fileName = replaceInvalidCharacters(params);
		
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		if(rsp.getEntity().getClass() == ByteArrayInputStream.class){
			ByteArrayInputStream bArrInput = (ByteArrayInputStream)rsp.getEntity();
			FileOutputStream fos2 = new FileOutputStream(fileName += ".jpeg");
			IOUtils.copy(bArrInput, fos2);
			fos2.flush();
			fos2.close();
		}
		else{
			oos.writeObject(rsp.getEntity());
			oos.flush();
			oos.close();
		}
		tc.updateCachingPeriod(fileName);
	}
	
	public boolean checkNoCaching(String c){
		if(c == null)
			return false; //TODO: This should return false once caching is implemented
		if(c.equals("true"))
			return true;
		return true;
	}
}

