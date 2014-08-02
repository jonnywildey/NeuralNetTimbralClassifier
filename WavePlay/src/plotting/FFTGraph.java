package plotting;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;

import riff.Signal;
import waveAnalysis.FFT;
import waveAnalysis.FrameFFT;
import filemanager.ArrayMethods;
import filemanager.Log;

/**Messy but somewhat functional FFT graph**/
public class FFTGraph extends PlotGraph {
	
	protected double[][] values;
	
	public FFTGraph(double[][] table, Dimension winSize, String[] axisLabels) {
		super();
		this.values = table;
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
				/ ArrayMethods.getMaxAbs(values));
		//Log.d(ArrayStuff.getMaxAbs(values));
	}
	
	
	/** converts amp value to graph value **/
	public int ampValue(double c) {
		//Log.d(c);
		 return (int) (offsetSize.height - (c * maxBar));
	}
	

	
	private String getHighFreq() {
		return String.valueOf(Math.rint(Math.pow(2,ArrayMethods.getMax(this.values[0]))) + "hz");
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
		//drawCentredString(getHighFreq(), this.size.width - offsetSize.width, (int) (this.heightness * 1.1), g2d); 
		double max = values[0][values[0].length - 1];
		double min = values[0][0];
		double num = max - min;
		double wid = 0;
		String s = "";
		for (double i = 0; i <= num; ++i) {
			s = String.valueOf(Math.rint(Math.pow(2, min + ((max - min) / num * i)))) + "hz";
			wid = offsetSize.width + ((this.size.width - 2 * offsetSize.width) / num * i);
			drawCentredString(s, (int) wid, (int) (this.heightness * 1.1), g2d); 
		}
		
		//the wavs
		//System.out.println("widthness" + widthness);
		//System.out.println("values length " + values[0].length);
		double factor = (double)(values[0].length) / (max - min);
		int c = 0;
		for (int j = 1; j < values.length; ++j) {
			double[] av = values[j];
			g2d.setColor(rColors[c]);
			for (int i = 0; i < av.length -1; ++i) {
				//wid = 1 + ((double)(i) / (double)(av.length));
				wid = 1;
				g2d.drawLine((int)(((values[0][i] - min) * wr * factor) + wOffset), 
							  ampValue(av[i]),  
							(int) (((values[0][i + 1] - min) * wr * factor) + wOffset), 
							  ampValue(av[i + 1])
							  );
				//System.out.println((i * wr) + wOffset);
				//System.out.println((int)values[i + 1] / 100);
			}
			++c;
		}
			
		
		
	}
	
}
