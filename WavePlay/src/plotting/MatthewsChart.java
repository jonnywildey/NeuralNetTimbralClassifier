package plotting;
import java.awt.*;

import javax.swing.*;

import filemanager.ArrayStuff;


public class MatthewsChart {
	
	private Dimension size;
	private long[][] signals;
	
	public MatthewsChart(long[][] signals) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.size = new Dimension(screenSize.width, screenSize.height / 3);
		this.signals = signals;
	}
	
	public MatthewsChart(long[][] signals, int width, int height) {
		this.size = new Dimension(width, height);
		this.signals = signals;
	}
	
	public void makeChart() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createChart(signals, size);
			}
		});
	}
	
	
	private void createChart(long[][] values, Dimension winSize) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		//Variables
		//String[] names = new String{""}; 
		String[] axisLabels = {"Matthews", "Epochs"};
		frame.getContentPane().add(this.new MatthewsGraph(values, winSize, axisLabels));
		//frame.pack();
		frame.setVisible(true);
	}
	
	class MatthewsGraph extends PlotGraph{

		public MatthewsGraph(long[][] values, Dimension winSize, String[] axisLabels) {
			super(values, winSize, axisLabels);
			this.max = 1d;
			maxBar = ((heightness - offsetSize.height) / 1d);
		}
		

		
		public void drawBar(Graphics2D g2d) {
			drawLines(g2d);
			drawGuides(g2d, 5);
			int c = 0;
			for(long[] av: values) {
				g2d.setColor(rColors[c]);
				for (int i = 0; i < av.length -1; ++i) {
					g2d.drawLine((int)((i * wr) + wOffset), heightness - (int)(av[i]),  
								 (int)(((i + 1) * wr) + wOffset), heightness -  (int) (av[i + 1]));
				}
				++c;
			}
		}

	}



	
}
