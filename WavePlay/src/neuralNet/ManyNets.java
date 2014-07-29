package neuralNet;

import filemanager.Log;

/** current dumping ground for any methods concerned
 * with running lots of nets at the same time
 * (needs more work)
 * @author Jonny
 *
 */
public class ManyNets {
	
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
	
	public static MultiLayerNet[][] tryDifferentLayers(int runCount, TestPatterns testPatterns, 
			boolean verbose) {
		//int[] layerOneSize = new int[]{1,5,10,20,30,50,70,90,100,110,120,150,160,200};
		int[] layerOneSize = new int[]{50};
		MultiLayerNet[][] mlns = new MultiLayerNet[layerOneSize.length][runCount];
		for (int i = 0; i < layerOneSize.length; ++i) {
			mlns[i] = runNets(runCount, testPatterns, layerOneSize[i], verbose);
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
