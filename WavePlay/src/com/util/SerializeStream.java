package com.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/** Deals with package changes **/
public class SerializeStream extends ObjectInputStream {

	public SerializeStream() throws IOException, SecurityException {
		super();
	}

	public SerializeStream(InputStream in) throws IOException {
		super(in);
	}
	
	@Override
	protected java.io.ObjectStreamClass readClassDescriptor() 
	        throws IOException, ClassNotFoundException {
	    ObjectStreamClass desc = super.readClassDescriptor();
	    if (desc.getName().equals("neuralNet.WavePatterns")) {
	        return ObjectStreamClass.lookup(com.neuralNet.pattern.WavePatterns.class);
	    }
	    if (desc.getName().equals("neuralNet.WavePattern")) {
	        return ObjectStreamClass.lookup(com.neuralNet.pattern.WavePattern.class);
	    }
	    if (desc.getName().equals("neuralNet.Pattern")) {
	        return ObjectStreamClass.lookup(com.neuralNet.pattern.Pattern.class);
	    }
	    if (desc.getName().equals("neuralNet.TestPatterns")) {
	        return ObjectStreamClass.lookup(com.neuralNet.pattern.TestPatterns.class);
	    }
	    return desc;
	};

}
