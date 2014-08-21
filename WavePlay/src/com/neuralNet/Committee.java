package com.neuralNet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import com.matrix.ConfusionMatrix;
import com.neuralNet.pattern.Pattern;
import com.neuralNet.pattern.TestPatterns;
import com.neuralNet.pattern.WavePatterns;
import com.riff.Signal;
import com.riff.Wave;
import com.util.ArrayMethods;
import com.util.Log;
import com.waveProcess.Conversion;

/** Class for combining multiple nets. Cannot be used for training **/
public class Committee implements Serializable {
	
	private static final long serialVersionUID = -3435279964711381310L;
	private MultiLayerNet[] nets;
	
	
	/** run a single pattern against the committee **/
	public static int runPattern(MultiLayerNet[] nets, Pattern p) {
		return runPatterns(nets, new Pattern[]{p})[0];
	}
	
	/** run a single pattern against the committee **/
	public int runPattern(Pattern p) {
		return runPattern(this.nets, p);
	}
	
	/** Run patterns against the committee **/
	public int[] runPatterns(Pattern[] p) {
		return runPatterns(this.nets, p);
	}
	
	/** Runs a wave through the committee **/
	public int runWave(File file) {
		Wave wave = new Wave(file);
		return runSignal(wave.getSignals());
	}
	
	/** Runs a wave through the Committee **/
	public int runWave(Wave wave) {
		return runSignal(wave.getSignals());
	}
	
	/** Runs a signal through the Committee **/
	public int runSignal(Signal s) {
		//find out input amount
		//int input = this.nets[0].getTestPatterns().getTestingPatterns().get(0).getInputArray().size();
		Pattern p = Conversion.signalToPatternMono(s);
		int answer = this.runPattern(p);
		return answer;
	}
	
	/** Runs a wave through the committee **/
	public String runWave(File file, WavePatterns wp) {
		Wave wave = new Wave(file);
		return runSignal(wave.getSignals(), wp);
	}
	
	/** Runs a wave through the Committee **/
	public String runWave(Wave wave, WavePatterns wp) {
		return runSignal(wave.getSignals(), wp);
	}
	
	/** Runs a signal through the Committee **/
	public String runSignal(Signal s, WavePatterns wp) {
		//find out input amount
		Pattern p = Conversion.signalToPatternMono(s);
		int answer = this.runPattern(p);
		//Log.d(answer);
		String ans = wp.getInstrumentFromTargetNumber(answer);
		return ans;
	}
	
	/** Run patterns against the committee **/
	public static int[] runPatterns(MultiLayerNet[] nets, Pattern[] patterns) {
		double[][][] vals = new double[nets.length][][];
		Epoch e = null;
		//get values
		for (int i = 0; i < vals.length; ++i) {
			e = new Epoch(null, null, nets[i].getNeuronLayers(), 0d, nets[i].isVerbose(), nets[i].isDebug());
			vals[i] = e.runPatterns(patterns);
		}
		double[][][] nv = new double[patterns.length][nets.length][patterns[0].getOutputCount()];
		int[] outputs = new int[patterns.length];
		for (int i = 0; i < patterns.length; ++i) {
			//get array of every net for pattern
			for (int j = 0; j < nets.length; ++j) {
				for (int k = 0; k < patterns[0].getOutputCount(); ++k) {
					nv[i][j][k] = vals[j][i][k];
				}
			}
			outputs[i] = getIndexOfMax(
					ArrayMethods.getAverageOfColumn(nv[i]));
		}
		return outputs;
	}
	
	/**return the index of the max **/
	public static int getIndexOfMax(double[] array) {
		double max = Double.MIN_VALUE;
		int index = 0;
		for (int i = 0; i < array.length; ++i) {
			if (array[i] > max) {
				max = array[i];
				index = i;
			}
		}
		return index;
	}
	
	/** Run patterns against the committee and return a confusion
	 * Matrix of results */
	public ConfusionMatrix testPatterns(Pattern[] patterns) {
		return testPatterns(this.nets, patterns);
	}
	
	/** Run patterns against the committee and return a confusion
	 * Matrix of results */
	public ConfusionMatrix testPatterns(ArrayList<Pattern> patterns) {
		return testPatterns(this.nets, patterns);
	}
	
	/** Run patterns against the committee and return a confusion
	 * Matrix of results */
	public static ConfusionMatrix testPatterns(MultiLayerNet[] nets, Pattern[] patterns) {
		int[] outputs = runPatterns(nets, patterns);
		ConfusionMatrix cm = new ConfusionMatrix(patterns[0].getOutputCount());
		for (int i = 0; i < patterns.length; ++i) {
			cm.addToCell(outputs[i], patterns[i].getTargetNumber());
		}
		Log.d("\n*****COMMITTEE*****\n");
		Log.d(cm.toString());
		Log.d("MC for Committee: " + cm.matthewsCoefficient());
		return cm;
	}
	
	/** Run patterns against the committee and return a confusion
	 * Matrix of results */
	public static ConfusionMatrix testPatterns(MultiLayerNet[] nets, ArrayList<Pattern> patterns) {
		return testPatterns(nets, TestPatterns.convertPatterns(patterns));
	}

	public MultiLayerNet[] getNets() {
		return nets;
	}

	public void setNets(MultiLayerNet[] nets) {
		this.nets = nets;
	}

	
}
