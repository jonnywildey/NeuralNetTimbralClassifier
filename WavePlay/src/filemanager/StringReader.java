package filemanager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 *
 * @author Jonny Wildey
 *
 */
public class StringReader {
	protected String filename;
	protected BufferedReader reader;
	protected boolean eof;
	
	/**File Prompt Constructor */
	public StringReader() {
		this(libraryclasses.HTML.fileChooser());
	}
	
	
	/**Main Constructor */
	public StringReader(String fname)
    {
      filename = fname ;
      try
      {
        reader = new BufferedReader(new FileReader(filename)) ;
      }
      catch (FileNotFoundException e)
      {
	error("Can't open file: " + filename) ;
      }
      
    }
	
	public String readFile() {
		StringBuffer sFile = new StringBuffer();
		try {
			sFile.append(getNewLine());
			while(eof() != true) {
				String s = getNewLine();
				if (s != null) {
					sFile.append("\n");
					sFile.append(s);
				}
				
			}
			close();
			return sFile.toString();
			
		} catch (Exception e) {
			error("couldn't read file");
			return null;
		}
		
	}
	
	
	public String getNewLine() {
    	String ss = "";
    	try
        {
    		ss = reader.readLine();
    		if (ss == null)
    		  {eof = true ;}
    		return ss;
        }
        catch (IOException e) {error("Couldn't read line"); return null;}

    }


    public boolean eof()
    {
      return eof ;
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

    protected void error(String msg)
    {
      System.out.println(msg) ;
      System.out.println("Unable to continue executing program.") ;
      System.exit(0) ;
    }

}
