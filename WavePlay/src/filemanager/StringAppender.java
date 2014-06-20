package filemanager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**String writer class. Makes a writer object and allows a couple of different ways
 * of writing and appending files. requires using the close() function to correctly close
 * @author Jonny
 *
 */
public class StringAppender {


	private String filename;
	private BufferedWriter writer;
	private String stringToWrite;
    
    /*file prompt constructor */
    public StringAppender() { 
    	this(libraryclasses.HTML.fileChooser());
    }
    
    //file and string to write constructor
    public StringAppender(String name, String stringToWrite) {
    	this(name);
    	this.stringToWrite = stringToWrite;
    }
    
	public StringAppender(final String name) {
	    filename = name ;
	    try {
	      writer = new BufferedWriter (new FileWriter (filename, true)) ;
	    }
	    catch (IOException e) {
	      error("Can't open file: " + filename) ;
	    }
	  }
	
	
	
	public void appendTheString() {
		try {
			appendTheString(stringToWrite);
		} catch (Exception e) {
			error("did not pass string to append");
		}
	}
	
	public void appendTheString(String file) {
		try {
			writer.append(file);
			} catch (IOException e) {
				error("Couldn't append string to file " + filename);
			}
			
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
