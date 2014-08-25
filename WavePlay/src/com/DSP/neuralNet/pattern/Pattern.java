package neuralNet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import com.util.Log;
import com.waveAnalysis.Statistics;

/**Basic pattern for neural networks. Principally an input array and a target array **/
public class Pattern implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1620481621127344275L;
	private ArrayList<InputShell> inputArray;
	private ArrayList<Double> targetArray;
	protected ArrayList<Double> errorList; 
	private Integer id; 
	public File filePath; //if the pattern relates to some other object
	
	/** Default constructor. Not recommended **/
	public Pattern() {
		super();
		this.setId(-1);
	}
	
	/** id constructor **/
	public Pattern(int id) {
		this.setId(id);
	}
	
	/** full parameter constructor. input, output, id **/
	public Pattern(ArrayList<Double> doubleArray, ArrayList<Double> targetArray, Integer id) {
		setInputArray(new ArrayList<InputShell>());
		for (Double f: doubleArray) { //set inputshell array
			getInputArray().add(new InputShell(f));
		}
		this.setTargetArray(new ArrayList<Double>(targetArray));
		this.errorList = new ArrayList<Double>();
		this.setId(id);
	}
	
	/** return the input vector count **/
	public int getInputCount() {
		return this.getInputArray().size();
	}
	
	/** return the target count **/
	public int getOutputCount() {
		return this.getTargetArray().size();
	}
	
	
	/** return the target as an int rather than as an array. 
	 * Only works with bit arrays **/
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
	
	/** Is there a target array? **/
	public boolean isTargetArray() {
		return (this.targetArray != null);
	}
	
	/** Is there an input array? **/
	public boolean isInputArray() {
		return (this.inputArray != null);
	}
	
	/**Comprehensible String representation of pattern.**/
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nTraining Pattern id: " + this.getId() + "\ninputs:\n");
		if (this.isInputArray()) {
			for (NeuralComponent i: getInputArray()) {
				sb.append(Statistics.round(i.getValue(), 4) + "\t");
			}
		} else {
			sb.append("No input array!");
		}
		sb.append("\nTarget\n" );
		if (this.isTargetArray()) {
			for (Double i: getTargetArray()) {
				sb.append(Statistics.round(i, 4) + "\t"); //rounded
			}
		} else {
			sb.append("No Target Array!");
		}
		sb.append("Number: ");
		sb.append(getTargetNumber());
		sb.append("\n");
		return sb.toString();
	}

	public File getFilePath() {
		return filePath;
	}

	public void setFilePath(File filePath) {
		this.filePath = filePath;
	}

	/**Converts double values to input shell objects **/
	public static ArrayList<InputShell> doubleToInputShell(double[] values) {
		ArrayList<InputShell> al = new ArrayList<InputShell>(values.length);
		for (double value : values) {
			al.add(new InputShell(value));
		}
		return al;
	}

	/** Return an arraylist of the input array **/
	public ArrayList<InputShell> getInputArray() {
		return inputArray;
	}

	/** Set the input array **/
	public void setInputArray(ArrayList<InputShell> inputArray) {
		this.inputArray = inputArray;
	}

	/** Get target array **/
	public ArrayList<Double> getTargetArray() {
		return targetArray;
	}

	/**Set target array **/
	public void setTargetArray(ArrayList<Double> targetArray) {
		this.targetArray = targetArray;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
	
}
