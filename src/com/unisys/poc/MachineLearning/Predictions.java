package com.unisys.poc.MachineLearning;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.exceptions.MySQLDataException;

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
import weka.experiment.InstanceQuery;



public class Predictions {

	 private static Instances instanceSet;
	
	 public static Instances loadTestData(Instances instanceSet,String input)
	 {
		 Instance instance = new Instance(5);
		 String []values = input.split(",");
		 for(int i=0;i<values.length;i++)
		 {
			 try{
				 
				 instance.setValue(instanceSet.attribute(i+1), values[i]);
			 }
			 catch(IllegalArgumentException ex)
		     {
		    	instance.setValue(instanceSet.attribute(i+1),Double.parseDouble(values[i]));
		     }
			 
			 instanceSet.add(instance);
		 }
		 
		 return instanceSet;
		 
		 
	 }
	 
	 public static Instances loadTestDataFromTextFile(Instances instanceSet,int instance_size,String filename)
	 {
		 try
		 {
			//BufferedReader reader = new BufferedReader(new FileReader("lib\\test.txt")); 
			
			 BufferedReader reader = new BufferedReader(new FileReader("lib\\"+filename));
			 String line;
			
			while((line = reader.readLine())!=null)
			{	
				 //instanceList.get(i) = new Instance(4);
			     //line = reader.readLine();
				 Instance in = new Instance(instance_size);
			     String[] values = line.split(",");
			     for(int i=0;i<values.length;i++)
			     {
			    	try{
			    	
			    		in.setValue(instanceSet.attribute(i+1),values[i]);
			    	}
			    	catch(IllegalArgumentException ex)
			    	{
			    		in.setValue(instanceSet.attribute(i+1),Double.parseDouble(values[i]));
			    	}
			    	 
			     }	
			     
			  
			     instanceSet.add(in);
			}
						
			//instanceSet.add(in);
		 }
		 catch(IOException fx)
		 {
			System.out.println("Ooops! File missing");
		 }
		 
		 return instanceSet;
		 
	 }
	 
	
	 public static Instances makeWeatherInstance()
	 {
		 FastVector features = new FastVector(6);
		 
		 FastVector att1_param = new FastVector(4);
		 att1_param.addElement("hot");
		 att1_param.addElement("pleasant");
		 att1_param.addElement("cool");
		 att1_param.addElement("cold");
		 
		 Attribute attribute1 = new Attribute("weather",att1_param);
		 Attribute attribute2 = new Attribute("temperature");
		 Attribute attribute3 =  new Attribute("pressure");
		 Attribute attribute4 = new Attribute("humidity");
		 //Attribute attribute5 = new Attribute("sea-level");
		 Attribute attribute5 = new Attribute("wind-speed");
		 
		 features.addElement(attribute1);
	     features.addElement(attribute2);
	     features.addElement(attribute3);
	     features.addElement(attribute4);
	     //features.addElement(attribute5);
	     features.addElement(attribute5);
	     
	     
		 instanceSet = new Instances("weather",features,16);
		 instanceSet.setClassIndex(0);
		 
		 return instanceSet;
		
	 }
	 
	 public static Instances loadDataFromDatabase(String table)
	 {
		InstanceQuery query=null;
		Instances instances=null;
		try {
				query = new InstanceQuery();
				query.setUsername("root");
				query.setPassword("Chaimysql14");
				query.setQuery("select * from "+table);
				instances = query.retrieveInstances();
				}
		        catch(MySQLDataException ex)
		        {
		        	ex.printStackTrace();
		        }
		        catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		return instances;
						 
		 
	 }
	 
	 //This method performs prediction of the instance
	 
	 public static String predictWeather(Instance in, Classifier cModel)
	 {
		String result = null;
		try 
		 {
			double pred = cModel.classifyInstance(in);
			result = "Predicted result : The weather is "+in.classAttribute().value((int)pred)+" at this location today";
			//System.out.println("====================Classification Result=====================\n\n");
			//System.out.println("Predicted result : The weather is "+in.classAttribute().value((int)pred)+" at this location today");
		 }
		catch(ArrayIndexOutOfBoundsException ex)
		 {
			ex.printStackTrace();
		 }
		 catch (Exception e)
		 {
		    System.out.println("==============Prediciton went wrong=====================\n");
			e.printStackTrace();
		 }
		
		return result;
		 
		 
	 }
	 

}
