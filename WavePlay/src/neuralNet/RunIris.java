package neuralNet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import plotting.MatthewsChart;
import filemanager.ArrayStuff;
import filemanager.CSVReader;

public class RunIris {

	public RunIris() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		

		//Make Iris data
		boolean verbose = true;
		long seed = System.currentTimeMillis();
		TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/iris.float.txt", verbose, seed);
		//TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/2BitXOR.txt", verbose, seed);
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
		//ls.addHiddenLayer(10);
		ls.addHiddenLayer(4);
		nn.setLayerStructure(ls);
		nn.setTestPatterns(testPatterns);
		nn.setDebug(false);
		nn.initialiseNeurons();
		nn.setVerbose(verbose);
		nn.setAcceptableErrorRate(0.1d);
		nn.setMaxEpoch(1000);
		nn.initialiseRandomWeights(seed2);
		nn.setShuffleTrainingPatterns(true, seed3);
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
	
	/** make sure output is .ser **/
	public static void serialize(MultiLayerNet mln, String output) {
		// Serialization code
        try {
            FileOutputStream fileOut = new FileOutputStream(output);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(mln);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
	}
	
	public MultiLayerNet getFromSerial(String file) {
		MultiLayerNet mln = null;
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            mln = (MultiLayerNet) in.readObject();
            in.close();
            fileIn.close();
           // System.out.println(mln.toString());
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } 
        return mln;
	}
	
	public static void logTesting() {
		FileHandler fh;
		try {
			fh = new FileHandler("fileDemo.txt");
			Logger logger = Logger.getLogger("New Log");
			logger.addHandler(fh);
			SimpleFormatter sf = new SimpleFormatter();
			fh.setFormatter(sf);
			logger.setLevel(Level.ALL);
			logger.log(Level.CONFIG, "TESTING");
			logger.log(Level.FINER, "Ytfgh");
			logger.log(Level.ALL, "TESTING2");
		} catch (Exception e) {
			e.printStackTrace();;
		} 
	}
	

}
