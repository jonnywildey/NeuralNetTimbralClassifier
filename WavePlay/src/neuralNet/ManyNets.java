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
											boolean verbose) {
		
		MultiLayerNet[] nns = new MultiLayerNet[runCount];		
		for (int i = 0; i < runCount; ++i) {
			long seed = System.currentTimeMillis();
			long seed2 = System.currentTimeMillis();
			nns[i] = RunIris.config(new MultiLayerNet(), testPatterns, verbose, seed, seed2); //Make a net;
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
	
	/** Makes a graph of your nets' Matthews Coefficient **/
	public static void graphNets(MultiLayerNet[] nets) {
		CoefficientLogger[] mBox = new CoefficientLogger[nets.length];
		for (int i = 0; i < nets.length; ++i) {
				mBox[i] = nets[i].getErrorBox();
		}
		CoefficientLogger.makeGraph(mBox);
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
