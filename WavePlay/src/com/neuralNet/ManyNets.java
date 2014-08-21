package com.neuralNet;

import java.io.File;
import java.util.concurrent.Callable;

import com.filemanager.CSVWriter;
import com.filemanager.Log;
import com.neuralNet.pattern.TestPatterns;

/** current dumping ground for any methods concerned
 * with running lots of nets at the same time
 * (needs more work)
 *ALSO
 *Threadable MLP
 * @author Jonny
 *
 */
public class ManyNets implements Callable<MultiLayerNet[][]>{
	
	public boolean even;
	public int runCount;
	public TestPatterns testPatterns;
	public boolean verbose;
	public File name;
	public int id;
	
	
	
	public ManyNets() {
		super();
	}


	public ManyNets(File name, int runCount, int id, TestPatterns testPatterns,
			boolean verbose) {
		super();
		this.name = name;
		this.runCount = runCount;
		this.id = id;
		this.testPatterns = testPatterns;
		this.verbose = verbose;
	}


	public MultiLayerNet[][] call() {
		
		MultiLayerNet[][] nets = tryDifferentLayers(runCount, testPatterns,verbose, even);
		//CoefficientLogger[][] cls = CoefficientLogger.getErrorsFromMultiLayer(nets);
		//CSVWriter cd = new CSVWriter(this.name.getAbsolutePath());
		//cd.writeArraytoFile(CoefficientLogger.getMaxErrorFromCL(cls));
		return nets;
	}
	
	
	/** Runs a network multiple times **/
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
	
	public MultiLayerNet[][] tryDifferentLayers(int runCount, TestPatterns testPatterns, 
			boolean verbose, boolean even) {
		int[] layerOneSize = new int[]{1,5,10,15,20,30,40,50,60,70,80,90,100,110,120,150,160,200,300,400};
		//int[] layerOneSize = new int[]{40,50,60};
		//int[] layerOneSize = new int[]{400,800,1600, 3200};
		//int[] layerOneSize = new int[]{50, 60};
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
	
	/** Makes a graph of your nets' Matthews Coefficient **/
	public static void graphNets(MultiLayerNet[] nets) {
		CoefficientLogger[] mBox = CoefficientLogger.getErrorsFromMultiLayer(nets);
		CoefficientLogger.makeGraph(mBox);
	}
	
	/** Makes a graph of your nets' Matthews Coefficient **/
	public static void graphNets(MultiLayerNet[][] nets) {
		CoefficientLogger[][] mBox = CoefficientLogger.getErrorsFromMultiLayer(nets);
		for (int i = 0; i < mBox.length; ++i) {
			CoefficientLogger.makeGraph(mBox[i]);
		}
		
	}
	
	public static MultiLayerNet pickBestNet(MultiLayerNet[] nns) {
		MultiLayerNet nn = null;
		double er = 0;
		for (MultiLayerNet mln : nns) {
			double val = Math.abs(mln.getErrorRate()); // + mln.getValidationErrorRate());
			if (val > er) {
				er = mln.getErrorRate(); // + mln.getValidationErrorRate();
				nn = mln;
			}
			if (er == 0) {
				break;
			}
		}
		return nn;
	}

}
