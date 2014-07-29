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
import filemanager.CSVWriter;
import filemanager.Log;
import filemanager.Serialize;

/**Run time part of NN **/
public class RunNetwork {

	
	public static void main(String[] args) {
		Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/RunNN.Log"));
		//Make Iris data
		boolean verbose = true;
		long seed = System.currentTimeMillis();
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/iris.float.txt", verbose, seed);
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/2BitXOR.txt", verbose, seed);
		String serialPatterns = "/Users/Jonny/Documents/Timbre/WavePatterns.ser";
		TestPatterns testPatterns = getWavePatternsSerial(seed, serialPatterns);
		
		int runCount = 1;
		MultiLayerNet[][] nets = ManyNets.tryDifferentLayers(runCount, testPatterns,verbose);
		//CoefficientLogger[][] cls = CoefficientLogger.getErrorsFromMultiLayer(nets);
		//CSVWriter cd = new CSVWriter("/Users/Jonny/Documents/Timbre/Logs/comp.csv");
		//cd.writeArraytoFile(CoefficientLogger.getMaxErrorFromCL(cls));
		ArrayList<WavePattern> wps = nets[0][0].getProblemPatterns(testPatterns.getValidationPatterns());
		File[] ps = WavePatterns.getFileNames(WavePatterns.arrayListToArray(wps));
		for (File f : ps) {
			Log.d(f.getName());
		}
		
		ManyNets.graphNets(nets);
	}

	/**Get Wave Patterns from a serialised file **/
	public static TestPatterns getWavePatternsSerial(long seed,
			String serialPatterns) {
		WavePatterns wavePatterns = (WavePatterns) Serialize.getFromSerial(
				serialPatterns);
		wavePatterns.reduceScale(1); //added
		TestPatterns testPatterns = new TestPatterns(wavePatterns.patterns, seed);
		return testPatterns;
	}
	
	/**Config settings for MLN **/
	public static MultiLayerNet config(MultiLayerNet nn, TestPatterns testPatterns, 
			Integer neuronCount, boolean verbose, long seed2, long seed3) {
		LayerStructure ls = new LayerStructure(testPatterns);
		if (neuronCount != null) {
			ls.addHiddenLayer(neuronCount);
		}
		nn.setTrainingRate(0.1d);
		nn.setLayerStructure(ls);
		nn.setTestPatterns(testPatterns);
		nn.setDebug(false);
		nn.initialiseNeurons();
		nn.setVerbose(verbose);
		nn.setAcceptableErrorRate(0.1d);
		nn.setMaxEpoch(200);
		nn.initialiseRandomWeights(seed2);
		nn.setShuffleTrainingPatterns(true, seed3);
		return nn;
	}
	
	
	/** Get test Patterns from a file **/
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
