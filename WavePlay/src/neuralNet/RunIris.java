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
		//generate random seed
		long seed = System.currentTimeMillis();
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
		
		
		
		
		//check shuffle
		int n = 100;
		ArrayList<Integer> ints = new ArrayList<Integer>(100);
		for (int i = 0; i < n; ++i) {
			ints.add(i + 1);
		}
		ints = NNUtilities.knuthShuffle(ints);
		System.out.println(ints.toString());
		
		
		//Make Iris data
		boolean verbose = true;
		String file = "/Users/Jonny/Documents/Timbre/NN/iris.float.txt";
		//String file = "/Users/Jonny/Documents/Timbre/NN/2BitOR.txt";
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
		// checking multilayer stuff
		LayerStructure ls = new LayerStructure(testPatterns);
		//ls.addHiddenLayer(10);
		ls.addHiddenLayer(5);
		//ls.addHiddenLayer(4);
		if (verbose) {System.out.println(ls.toString());}
		
		
		MultiLayerNet id = new MultiLayerNet(); //Make a net;
		id.setLayerStructure(ls);
		id.setTestPatterns(testPatterns);
		id.setDebug(false);
		id.initialiseNeurons();
		id.setVerbose(verbose);
		id.setShuffleTrainingPatterns(true);
		id.setAcceptableErrorRate(0.1d);
		id.setMaxEpoch(100);
		long seed2 = id.initialiseRandomWeights();
		
		try {
			id.runEpoch();
			id.validate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//run validation
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
        }
		
		
		

			
			
			
		
	}

}
