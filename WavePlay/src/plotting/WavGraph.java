package plotting;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;

import filemanager.ArrayStuff;

public class WavGraph extends PlotGraph {
	
	public WavGraph(long[][] values, Dimension winSize, String[] axisLabels) {
		super(values, winSize, axisLabels);
		maxBar = (((heightness - offsetSize.height) / ArrayStuff.getMax(values)) * 0.5);
	}
	
	
	/** converts amp value to graph value **/
	public int ampValue(double c) {
		 //System.out.println((int) (half - (c * maxBar)));
		 return (int) (this.half - (c * maxBar));
		
	}
	@Override
	public void drawBar(Graphics2D g2d) {
		//Y axis
		g2d.drawLine(offsetSize.width, heightness, offsetSize.width, offsetSize.height);
		//X axis
		g2d.drawLine(offsetSize.width, heightness, size.width - offsetSize.width, heightness);
		g2d.drawLine(offsetSize.width, this.half, 
						size.width - offsetSize.width, this.half);
		//axis labels
		drawCentredString(axisLabels[0], offsetSize.width, offsetSize.height, g2d);
		drawCentredString(axisLabels[1], widthness + offsetSize.width, heightness, g2d);
	
		//the wavs
		//System.out.println("widthness" + widthness);
		//System.out.println("values length " + values[0].length);
		int c = 0;
		for(long[] av: values) {
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
	
}
