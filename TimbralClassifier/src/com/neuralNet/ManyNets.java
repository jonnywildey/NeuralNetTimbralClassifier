package com.neuralNet;

import java.io.File;
import java.util.concurrent.Callable;

import com.neuralNet.pattern.TestPatterns;
import com.util.Log;
import com.util.fileReading.CSVWriter;

/**
 * Run multiple sets of multiple nets.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class ManyNets implements Callable<MultiLayerNet[][]>{
	
	public boolean even;
	public int runCount;
	public TestPatterns testPatterns;
	public boolean verbose;
	public File name;
	public int id;
	
	
	
	/**
	 * Instantiates a new many nets.
	 */
	public ManyNets() {
		super();
	}


	/**
	 * Instantiates a new many nets.
	 *
	 * @param name the name
	 * @param runCount the run count
	 * @param id the id
	 * @param testPatterns the test patterns
	 * @param verbose the verbose
	 */
	public ManyNets(File name, int runCount, int id, TestPatterns testPatterns,
			boolean verbose) {
		super();
		this.name = name;
		this.runCount = runCount;
		this.id = id;
		this.testPatterns = testPatterns;
		this.verbose = verbose;
	}


	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	public MultiLayerNet[][] call() {
		
		MultiLayerNet[][] nets = tryDifferentLayers(runCount, testPatterns,verbose, even);
		//CoefficientLogger[][] cls = CoefficientLogger.getErrorsFromMultiLayer(nets);
		//CSVWriter cd = new CSVWriter(this.name.getAbsolutePath());
		//cd.writeArraytoFile(CoefficientLogger.getMaxErrorFromCL(cls));
		return nets;
	}
	
	
	/**
	 * Runs a network multiple times *.
	 *
	 * @param runCount the run count
	 * @param testPatterns the test patterns
	 * @param neuronCount the neuron count
	 * @param verbose the verbose
	 * @return the multi layer net[]
	 */
	public static MultiLayerNet[] runNets(int runCount, TestPatterns testPatterns, 
											Integer neuronCount, boolean verbose) {
		MultiLayerNet[] nns = new MultiLayerNet[runCount];		
		for (int i = 0; i < runCount; ++i) {
			long seed = System.currentTimeMillis();
			long seed2 = System.currentTimeMillis();
			nns[i] = RunNetwork.config(new MultiLayerNet(), testPatterns, neuronCount,
					verbose, seed, seed2); //Make a net;
			try {
				nns[i].runEpoch();
				nns[i].runTestPatterns();
				Log.d("NN: " + i + " Error: " + nns[i].getErrorRate());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return nns;		
	}
	
	/**
	 * Try different layers.
	 *
	 * @param runCount the run count
	 * @param testPatterns the test patterns
	 * @param verbose the verbose
	 * @param even the even
	 * @return the multi layer net[][]
	 */
	public MultiLayerNet[][] tryDifferentLayers(int runCount, TestPatterns testPatterns, 
			boolean verbose, boolean even) {
		int[] layerOneSize = new int[]{1,5,10,15,20,30,40,50,60,70,80,90,100,110,120,150,160,200};
		MultiLayerNet[][] mlns = new MultiLayerNet[layerOneSize.length / 2][runCount];
		if (even) {
		for (int i = 0; i < mlns.length; i++) {
			mlns[i] = runNets(runCount, testPatterns, layerOneSize[(i * 2)], verbose);
		} 
		}else {
			for (int i = 0; i < mlns.length; i++) {
				mlns[i] = runNets(runCount, testPatterns, layerOneSize[(i * 2) + 1], verbose);
			}
		}
		return mlns;
	}

}
