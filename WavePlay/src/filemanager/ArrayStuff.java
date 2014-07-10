package filemanager;

import java.util.ArrayList;

public class ArrayStuff {

	public static ArrayList<Byte> addBytes(ArrayList<Byte> al, byte[] bytes) {
		for (byte b : bytes) {
			al.add(b);
		}
		return al;
	}
	
	public static byte[] bigByteTobyte(Byte[] bytes) {
		byte[] nb = new byte[bytes.length];
		for (int i = 0; i < bytes.length; ++i) {
			nb[i] = bytes[i];
		}
		return nb;
	}
	
	
	public static double[] normalizeDouble(double[] values, double ceiling) {
		double multi = ceiling / getMax(values);
		for (int i = 0; i < values.length; ++i) {
			values[i] *= multi;
		}
		return values;
	}
	
	public static double[][] normaliseDouble(double[][] values, double ceiling) {
		double multi = ceiling / getMax(values);
		for (int i = 0; i < values.length; ++i) {
			for (int j = 0; j < values[i].length; ++j) {
				values[i][j] *= multi;
			}
		}
		return values;
	}
	
	public static long[] doubleToLong(double[] vals) {
		long[] nv = new long[vals.length];
		for (int i = 0; i < vals.length; ++i) {
			nv[i] = (long) Math.rint(vals[i]);
		}
		return nv;
	}
	
	public static int[] doubleToInt(double[] vals) {
		int[] nv = new int[vals.length];
		for (int i = 0; i < vals.length; ++i) {
			nv[i] = (int) Math.rint(vals[i]);
		}
		return nv;
	}
	
	public static double getMax(double[] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double d : values) {
			max = (d>max)?d:max;
		}
		return max;
	}
	
	public static double getMax(double[][] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double[] d : values) {
			double c = getMax(d);
			max = (c>max) ?c:max;
		}
		return max;
	}

	public static double getMax(long[][] vals) {
		double max = 0;
		for (long[] l: vals) {
			for (int i = 0; i < l.length; ++i) {
				if (Math.abs(l[i]) > max) {
					max = Math.abs(l[i]);
				}
			}
		}
		return max;
	}

	public static byte[] addBytes(byte[] addTo, byte[] from, int offset) {
		for (int i = offset; i < from.length + offset; ++i) {
			addTo[i] = from[i - offset];
		}
		return addTo;
	}

	/**Get a subset of array **/
	public static byte[] getSubset(byte[] bytes, int start, int end) {
		byte[] newBytes = new byte[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = bytes[start + i];
		}
		return newBytes;
	}
	
	public static byte[] getSubset(byte[] bytes, int start) {
		
		return getSubset(bytes, start, bytes.length - 1);
	}
	
	public static byte[] arrayListToByte(ArrayList<Byte> bList) {
		byte[] bytes = new byte[bList.size()];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = bList.get(i);
		}
		return bytes;
	}
	
	
	/** changes a 2d array to a long 1d array **/
	public static double[] tableToLongRow(double[][] table) {
		//get length
		int l = 0;
		for (double[] row : table) {
			l += row.length;
		}
		double[] lr = new double[l];
		int c = 0;
		for (int i = 0; i < table.length; ++i) {
			for (int j = 0; j < table[i].length; ++j) {
				lr[c] = table[i][j];
				c++;
			}
		}
		return lr;
	}
	
	/** changes a 2d array to a long 1d array **/
	public static byte[] tableToLongRow(byte[][] table) {
		//get length
		int l = 0;
		for (byte[] row : table) {
			l += row.length;
		}
		byte[] lr = new byte[l];
		int c = 0;
		for (int i = 0; i < table.length; ++i) {
			for (int j = 0; j < table[i].length; ++j) {
				lr[c] = table[i][j];
				c++;
			}
		}
		return lr;
	}

}
