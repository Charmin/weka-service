package com.unisys.poc.JerseyClient;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.transform.Result;

import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.json.*;
import com.unisys.poc.MachineLearning.WeatherResource;

public class WekaClient {
	
     public  static void main(String []args)
     {
    	 WeatherResource resource = new WeatherResource();
    	 try{
    	 resource.setLatitude(100.5);
    	 resource.setLongitude(200.4);
    	 
    	 Form form = new Form();
    	 
    	 form.add("lat",100);
    	 form.add("long",50);
    	 
    	 Client c = Client.create();
    	 WebResource web = c.resource("http://localhost:8080/ML_PoC_Project/rest/world/outlook");
    	 
    	// Builder response = web.accept("application/x-www-form-urlencoded").post(Builder.class,form);
    	 ClientResponse response = web.accept(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class,form);
    	 int   p = response.getStatus();
    	 System.out.println(p);
    	 
         
    	 System.out.println("Server response....");
          
    	 String output= response.getEntity(String.class);
    	 System.out.println(output);
    	 c.destroy();
    	
    	 }
    	 
    	 catch(Exception e)
    	 {
    		 e.printStackTrace();
    	 }
    	 
     }
	

}
