package com.unisys.poc.MachineLearning;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;


public class OpenWeatherApi {
	
	static HttpURLConnection connection = null;
	
    private boolean OpenConnection(String url) throws IOException
	{
		
    	try{
    		URL api_url = new URL(url);
	    	connection = (HttpURLConnection)api_url.openConnection();
		
		   if(connection.getResponseCode()== HttpURLConnection.HTTP_OK)
		   {
			  return true;   
		   }
		   else
		   {
			   System.out.println("Error in response code!");
			   return false;
		   }
		  
		}
	     catch (MalformedURLException ex) {
          Logger.getLogger(OpenWeatherApi.class.getName()).log(Level.SEVERE, null, ex);
          return false;
          
        } catch (IOException ex) {
          Logger.getLogger(OpenWeatherApi.class.getName()).log(Level.SEVERE, null, ex);
          return false;
	  }
	
   }
	
    //To retrieve the json data by country
   public String getDataByCountry(String country) 
	{
		String url = "http://api.openweathermap.org/data/2.5/weather?q="+country;
		String jsonResult = "";
		String result="";
		try
		{
		  if(connection==null)
			 OpenConnection(url);
		  
		     InputStreamReader reader = new InputStreamReader(connection.getInputStream());
		     BufferedReader bufferedReader = new BufferedReader(reader,8192);
		     
		     while((jsonResult = bufferedReader.readLine())!=null)
		        {
		    	     result+=jsonResult;  
		    	 
		    	}
		     
		     bufferedReader.close();
		     
		 		    		      	
		}
		catch(IOException ex)
		{
			Logger.getLogger(OpenWeatherApi.class.getName()).log(Level.SEVERE, null, ex);
		}
        finally
        {
        	connection.disconnect();
        }
		result = parseResult(result);
	    return result;		
	}
	
	//To retrieve the Json data by coorditnates
  public String getDataByCoordinates(double latitiude,double longitude) 
	{
		String url = "http://api.openweathermap.org/data/2.5/weather?lat="+latitiude+"&lon="+longitude;
		String jsonResult = "";
		String result="";
		 BufferedReader bufferedReader;
		try
		{
		  if(connection==null)
			 OpenConnection(url);
		  
		     InputStreamReader reader = new InputStreamReader(connection.getInputStream());
		     bufferedReader = new BufferedReader(reader,8192);
		     
		     while((jsonResult = bufferedReader.readLine())!=null)
		        {
		    	     result+=jsonResult;  
		    	 
		    	}
		     bufferedReader.close();
		  
		     result = parseResult(result);
			  
		 		    		      	
		}
		catch(IOException ex)
		{
			Logger.getLogger(OpenWeatherApi.class.getName()).log(Level.SEVERE, null, ex);
		}
		finally
		{
			
			connection=null;
			  
		}
		
		 return result;	
          
			
	}
	
  private String parseResult(String jsonInput) 
    {
	   StringBuffer parsed = new StringBuffer("");
	 
	   try{
		
		   JSONObject jsonObject = new JSONObject(jsonInput);
		   System.out.println("Length : "+jsonObject.length());
		
		
		  //"main"
	      //need temperature, pressure, humidity,wind-speed
		  //"main"
         JSONObject JSONObject_main;
		
         String place = jsonObject.getString("name");
         parsed.append(place);
         
		 JSONObject_main = jsonObject.getJSONObject("main");
	
         Double result_temp = JSONObject_main.getDouble("temp");
         parsed.append(","+Double.toString(result_temp));
         Double result_pressure = JSONObject_main.getDouble("pressure");
         parsed.append(","+Double.toString(result_pressure));
         Double result_humidity = JSONObject_main.getDouble("humidity");
         parsed.append(","+Double.toString(result_humidity));
         // Double result_sealevel = JSONObject_main.getDouble("sea_level");
         // parsed.append(","+Double.toString(result_sealevel));
         //"wind"
         JSONObject JSONObject_wind = jsonObject.getJSONObject("wind");
         Double result_speed = JSONObject_wind.getDouble("speed");
         parsed.append(","+Double.toString(result_speed));
         
    	
	   }
	   catch(JSONException ex)
	    {
		   ex.printStackTrace();
		   ex.toString();
	    }
	
	
		return parsed.toString();
	
    }

}
