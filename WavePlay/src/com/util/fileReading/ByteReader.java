package com.util.fileReading;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * ByteReader
 *
 * @author Jonny Wildey
 *
 */
public class ByteReader {
	private String filename;
	private FileInputStream reader;
	private boolean eof;
	private ArrayList<Integer> byteArray;
	
	/**File Prompt Constructor */
	public ByteReader() {
		this(com.util.fileReading.HTML.fileChooser());
	}
	
	
	/**Main Constructor */
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
	
	/**Converts arraylist to byteArray*/
	
	private byte[] IntegerToByte() {
		byte[] byteIntArray = new byte[byteArray.size()];
		
    	for (int i = 0; i < byteArray.size(); ++i) {
    		byteIntArray[i] = byteArray.get(i).byteValue();
    	}
    	
    	return byteIntArray;
    }
	

    public boolean eof()
    {
      return eof ;
    }
	
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

    private void error(String msg)
    {
      System.out.println(msg) ;
      System.out.println("Unable to continue executing program.") ;
      System.exit(0) ;
    }

}
