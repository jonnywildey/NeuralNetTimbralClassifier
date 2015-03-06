package com.neuralNet.pattern;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import com.DSP.waveAnalysis.Statistics;
import com.neuralNet.NeuralComponent;
import com.neuralNet.layers.InputShell;

/**
 * Basic pattern for neural networks. Principally an input array and a target
 * array
 **/
public class Pattern implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1620481621127344275L;
	/** Converts double values to input shell objects **/
	public static ArrayList<InputShell> doubleToInputShell(double[] values) {
		ArrayList<InputShell> al = new ArrayList<InputShell>(values.length);
		for (double value : values) {
			al.add(new InputShell(value));
		}
		return al;
	}
	private ArrayList<InputShell> inputArray;
	private ArrayList<Double> targetArray;
	protected ArrayList<Double> errorList;
	private Integer id;

	public File filePath; // if the pattern relates to some other object

	/** Default constructor. Not recommended **/
	public Pattern() {
		super();
		this.targetArray = new ArrayList<Double>();
		this.inputArray = new ArrayList<InputShell>();
		this.errorList = new ArrayList<Double>();
		this.setId(-1);
	}

	/** full parameter constructor. input, output, id **/
	public Pattern(ArrayList<Double> doubleArray,
			ArrayList<Double> targetArray, Integer id) {
		setInputArray(new ArrayList<InputShell>());
		for (Double f : doubleArray) { // set inputshell array
			getInputArray().add(new InputShell(f));
		}
		this.setTargetArray(new ArrayList<Double>(targetArray));
		this.errorList = new ArrayList<Double>();
		this.setId(id);
	}

	/** id constructor **/
	public Pattern(int id) {
		this();
		this.setId(id);
	}

	public File getFilePath() {
		return filePath;
	}

	public Integer getId() {
		return id;
	}

	/** Return an arraylist of the input array **/
	public ArrayList<InputShell> getInputArray() {
		return inputArray;
	}

	/** return the input vector count **/
	public int getInputCount() {
		return this.getInputArray().size();
	}

	/** return the target count **/
	public int getOutputCount() {
		return this.getTargetArray().size();
	}

	/** Get target array **/
	public ArrayList<Double> getTargetArray() {
		return targetArray;
	}

	/**
	 * return the target as an int rather than as an array. Only works with bit
	 * arrays
	 **/
	public int getTargetNumber() {
		int answer = 999;
		for (int i = 0; i < this.getInputArray().size(); ++i) {
			if (getTargetArray().get(i) == 1) {
				answer = i;
				break;
			}
		}
		return answer;
	}

	/** Is there an input array? **/
	public boolean isInputArray() {
		return (this.inputArray != null);
	}

	/** Is there a target array? **/
	public boolean isTargetArray() {
		return (this.targetArray != null);
	}

	public void setFilePath(File filePath) {
		this.filePath = filePath;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/** Set the input array **/
	public void setInputArray(ArrayList<InputShell> inputArray) {
		this.inputArray = inputArray;
	}

	/** Set target array **/
	public void setTargetArray(ArrayList<Double> targetArray) {
		this.targetArray = targetArray;
	}

	/** Comprehensible String representation of pattern. **/
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nTraining Pattern id: " + this.getId() + "\ninputs:\n");
		if (this.isInputArray()) {
			for (NeuralComponent i : getInputArray()) {
				sb.append(Statistics.round(i.getValue(), 4) + "\t");
			}
		} else {
			sb.append("No input array!");
		}
		sb.append("\nTarget\n");
		if (this.isTargetArray()) {
			for (Double i : getTargetArray()) {
				sb.append(Statistics.round(i, 4) + "\t"); // rounded
			}
		} else {
			sb.append("No Target Array!");
		}
		sb.append("Number: ");
		sb.append(getTargetNumber());
		sb.append("\n");
		return sb.toString();
	}

}
