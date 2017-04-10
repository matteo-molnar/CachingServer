package com.RESTProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class TimerControl {
	File f = new File("CachingPeriod.txt");
	private long cachingPeriod = 5;
	
	public TimerControl() throws IOException{
		if(!f.exists()){
			f.createNewFile();
		}
	}
	
	public boolean checkCachePeriodValid(String savedRequest, boolean isImage) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		
		if(isImage){
			savedRequest += ".jpeg";
		}
		
		while((line=br.readLine())!= null){
			if(line.equals(savedRequest)){
				String time = br.readLine();
				LocalDateTime saved = LocalDateTime.parse(time);
				LocalDateTime now = LocalDateTime.now();
				
				if(now.isBefore(saved)){
					br.close();
					return true;
				}
			}
		}
		
		br.close();
		return false;
	}
	
	public void updateCachingPeriod(String savedRequest) throws IOException{
		LocalDateTime validUntil = LocalDateTime.now();
		validUntil = validUntil.plusMinutes(cachingPeriod);
		FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(savedRequest);
		bw.newLine();
		bw.write(validUntil.toString());
		bw.newLine();
		bw.flush();
		
		bw.close();
	}
}
