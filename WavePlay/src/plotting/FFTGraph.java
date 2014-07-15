package plotting;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;

import riff.Signal;
import waveAnalysis.FFT;
import filemanager.ArrayStuff;
import filemanager.Log;

public class FFTGraph extends PlotGraph {
	
	protected double[][] values;
	protected FFT fft;
	
	public FFTGraph(FFT fft, Dimension winSize, String[] axisLabels) {
		super();
		this.values = new double[][]{fft.getTable()[1]};
		this.fft = fft;
		this.axisLabels = axisLabels;
		size = winSize;
		offsetSize = new Dimension((int) (size.getWidth() * marginOffset), (int) (size.getHeight() * marginOffset));
		widthness = size.width - (offsetSize.width * 2);
		heightness = size.height - (offsetSize.height * 2);
		rColors = colorArray(values.length);
		wOffset = (winSize.getWidth() - widthness) / 2;
		hOffset = (size.getHeight() - offsetSize.getHeight()) / 2;
		wr = ((double)widthness / (double)values[0].length);
		this.half = (int)((size.height - offsetSize.height) * 0.5);
		maxBar = ((double)(heightness - (offsetSize.height )) 
				/ ArrayStuff.getMaxAbs(values));
		//Log.d(maxBar);
	}
	
	
	/** converts amp value to graph value **/
	public int ampValue(double c) {
		//Log.d(c);
		 return (int) (this.heightness - (c * maxBar));
	}
	
	private String getLowFreq() {
		//Log.d(Arrays.deepToString(this.fft.getTable()));
		return String.valueOf(Math.rint(ArrayStuff.getMin(this.fft.getTable()[0]))) + "hz";
	}
	
	private String getHighFreq() {
		return String.valueOf(Math.rint(ArrayStuff.getMax(this.fft.getTable()[0]))) + "hz";
	}
	
	@Override
	public void drawBar(Graphics2D g2d) {
		//Y axis
		g2d.drawLine(offsetSize.width, heightness, offsetSize.width, offsetSize.height);
		//X axis
		g2d.drawLine(offsetSize.width, heightness, size.width - offsetSize.width, heightness);
		//axis labels
		drawCentredString(axisLabels[0], offsetSize.width, offsetSize.height, g2d);
		drawCentredString(axisLabels[1], widthness + offsetSize.width, heightness, g2d);
		//freq
		drawCentredString(getLowFreq(), offsetSize.width, (int) (this.heightness * 1.1), g2d);
		drawCentredString(getHighFreq(), this.size.width - offsetSize.width, (int) (this.heightness * 1.1), g2d);
	
		//the wavs
		//System.out.println("widthness" + widthness);
		//System.out.println("values length " + values[0].length);
		int c = 0;
		double[] av = values[0];
			g2d.setColor(rColors[c]);
			for (int i = 0; i < av.length -1; ++i) {
				g2d.drawLine((int)((i * wr) + wOffset), 
							  ampValue(av[i]),  
							 (int)(((i + 1) * wr) + wOffset), 
							  ampValue(av[i + 1])
							  );
				//System.out.println((i * wr) + wOffset);
				//System.out.println((int)values[i + 1] / 100);
			}
			++c;
		
		
	}
	
}
