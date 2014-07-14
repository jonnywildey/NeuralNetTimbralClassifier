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
	
	/**Extends the array to length x. Adding 0s after. 
	 * Will also work for shortening **/
	public static double[] extend(double[] values, int newLength) {
		double[] na = new double[newLength];
		for (int i = 0; i < newLength; ++i) {
			if (i < values.length) {
				na[i] = values[i];
			} else {
				na[i] = 0;
			}
		}
		return na;
	}
	
	/**Extends the array to length x. Adding 0s after. 
	 * Will also work for shortening. Will only work for rectangular
	 * arrays **/
	public static double[][] extend(double[][] values, int newLength) {
		double[][] na = new double[newLength][values[0].length];
		for (int i = 0; i < newLength; ++i) {
			
			for (int j = 0; j < values[i].length; ++j) {
				if (i < values.length) {
					na[i][j] = values[i][j];
				} else {
					na[i][j] = 0;
				}
			}
		}
		return na;
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

	/**Convert columns to rows and rows to columns **/
	public static double[][] flip(double[][] table, boolean verbose) {
		//assume first row has correct column amount
		int columnNo = table[0].length;
		if (verbose) {System.out.println("Column No.: " + columnNo);}
		double[][] flipTable = new double[columnNo][table.length];
		int i = 0;
		for (double[] row: table) {
			int j = 0;
			for (double val: row) {
				flipTable[j][i] = val;
				j++;
			}
			i++;
		}
		return flipTable;
	}

	public static double[][] flip(double[][] table) {
		return flip(table, false);
	}

	/**Convert columns to rows and rows to columns **/
	public static String[][] flip(String[][] table, boolean verbose) {
		//assume first row has correct column amount
		int columnNo = table[0].length;
		if (verbose) {System.out.println("Column No.: " + columnNo);}
		String[][] flipTable = new String[columnNo][table.length];
		int i = 0;
		for (String[] row: table) {
			int j = 0;
			for (String val: row) {
				flipTable[j][i] = val;
				j++;
			}
			i++;
		}
		return flipTable;
	}

	public static String[][] flip(String[][] table) {
		return flip(table, false);
	}

	/**Convert columns to rows and rows to columns **/
	public static Object[][] flip(Object[][] table, boolean verbose) {
		//assume first row has correct column amount
		int columnNo = table[0].length;
		if (verbose) {System.out.println("Column No.: " + columnNo);}
		Object[][] flipTable = new Object[columnNo][table.length];
		int i = 0;
		for (Object[] row: table) {
			int j = 0;
			for (Object val: row) {
				flipTable[j][i] = val;
				j++;
			}
			i++;
		}
		return flipTable;
	}

	public static Object[][] flip(Object[][] table) {
		return flip(table, false);
	}

	

}
