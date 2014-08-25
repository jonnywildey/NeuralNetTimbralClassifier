package com.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
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
	        return ObjectStreamClass.lookup(com.DSP.neuralNet.pattern.WavePatterns.class);
	    }
	    if (desc.getName().equals("neuralNet.WavePattern")) {
	        return ObjectStreamClass.lookup(com.DSP.neuralNet.pattern.WavePattern.class);
	    }
	    if (desc.getName().equals("neuralNet.Pattern")) {
	        return ObjectStreamClass.lookup(com.DSP.neuralNet.pattern.Pattern.class);
	    }
	    if (desc.getName().equals("neuralNet.TestPatterns")) {
	        return ObjectStreamClass.lookup(com.DSP.neuralNet.pattern.TestPatterns.class);
	    }
	    if (desc.getName().equals("neuralNet.InputShell")) {
	        return ObjectStreamClass.lookup(com.DSP.neuralNet.layers.InputShell.class);
	    }
	    return desc;
	};
	
	/**
     * Load the local class equivalent of the specified stream class
     * description.  Subclasses may implement this method to allow classes to
     * be fetched from an alternate source.
     *
     * <p>The corresponding method in <code>ObjectOutputStream</code> is
     * <code>annotateClass</code>.  This method will be invoked only once for
     * each unique class in the stream.  This method can be implemented by
     * subclasses to use an alternate loading mechanism but must return a
     * <code>Class</code> object. Once returned, if the class is not an array
     * class, its serialVersionUID is compared to the serialVersionUID of the
     * serialized class, and if there is a mismatch, the deserialization fails
     * and an {@link InvalidClassException} is thrown.
     *
     * <p>The default implementation of this method in
     * <code>ObjectInputStream</code> returns the result of calling
     * <pre>
     *     Class.forName(desc.getName(), false, loader)
     * </pre>
     * where <code>loader</code> is determined as follows: if there is a
     * method on the current thread's stack whose declaring class was
     * defined by a user-defined class loader (and was not a generated to
     * implement reflective invocations), then <code>loader</code> is class
     * loader corresponding to the closest such method to the currently
     * executing frame; otherwise, <code>loader</code> is
     * <code>null</code>. If this call results in a
     * <code>ClassNotFoundException</code> and the name of the passed
     * <code>ObjectStreamClass</code> instance is the Java language keyword
     * for a primitive type or void, then the <code>Class</code> object
     * representing that primitive type or void will be returned
     * (e.g., an <code>ObjectStreamClass</code> with the name
     * <code>"int"</code> will be resolved to <code>Integer.TYPE</code>).
     * Otherwise, the <code>ClassNotFoundException</code> will be thrown to
     * the caller of this method.
     *
     * @param   desc an instance of class <code>ObjectStreamClass</code>
     * @return  a <code>Class</code> object corresponding to <code>desc</code>
     * @throws  IOException any of the usual Input/Output exceptions.
     * @throws  ClassNotFoundException if class of a serialized object cannot
     *          be found.
     */
    protected Class<?> resolveClass(ObjectStreamClass desc)
        throws IOException, ClassNotFoundException
    {
        String name = desc.getName();
        if (desc.getName().equals("neuralNet.WavePatterns")) {
	        return com.DSP.neuralNet.pattern.WavePatterns.class;
	    }
	    if (desc.getName().equals("neuralNet.WavePattern")) {
	        return com.DSP.neuralNet.pattern.WavePattern.class;
	    }
	    if (desc.getName().equals("neuralNet.Pattern")) {
	        return com.DSP.neuralNet.pattern.Pattern.class;
	    }
	    if (desc.getName().equals("neuralNet.TestPatterns")) {
	        return com.DSP.neuralNet.pattern.TestPatterns.class;
	    }
	    if (desc.getName().equals("neuralNet.InputShell")) {
	        return com.DSP.neuralNet.layers.InputShell.class;
	    }
	    return desc.getClass();
    }

}
