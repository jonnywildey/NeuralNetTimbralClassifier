package com.plotting;

import java.awt.*;
import com.DSP.waveAnalysis.FFTBox;

/**
 * Controller for the FFT graph *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class FFTController extends Controller {

	/**
	 * Instantiates a new fFT controller.
	 * 
	 * @param table
	 *            the table
	 */
	public FFTController(double[][] table) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.table = table;
	}

	/**
	 * Instantiates a new fFT controller.
	 * 
	 * @param fftBox
	 *            the fft box
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public FFTController(FFTBox fftBox, int width, int height) {
		this.size = new Dimension(width, height);
		this.table = fftBox.getTable();
	}

}
