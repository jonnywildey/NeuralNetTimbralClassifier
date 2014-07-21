package filemanager;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayStuff {
	
	/* public static ArrayList<Byte> addBytes(ArrayList<Byte> al, byte[] bytes) {
		for (byte b : bytes) {
			al.add(b);
		}
		return al;
	} */
	
	/**Add bytes to an array from another **/
	public static byte[] addBytes(byte[] addTo, byte[] from, int offset) {
		for (int i = offset; i < from.length + offset; ++i) {
			addTo[i] = from[i - offset];
		}
		return addTo;
	}
	
	/** **/
	public static double[] stringToDouble(String[] strings) {
		double[] doubles = new double[strings.length];
		for (int i = 0; i < doubles.length; ++i) {
			doubles[i] = Double.parseDouble(strings[i]);
		}
		return doubles;
	}
	
	
	/**Convert Byte ArrayList to byte array **/
	public static byte[] arrayListToByte(ArrayList<Byte> bList) {
		byte[] bytes = new byte[bList.size()];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = bList.get(i);
		}
		return bytes;
	}
	
	/**Conversion from Byte[] to byte[] **/
	public static byte[] bigByteTobyte(Byte[] bytes) {
		byte[] nb = new byte[bytes.length];
		for (int i = 0; i < bytes.length; ++i) {
			nb[i] = bytes[i];
		}
		return nb;
	}
	
	/** return the percentage similarity between two byte arrays
	 * (based on hamming distance)
	 */
	public static double byteSimilarity(byte[] byte1, byte[] byte2) {
		if (byte1.length < byte2.length) {
			byte1 = ArrayStuff.extend(byte1, byte2.length);
		}
		if (byte2.length < byte1.length) {
			byte2 = ArrayStuff.extend(byte2, byte1.length);
		}
		
		//get total
		double total = byte1.length * 8;
		return  compareBytesHamm(byte1, byte2) / total;
	}
	
	/**Convert byte to a bit char array (e.g. 00100000) **/
	public static char[] byteToBitChars(byte b) {
		char[] str = new char[8];
		boolean negative = b < 0;
		if (negative) {
			b *= -1;
			b--;
		}
		for (int i = 0; i < 8; ++i) {
			str[7 - i] = (char) ((b % 2) + 48);
			b /= 2;
		}
		//flip
		if (negative) {
			for (int i = 0; i < 8; ++i) {
				str[i] = (str[i] == '0') ? '1':'0';
			}
		}
		//Log.d(Arrays.toString(str));
		return str;
	}
	
	/**Convert byte to a bit string (e.g. 00100000) **/
	public static String byteToBitString(byte b) {
		return String.valueOf(byteToBitString(b));
	}
	
	public static ArrayList<Double> doubleToArrayList(double[] array) {
		ArrayList<Double> al = new ArrayList<Double>(array.length);
		for (double d : array) {
			al.add(d);
		}
		return al;
	}
	
	/** get the bitwise hamming distance between two byte arrays **/
	public static int compareBytesHamm(byte[] byte1, 
			byte[] byte2) {
		int c = 0;
		for (int i = 0; i < byte1.length; ++i) {
			c += hamm(byteToBitChars(byte1[i]), 
					byteToBitChars(byte2[i]));
		}
		Log.d("Hamming distance: " + byte2.length + " " + c);
		return c;
	}
	
	/**Convert double array to int array **/
	public static int[] doubleToInt(double[] vals) {
		int[] nv = new int[vals.length];
		for (int i = 0; i < vals.length; ++i) {
			nv[i] = (int) Math.rint(vals[i]);
		}
		return nv;
	}
	
	/**Convert double array to long array **/
	public static long[] doubleToLong(double[] vals) {
		long[] nv = new long[vals.length];
		for (int i = 0; i < vals.length; ++i) {
			nv[i] = (long) Math.rint(vals[i]);
		}
		return nv;
	}
	
	/**Extends the array to length x. Adding 0s after. 
	 * Will also work for shortening **/
	public static byte[] extend(byte[] values, int newLength) {
		byte[] na = new byte[newLength];
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
	public static byte[][] extend(byte[][] values, int newLength) {
		byte[][] na = new byte[newLength][values[0].length];
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
	
	


	/**Convert columns to rows and rows to columns **/
	public static double[][] flip(double[][] table) {
		return flip(table, false);
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

	/**Convert columns to rows and rows to columns **/
	public static Object[][] flip(Object[][] table) {
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

	/**Convert columns to rows and rows to columns **/
	public static String[][] flip(String[][] table) {
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
	
	/** return the maximum value in array **/
	public static double getMin(double[] values) {
		double min = Double.POSITIVE_INFINITY;
		for (double d : values) {
			min = (d<min)?d:min;
		}
		return min;
	}
	
	/** return the maximum value in array **/
	public static double getMax(double[] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double d : values) {
			max = (d>max)?d:max;
		}
		return max;
	}
	
	/** return the maximum value in array **/
	public static double getMax(double[][] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double[] d : values) {
			double c = getMax(d);
			max = (c>max) ?c:max;
		}
		return max;
	}
	
	/** return the maximum value in array **/
	public static double getMax(int[] values) {
		int max = Integer.MIN_VALUE;
		for (int d : values) {
			max = (d>max)?d:max;
		}
		return max;
	}
	
	/** get the maximum value in an array **/
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
	
	/** get the absolute maximum value in an array **/
	public static double getMaxAbs(double[] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double d : values) {
			max = (Math.abs(d)>max)?Math.abs(d):max;
		}
		return max;
	}
	
	
	/** get the absolute maximum value in an array **/
	public static double getMaxAbs(double[][] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double[] d : values) {
			double c = getMaxAbs(d);
			max = (c>max) ?c:max;
		}
		return max;
	}
	
	/**Get a subset of an array **/
	public static byte[] getSubset(byte[] bytes, int start) {
		return getSubset(bytes, start, bytes.length - 1);
	}

	/**Get a subset of array **/
	public static byte[] getSubset(byte[] bytes, int start, int end) {
		byte[] newBytes = new byte[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = bytes[start + i];
		}
		return newBytes;
	}
	
	/**Get a subset of array **/
	public static double[] getSubset(double[] doubles, int start, int end) {
		double[] newBytes = new double[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = doubles[start + i];
		}
		return newBytes;
	}
	
	/**Get a subset of an array **/
	public static double[] getSubset(double[] doubles, int start) {
		return getSubset(doubles, start, doubles.length - 1);
	}
	
	/** calculate hamming distance for a bit char array **/
	private static int hamm(char[] s1, char[] s2) {
		int c = 0;
		for (int i = 0; i < s1.length; ++i) {
			if (s1[i] == s2[i]) {
				c++;
			}
		}
		return c;
	}

	/** Normailse to a ceiling value **/
	public static double[][] normaliseDouble(double[][] values, double ceiling) {
		double multi = ceiling / getMax(values);
		for (int i = 0; i < values.length; ++i) {
			for (int j = 0; j < values[i].length; ++j) {
				values[i][j] *= multi;
			}
		}
		return values;
	}
	
	/** normalise to a ceiling value **/
	public static double[] normalizeDouble(double[] values, double ceiling) {
		double multi = ceiling / getMax(values);
		for (int i = 0; i < values.length; ++i) {
			values[i] *= multi;
		}
		return values;
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


	/**Linearly approximates the continous value between two discrete values **/
	public static double linearApproximate(double[] array, double distance) {
		/*Obviously we can't round the array lengths so I've decided to slightly 
		 * extend the new one by one sample. Probably better than reducing the information
		 */
		if ((int)distance >= array.length - 1) { 
			return array[array.length - 1];
		} else {
			int r = (int) distance;
			double rd = array[r];
			double ru = array[r + 1];
			double f = distance - r;
			return rd + (ru - rd) * f;
		}
	}

	
	/** Get the average of a particular subset of an array **/
	public static double getAverageOfSubset(double[] array, int from, int to) {
		double average = 0;
		for (int i = from; i < to; ++i) {
			average += array[i];
		}
		average /= (to - from);
		return average;
	}


	public static double[][] copy(double[][] table) {
		double[][] nt = new double[table.length][];
		for (int i = 0; i < table.length; ++i) {
			double[] nr = new double[table[i].length];
			for (int j = 0; j < table[i].length;++j) {
				nr[j] = table[i][j];
			}
			nt[i] = nr;
		}
		return nt;
	}


	public static byte[] concat(byte[]...bytes) {
		//get length
		int length = 0;
		for (int i = 0; i < bytes.length; ++i) {
			length += bytes[i].length;
		}
		byte[] newBytes = new byte[length];
		int c = 0;
		for (int i = 0; i < bytes.length; ++i) {
			for (int j = 0; j < bytes[i].length; ++j) {
				newBytes[c] = bytes[i][j];
				c++;
			}
		}
		return newBytes;
	}


	public static String arrayToString(double[][] dd) {
		StringBuilder sb = new StringBuilder(dd.length * dd[0].length * 15);
		for (int i = 0; i < dd.length; ++i) {
			//sb.append("{");
			for (int j = 0; j < dd[i].length; ++j) {
				sb.append(dd[i][j]);
				if (j != dd[i].length - 1) {
					sb.append(",");
				}
				
			}
			//sb.append("}");
			sb.append("\n");
		}
		return sb.toString();
	}

	



	

	

}
