package neuralNet;

import plotting.MatthewsChart;
import filemanager.ArrayStuff;

/** a little holder for errors **/
public class CoefficientLogger {
	private double[] errors;
	private int iterator;
	public CoefficientLogger(int epochs) {
		this.errors = new double[epochs];
		this.iterator = 0;
	}
	public void add(double val) {
		errors[iterator] = val;
		iterator++;
	}
	
	public void makeGraph() {
		double[] bd = ArrayStuff.normalizeDouble(this.errors, 228); //400 somewhat arbitrary
		long[] la = ArrayStuff.doubleToLong(bd);
		long[][] las = {la};
		MatthewsChart nc = new MatthewsChart(las, 900, 300);
		nc.makeChart();
	}
	
	public double[] getErrors() {
		return this.errors;
	}

}
