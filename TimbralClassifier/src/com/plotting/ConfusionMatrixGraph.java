package com.plotting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import com.neuralNet.matrix.ConfusionMatrix;

/**
 * A colour coded representation of a confusion matrix to allow for slightly
 * easier reading *.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class ConfusionMatrixGraph extends JPanel {

	protected static final long serialVersionUID = -6252441881996482890L;
	public Dimension size;
	public ConfusionMatrix confusionMatrix;

	/**
	 * Instantiates a new confusion matrix graph.
	 * 
	 * @param confusionMatrix
	 *            the confusion matrix
	 * @param size
	 *            the size
	 */
	public ConfusionMatrixGraph(ConfusionMatrix confusionMatrix, Dimension size) {
		super();
		this.size = size;
		this.confusionMatrix = confusionMatrix;
	}

	/**
	 * Background color.
	 * 
	 * @return the color
	 */
	public Color backgroundColor() {
		int x = Color.HSBtoRGB(0.8f, 0.1f, 0.5f);
		Color c = new Color(x);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
	}

	/**
	 * represents 0 -1 as red to blue *.
	 * 
	 * @param coef
	 *            the coef
	 * @return the color
	 */
	public Color coefficcientToColor(double coef) {
		int x = Color.HSBtoRGB((float) (coef), 0.9f, 0.5f);
		Color c = new Color(x);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
	}

	/**
	 * Draw string in centre of square *.
	 * 
	 * @param str
	 *            the str
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param g2d
	 *            the g2d
	 */
	protected void drawCentredString(String str, int x, int y, int width,
			int height, Graphics2D g2d) {
		Rectangle2D strSize = g2d.getFontMetrics().getStringBounds(str, g2d);
		int widthAlign = (int) (((double) (width) - strSize.getWidth()) / 2d);
		int heightAlign = (int) (((double) (height)) / 2d);
		g2d.drawString(str, x + widthAlign, y + heightAlign);
	}

	/**
	 * Draw the actual matrix *.
	 * 
	 * @param g2d
	 *            the g2d
	 */
	public void drawMatrix(Graphics2D g2d) {
		whiteBackground(g2d);
		int squareWidth = genSquareWidth();
		int squareHeight = genSquareHeight();
		int xStart = 0;
		int yStart = 0;
		Color backCol = backgroundColor();
		//

		for (int i = 0; i < confusionMatrix.getSize(); ++i) {
			for (int j = 0; j < confusionMatrix.getSize(); ++j) {
				xStart = genXStart(i);
				yStart = genYStart(j);
				if (i == j) {
					g2d.setColor(coefficcientToColor(getCoef(j, i)));
				} else {
					g2d.setColor(incorrectToColor(getCoef(j, i)));
				}
				g2d.fillRect(xStart, yStart, squareWidth, squareHeight);
				g2d.drawRect(xStart, yStart, squareWidth, squareHeight);
				g2d.setColor(Color.WHITE);
				drawCentredString(getString(j, i), xStart, yStart, squareWidth,
						squareHeight, g2d);
			}
		}

	}

	/**
	 * generate the width of a square based on CM size *.
	 * 
	 * @return the int
	 */
	protected int genSquareHeight() {
		return (int) (this.size.getHeight() / (double) (this.confusionMatrix
				.getSize()));
	}

	/**
	 * generate the width of a square based on CM size *.
	 * 
	 * @return the int
	 */
	protected int genSquareWidth() {
		return (int) (this.size.getWidth() / (double) (this.confusionMatrix
				.getSize()));

	}

	/**
	 * Return start x position of a square *.
	 * 
	 * @param no
	 *            the no
	 * @return the int
	 */
	protected int genXStart(int no) {
		double cmSize = confusionMatrix.getSize();
		return (int) ((this.size.getWidth() / cmSize) * (double) (no));
	}

	/**
	 * Return start y position of a square *.
	 * 
	 * @param no
	 *            the no
	 * @return the int
	 */
	protected int genYStart(int no) {
		double cmSize = confusionMatrix.getSize();
		return (int) ((this.size.getHeight() / cmSize) * (double) (no));
	}

	/**
	 * return success of cell as coefficient. 1 is perfect *
	 * 
	 * @param col
	 *            the col
	 * @param row
	 *            the row
	 * @return the coef
	 */
	protected double getCoef(int col, int row) {
		return ((Integer) (confusionMatrix.getCell(col, row))).doubleValue()
		/ (double) (confusionMatrix.getTotalForCell(col, row));
	}

	/**
	 * Gets the string.
	 * 
	 * @param col
	 *            the col
	 * @param row
	 *            the row
	 * @return the string
	 */
	protected String getString(int col, int row) {
		return String
		.valueOf((Integer) (this.confusionMatrix.getCell(col, row)));
	}

	/**
	 * Gets the sum column.
	 * 
	 * @param col
	 *            the col
	 * @return the sum column
	 */
	protected int getSumColumn(int col) {
		return confusionMatrix.sumColumn(col);
	}

	/**
	 * represents 0 -1 as red to blue *.
	 * 
	 * @param coef
	 *            the coef
	 * @return the color
	 */
	public Color incorrectToColor(double coef) {
		int x = Color.HSBtoRGB((float) (0.8 - coef), 0.2f + (float) (coef * 2),
				0.5f);
		Color c = new Color(x);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
	}

	/**
	 * The main drawing part *.
	 * 
	 * @param g
	 *            the g
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// clear the screen
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		drawMatrix(g2d);
	}

	/**
	 * White background.
	 * 
	 * @param g2d
	 *            the g2d
	 */
	protected void whiteBackground(Graphics2D g2d) {
		g2d.setFont(new Font("Helvetica", Font.BOLD, 22));
		g2d.setBackground(Color.WHITE);
		g2d.clearRect(0, 0, this.size.width, this.size.height);
	}

}
