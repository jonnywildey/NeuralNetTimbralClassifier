package plotting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import filemanager.ArrayStuff;

public class PlotGraph extends JPanel {

	protected long[][] values;
	protected Dimension offsetSize;
	protected Dimension size;
	protected String[] axisLabels;
	protected double marginOffset = 0.08;
	protected double maxBar;
	protected int widthness;
	protected double wOffset;
	protected int heightness;
	protected int alpha;
	protected Color[] rColors;
	protected double wr;
	protected double hOffset;
	protected int half;
	
	public PlotGraph() {
		
	}
	
	public PlotGraph(long[][] values, Dimension winSize, String[] axisLabels)  {
		super();
		this.values = values;
		this.axisLabels = axisLabels;
		size = winSize;
		offsetSize = new Dimension((int) (size.getWidth() * marginOffset), (int) (size.getHeight() * marginOffset));
		//dims of the actual chart part
		widthness = size.width - (offsetSize.width * 2);
		heightness = size.height - (offsetSize.height * 2);
		//get bar value - height of chart ratio (so biggest value is at top of chart)
		maxBar = (((heightness - offsetSize.height) / ArrayStuff.getMax(values)));
		//System.out.println("max b " + maxBar);
		rColors = colorArray(values.length);
		wOffset = (winSize.getWidth() - widthness) / 2;
		hOffset = (size.getHeight() - offsetSize.getHeight()) / 2;
		wr = ((double)widthness / (double)values[0].length);
		this.half = (int)((size.height - offsetSize.height) * 0.5);
		}


	public Color getColor() {
		/*returns a random color with alpha choice */
		Color c = new Color((float)(Math.random()), 0.4f, 0.9f, 0.7f);
		return c;
	}

	protected Color[] colorArray(int a) {
		/*returns an array of colors */
		float am = 1f / (float)a;
		Color[] rcol = new Color[a];
		
		for (int i = 0; i < a; ++i) {
			int x = Color.HSBtoRGB((i) * am, 0.8f, 0.8f);
			Color c = new Color(x);
			rcol[i] = new Color(c.getRed(), c.getGreen(), c.getBlue(), 40);
	
			
		}
		return rcol;
	}

	protected void drawCentredString(String str, int x, int y,
			Graphics2D g2d) {
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
	
	protected void drawLines(Graphics2D g2d) {
		//Y axis
		g2d.drawLine(offsetSize.width, heightness, offsetSize.width, offsetSize.height);
		//X axis
		g2d.drawLine(offsetSize.width, heightness, size.width - offsetSize.width, heightness);
		//axis labels
		drawCentredString(axisLabels[0], offsetSize.width, offsetSize.height, g2d);
		drawCentredString(axisLabels[1], widthness + offsetSize.width, heightness, g2d);
	}

	public void drawBar(Graphics2D g2d) {
		drawLines(g2d);
		int c = 0;
		for(long[] av: values) {
			g2d.setColor(rColors[c]);
			for (int i = 0; i < av.length -1; ++i) {
				g2d.drawLine((int)((i * wr) + wOffset), heightness - (int)(av[i]*maxBar),  
							 (int)(((i + 1) * wr) + wOffset), heightness -  (int) (av[i + 1]*maxBar));
			}
			++c;
		}
	}

}