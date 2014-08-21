

/**
 * <dl>
 * 	<dd> Useful Library things
 * 
 * 	<dt> Description
 * 	<dd> HTML read & write methods and some JPanel displays for them
 * 	</dl>
 * 
 * 	@author Jonny Wildey
 * 	@version $Date: 2013/10/9 $
 */
package com.filemanager;
import javax.swing.*;

import java.awt.*;
import java.io.*;
public class HTML {
	
	public static String tableColumner(String[][] items) {
		/*surprisingly useful 2darray to html table thing. Have swapped round column and table values so this makes sense with arrays.
		 * (if you think about it html tables treat rows as the index) if you REALLY think about it. REALLY */
		String htmler = "<html> \n <p \" font-size: 3px; \"> <table width='100%' height = '100%' border='0' cellspacing='0' cellpadding='0'> \n";
		for (int i = 0; i < items.length; i++) {
			htmler += "<tr> \n" + "</tr> \n";
			for (int j = 0; j < items[i].length; j++) {
				htmler += "<td width = '4em' height = '4em'> \n" + items[j][i] + "\n </td> \n";
			}
		}
		htmler += "</table> \n </p> </html>";
		return htmler;
	}
	
	
	public static String fileChooser() {
		/*chooses a file, outputs stringpath*/
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(null);
		//chooser.setVisible(true);
		chooser.showOpenDialog(null);
		while (chooser.getSelectedFile() == null) {
			//LogWin.messagePrintNoCancel("No file selected");
			chooser.showOpenDialog(null);
		} 
		
		String a = chooser.getSelectedFile().toString();
		return a;
	}
	
	public static void writeToHTML(String text, String filename){
		/* give it a string and a filepath and it will make it */
		Writer writer = null;
	
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(filename), "utf-8"));
		    writer.write(text);
		} catch (IOException ex){
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
		
	}
	
	public static String fileReader(String path) {
		/* Takes a string pathname and outputs string of file */
		String str = "";
		String longStr = "";
		try {
			FileReader reader = new FileReader(path);	
			BufferedReader br = new BufferedReader(reader); 
			while((str = br.readLine()) != null) { 
			longStr += str + "\n"; 
			} 
			reader.close();
			
			return longStr;
		} catch(Exception ex) {
			System.out.print("baaad file");
			return null;
		}
		
		
		
	}
	
	  public static void makeFrame(JScrollPane window) {
		//Create and set up the window.
	        JFrame frame = new JFrame(window.getName());
	        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        //Add content to the window.
	        frame.add(window);
	        frame.setLocationRelativeTo(null);
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        //frame.setBounds((int)(screenSize.getWidth() / 6), ((int)screenSize.getHeight() / 6), (int)(screenSize.getWidth() * 0.6), (int)(screenSize.getHeight() * 0.6));
	      frame.setBounds((int)(screenSize.getWidth() / 40), ((int)screenSize.getHeight() / 60), (int)(screenSize.getHeight() * 0.5), (int)(screenSize.getHeight() * 0.5));
	        //Display the window.
	        
	        frame.setVisible(true);
	  }
	  
	  public static JFrame makeFrameAndReturn(JScrollPane window) {
			//Create and set up the window.
		        JFrame frame = new JFrame(window.getName());
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		        //Add content to the window.
		        frame.add(window);
		        frame.setLocationRelativeTo(null);
		        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		        //frame.setBounds((int)(screenSize.getWidth() / 6), ((int)screenSize.getHeight() / 6), (int)(screenSize.getWidth() * 0.6), (int)(screenSize.getHeight() * 0.6));
		      frame.setBounds((int)(screenSize.getWidth() / 20), ((int)screenSize.getHeight() / 20), (int)(screenSize.getHeight() * 0.5), (int)(screenSize.getHeight() * 0.5));
		        //Display the window.
		        
		        frame.setVisible(true);
		        return frame;
		  }
	  
	
	public static void winDisplay(String text) {
		/* displays an HTML string in a JPanel. 
		 * Hopefully correctly */
		 JTextPane window = new JTextPane();
		 window.setContentType("text/html");
		 window.setText(text);
		 JScrollPane jsp = new JScrollPane(window);		 
		makeFrame(jsp);
		
		
	}
	
	public static JFrame winDisplayReturnFrame(String text) {
		/* displays an HTML string in a JPanel. 
		 * Hopefully correctly */
		 JTextPane window = new JTextPane();
		 window.setContentType("text/html");
		 window.setText(text);
		 JScrollPane jsp = new JScrollPane(window);		 
		JFrame frame = makeFrameAndReturn(jsp);
		return frame;
		
	}
	
	
	public static void winDisplayFile(String file) {
		/* outputs an html file into java */
		String str = fileReader(file);
		if (str != null) {
			winDisplay(str);
		}
		
		
	}
	
	public static JFrame winDisplayFileReturnFrame(String file) {
		/* outputs an html file into java */
		String str = fileReader(file);
		if (str != null) {
			JFrame x = winDisplayReturnFrame(str);
			return x;
		}
		return null;
		
		
	}
	
	public static void arrayStringer(String[][] str) {
		/* Formats a 2D array to html. i.e. adds <br> 
		 * Then displays it*/
		StringBuffer sb = new StringBuffer();
		String s = "";
		for (int i = 0; i < str.length; ++i) {
			for (int j = 0; j < str[i].length; ++j) {
				sb.append(str[i][j]);
			}
			sb.append("<br>");
		}
		sb.append("</p>");
		s = sb.toString();
		
		winDisplay(s);
		
		
		
	}
	
	//public static void main(String[] args) {
	
	//winDisplayFile(fileChooser());
	
	//}
	
	

}
