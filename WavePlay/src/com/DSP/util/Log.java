package com.util;

import java.awt.Dialog;
import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.util.fileReading.HTML;



public class Log {
	
	public static FileHandler fileHandler;
	private static Logger logger;
	public static boolean init = false;
	
	public static void setFilePath(File f) {
		try {
			
			fileHandler = new FileHandler(f.getAbsolutePath());
			fileHandler.setFormatter(new SimpleFormatter() {
		            @Override
		            public String format(LogRecord arg0) {
		                StringBuilder b = new StringBuilder();
		                b.append(arg0.getMessage());
		                b.append(System.getProperty("line.separator"));
		                return b.toString();
		            }
			});
			
			logger = Logger.getLogger(Log.class.getName());
			logger.addHandler(fileHandler);

			logger.setLevel(Level.ALL);
			logger.setUseParentHandlers(false);
			init = true;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**Standard debug log **/
	public static void d(String str) {
		if (init) {
			logger.fine(str);;
		}
		System.out.println(str);
	}
	
	/**Generate popup of message **/
	public static void popUp(String str) {
		if (init) {
			logger.fine(str);;
		}
		HTML.winDisplay(str);
	}
	
	/**Standard debug log **/
	public static void d(double d) {
		d(String.valueOf(d));
	}
	
	/**Standard debug log **/
	public static void d(byte d) {
		d(String.valueOf(d));
	}
	
	/**Standard debug log **/
	public static void d(int d) {
		d(String.valueOf(d));
	}
	
	/**Standard debug log **/
	public static void d(char d) {
		d(String.valueOf(d));
	}
	
	/**Standard debug log **/
	public static void d(Object o) {
		d(o.toString());
	}
	
	public static void bad(String str) {
		if (init) {
			logger.fine(str);;
		}
		System.err.println(str);
	}
	
	public static void bad(double f) {
		bad(String.valueOf(f));
	}
	
	public static void bad(int f) {
		bad(String.valueOf(f));
	}
	
	public static void bad(long f) {
		bad(String.valueOf(f));
	}
	
	public static void bad(byte f) {
		bad(String.valueOf(f));
	}
	
	public static void bad(float f) {
		bad(String.valueOf(f));
	}
	
	public static void bad(Object o) {
		bad(o.toString());
	}
	
	
	
	
	public static void logTesting() {
		FileHandler fh;
		try {
			fh = new FileHandler("fileDemo.txt");
			Logger logger = Logger.getLogger("New Log");
			logger.addHandler(fh);
			SimpleFormatter sf = new SimpleFormatter();
			fh.setFormatter(sf);
			logger.setLevel(Level.ALL);
			logger.log(Level.CONFIG, "TESTING");
			logger.log(Level.FINER, "Ytfgh");
			logger.log(Level.ALL, "TESTING2");
		} catch (Exception e) {
			e.printStackTrace();;
		} 
	}

	

	
	

}
