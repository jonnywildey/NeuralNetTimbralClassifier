package com.util.fileReading;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * ByteReader.
 *
 * @author Jonny Wildey
 */
public class ByteReader {
	
	/** The filename. */
	private String filename;
	
	/** The reader. */
	private FileInputStream reader;
	
	/** The eof. */
	private boolean eof;
	
	/** The byte array. */
	private ArrayList<Integer> byteArray;
	
	/**
	 * File Prompt Constructor.
	 */
	public ByteReader() {
		this(com.util.fileReading.HTML.fileChooser());
	}
	
	
	/**
	 * Main Constructor.
	 *
	 * @param fname the fname
	 */
	public ByteReader(String fname)
    {
      filename = fname ;
      try
      {
        reader = new FileInputStream(fname) ;
      }
      catch (FileNotFoundException e)
      {
	error("Can't open file: " + filename) ;
      }
      
    }
	
		/**
		 * Read file.
		 *
		 * @return the byte[]
		 */
		public byte[] readFile() {
		
		byteArray = new ArrayList<Integer>();
		
		int c = read();
		while (eof() != true) {
		byteArray.add(c);
		c = read();
		}
		byte[] byteIntArray = IntegerToByte();
		close();
		return byteIntArray;
	}
	
	/**
	 * Converts arraylist to byteArray.
	 *
	 * @return the byte[]
	 */
	
	private byte[] IntegerToByte() {
		byte[] byteIntArray = new byte[byteArray.size()];
		
    	for (int i = 0; i < byteArray.size(); ++i) {
    		byteIntArray[i] = byteArray.get(i).byteValue();
    	}
    	
    	return byteIntArray;
    }
	

    /**
     * Eof.
     *
     * @return true, if successful
     */
    public boolean eof()
    {
      return eof ;
    }
	
    /**
     * Read.
     *
     * @return the int
     */
    public int read() {
    	try {
    		int c = reader.read();
    		if (c == -1) {
    			eof = true;
    		}
    		return c;
    		
    	} catch (IOException e) {
    		error("can't read byte");
    	}
    	return 0;
    }
	
	
	/**
	 * Close.
	 */
	public void close()
    {
      try
      {
        reader.close() ;
      } 
      catch (IOException e)
      {
        error("Can't close file: " + filename) ;
      }
    }

    /**
     * Error.
     *
     * @param msg the msg
     */
    private void error(String msg)
    {
      System.out.println(msg) ;
      System.out.println("Unable to continue executing program.") ;
      System.exit(0) ;
    }

}
