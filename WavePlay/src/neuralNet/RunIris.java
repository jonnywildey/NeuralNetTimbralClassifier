package neuralNet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import plotting.MatthewsChart;
import filemanager.ArrayStuff;
import filemanager.CSVReader;
import filemanager.Log;
import filemanager.Serialize;

public class RunIris {

	public RunIris() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/RunNN.Log"));
		//Make Iris data
		boolean verbose = true;
		long seed = System.currentTimeMillis();
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/iris.float.txt", verbose, seed);
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/2BitXOR.txt", verbose, seed);
		WavePatterns wavePatterns = (WavePatterns) Serialize.getFromSerial(
				"/Users/Jonny/Documents/Timbre/WavePatterns.ser");
		wavePatterns.reduceScale();
		TestPatterns testPatterns = new TestPatterns(wavePatterns.patterns, seed);
		System.out.println("TP" + testPatterns.toString());
		int runCount = 1;
		MultiLayerNet bestNN = ManyNets.runNets(runCount, testPatterns,verbose);
		//System.out.println(bestNN.toString());
		bestNN.getErrorBox().makeGraph();
		//crappy graph test
		double[] dds = {0.1, 0.5, 0, 0.3, 0.4, 0.5};
		
		dds = ArrayStuff.normalizeDouble(dds, 400);
		long[] la = ArrayStuff.doubleToLong(dds);
		long[][] las = {la};
		MatthewsChart nc = new MatthewsChart(las);
		//nc.makeChart();
		

	}
	
	public static MultiLayerNet config(MultiLayerNet nn, TestPatterns testPatterns, 
										boolean verbose, long seed2, long seed3) {
		LayerStructure ls = new LayerStructure(testPatterns);
		ls.addHiddenLayer(150);
		//ls.addHiddenLayer(20);
		nn.setTrainingRate(0.1d);
		nn.setLayerStructure(ls);
		nn.setTestPatterns(testPatterns);
		nn.setDebug(false);
		nn.initialiseNeurons();
		nn.setVerbose(verbose);
		nn.setAcceptableErrorRate(0.1d);
		nn.setMaxEpoch(200);
		nn.initialiseRandomWeights(seed2);
		nn.setShuffleTrainingPatterns(false, seed3);

		//TURN OFF SHUFFLING AFTER A WHILE
		return nn;
	}
	
	public static TestPatterns getTestPatterns(String file, boolean verbose, long seed) {
		CSVReader sr = new CSVReader(file);
		sr.readFile();
		double[][] arr = sr.makeDoubleArray();
		arr = NNUtilities.removeNulls(arr, verbose); //remove null rows
		//System.out.println(Arrays.deepToString(arr));
		NNUtilities.createTargetConversionTable(arr, verbose); //see how bit array relates to original
		//separate pattern into training, testing and validation into 3 1 1 ratio
		ArrayList<Pattern> patterns = NNUtilities.createPatterns(arr, false);
		TestPatterns testPatterns = new TestPatterns(patterns, seed);
		System.out.println("TP" + testPatterns.toString());
		return testPatterns;
	}
	
	
	
	
	

}
