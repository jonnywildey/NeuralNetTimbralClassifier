package wavePlot;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;

import libraryclasses.LogWin;
public class HorizontalChartPanel extends JPanel {
	
	protected String[] names;
	protected double[] values;
	protected Dimension offsetSize;
	protected Dimension size;
	protected String[] axisLabels;
	//Offset the size dimension so there's a margin
	protected double marginOffset = 0.08;
	protected double maxBar;
	//Get dims of actual chart. Kind of redundant but my statement get messy otherwise
	protected int widthness;
	protected double wOffset;
	protected int heightness;
	protected int barHeight;
	protected int[][][] barPos;
	protected int alpha;
	protected Color[] rColors;
	protected double wr;
	protected double hOffset;
	
	public HorizontalChartPanel(double[] values, String[] names, Dimension winSize, String[] axisLabels) {
		
		this.names = names;
		this.values = values;
		this.axisLabels = axisLabels;
		size = winSize;
		offsetSize = new Dimension((int) (size.getWidth() * marginOffset), (int) (size.getHeight() * marginOffset));
		//dims of the actual chart part
		widthness = size.width - (offsetSize.width * 2);
		heightness = size.height - (offsetSize.height * 2);
		//get bar value - height of chart ratio (so biggest value is at top of chart)
		maxBar = (heightness / getMax(values)) / 2;
		System.out.println("max b " + maxBar);
		barHeight = (int)((heightness - offsetSize.height) / values.length) ;
		barPos = new int[values.length][2][4];
		alpha = 60;
		rColors = ranColorArray(values.length + 1, alpha);
		wOffset = (winSize.getWidth() - widthness) / 2;
		hOffset = (size.getHeight() - offsetSize.getHeight()) / 2;
		System.out.println("h offset " + hOffset);
		wr = ((double)widthness / (double)values.length);
		System.out.println("wr " + wr);
		
	}
	
	private double getMax(double[] array) {
		double max = 0;
		for (int i = 0; i < array.length; ++i) {
			if (Math.abs(array[i]) > max) {
				max = Math.abs(array[i]);
			}
		}
		return max / 0.9;
	}
	
	public Color randomColor(int alpha) {
		/*returns a random color with alpha choice */
		return new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255), alpha);
	}
	
	public Color randomColor() {
		return new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255), 255);
	}
	
	private Color[] ranColorArray(int a, int alpha) {
		/*returns an array of random colors */
		Color[] rcol = new Color[a];
		for (int i = 0; i < a; ++i) {
			rcol[i] = randomColor(alpha);
		}
		return rcol;
	}
	
	
	
	private void drawCentredString(String str, int x, int y, Graphics2D g2d) {
		/* makes a centre aligned string. Useful for graph labels etc. */
		int widthAlign = (int)((g2d.getFontMetrics().getStringBounds(str, g2d).getWidth()/2)); 
		int heightAlign = (int)((g2d.getFontMetrics().getStringBounds(str, g2d).getHeight()/2)); 
		g2d.drawString(str, x - widthAlign, y - heightAlign);
	}
	
	



	public void paintComponent(Graphics g) {
			super.paintComponent(g);
			//clear the screen
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			drawBar(g2d);
			
	}
	
	/** converts amp value to graph value **/
	public int ampValue(double c) {
		 double half = (heightness / 2)  ;
		 //System.out.println(half - (c * 0.001));
		 return (int) (half - (c * maxBar));
		
	}
	
	public void drawBar(Graphics2D g2d) {
		


		//Y axis
		g2d.drawLine(offsetSize.width, heightness, offsetSize.width, offsetSize.height);
		//X axis
		g2d.drawLine(offsetSize.width, heightness, size.width - offsetSize.width, heightness);
		g2d.drawLine(offsetSize.width, heightness / 2, size.width - offsetSize.width, heightness / 2);
		//axis labels
		drawCentredString(axisLabels[0], offsetSize.width, offsetSize.height, g2d);
		drawCentredString(axisLabels[1], widthness + offsetSize.width, heightness, g2d);
		
		//the bars
		
		//LibraryClasses.LogWin.messagePrint(Arrays.deepToString(barPos));
		
		g2d.setColor(rColors[0]);
		System.out.println("widthness" + widthness);
		System.out.println("values length " + values.length);
		for (int i = 0; i < values.length -1; ++i) {
			g2d.drawLine((int)((i * wr) + wOffset), ampValue(values[i]),  
						 (int)(((i + 1) * wr) + wOffset), ampValue(values[i + 1]));
			//System.out.println((int)values[i] / 100);
			//System.out.println((int)values[i + 1] / 100);
		}
		
		
	}
	
}
