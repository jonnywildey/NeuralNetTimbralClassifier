package com.plotting;
import java.awt.*;
import com.DSP.waveAnalysis.FFTBox;

/** Controller for the FFT graph **/
public class FFTController extends Controller{

	public FFTController(double[][] table) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.table = table;
	}
	
	public FFTController(FFTBox fftBox, int width, int height) {
		this.size = new Dimension(width, height);
		this.table = fftBox.getTable();
	}

}
