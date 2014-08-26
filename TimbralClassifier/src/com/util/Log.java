package com.util;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import com.util.fileReading.HTML;

/**
 * Basic simple, static logger used throughout project.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class Log {
	
	/** The file handler. */
	public static FileHandler fileHandler;
	
	/** The logger. */
	private static Logger logger;
	/** The init. */
	public static boolean init = false;
	
	/**
	 * Sets the file path.
	 *
	 * @param f the new file path
	 */
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
	
	/**
	 * Standard debug log *.
	 *
	 * @param str the str
	 */
	public static void d(String str) {
		if (init) {
			logger.fine(str);;
		}
		System.out.println(str);
	}
	
	/**
	 * Generate popup of message *.
	 *
	 * @param str the str
	 */
	public static void popUp(String str) {
		if (init) {
			logger.fine(str);;
		}
		HTML.winDisplay(str);
	}
	
	/**
	 * Standard debug log *.
	 *
	 * @param d the d
	 */
	public static void d(double d) {
		d(String.valueOf(d));
	}
	
	/**
	 * Standard debug log *.
	 *
	 * @param d the d
	 */
	public static void d(byte d) {
		d(String.valueOf(d));
	}
	
	/**
	 * Standard debug log *.
	 *
	 * @param d the d
	 */
	public static void d(int d) {
		d(String.valueOf(d));
	}
	
	/**
	 * Standard debug log *.
	 *
	 * @param d the d
	 */
	public static void d(char d) {
		d(String.valueOf(d));
	}
	
	/**
	 * Standard debug log *.
	 *
	 * @param o the o
	 */
	public static void d(Object o) {
		d(o.toString());
	}
	
	/**
	 * Error/bad thing log *.
	 *
	 * @param str the str
	 */
	public static void bad(String str) {
		if (init) {
			logger.fine(str);;
		}
		System.err.println(str);
	}
	
	/**
	 * Error/bad thing log *.
	 *
	 * @param f the f
	 */
	public static void bad(double f) {
		bad(String.valueOf(f));
	}
	
	/**
	 * Error/bad thing log *.
	 *
	 * @param f the f
	 */
	public static void bad(int f) {
		bad(String.valueOf(f));
	}
	
	/**
	 * Error/bad thing log *.
	 *
	 * @param f the f
	 */
	public static void bad(long f) {
		bad(String.valueOf(f));
	}
	
	/**
	 * Error/bad thing log *.
	 *
	 * @param f the f
	 */
	public static void bad(byte f) {
		bad(String.valueOf(f));
	}
	
	/**
	 * Error/bad thing log *.
	 *
	 * @param f the f
	 */
	public static void bad(float f) {
		bad(String.valueOf(f));
	}
	
	/**
	 * Error/bad thing log *.
	 *
	 * @param o the o
	 */
	public static void bad(Object o) {
		bad(o.toString());
	}
	

}
