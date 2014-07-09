package neuralNet;

/** current dumping ground for any methods concerned
 * with running lots of nets at the same time
 * (needs more work)
 * @author Jonny
 *
 */
public class ManyNets {

	public static MultiLayerNet runNets(int runCount, TestPatterns testPatterns, 
											boolean verbose) {
		
		MultiLayerNet[] nns = new MultiLayerNet[runCount];
		
		for (int i = 0; i < runCount; ++i) {
			long seed = System.currentTimeMillis();
			long seed2 = System.currentTimeMillis();
			nns[i] = RunIris.config(new MultiLayerNet(), testPatterns, verbose, seed, seed2); //Make a net;
			try {
				nns[i].runEpoch();
				nns[i].runTestPatterns();;
				System.out.println("NN: " + i + " Error: " + nns[i].getErrorRate());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ManyNets.pickBestNet(nns);		
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
