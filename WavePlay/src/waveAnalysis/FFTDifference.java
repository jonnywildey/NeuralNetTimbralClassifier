package waveAnalysis;

import filemanager.ArrayStuff;
import filemanager.Log;
import plotting.FFTController;
import plotting.FFTDifferenceController;

/** Object for analysing the difference between
 * successive FFT windows
 * @author Jonny
 *
 */
public class FFTDifference {
	
	protected FrameFFT frameFFT;
	protected double[][] table;
	
	public FFTDifference(FrameFFT frameFFT) {
		this.frameFFT = frameFFT;
	}
	
	public double[][] analyse() {
		this.table = getDifferenceTable(this.frameFFT.getTable(), 
				this.frameFFT.getTable().length - 1);
		return this.table;
	}
	
	public double[][] analyse(int frames) {
		this.table = getDifferenceTable(this.frameFFT.getTable(), 
				frames);
		return this.table;
	}
	
	/**Quick normalised frequency response graph **/
	public void makeGraph() {
		makeGraph(40, 20000, 800, 600);
	}
	
	/** split the frequency bins into x domains and average them **/
	public static double[][] splitAndAverage(double[][] table, int bands) {
		double length = table[0].length;
		int bw = (int) (length / (double)bands);
		double[][] nb = new double[table.length][bands];
		//relabel
		for (int i = 0; i < bands; ++i) {
			nb[0][i] = table[0][i * bw];
		}
		//get averages
		for (int i = 1; i < nb.length; ++i) {
			for (int j = 0; j < bands; ++j) {
				nb[i][j] = ArrayStuff.getAverageOfSubset(table[i], j * bw, (j + 1) * bw);
			}
		}
		return nb;
	}
	
	
	/**Quick normalised frequency response graph with filter options**/
	public void makeGraph(int filterFrom, int filterTo, int width, int height) {
		FFTDifferenceController sc = new FFTDifferenceController(FrameFFT.logarithmicFreq(FFT.filter(
				//FrameFFT.convertTableToDecibels(this.frameFFT.signal, this.table, this.frameFFT.frameSize),  
						this.table,
						filterFrom, filterTo)), width, height);
		sc.makeChart();
	}
	
	/** Obtain the difference table **/
	public static double[][] getDifferenceTable(double[][] table, int frames) {
		//array is one less than table (because of difference)
		double[][] dif = new double[frames + 1][table[0].length];
		dif[0] = table[0]; //freq rows are the same
		for (int i = 2; i < frames + 1; ++i) {
			//Log.d(table[i].length);
			for (int j = 0; j < table[0].length;++j) {
				dif[i - 1][j] = (table[i - 1][j] - table[i][j]);
				//Log.d(dif[i-1][j]);
			}
			
		}
		return dif;
	}
}
