package com.neuralNet.matrix;

import com.DSP.waveAnalysis.Statistics;
import com.neuralNet.NNFunctions;
import com.plotting.ConfusionMatrixController;
import com.util.Log;

/**
 * Matrix for mapping classifications. Typically classification is row,
 * correct output is column *
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class ConfusionMatrix extends Matrix {

	/**
	 * Instantiates a new confusion matrix.
	 *
	 * @param size the size
	 */
	public ConfusionMatrix(int size) {
		super(size);
		this.initValue(new Integer(0));
	}
	
	/**
	 * Instantiates a new confusion matrix.
	 *
	 * @param size the size
	 * @param verbose the verbose
	 */
	public ConfusionMatrix(int size, boolean verbose) {
		this(size);
		this.verbose = verbose;
	}
	
	/**
	 * get maximum value of matrix *.
	 *
	 * @return the max
	 */
	public int getMax() {
		Integer max = 0;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				if ((Integer)array[i][j] > max) {
					max = (Integer) array[i][j];
				}
			}
		}
		return max.intValue();
	}
	
	/**
	 * get overall count of matrix *.
	 *
	 * @return the count
	 */
	public int getCount() {
		Integer count = 0;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
					count += (Integer) array[i][j];
			}
		}
		return count.intValue();
	}
	
	/**
	 * get total of matrix *.
	 *
	 * @return the total
	 */
	public int getTotal() {
		Integer tally = 0;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				tally += (Integer)array[i][j];
			}
		}
		return tally;
	}
	
	/**
	 * Adds the to cell.
	 *
	 * @param column the column
	 * @param row the row
	 */
	public void addToCell(int column, int row) {
		this.array[column][row] = (Integer)this.array[column][row] + 1;
	}
	
	/**
	 * calculate Matthews coefficient for individual column *.
	 *
	 * @param col the col
	 * @return the double
	 */
	public double matthewsCoefficient(int col) {
		double truePos = (Integer)array[col][col];
		double trueNeg = getCount() - truePos;
		double falsePos = getFalsePositives(col);
		double falseNeg = getFalseNegatives(col);
		Double mat = 0d;
		//System.out.println("tp " + truePos + " tn " + trueNeg + 
		//					" fp " + falsePos + " fn " + falseNeg);
		double th = (truePos * trueNeg) - (falsePos * falseNeg);
		double bh = ((truePos + falseNeg) * (truePos + falsePos) *
				(trueNeg + falseNeg) * (trueNeg + falsePos));
		bh = (bh == 0) ? 1 : bh;
		mat = th / Math.pow(bh, 0.5);
		return mat;
	}
	
	/**
	 * Return the total number of correct and incorrect classifications *.
	 *
	 * @param col the col
	 * @param row the row
	 * @return the total for cell
	 */
	public int getTotalForCell(int col, int row) {
		return sumColumn(col) + sumRow(row);
	}
	
	/**
	 * Get the sum of a column *.
	 *
	 * @param column the column
	 * @return the int
	 */
	public int sumColumn(int column) {
		int sum = 0;
		for (int i = 0; i < size; ++i) {
			sum += (Integer)array[column][i];
		}
		return sum;
	}
	
	/**
	 * Get the sum of a row *.
	 *
	 * @param row the row
	 * @return the int
	 */
	public int sumRow(int row) {
		int sum = 0;
		for (int i = 0; i < size; ++i) {
			sum += (Integer)array[i][row];
		}
		return sum;
	}
	
	/**
	 * Calculate average Matthews Coefficient *.
	 *
	 * @return the double
	 */
	public double matthewsCoefficient() {
		double[] ms = new double[array.length];
		for (int i = 0; i < array.length; ++i) {
			ms[i] = matthewsCoefficient(i);
			if (this.verbose) {
				Log.d("MC" + i + ": " + Statistics.round(ms[i], 4));
			}
			
		}
		return NNFunctions.average(ms);
	}
	
	/**
	 * Return all false positives in a column *.
	 *
	 * @param col the col
	 * @return the false positives
	 */
	private int getFalsePositives(int col) {
		int total = 0;
		for (int i = 0; i < array.length; ++i) {
			if (i != col) {
				total += (Integer)array[i][col];
			}
		}
		return total;
	}
	
	/**
	 * Return all false negatives in a column *.
	 *
	 * @param col the col
	 * @return the false negatives
	 */
	private int getFalseNegatives(int col) {
		int total = 0;
		for (int i = 0; i < array.length; ++i) {
			if (i != col) {
				total += (Integer)array[col][i];
			}
		}
		return total;
	}
	
	
	/**
	 * returns rate of incorrect classifications to correct (i.e. 0 is perfect)*
	 *
	 * @param total the total
	 * @return the error rate
	 */
	public double getErrorRate(double total) {
		double rightCount = 0;
		double wrongCount = 0;
		for (int i = 0; i < this.size; ++i) {
					rightCount += (Integer)this.array[i][i]; //this will work right?
					for (int j = 0; j < this.size; ++j) {
						if (j != i) {
							wrongCount += (Integer)this.array[i][j];
						}
					}
			}
		if (rightCount + wrongCount < total) {
			return (total / rightCount) - 1;
		} else {
			
			return wrongCount / rightCount;
		}
	}
	
	/**
	 * Useful ASCII representation of matrix *.
	 *
	 * @return the string
	 */
	public String toString() {
		//get max
		int max = this.getMax();
		//get length of max value.
		int length = (int)Math.floor(Math.log10((double)max)) + 1;
		String mainString = "";
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				
				String str = "|" + array[i][j].toString();
				while (str.length() <= length) {
					str = str.concat(" ");
				}
				mainString = mainString.concat(str);
			}
			mainString = mainString.concat("|\n");
		}
		//System.out.println(Arrays.deepToString(array));
		return mainString;
	}
	
	/**
	 * Make graph.
	 */
	public void makeGraph() {
		ConfusionMatrixController cmc = new ConfusionMatrixController(this);
		cmc.makeChart();
	}
	


}
