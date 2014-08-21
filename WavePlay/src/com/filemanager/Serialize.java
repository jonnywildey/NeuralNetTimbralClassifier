package com.filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.neuralNet.MultiLayerNet;

/**Static methods for easy serialization **/
public class Serialize {

	/**Serializes an object to the output path. make sure output is .ser **/
	public static void serialize(Object mln, String output) {
		// Serialization code
	    try {
	        FileOutputStream fileOut = new FileOutputStream(output);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(mln);
	        out.close();
	        fileOut.close();
	    } catch (IOException i) {
	        i.printStackTrace();
	    }
	}
	
	/**Serializes an object to the output path. make sure output is .ser **/
	public static void serialize(Object mln, File output) {
		serialize(mln, output.getAbsolutePath());
	}
	
	/**Returns the serialized object from the filepath **/
	public static Object getFromSerial(String file) {
		Object mln = null;
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            mln = in.readObject();
            in.close();
            fileIn.close();
           // System.out.println(mln.toString());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } 
        return mln;
	}
	
	/** Receive only the visible files and non directories
	 * in a path **/
	public static File[] getActualFiles(File dir) {
		if (dir.isDirectory()) {
			File[] allFiles = dir.listFiles(new FilenameFilter(){
				public boolean accept(File dir,
			               String name) {
					File f = new File(dir.getAbsolutePath() + "/" + name);
					return (!name.startsWith(".") & !f.isDirectory());
				}
			});
			return allFiles;
		} else {
			return null;
		}
		
	}

}
