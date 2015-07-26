package com.unisys.poc.MachineLearning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


@Path("world")
public class WekaApp {
	
	OpenWeatherApi  apiWeather;
	static WeatherResource resource = new WeatherResource();
		
	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String test()
	{
		return "Hello from jersey";
		
	}
	
	@POST
	@Path("/predict")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response predictByCoordinates(WeatherResource resource)
	{
		apiWeather = new OpenWeatherApi();
		//WeatherResource resource = new WeatherResource();
		
		String result = apiWeather.getDataByCoordinates(resource.getLatitude(), resource.getLongitude());
		//String result = apiWeather.getDataByCoordinates(latitude, longitude);
		String []tokens = result.split(",");
		String location = tokens[0];
		StringBuilder result_new = new StringBuilder();
		for(int i=1;i<tokens.length;i++)
			result_new.append(tokens[i]+",");
	 
		Classifier model = new J48();
		WekaProcess.setUpModel("weather_data", model);
		Instances sampleInstances = Predictions.makeWeatherInstance();
		sampleInstances = Predictions.loadTestData(sampleInstances,result_new.toString());
		System.out.println("The outlook at "+location+" today");
		String outlook =  Predictions.predictWeather(sampleInstances.instance(0),model);
		
		resource.setOutlook(outlook);
		
		return Response.status(200).entity(resource).build();
		
	 }
	
	@POST
	@Path("/outlook")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postCordinates(@FormParam("long") String longitude,@FormParam("lat") String latitude)
    {
		apiWeather = new OpenWeatherApi();
		double lati = Double.parseDouble(latitude);
		double longi = Double.parseDouble(longitude);
		//WeatherResource resource = new WeatherResource();
		resource.setLatitude(lati);
		resource.setLongitude(longi);
	    
		String result = apiWeather.getDataByCoordinates(lati,longi);
		//String result = apiWeather.getDataByCoordinates(latitude, longitude);
		String []tokens = result.split(",");
		String location = tokens[0];
		StringBuilder result_new = new StringBuilder();
		for(int i=1;i<tokens.length;i++)
			result_new.append(tokens[i]+",");
	 
		Classifier model = new J48();
		WekaProcess.setUpModel("weather_data", model);
		Instances sampleInstances = Predictions.makeWeatherInstance();
		sampleInstances = Predictions.loadTestData(sampleInstances,result_new.toString());
		//System.out.println("The outlook at "+location+" today");
		String outlook =  Predictions.predictWeather(sampleInstances.instance(0),model);
		outlook = "Location: "+location+".\n"+outlook;
		resource.setOutlook(outlook);	    
		return Response.status(200).entity(outlook).build();
		
		
    }
	
	@GET
	@Path("/predictioResult")
	@Produces(MediaType.TEXT_PLAIN)
	public String getResults()
	{
		String result = resource.getOutlook();
		return result;
		
		
	}

}
