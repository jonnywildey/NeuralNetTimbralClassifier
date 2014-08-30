package com.neuralNet;

import java.util.ArrayList;
import java.util.Random;

import com.neuralNet.layers.LayerList;
import com.neuralNet.layers.NeuralLayer;
import com.neuralNet.layers.Neuron;
import com.neuralNet.matrix.ConfusionMatrix;
import com.neuralNet.pattern.Pattern;
import com.neuralNet.pattern.WavePattern;
import com.util.Log;

/**
 * Run through of a group of patterns *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class Epoch {
	protected ArrayList<Pattern> trainingPatterns;
	protected ArrayList<Pattern> testingPatterns;
	protected LayerList neurons;
	protected ArrayList<Double> errorAmount;
	protected ArrayList<Double> errorList;
	protected ArrayList<Double> rmsError;
	protected Double trainingRate;
	protected ConfusionMatrix testConfusionMatrix;
	public ConfusionMatrix trainingConfusionMatrix;
	protected boolean debug;
	protected boolean verbose;
	protected Random shuffleRandom;
	protected String[] targets; // labels for outputs
	private boolean addMaxOutputNeuron;
	public Double meanError;

	/**
	 * Instantiates a new epoch.
	 */
	public Epoch() {
		this.trainingPatterns = new ArrayList<Pattern>();
		this.testingPatterns = new ArrayList<Pattern>();
		this.errorAmount = new ArrayList<Double>();
		this.errorList = new ArrayList<Double>();
		this.rmsError = new ArrayList<Double>();
		this.shuffleRandom = new Random();
	}

	/**
	 * Instantiates a new epoch.
	 * 
	 * @param trainingPatterns
	 *            the training patterns
	 * @param testingPatterns
	 *            the testing patterns
	 * @param neurons
	 *            the neurons
	 * @param trainingRate
	 *            the training rate
	 * @param verbose
	 *            the verbose
	 * @param debug
	 *            the debug
	 */
	public Epoch(ArrayList<Pattern> trainingPatterns,
			ArrayList<Pattern> testingPatterns, LayerList neurons,
			Double trainingRate, String[] targets, boolean verbose,
			boolean debug) {
		this();
		this.neurons = neurons;
		this.trainingPatterns = trainingPatterns;
		this.testingPatterns = testingPatterns;
		this.trainingRate = trainingRate;
		this.verbose = verbose;
		this.debug = debug;
		this.targets = targets;
		this.addMaxOutputNeuron = true;

		// set up lists. only useful for error based epochs
		for (int i = 0; i < neurons.getOutputCount(); ++i) { // is only last
			// correct?
			this.errorAmount.add(0d);
			this.errorList.add(0d);
			this.rmsError.add(0d);
		}
	}

	/**
	 * Adds the max.
	 * 
	 * @param p
	 *            the p
	 * @param cm
	 *            the cm
	 */
	public void addMax(Pattern p, ConfusionMatrix cm) {
		int nid = getMaxId();
		cm.addToCell(nid, p.getTargetNumber());
		// Neuron Row, Class Column
	}

	/**
	 * add neuron outputs > 0.5 *
	 * 
	 * @param p
	 *            the p
	 * @param cm
	 *            the cm
	 */
	public void addRounded(Pattern p, ConfusionMatrix cm) {
		for (Neuron n : neurons.getLastLayer().neurons) {
			int roundedOut = (int) Math.rint(n.getOutput());
			if (roundedOut == 1) {
				cm.addToCell(n.getId(), p.getTargetNumber());
				// Neuron Row, Class Column
			}
		}
	}

	/**
	 * Adds the training pattern.
	 * 
	 * @param trainingPattern
	 *            the training pattern
	 */
	public void addTrainingPattern(Pattern trainingPattern) {
		trainingPatterns.add(trainingPattern);
	}

	/**
	 * Gets the confusion matrix.
	 * 
	 * @return the confusion matrix
	 */
	public ConfusionMatrix getConfusionMatrix() {
		return testConfusionMatrix;
	}

	/**
	 * Gets the max id.
	 * 
	 * @return the max id
	 */
	public int getMaxId() {
		double max = 0;
		int nid = 0;
		for (Neuron n : neurons.getLastLayer().neurons) {
			if (n.getOutput() > max) {
				max = n.getOutput();
				nid = n.getId();
			}
		}
		return nid;
	}

	/**
	 * Gets the outputs.
	 * 
	 * @return the outputs
	 */
	public double[] getOutputs() {
		double[] d = new double[this.neurons.getOutputCount()];
		for (int i = 0; i < d.length; ++i) {
			d[i] = this.neurons.getLastLayer().neurons.get(i).getOutput();
		}
		return d;
	}

	/**
	 * returns an arraylist of all patterns that were incorrect *.
	 * 
	 * @return the problem patterns
	 */
	public ArrayList<WavePattern> getProblemPatterns() {
		ArrayList<WavePattern> problems = new ArrayList<WavePattern>();
		for (Pattern pattern : this.testingPatterns) {
			WavePattern p = (WavePattern) pattern;
			// input pattern inputs
			// forward pass
			for (int i = 0; i < neurons.getLayerCount(); ++i) {
				NeuralLayer l = neurons.getLayer(i);
				l.setTrainingRate(trainingRate);
				l.process(p);
			}
			if (!isRight(p)) {
				problems.add(p);
			}
		}
		return problems;
	}

	/**
	 * Checks if is right.
	 * 
	 * @param p
	 *            the p
	 * @return true, if is right
	 */
	public boolean isRight(Pattern p) {
		double max = 0;
		int nid = 0;
		for (Neuron n : neurons.getLastLayer().neurons) {
			if (n.getOutput() > max) {
				max = n.getOutput();
				nid = n.getId();
			}
		}
		return nid == p.getTargetNumber();
	}

	/**
	 * Runs a training epoch.
	 */
	public void runEpoch() {
		trainingConfusionMatrix = new ConfusionMatrix(neurons.getOutputCount(),
				verbose);
		trainingConfusionMatrix.setTargets(this.targets);
		Double patternErrors = 0d;
		// Load Pattern
		neurons.setLearning(true);
		for (Pattern p : this.trainingPatterns) {
			// forward pass
			for (int i = 0; i < neurons.getLayerCount(); ++i) {
				NeuralLayer l = neurons.getLayer(i);
				l.setTrainingRate(trainingRate);
				l.process(p);
			}
			// backward pass
			for (int i = neurons.getLayerCount() - 1; i >= 0; --i) {
				NeuralLayer l = neurons.getLayer(i);
				l.calculateDelta(p);
				l.setWeights(p);
			}
			if (addMaxOutputNeuron) {
				addMax(p, trainingConfusionMatrix);
			} else {
				addRounded(p, trainingConfusionMatrix);
			}
			patternErrors += neurons.getLastLayer().getErrors();
		}
		if (verbose) {
			Log.d(trainingConfusionMatrix.toString());
		}
		double error = Math.pow(
				(this.trainingPatterns.size() * this.neurons.getOutputCount()),
				-1) * patternErrors;
		this.meanError = Math.pow(error, 0.5);

	}

	/**
	 * Run pattern.
	 * 
	 * @param p
	 *            the p
	 * @return the double[]
	 */
	public double[] runPattern(Pattern p) {
		neurons.setLearning(false);
		for (int i = 0; i < neurons.getLayerCount(); ++i) {
			NeuralLayer l = neurons.getLayer(i);
			l.process(p);
		}
		return getOutputs();
	}

	/**
	 * Run patterns.
	 * 
	 * @param patterns
	 *            the patterns
	 * @return the double[][]
	 */
	public double[][] runPatterns(ArrayList<Pattern> patterns) {
		double[][] ds = new double[patterns.size()][];
		for (int i = 0; i < ds.length; ++i) {
			ds[i] = runPattern(patterns.get(i));
		}
		return ds;
	}

	/**
	 * Run patterns.
	 * 
	 * @param patterns
	 *            the patterns
	 * @return the double[][]
	 */
	public double[][] runPatterns(Pattern[] patterns) {
		double[][] ds = new double[patterns.length][];
		for (int i = 0; i < ds.length; ++i) {
			ds[i] = runPattern(patterns[i]);
		}
		return ds;
	}

	/**
	 * Run patterns get max.
	 * 
	 * @param patterns
	 *            the patterns
	 * @return the int[]
	 */
	public int[] runPatternsGetMax(ArrayList<Pattern> patterns) {
		int[] ds = new int[patterns.size()];
		for (int i = 0; i < ds.length; ++i) {
			runPattern(patterns.get(i));
			ds[i] = getMaxId();
		}
		return ds;
	}

	/**
	 * Run patterns get max.
	 * 
	 * @param patterns
	 *            the patterns
	 * @return the int[]
	 */
	public int[] runPatternsGetMax(Pattern[] patterns) {
		int[] ds = new int[patterns.length];
		for (int i = 0; i < ds.length; ++i) {
			runPattern(patterns[i]);
			ds[i] = getMaxId();
		}
		return ds;
	}

	/**
	 * Run validation epoch.
	 * 
	 * @return the confusion matrix
	 */
	public ConfusionMatrix runValidationEpoch() {
		testConfusionMatrix = new ConfusionMatrix(neurons.getOutputCount(),
				verbose);
		testConfusionMatrix.setTargets(this.targets);
		neurons.setLearning(false);
		for (Pattern p : this.testingPatterns) {
			// input pattern inputs
			// forward pass
			for (int i = 0; i < neurons.getLayerCount(); ++i) {
				NeuralLayer l = neurons.getLayer(i);
				// l.setTrainingRate(trainingRate);
				l.process(p);
			}
			if (addMaxOutputNeuron) {
				addMax(p, testConfusionMatrix);
			} else {
				addRounded(p, testConfusionMatrix);
			}
		}
		if (verbose) {
			Log.d(testConfusionMatrix.toString());
		}
		return testConfusionMatrix;
	}

	/**
	 * Sets the debug.
	 * 
	 * @param debug
	 *            the new debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Sets the shuffle seed.
	 * 
	 * @param seed
	 *            the new shuffle seed
	 */
	public void setShuffleSeed(long seed) {
		shuffleRandom.setSeed(seed);
	}

	/**
	 * Sets the training rate.
	 * 
	 * @param trainingRate
	 *            the new training rate
	 */
	public void setTrainingRate(Double trainingRate) {
		this.trainingRate = trainingRate;
	}

	/**
	 * Sets the verbose.
	 * 
	 * @param verbose
	 *            the new verbose
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Shuffle training patterns.
	 */
	public void shuffleTrainingPatterns() {
		Pattern holder = null;// ugh
		int j = 0;
		for (int i = trainingPatterns.size() - 1; i > 0; --i) {
			j = (int) Math.rint(shuffleRandom.nextDouble() * i); // random
			// number
			holder = (trainingPatterns.get(j));
			trainingPatterns.set(j, trainingPatterns.get(i));
			trainingPatterns.set(i, holder);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.neurons.toString();

	}

}
