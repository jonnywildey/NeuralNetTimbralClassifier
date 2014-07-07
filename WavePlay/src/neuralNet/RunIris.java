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

import filemanager.CSVReader;

public class RunIris {

	public RunIris() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
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
		/**
		//check shuffle
		int n = 100;
		ArrayList<Integer> ints = new ArrayList<Integer>(100);
		for (int i = 0; i < n; ++i) {
			ints.add(i + 1);
		}
		ints = NNUtilities.knuthShuffle(ints);
		System.out.println(ints.toString());
		**/
		//Make Iris data
		boolean verbose = true;
		long seed = System.currentTimeMillis();
		TestPatterns testPatterns = getTestPatterns("/Users/Jonny/Documents/Timbre/NN/iris.float.txt", verbose, seed);
		int runCount = 30;
		MultiLayerNet bestNN = runNets(runCount, testPatterns,verbose);
		System.out.println(bestNN.toString());
		
		/**
		// Serialization code
        try {
            FileOutputStream fileOut = new FileOutputStream("testID.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(id);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
     // De-serialization code
        @SuppressWarnings("unused")
        MultiLayerNet mln = null;
        try {
            FileInputStream fileIn = new FileInputStream("testID.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            mln = (MultiLayerNet) in.readObject();
            in.close();
            fileIn.close();
            System.out.println(mln.toString());
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } **/
	}
	
	public static MultiLayerNet config(MultiLayerNet nn, TestPatterns testPatterns, 
										boolean verbose, long seed2, long seed3) {
		LayerStructure ls = new LayerStructure(testPatterns);
		ls.addHiddenLayer(10);
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
	
	public static MultiLayerNet runNets(int runCount, TestPatterns testPatterns, 
											boolean verbose) {
		
		MultiLayerNet[] nns = new MultiLayerNet[runCount];
		
		for (int i = 0; i < runCount; ++i) {
			long seed = System.currentTimeMillis();
			long seed2 = System.currentTimeMillis();
			nns[i] = config(new MultiLayerNet(), testPatterns, verbose, seed, seed2); //Make a net;
			try {
				nns[i].runEpoch();
				nns[i].validate();
				System.out.println("NN: " + i + " Error: " + nns[i].getErrorRate());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pickBestNet(nns);		
	}
	
	public static MultiLayerNet pickBestNet(MultiLayerNet[] nns) {
		MultiLayerNet nn = null;
		double er = 0;
		for (MultiLayerNet mln : nns) {
			double val = Math.abs(mln.getErrorRate() + mln.getValidationErrorRate());
			if (val > er) {
				er = mln.getErrorRate() + mln.getValidationErrorRate();
				nn = mln;
			}
			if (er == 0) {
				break;
			}
		}
		return nn;
	}
	

}
