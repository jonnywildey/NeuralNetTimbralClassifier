package wavePlot;
import java.awt.*;
import javax.swing.*;


public class Controller {
	
	
	
	public void makeChart(final long[] values) {
		final double[] val = new double[values.length];
		//System.out.println("Length " + values.length);
		for (int i = 0; i < values.length; ++i) {
			val[i] = (double)values[i];
			//System.out.println(val[i]);
		}
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI2(val);
			}
		});
	}
	
	
	
	
	private static void createAndShowGUI2(double[] values) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension winSize = new Dimension(600,300);
		frame.setBounds(winSize.width, 0, winSize.width, winSize.height);
		frame.setSize(winSize);
		//Variables
		String[] names = new String[5];
		String[] axisLabels = {"amplitude", "time"};
		
		
		frame.getContentPane().add(new HorizontalChartPanel(values, names, winSize, axisLabels));
		//frame.pack();
		frame.setVisible(true);
	}
	
}
