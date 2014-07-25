package waveAnalysis;

/** Object for representing the data from Fourier Transforms **/
public class FTable {
	
	double[][] table;
	boolean freq;
	boolean bark;
	boolean log;
	
	/** Returns the frequency row in its current form **/
	public double[] getFreqRow() {
		if (tableExists()) {
			return table[0];
		} else {
			return null;
		}
	}
	
	/**Converts the table to bark scale, if it isn't already **/
	public void convertToBark() {
		if (!this.bark) {
			//do thing
			
			this.bark = true;
			this.freq = false;
			this.log = false;
		}
	}
	
	/**Converts the table to freq scale, if it isn't already **/
	public void convertToFreq() {
		if (!this.freq) {
			//do thing
			
			this.bark = false;
			this.freq = true;
			this.log = false;
		}
	}
	
	/**Converts the table to log scale, if it isn't already **/
	public void convertToLog() {
		if (!this.log) {
			//do thing
			
			this.bark = false;
			this.freq = false;
			this.log = true;
		}
	}
	
	/** returns how many windows in table **/
	public int getWindows() {
		if (tableExists()) {
			return table.length - 1;
		} else {
			return 0;
		}
	}
	
	/**Does the table exist? **/
	private boolean tableExists() {
		return (this.table == null);
	}
}
