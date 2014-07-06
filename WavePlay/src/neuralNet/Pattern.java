package neuralNet;

import java.io.Serializable;
import java.util.ArrayList;

public class Pattern implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1620481621127344275L;
	//protected ArrayList<Double> inputArray;
	protected ArrayList<InputShell> inputArray;
	protected ArrayList<Double> targetArray;
	protected ArrayList<Double> errorList;
	protected Integer id;
	
	public Pattern(int id) {
		this.id = id;
	}
	
	public Pattern(ArrayList<Double> DoubleArray, ArrayList<Double> targetArray, Integer id) {
		inputArray = new ArrayList<InputShell>();
		for (Double f: DoubleArray) { //set inputshell array
			inputArray.add(new InputShell(f));
		}
		
		this.targetArray = new ArrayList<Double>(targetArray);
		this.errorList = new ArrayList<Double>();
		this.id = id;
	}
	
	/** return the input vector count **/
	public int getInputCount() {
		return this.inputArray.size();
	}
	
	/** return the target count **/
	public int getOutputCount() {
		return this.targetArray.size();
	}
	
	public int getTargetNumber() {
		int answer = 999;
		for (int i = 0; i < this.inputArray.size(); ++i) {
			if (targetArray.get(i) == 1) {
				//System.out.println(i + " " + this.targetArray.toString());
				answer = i;
				break;
			}
		}
		return answer;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nTraining Pattern id: " + this.id + "\ninputs:\n");
		for (NeuralComponent i: inputArray) {
			sb.append(i.getValue() + "\t");
		}
		sb.append("\nTarget\n" );
		for (Double i: targetArray) {
			sb.append(i + "\t");
		}
		sb.append("Number: ");
		sb.append(getTargetNumber());
		sb.append("\n");
		return sb.toString();
	}
	
	
	
	
	
}
