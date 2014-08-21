package com.plotting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.matrix.ConfusionMatrix;

/** A colour coded representation of a confusion matrix to allow for slightly easier reading **/
public class ConfusionMatrixGraph extends JPanel {

	protected static final long serialVersionUID = -6252441881996482890L;
	public Dimension size;
	public ConfusionMatrix confusionMatrix;

	public ConfusionMatrixGraph(ConfusionMatrix confusionMatrix, Dimension size) {
		super();
		this.size = size;
		this.confusionMatrix = confusionMatrix;
	}

	/** represents 0 -1 as red to blue **/
	public Color coefficcientToColor(double coef) {
		int x = Color.HSBtoRGB((float) (coef), 0.9f, 0.5f);
		Color c = new Color(x);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
	}
	
	/** represents 0 -1 as red to blue **/
	public Color incorrectToColor(double coef) {
		int x = Color.HSBtoRGB((float) (0.8 - coef), 0.2f + (float)(coef * 2), 0.5f);
		Color c = new Color(x);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
	}

	public Color backgroundColor() {
		int x = Color.HSBtoRGB(0.8f, 0.1f, 0.5f);
		Color c = new Color(x);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
	}

	/** generate the width of a square based on CM size **/
	protected int genSquareWidth() {
		return (int) (this.size.getWidth() / (double) (this.confusionMatrix
				.getSize()));

	}

	/** generate the width of a square based on CM size **/
	protected int genSquareHeight() {
		return (int) (this.size.getHeight() / (double) (this.confusionMatrix
				.getSize()));
	}

	/** Return start x position of a square **/
	protected int genXStart(int no) {
		double cmSize = confusionMatrix.getSize();
		return (int) ((this.size.getWidth() / cmSize) * (double) (no));
	}

	/** Return start y position of a square **/
	protected int genYStart(int no) {
		double cmSize = confusionMatrix.getSize();
		return (int) ((this.size.getHeight() / cmSize) * (double) (no));
	}

	protected int getSumColumn(int col) {
		return confusionMatrix.sumColumn(col);
	}

	protected String getString(int col, int row) {
		return String
				.valueOf((Integer) (this.confusionMatrix.getCell(col, row)));
	}
	
	/** return success of cell as coefficient. 1 is perfect **/
	protected double getCoef(int col, int row) {
		return ((Integer) (confusionMatrix.getCell(col, row))).doubleValue()
				/ (double) (confusionMatrix.getTotalForCell(col, row));
	}

	/** The main drawing part **/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// clear the screen
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		drawMatrix(g2d);
	}

	/** Draw string in centre of square **/
	protected void drawCentredString(String str, int x, int y, int width,
			int height, Graphics2D g2d) {
		Rectangle2D strSize = g2d.getFontMetrics().getStringBounds(str, g2d);
		int widthAlign = (int) (((double) (width) - strSize.getWidth()) / 2d);
		int heightAlign = (int) (((double) (height)) / 2d);
		g2d.drawString(str, x + widthAlign, y + heightAlign);
	}
	
	protected void whiteBackground(Graphics2D g2d) {
		g2d.setFont(new Font("Helvetica", Font.BOLD,  22));
		g2d.setBackground(Color.WHITE);
		g2d.clearRect(0, 0, this.size.width, this.size.height);
	}

	/** Draw the actual matrix **/
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

}
