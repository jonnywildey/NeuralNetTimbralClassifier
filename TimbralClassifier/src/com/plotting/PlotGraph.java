package com.plotting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import com.DSP.waveAnalysis.Statistics;
import com.util.ArrayMethods;

/**
 * Basic plotting graph object for 2d values (Different colours) *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class PlotGraph extends JPanel {

	private static final long serialVersionUID = 1110962484308363573L;
	protected long[][] values; //values to plot
	protected Dimension offsetSize; //offset of where graph actually starts
	protected Dimension size; //size of entire window
	protected String[] axisLabels; //labels for axis
	protected double marginOffset = 0.08; //how much of graph is margin
	protected double maxBar; // amount to change values by to fit to plot
	protected int widthness; //size of size - offset
	protected double wOffset; 
	protected int heightness; //size of size - offset
	protected int alpha; //set alpha
	protected Color[] rColors; //color arrays
	protected double wr;
	protected double hOffset;
	protected int half;
	protected double max; //maximum value

	/**
	 * Instantiates a new plot graph.
	 */
	public PlotGraph() {

	}

	/**
	 * Instantiates a new plot graph.
	 *
	 * @param values the values
	 * @param winSize the win size
	 * @param axisLabels the axis labels
	 */
	public PlotGraph(long[][] values, Dimension winSize, String[] axisLabels) {
		super();
		initialise(values, winSize, axisLabels);
	}

	/**
	 * set up margins etc *.
	 *
	 * @param values the values
	 * @param winSize the win size
	 * @param axisLabels the axis labels
	 */
	protected void initialise(long[][] values, Dimension winSize,
			String[] axisLabels) {
		this.values = values;
		this.axisLabels = axisLabels;
		initDimensions(winSize);
		// get bar value - height of chart ratio (so biggest value is at top of
		// chart)
		initMax(values);
		rColors = colorArray(values.length);

	}

	/**
	 * for normalising of table values *.
	 *
	 * @param values the values
	 */
	protected void initMax(long[][] values) {
		max = ArrayMethods.getMax(values);
		maxBar = ((heightness - offsetSize.height) / max);
	}

	/**
	 * Inits the dimensions.
	 *
	 * @param winSize the win size
	 */
	protected void initDimensions(Dimension winSize) {
		size = winSize;
		offsetSize = new Dimension((int) (size.getWidth() * marginOffset),
				(int) (size.getHeight() * marginOffset));
		// dims of the actual chart part
		widthness = size.width - (offsetSize.width * 2);
		heightness = size.height - (offsetSize.height * 2);
		wOffset = (winSize.getWidth() - widthness) / 2;
		hOffset = (size.getHeight() - offsetSize.getHeight()) / 2;
		wr = ((double) widthness / (double) values[0].length);
		this.half = (int) ((size.height - offsetSize.height) * 0.5);
	}

	/**
	 * returns a random color with alpha choice.
	 *
	 * @return the color
	 */
	public Color getColor() {
		Color c = new Color((float) (Math.random()), 0.4f, 0.9f, 0.7f);
		return c;
	}

	/**
	 * Create a color array *.
	 *
	 * @param a the a
	 * @return the color[]
	 */
	protected Color[] colorArray(int a) {
		/* returns an array of colors */
		float am = 1f / a;
		Color[] rcol = new Color[a];
		for (int i = 0; i < a; ++i) {
			int x = Color.HSBtoRGB((i) * am, 0.8f, 0.8f);
			Color c = new Color(x);
			rcol[i] = new Color(c.getRed(), c.getGreen(), c.getBlue(), 40);

		}
		return rcol;
	}

	/**
	 * draw a string from its centre position *.
	 *
	 * @param str the str
	 * @param x the x
	 * @param y the y
	 * @param g2d the g2d
	 */
	protected void drawCentredString(String str, int x, int y, Graphics2D g2d) {
		int widthAlign = (int) ((g2d.getFontMetrics().getStringBounds(str, g2d)
				.getWidth() / 2));
		int heightAlign = (int) ((g2d.getFontMetrics()
				.getStringBounds(str, g2d).getHeight() / 2));
		g2d.drawString(str, x - widthAlign, y - heightAlign);
	}

	/**
	 * The drawing method *.
	 *
	 * @param g the g
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// clear the screen
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		drawBar(g2d);
	}

	/**
	 * Draw axis lines etc. *
	 *
	 * @param g2d the g2d
	 */
	protected void drawLines(Graphics2D g2d) {
		// Y axis
		g2d.drawLine(offsetSize.width, heightness, offsetSize.width,
				offsetSize.height);
		// X axis
		g2d.drawLine(offsetSize.width, heightness, size.width
				- offsetSize.width, heightness);
		// axis labels
		drawCentredString(axisLabels[0], (int) (offsetSize.width * 1.25),
				offsetSize.height, g2d);
		drawCentredString(axisLabels[1], widthness + offsetSize.width,
				heightness, g2d);
	}

	/**
	 * Draw guides etc. *
	 *
	 * @param g2d the g2d
	 * @param number the number
	 */
	protected void drawGuides(Graphics2D g2d, int number) {
		for (int i = 0; i <= number; ++i) {
			g2d.drawLine(
					(int) (offsetSize.width * 0.95),
					(int) (offsetSize.height + (((heightness - offsetSize.height)
							/ ((double) (number)) * i))),
					offsetSize.width,
					(int) (offsetSize.height + (((heightness - offsetSize.height)
							/ ((double) (number)) * i))));
			// label
			drawCentredString(
					Double.toString(Statistics.round(
							(max - (i * (max / (number)))), 2)),
					(int) (offsetSize.width * 0.8),
					(int) ((offsetSize.height * 1.3) + (((heightness - offsetSize.height) / (double) (number)) * i)),
					g2d);
			// x axis
			g2d.drawLine(
					(int) (offsetSize.width + ((widthness / (double) (number)) * i)),
					(int) (heightness * 1.02),
					(int) (offsetSize.width + ((widthness / (double) (number)) * i)),
					heightness);
			drawCentredString(
					Integer.toString((int) ((i * ((double) (this.values[0].length) / (double) (number))))),
					(int) (offsetSize.width + ((widthness / (double) (number)) * i)),
					(int) (heightness * 1.08), g2d);

		}

	}

	/**
	 * draw values *.
	 *
	 * @param g2d the g2d
	 */
	public void drawBar(Graphics2D g2d) {
		whiteBackground(g2d);
		drawLines(g2d);
		int c = 0;
		for (long[] av : values) {
			g2d.setColor(rColors[c]);
			for (int i = 0; i < av.length - 1; ++i) {
				g2d.drawLine((int) ((i * wr) + wOffset), heightness
						- (int) (av[i] * maxBar),
						(int) (((i + 1) * wr) + wOffset), heightness
								- (int) (av[i + 1] * maxBar));
			}
			++c;
		}
	}

	/**
	 * White background.
	 *
	 * @param g2d the g2d
	 */
	protected void whiteBackground(Graphics2D g2d) {
		g2d.setBackground(Color.white);
		g2d.clearRect(0, 0, this.size.width, this.size.height);
		g2d.setColor(Color.black);
	}

}