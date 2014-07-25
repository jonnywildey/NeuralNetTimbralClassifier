package neuralNet;

import java.util.Arrays;

import plotting.MatthewsChart;
import filemanager.ArrayStuff;
import filemanager.Log;

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
		//double[] bd = ArrayStuff.normalizeDouble(this.errors, ); //400 somewhat arbitrary
		//long[] la = ArrayStuff.doubleToLong(this.errors);
		double[][] las = {this.errors};
		MatthewsChart nc = new MatthewsChart(las, 1200, 600);
		nc.makeChart();
	}
	
	public static void makeGraph(CoefficientLogger[] errors) {
		//double[] bd = ArrayStuff.normalizeDouble(this.errors, ); //400 somewhat arbitrary
		//long[] la = ArrayStuff.doubleToLong(this.errors);
		double[][] las = new double[errors.length][];
		for (int i = 0; i < errors.length;++i) {
			las[i] = errors[i].getErrors();
		}
		MatthewsChart nc = new MatthewsChart(las, 1200, 600);
		nc.makeChart();
	}
	
	public double[] getErrors() {
		return this.errors;
	}

}
