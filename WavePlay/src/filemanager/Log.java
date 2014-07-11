package filemanager;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



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
	
	public static void d(String str) {
		if (init) {
			logger.fine(str);;
		}
		System.out.println(str);
	}
	
	public static void bad(String str) {
		if (init) {
			logger.fine(str);;
		}
		System.err.println(str);
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
