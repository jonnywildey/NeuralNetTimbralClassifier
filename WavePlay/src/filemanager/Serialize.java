package filemanager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import neuralNet.MultiLayerNet;

public class Serialize {

	/** make sure output is .ser **/
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

}
