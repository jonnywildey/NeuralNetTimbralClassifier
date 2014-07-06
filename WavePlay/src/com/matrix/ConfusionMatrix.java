package com.matrix;

import java.util.Arrays;

public class ConfusionMatrix extends Matrix {

	public ConfusionMatrix(int size) {
		super(size);
		this.initValue(new Integer(0));
	}
	
	/** get maximum value of matrix **/
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
	
	public int getCount() {
		Integer count = 0;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
					count += (Integer) array[i][j];
			}
		}
		return count.intValue();
	}
	
	/** get total of matrix **/
	public int getTotal() {
		Integer tally = 0;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				tally += (Integer)array[i][j];
			}
		}
		return tally;
	}
	
	public void addToCell(int column, int row) {
		this.array[column][row] = (Integer)this.array[column][row] + 1;
	}
	
	/** returns rate of correct classifications to incorrect (i.e. 1 is perfect)**/
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
		//System.out.println(rightCount + " " + wrongCount);
		
		if (rightCount + wrongCount < total) {
			return total / rightCount;
		} else {
			return wrongCount / rightCount;
		}
		
		
	}
	
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
	


}
