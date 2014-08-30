package com.neuralNet;

import java.io.File;

import com.neuralNet.pattern.TestPatterns;
import com.util.Log;

/**
 * Threadable NN with the ability to pretrain with one set of data and then add
 * additional data sets.
 * 
 * @author Jonny Wildey
 */
public class PretrainThreadNet extends ThreadNet {

	/**
	 * Combine test patterns, maintaining training, validation and test sets *.
	 * 
	 * @param preTrainPatterns
	 *            the pre train patterns
	 * @param postTrainPatterns
	 *            the post train patterns
	 * @param verbose
	 *            the verbose
	 * @return the test patterns
	 */
	protected static TestPatterns combinePatterns(
			TestPatterns preTrainPatterns, TestPatterns postTrainPatterns,
			boolean verbose) {
		TestPatterns ntp = new TestPatterns();

		ntp.setTrainingPatterns(preTrainPatterns.getTrainingPatterns());
		ntp.getTrainingPatterns().addAll(
				postTrainPatterns.getTrainingPatterns());
		if (verbose) {
			Log.d("Combined Training patterns count "
					+ ntp.getTrainingPatterns().size() + "\n");
		}

		ntp.setValidationPatterns(preTrainPatterns.getValidationPatterns());
		ntp.getValidationPatterns().addAll(
				postTrainPatterns.getValidationPatterns());
		if (verbose) {
			Log.d("Combined Validation patterns count "
					+ ntp.getValidationPatterns().size() + "\n");
		}

		ntp.setTestingPatterns(preTrainPatterns.getTestingPatterns());
		ntp.getTestingPatterns().addAll(postTrainPatterns.getTestingPatterns());
		if (verbose) {
			Log.d("Combined Testing patterns count "
					+ ntp.getTestingPatterns().size() + "\n");
		}
		return ntp;
	}
	public TestPatterns postTrainPatterns; // patterns to introduce after
	// pretrain
	public Integer postEpoch; // how many epochs to in post train

	public boolean combine; // Whether to add pretrain patterns to posttrain
	// patterns

	/**
	 * Instantiates a new pretrain thread net.
	 */
	public PretrainThreadNet() {
		super();
	}

	/**
	 * Create Thread Net. filename, testPatterns, hidden layer max epoch, id,
	 * verbose
	 * 
	 * @param name
	 *            the name
	 * @param testPatterns
	 *            the test patterns
	 * @param hiddenLayer
	 *            the hidden layer
	 * @param maxEpoch
	 *            the max epoch
	 * @param id
	 *            the id
	 * @param verbose
	 *            the verbose
	 */
	public PretrainThreadNet(File name, TestPatterns testPatterns,
			int hiddenLayer, int maxEpoch, int id, boolean verbose) {
		super(name, testPatterns, hiddenLayer, maxEpoch, id, verbose);
	}

	/**
	 * Create Thread Net. filename, pretrainPatterns, posttrain patterns, hidden
	 * layer max epoch, postepoch, id, combine, verbose
	 * 
	 * @param name
	 *            the name
	 * @param preTrainPatterns
	 *            the pre train patterns
	 * @param postTrainPatterns
	 *            the post train patterns
	 * @param hiddenLayer
	 *            the hidden layer
	 * @param maxEpoch
	 *            the max epoch
	 * @param postEpoch
	 *            the post epoch
	 * @param id
	 *            the id
	 * @param combine
	 *            the combine
	 * @param verbose
	 *            the verbose
	 */
	public PretrainThreadNet(File name, TestPatterns preTrainPatterns,
			TestPatterns postTrainPatterns, int hiddenLayer, int maxEpoch,
			int postEpoch, int id, boolean combine, boolean verbose) {
		this.name = name;
		this.testPatterns = preTrainPatterns;
		this.combine = combine;
		if (combine) {
			this.postTrainPatterns = combinePatterns(preTrainPatterns,
					postTrainPatterns, verbose);
		} else {
			this.postTrainPatterns = postTrainPatterns;
		}
		this.postEpoch = postEpoch;
		this.hiddenLayer = hiddenLayer;
		this.maxEpoch = maxEpoch;
		this.id = id;
		this.verbose = verbose;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.neuralNet.ThreadNet#call()
	 */
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

	/**
	 * Gets the post train patterns.
	 * 
	 * @return the post train patterns
	 */
	public TestPatterns getPostTrainPatterns() {
		return postTrainPatterns;
	}

	/**
	 * Checks for post epoch.
	 * 
	 * @return true, if successful
	 */
	private boolean hasPostEpoch() {
		return this.postEpoch != null;
	}

	/**
	 * Checks for post train patterns.
	 * 
	 * @return true, if successful
	 */
	private boolean hasPostTrainPatterns() {
		return (this.postTrainPatterns != null);
	}

	/**
	 * Post config.
	 * 
	 * @param mn
	 *            the mn
	 */
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

	/**
	 * Sets post train patterns and whether they should be combined *.
	 * 
	 * @param postTrainPatterns
	 *            the post train patterns
	 * @param combine
	 *            the combine
	 */
	public void setPostTrainPatterns(TestPatterns postTrainPatterns,
			boolean combine) {
		if (combine) {
			this.postTrainPatterns = combinePatterns(this.testPatterns,
					postTrainPatterns, verbose);
		} else {
			this.postTrainPatterns = postTrainPatterns;
		}
	}

}
