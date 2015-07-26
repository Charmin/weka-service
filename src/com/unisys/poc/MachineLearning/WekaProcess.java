package com.unisys.poc.MachineLearning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.mysql.jdbc.exceptions.MySQLDataException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.DatabaseSaver;

public class WekaProcess {
	
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
	}

	return inputReader;
}

public static Evaluation classify(Classifier model,
    Instances trainingSet, Instances testingSet) throws Exception {
	Evaluation evaluation = new Evaluation(trainingSet);

	model.buildClassifier(trainingSet);
	evaluation.evaluateModel(model, testingSet);
	return evaluation;
}

public static double calculateAccuracy(FastVector predictions) {
	double correct = 0;

	for (int i = 0; i < predictions.size(); i++) {
		NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
		if (np.predicted() == np.actual()) {
			correct++;
		}
	}
		return 100 * correct / predictions.size();
}

public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
	Instances[][] split = new Instances[2][numberOfFolds];

	for (int i = 0; i < numberOfFolds; i++) {
		split[0][i] = data.trainCV(numberOfFolds, i);
		split[1][i] = data.testCV(numberOfFolds, i);
	}

	return split;
}

public static void arffToDatabase(String filename){	
	try {
	
	Instances data = new Instances(new BufferedReader(new FileReader(filename)));
	data.setClassIndex(0);
	DatabaseSaver save = new DatabaseSaver();
	save.setUrl("jdbc:mysql://localhost:3306/MLWeather");
    save.setUser("root");
    save.setPassword("Chaimysql14");
    save.setInstances(data);
    save.setRelationForTableName(false);
    save.setTableName("weather_data");
    save.connectToDatabase();
    save.writeBatch();
	}
   
    catch (FileNotFoundException e) {
	
	e.printStackTrace();
    }
	catch (IOException e) {
			
	e.printStackTrace();
		}
    catch (Exception e) {
	
	e.printStackTrace();
      }
	}
	 
    public static void setUpModel(String table_name,Classifier model)
    {
    	try{
    		
    	Instances dataset = Predictions.loadDataFromDatabase(table_name);//weather_data
		dataset.setClassIndex(0);
	
	    // Do 10-split cross validation
		Instances[][] split = WekaProcess.crossValidationSplit(dataset, 10);
 
		// Separate split into training and testing arrays
		Instances[] trainingSplits = split[0];
		Instances[] testingSplits = split[1];
 
	    //Classifier model = new J48();
		Evaluation validation;
		
		validation = WekaProcess.classify(model, trainingSplits[0], testingSplits[0]);
			
		FastVector predictions = new FastVector();
		predictions.appendElements(validation.predictions());
	 
			
		} 
    	catch(MySQLDataException ex)
    	{
    		ex.printStackTrace();
    	}
    	catch (Exception e) {
			
			e.printStackTrace();
		}	        
		
		System.out.println(model.toString());
		
    }

}
