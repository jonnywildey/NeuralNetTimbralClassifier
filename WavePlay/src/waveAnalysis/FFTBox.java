package waveAnalysis;

import riff.Signal;
import waveProcess.Gain;
import waveProcess.Pitch;
import filemanager.ArrayStuff;
import filemanager.Log;

/** Object for representing the data from Fourier Transforms. Would have called
 * it FFTTable but i found the two Ts confusing. The box contains a row representing
 * frequency (or some variant of) and at least 1 row (potentially more if using windows)
 * of fourier data. Also contains lots of useful static methods for manipulating this data**/
public class FFTBox {
	
	double[][] table;
	int sampleFreq;
	int bits;
	int frameSize; //frame size of fft. Must be 2^
	boolean freq;
	boolean bark;
	boolean log;
	
	public FFTBox(double[][] fftData, Signal s1) {
		this(fftData);
		this.setFromSignal(s1);
	}
	
	private void setFromSignal(Signal s1) {
		if (s1 != null) {
			this.sampleFreq = s1.getSampleRate();
			this.bits = s1.getBit();
		}
	}

	public FFTBox(double[][] fftData) {
		this.setTable(fftData);
	}
	
	/**Constructor that takes soft information (bits, sampleRate) from fftBox **/
	public FFTBox(double[][] fftData, FFTBox fftBox) {
		this(fftData);
		this.setSoft(fftBox);
	}

	/** sets everything apart from table data **/
	private void setSoft(FFTBox fftBox) {
		this.bark = fftBox.bark;
		this.freq = fftBox.freq;
		this.log = fftBox.log;
		this.frameSize = fftBox.frameSize;
		this.bits = fftBox.bits;
		this.sampleFreq = fftBox.sampleFreq;
		
	}

	private void setTable(double[][] fftData) {
		this.table = fftData;
		this.frameSize = fftData[0].length;
		this.freq = true;
		this.bark = false;
		this.log = false;
	}

	/** Returns the frequency row in its current form **/
	public double[] getFreqRow() {
		if (tableExists()) {
			return table[0];
		} else {
			Log.d("ARGHH");
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
		return (this.table != null);
	}

	/** Converts tables amplitude values to decibels away from maximum amplitude **/
	public static FFTBox convertTableToDecibels(Signal s, FFTBox fftBox) {
		double[][] table = fftBox.getTable();
		double max = Gain.amplitudeToDecibel(s.getMaxAmplitude());
		//Log.d(max);
		double[][] nt = new double[table.length][table[0].length];
		nt[0] = table[0]; //freqrrow
		for (int i = 1; i < table.length; ++i) { //skip first column
			for (int j = 0; j < table[i].length; ++j) {
				nt[i][j] = Gain.amplitudeToDecibel(table[i][j]) - max;
				//Log.d(table[i][j]);
			}
		}
		return new FFTBox(nt);
	}
	
	/**converts table to decibels **/
	public static FFTBox convertTableToDecibels(FFTBox fftBox) {
		Signal s = new Signal(null, 0, 0); // 0 max amplitude
		return convertTableToDecibels(s, fftBox);
	}

	/** returns the table as bark subset **/
	public static FFTBox getBarkedSubset(FFTBox fftBox) {
		return getHiResBarkedSubset(fftBox, 0, 1);
	}

	/** returns the table as bark subset with extra half measurements in the first x **/
	public static FFTBox getHiResBarkedSubset(FFTBox fftBox, int x, double div) {
		double[][] table = fftBox.getTable();
		//get tables 
		int to = 0; //holder for array index
		int from = 0; //holder for array index
		int b = 20 + x; //how many barks max
		double lim = 0; //for determining cutoff
		double[][] nt = new double[table.length][b]; //new array
		table[0] = Pitch.freqToBark(table[0]);
		double bCount = 1;
		for (int i = 0;i < b; ++i) { //for each bark
			nt[0][i] = bCount;
			if (i < x) {
				bCount += div;
				lim = i + div;
			} else {
				lim = i + 1;
				bCount += 1;
			}
			
			for (int j = to; j < table[0].length; ++j) { //cycle through from last
				if (table[0][j] > lim | 
						j >= table[0].length - 1) { //found new value in freq table
					//Log.d(from + " " + to + " : " + table[0][j] + " " + j);
					lim = table[0][j]; //set to new
					from = to; // set to new
					to = j; //set to new
					for (int k = 1; k < table.length; ++k) { //input into new array
						nt[k][i] = ArrayStuff.getAverageOfSubset(table[k], from, to);
					}
					break; //and out to i loop
				}
			}
		}
		return new FFTBox(nt);
	}

	/** Finds the maximum value in an FFT table and normalises the table to the ceiling. **/
	public static FFTBox normaliseTable(FFTBox fftBox, double ceiling) {
		double[][] table = fftBox.getTable();
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 1; i < table.length; ++i) {
			for (int j = 0; j < table[i].length;++j) {
				max = (table[i][j] > max) ? table[i][j] : max;
			}
		}
		//Log.d("max:" + max);
		double[][] nt = new double[table.length][table[0].length];
		double factor = ceiling / max;
		nt[0] = table[0]; //get barks
		//Log.d("factor: " + factor);
		for (int i = 1; i < table.length; ++i) {
			for (int j = 0; j < table[i].length;++j) {
				nt[i][j] = table[i][j] * factor;
				
			}
		}
		return new FFTBox(nt);
	}

	/** converts frequecies to a power of x. **/
	public static FFTBox logarithmicFreq(FFTBox fftBox, double power) {
		double[][] table = fftBox.getTable();
		//double min = ArrayStuff.getMin(table[0]);
		double[][] nt = ArrayStuff.copy(table);
		double mlog = Math.log(power);
		for (int i = 0; i < table[0].length; ++i) {
			nt[0][i] = Math.log(table[0][i])	/ mlog;		
		}
		return new FFTBox(nt);
	}
	
	/** converts frequecies to a power of 2. Useful for graphs **/
	public static FFTBox logarithmicFreq(FFTBox fftBox) {
		return logarithmicFreq(fftBox, 2);
	}
	
	/**Sets the frequency row. Assumes hz **/
	public void setFreqRow(double[] freqRow) {
		//check if freq row length and f lengths are the same?
		this.table[0] = freqRow;
		this.bark = false;
		this.freq = true;
		this.log = false;
	}

	/** returns the sum of the bins **/
	public static FFTBox getSumFFTBox(FFTBox fftBox) {
		return getSumTable(fftBox, fftBox.getTable().length);
	}

	/** returns the sum of the bins **/
	public static FFTBox getSumTable(FFTBox fftBox, int count) {
		double[][] table = fftBox.getTable();
		if (count + 1 > table.length) {
			Log.d("Count larger than table length. Using table length");
			count = table.length -1;
		}
		double[][] sums = new double[2][table[0].length];
		sums[0] = table[0]; //freq row
		for (int i = 0; i < table[0].length; ++i) {
			for (int j = 1; j < count + 1; ++j) {
				sums[1][i] += table[j][i];
			}
		}
		return new FFTBox(sums, fftBox);
	}

	/** Sum table, adding increasingly less value to further rows in time **/
	public static FFTBox getExponentFFTBox(FFTBox fftBox, double exponent) {
		double[][] table = fftBox.getTable();
		double[][] sums = new double[2][table[0].length];
		sums[0] = table[0]; //freq row
		for (int i = 0; i < table[0].length; ++i) {
			for (int j = 1; j < table.length; ++j) {
				sums[1][i] += table[j][i] * (Math.pow(exponent, j));
			}
		}
		return new FFTBox(sums, fftBox);
	}
	
	/** return the values (without the frequency row) **/
	public double[][] getValues() {
		
		double[][] vals = new double[this.table.length - 1][];
		for (int i = 0; i < vals.length; ++i) {
			vals[i] = this.table[i + 1];
		}
		return vals;
	}
	
	/** get frequency row and values as a single 2d table **/
	public double[][] getTable() {
		return table;
	}

	public int getSampleFreq() {
		return sampleFreq;
	}

	public int getBits() {
		return bits;
	}

	public int getFrame() {
		return frameSize;
	}
	
	/** Sets the frame size. Be careful as this can 
	 * be different than the table lengths and could 
	 * badly effect normalisation. **/
	public void setFrameSize(double frameSize) {
		this.frameSize = (int) frameSize;
	}
	
	/** Sets the frame size. Be careful as this can 
	 * be different than the table lengths and could 
	 * badly effect normalisation. **/
	public void setFrameSize(int frameSize) {
		this.frameSize = frameSize;
	}

	/**Filters an FFTBox by frequency. Does not care about frame size
	 * (as long as to and from frequencies exist). Also works for windowed FFTs **/
	public static FFTBox filter(FFTBox fftBox, double from, double to) {
		double[][] table = fftBox.getTable();
		//get min and max
		int min = 0;
		int max = 0;
		double[] freqRow = table[0];
		//get array positions of to and from
		for (int i = 0; i < freqRow.length; ++i) {
			if (freqRow[i] > from) {
				min = i;
				break;
			}
		}
		for (int i = freqRow.length - 1; i >= 0; --i) {
			if (freqRow[i] < to) {
				max = i;
				break;
			}
		}
		//make new table
		double[][] newt = new double[table.length][];
		for (int i = 0; i < newt.length; ++i) {
			newt[i] = ArrayStuff.getSubset(table[i], min, max);
			
		}
		return new FFTBox(newt, fftBox);
	}
	
	/** Combines the frequency row and values into one table **/
	protected static double[][] combine(double[] freqRow, double[][] values) {
		double[][] nt = new double[values.length + 1][];
		nt[0] = freqRow;
		for (int i = 1; i < nt.length; ++i) {
			nt[i] = values[i - 1];
		}
		return nt;
	}

	/** basic noise cancelling. Also removes first one of array 
	 * @return **/
	public static FFTBox sumDifference(FFTBox fftBox) {
		double[][] table = fftBox.getTable();
		double[][] nt = new double[table.length - 1][table[0].length];
		nt[0] = table[0];
		for (int i = 2; i <= nt.length; ++i) {
			for (int j = 0; j < table[i].length; ++j) {
			nt[i - 1][j] = (table[i - 1][j] - table[i][j]); 
			}
		}
		//Log.d(Arrays.deepToString(nt));
		return new FFTBox(nt, fftBox);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(this.table.length * 15);
		double[] fr = this.getFreqRow();
		double[][] vals = this.getValues();
		for (int i = 0; i < vals.length; ++i) {
			for (int j = 0; j < vals[i].length; ++j) {
				sb.append(Statistics.round(fr[j], 4) + "hz ");
				sb.append(Statistics.round(vals[i][j], 4) + " ");
			}
			sb.append("/t");
		}
		return sb.toString();
	}
}
