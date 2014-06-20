package filemanager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**String writer class. Makes a writer object and allows a couple of different ways
 * of writing and appending files. requires using the close() function to correctly close
 * WRITE DAT BYTE
 * @author Jonny
 *
 */
public class ByteWriter {


	private String filename;
	private BufferedWriter writer;
	private byte[] byteToWrite;
    
    /*file prompt constructor */
    public ByteWriter() { 
    	this(libraryclasses.HTML.fileChooser());
    }
    
    //file and string to write constructor
    public ByteWriter(String name, byte[] byteToWrite) {
    	this(name);
    	this.byteToWrite = byteToWrite;
    }
    
	public ByteWriter(final String name) {
	    filename = name ;
	    try {
	      writer = new BufferedWriter (new FileWriter (filename)) ;
	    }
	    catch (IOException e) {
	      error("Can't open file: " + filename) ;
	    }
	  }
	
	public void setByteArray(byte[] byteToWrite) {
		this.byteToWrite = byteToWrite;
	}
	
	/** single byte writing method */
	public void writeTheByte(byte sByte) {
		try {
		writer.write(sByte);
		} catch (IOException e) {
			error("Couldn't write byte to file " + filename);
		}
		
	}
	/**writes the array of bytes */
	public void writeByteArray() {
		
		for (int i = 0; i < byteToWrite.length; ++i) {
			writeTheByte(byteToWrite[i]);
		}
		
		close();
		System.out.print("file written successfully");
	}
	
	public void writeByteArray(Byte[] byteToWrite) {
		
		for (int i = 0; i < byteToWrite.length; ++i) {
			writeTheByte(byteToWrite[i]);
		}
		
		close();
		System.out.print("file written successfully");
	}
	


	public void close()
    {
      try
      {
        writer.close() ;
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
