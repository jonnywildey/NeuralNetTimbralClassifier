package com.neuralNet;

import java.io.Serializable;
import java.util.Arrays;

import com.DSP.waveProcess.filters.MultiLayerNet;
import com.plotting.MatthewsChart;
import com.util.ArrayMethods;
import com.util.Log;

/** a little holder for errors **/
public class CoefficientLogger implements Serializable {
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
	
	
	/** Convert errors to a double table **/
	public static double[][] getErrorsFromCL(CoefficientLogger[] cls) {
		double[][] errors = new double[cls.length][];
		for (int i = 0; i < cls.length; ++i) {
			errors[i] = cls[i].getErrors();
		}
		return errors;
	}
	
	/**Return the maximum error from coefficient logger **/
	public static double[] getMaxErrorFromCL(CoefficientLogger[] cls) {
		double[] errors = new double[cls.length];
		for (int i = 0; i < cls.length; ++i) {
			errors[i] = cls[i].maxCoefficient();
		}
		return errors;
	}
	
	/**Return the maximum error from coefficient logger **/
	public static double[][] getMaxErrorFromCL(CoefficientLogger[][] cls) {
		double[][] errors = new double[cls.length][];
		for (int i = 0; i < cls.length; ++i) {
			if (cls[i] != null) {
				errors[i] = getMaxErrorFromCL(cls[i]);
			}
		}
		return errors;
	}
	
	/** Return the highest Matthews Coefficient logged **/
	public double maxCoefficient() {
		return ArrayMethods.getMax(this.errors);
	}
	
	public double[] getErrors() {
		return this.errors;
	}
	/** Returns Coefficient Loggers from the MultiLayer Nets **/
	public static CoefficientLogger[][] getErrorsFromMultiLayer(MultiLayerNet[][] multiLayer) {
		CoefficientLogger[][] cl = new CoefficientLogger[multiLayer.length][];
		for (int i = 0; i < multiLayer.length; ++i) {
			cl[i] = getErrorsFromMultiLayer(multiLayer[i]);
		}
		return cl;
	}
	/** Returns Coefficient Loggers from the MultiLayer Nets **/
	public static CoefficientLogger[] getErrorsFromMultiLayer(MultiLayerNet[] nets) {
		CoefficientLogger[] mBox = new CoefficientLogger[nets.length];
		for (int i = 0; i < nets.length; ++i) {
			if (nets[i] != null) {
				mBox[i] = nets[i].getErrorBox();
			}
		}
		return mBox;
	}

}
