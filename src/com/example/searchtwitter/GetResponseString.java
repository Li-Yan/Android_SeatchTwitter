package com.example.searchtwitter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetResponseString {
	public static String getResp(String urlString){
		  String result= null;  
		    URL url = null;  
		    HttpURLConnection urlConnection = null;  
		    
		    try {  
		        url = new URL(urlString);    
		        urlConnection = (HttpURLConnection) url.openConnection();  
		        InputStream in = new BufferedInputStream(urlConnection.getInputStream());  
		        result = readStream(in);  
		        System.out.println(result);  
		    } catch (MalformedURLException e) {  
		        e.printStackTrace();  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    }  
		    return result;
	}
	
	public static String readStream(InputStream in) throws IOException {  
	    StringBuilder sb = new StringBuilder();  
	    BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);  
	    for (String line = r.readLine(); line != null; line =r.readLine()){  
	        sb.append(line);  
	    }  
	    in.close();  
	    return sb.toString();  
	   
		}

}