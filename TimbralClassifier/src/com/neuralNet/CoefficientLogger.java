package com.neuralNet;

import java.io.Serializable;
import com.plotting.MatthewsChart;
import com.util.ArrayMethods;

/**
 * a little holder for errors *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class CoefficientLogger implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1323635510235826527l;
	/**
	 * Convert errors to a double table *.
	 * 
	 * @param cls
	 *            the cls
	 * @return the errors from cl
	 */
	public static double[][] getErrorsFromCL(CoefficientLogger[] cls) {
		double[][] errors = new double[cls.length][];
		for (int i = 0; i < cls.length; ++i) {
			errors[i] = cls[i].getErrors();
		}
		return errors;
	}
	/**
	 * Convert errors to a double table *.
	 * 
	 * @param cls
	 *            the cls
	 * @return the errors from cl
	 */
	public static double[][] getErrorsFromCL(MultiLayerNet[] cls) {
		double[][] errors = new double[cls.length][];
		for (int i = 0; i < cls.length; ++i) {
			errors[i] = cls[i].getErrorBox().getErrors();
		}
		return errors;
	}

	/**
	 * Returns Coefficient Loggers from the MultiLayer Nets *.
	 * 
	 * @param nets
	 *            the nets
	 * @return the errors from multi layer
	 */
	public static CoefficientLogger[] getErrorsFromMultiLayer(
			MultiLayerNet[] nets) {
		CoefficientLogger[] mBox = new CoefficientLogger[nets.length];
		for (int i = 0; i < nets.length; ++i) {
			if (nets[i] != null) {
				mBox[i] = nets[i].getErrorBox();
			}
		}
		return mBox;
	}

	/**
	 * Returns Coefficient Loggers from the MultiLayer Nets *.
	 * 
	 * @param multiLayer
	 *            the multi layer
	 * @return the errors from multi layer
	 */
	public static CoefficientLogger[][] getErrorsFromMultiLayer(
			MultiLayerNet[][] multiLayer) {
		CoefficientLogger[][] cl = new CoefficientLogger[multiLayer.length][];
		for (int i = 0; i < multiLayer.length; ++i) {
			cl[i] = getErrorsFromMultiLayer(multiLayer[i]);
		}
		return cl;
	}

	/**
	 * Return the maximum error from coefficient logger *.
	 * 
	 * @param cls
	 *            the cls
	 * @return the max error from cl
	 */
	public static double[] getMaxErrorFromCL(CoefficientLogger[] cls) {
		double[] errors = new double[cls.length];
		for (int i = 0; i < cls.length; ++i) {
			errors[i] = cls[i].maxCoefficient();
		}
		return errors;
	}

	/**
	 * Return the maximum error from coefficient logger *.
	 * 
	 * @param cls
	 *            the cls
	 * @return the max error from cl
	 */
	public static double[][] getMaxErrorFromCL(CoefficientLogger[][] cls) {
		double[][] errors = new double[cls.length][];
		for (int i = 0; i < cls.length; ++i) {
			if (cls[i] != null) {
				errors[i] = getMaxErrorFromCL(cls[i]);
			}
		}
		return errors;
	}

	/**
	 * Make graph.
	 * 
	 * @param errors
	 *            the errors
	 */
	public static void makeGraph(CoefficientLogger[] errors) {
		double[][] las = new double[errors.length][];
		for (int i = 0; i < errors.length; ++i) {
			las[i] = errors[i].getErrors();
		}
		MatthewsChart nc = new MatthewsChart(las, 1200, 600);
		nc.makeChart();
	}

	/**
	 * Make graph.
	 * 
	 * @param errors
	 *            the errors
	 */
	public static void makeGraph(MultiLayerNet[] net) {
		double[][] las = new double[net.length][];
		for (int i = 0; i < net.length; ++i) {
			las[i] = net[i].getErrorBox().getErrors();
		}
		MatthewsChart nc = new MatthewsChart(las, 1200, 600);
		nc.makeChart();
	}

	private double[] errors;

	private int iterator;

	/**
	 * Instantiates a new coefficient logger.
	 * 
	 * @param epochs
	 *            the epochs
	 */
	public CoefficientLogger(int epochs) {
		this.errors = new double[epochs];
		this.iterator = 0;
	}

	/**
	 * Adds the.
	 * 
	 * @param val
	 *            the val
	 */
	public void add(double val) {
		errors[iterator] = val;
		iterator++;
	}

	/**
	 * Gets the errors.
	 * 
	 * @return the errors
	 */
	public double[] getErrors() {
		return this.errors;
	}

	/**
	 * Make graph.
	 */
	public void makeGraph() {
		double[][] las = { this.errors };
		MatthewsChart nc = new MatthewsChart(las, 1200, 600);
		nc.makeChart();
	}

	/**
	 * Return the highest Matthews Coefficient logged *.
	 * 
	 * @return the double
	 */
	public double maxCoefficient() {
		return ArrayMethods.getMax(this.errors);
	}
	/**Has anything been logged? **/
	public boolean hasLogged() {
		//assumes no progression means no errors added
		return (this.errors[0] != 0 & 
				this.errors[this.errors.length - 1] != 0);
	}
	/** Increase capactity of logger **/
	public void increaseSize(int maxEpoch) {
		this.errors = ArrayMethods.extend(
				this.errors, this.errors.length + maxEpoch);	
	}

}
