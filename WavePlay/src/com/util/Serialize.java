package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.google.gson.Gson;

/**
 * Static methods for easy serialization *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class Serialize {

	/**
	 * Serializes an object to the output path. make sure output is .ser *
	 *
	 * @param mln the mln
	 * @param output the output
	 */
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
	
	/**
	 * Serializes an object to the output path. make sure output is .ser *
	 *
	 * @param obj the obj
	 * @param output the output
	 */
	public static void serializeGson(Object obj, File output) {
		// Serialization code
		Gson gson = new Gson();
		String json = gson.toJson(obj);

		try {
			//write converted json data to a file named "file.json"
			FileWriter writer = new FileWriter(output);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Serializes an object to the output path. make sure output is .ser *
	 *
	 * @param <T> the generic type
	 * @param json the json
	 * @param type the type
	 * @return the from gson
	 */
	public static <T> T getFromGson(File json, Class<T> type) {
		Gson gson = new Gson();
		T obj = null;
		try {
			BufferedReader br = new BufferedReader(
				new FileReader(json));
			//convert the json string back to object
			obj = gson.fromJson(br, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * Serializes an object to the output path. make sure output is .ser *
	 *
	 * @param mln the mln
	 * @param output the output
	 */
	public static void serialize(Object mln, File output) {
		serialize(mln, output.getAbsolutePath());
	}
	
	/**
	 * Returns the serialized object from the filepath *.
	 *
	 * @param file the file
	 * @return the from serial
	 */
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
	
	/**
	 * Receive only the visible files and non directories
	 * in a path *.
	 *
	 * @param dir the dir
	 * @return the actual files
	 */
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
