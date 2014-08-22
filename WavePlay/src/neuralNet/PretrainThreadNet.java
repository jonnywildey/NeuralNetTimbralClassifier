package neuralNet;

import java.io.File;

import com.util.Log;

/** Threadable NN with the ability to pretrain with one set of data
 * and then add additional data sets
 * @author Jonny
 *
 */
public class PretrainThreadNet extends ThreadNet{
	
	public TestPatterns postTrainPatterns; //patterns to introduce after pretrain
	public Integer postEpoch; //how many epochs to in post train
	public boolean combine; //Whether to add pretrain patterns to posttrain patterns
	
	/** Create Thread Net. filename, runCount, testPatterns, hidden layer
	 * max epoch, id, verbose
	 */
	public PretrainThreadNet(File name, int runCount, TestPatterns testPatterns,
			int hiddenLayer, int maxEpoch, int id, boolean verbose) {
		super(name, runCount, testPatterns, hiddenLayer, maxEpoch, id, verbose);
	}
	
	/** Create Thread Net. filename, runCount, pretrainPatterns, 
	 * posttrain patterns, hidden layer
	 * max epoch, postepoch, id, combine, verbose
	 */
	public PretrainThreadNet(File name, int runCount, TestPatterns preTrainPatterns, 
			TestPatterns postTrainPatterns,
			int hiddenLayer, int maxEpoch, int postEpoch, int id, 
			boolean combine, boolean verbose) {
		this.name = name;
		this.runCount = runCount;
		this.testPatterns = preTrainPatterns;
		this.combine = combine;
		if (combine) {
			this.postTrainPatterns = combinePatterns(
					preTrainPatterns, postTrainPatterns, verbose);
		} else {
			this.postTrainPatterns = postTrainPatterns;
		}
		this.postEpoch = postEpoch;
		this.hiddenLayer = hiddenLayer;
		this.maxEpoch = maxEpoch;
		this.id = id;
		this.verbose = verbose;
	}
	
	@Override
	public MultiLayerNet call() throws Exception {
		MultiLayerNet nn = new MultiLayerNet();
		config(nn);
		try {
			if (verbose) {
				Log.d("Pretraining patterns");
			}
			nn.runEpoch();
			nn.runTestPatterns();
			if (verbose) {
				Log.d("Post training patterns");
			}
			postConfig(nn);
			nn.runEpoch();
			nn.runTestPatterns();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nn;
	}
	

	public TestPatterns getPostTrainPatterns() {
		return postTrainPatterns;
	}

	/** Sets post train patterns and whether they should be combined **/
	public void setPostTrainPatterns(TestPatterns postTrainPatterns, boolean combine) {
		if (combine) {
			this.postTrainPatterns = combinePatterns(
					this.testPatterns, postTrainPatterns, verbose);
		} else {
			this.postTrainPatterns = postTrainPatterns;
		}
	}
	
	/** Combine test patterns, maintaining training, validation and test sets **/
	protected static TestPatterns combinePatterns(TestPatterns preTrainPatterns,
			TestPatterns postTrainPatterns, boolean verbose) {
		TestPatterns ntp = new TestPatterns();
		
		ntp.setTrainingPatterns(preTrainPatterns.getTrainingPatterns());
		ntp.getTrainingPatterns().addAll(postTrainPatterns.getTrainingPatterns());
		if (verbose) {
		Log.d("Combined Training patterns count " + ntp.getTestingPatterns().size() + "\n");
		}
		
		ntp.setValidationPatterns(preTrainPatterns.getValidationPatterns());
		ntp.getValidationPatterns().addAll(postTrainPatterns.getValidationPatterns());
		if (verbose) {
		Log.d("Combined Validation patterns count " + ntp.getTestingPatterns().size() + "\n");
		}
		
		ntp.setTestingPatterns(preTrainPatterns.getTestingPatterns());
		ntp.getTestingPatterns().addAll(postTrainPatterns.getTestingPatterns());
		if (verbose) {
		Log.d("Combined Testing patterns count " + ntp.getTestingPatterns().size() + "\n");
		}
		return ntp;
	}

	public PretrainThreadNet() {
		super();
	}
	
	public void postConfig(MultiLayerNet mn) {
		if (hasPostTrainPatterns()) {
			mn.setTestPatterns(postTrainPatterns);
		} else {
			if (verbose) {
				Log.d("No Posttraining patterns specified. Using pretraining");
			}
		}
		if (hasPostEpoch()) {
			mn.setMaxEpoch(postEpoch);
		} 
	}
	
	private boolean hasPostEpoch() {
		return this.postEpoch != null;
	}

	private boolean hasPostTrainPatterns() {
		return (this.postTrainPatterns != null);
	}


	
	


}
