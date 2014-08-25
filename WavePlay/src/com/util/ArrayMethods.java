package com.util;

import java.util.ArrayList;
import com.DSP.waveAnalysis.Statistics;

/**
 * Useful static methods for manipulating arrays *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class ArrayMethods {

	/**
	 * Return the average of a specific row of a table *.
	 *
	 * @param table the table
	 * @return the average of row
	 */
	public static double[] getAverageOfRow(double[][] table) {
		double[] d = new double[table.length];
		for (int i = 0; i < table.length; ++i) {
			d[i] = getAverage(table[i]);
		}
		return d;
	}
	
	/**
	 * Return the average of a specific column *.
	 *
	 * @param table the table
	 * @return the average of column
	 */
	public static double[] getAverageOfColumn(double[][] table) {
		return getAverageOfRow(flip(table));
	}
	
	/**
	 * Return the average of an array *.
	 *
	 * @param array the array
	 * @return the average
	 */
	public static double getAverage(double[] array) {
		double d = 0;
		// Log.d("length " + array.length);
		for (int i = 0; i < array.length; ++i) {
			d += array[i];
		}
		return d / array.length;
	}

	/**
	 * Add bytes to an array from another *.
	 *
	 * @param addTo the add to
	 * @param from the from
	 * @param offset the offset
	 * @return the byte[]
	 */
	public static byte[] addBytes(byte[] addTo, byte[] from, int offset) {
		for (int i = offset; i < from.length + offset; ++i) {
			addTo[i] = from[i - offset];
		}
		return addTo;
	}

	/**
	 * Add doubles to an array from another *.
	 *
	 * @param addTo the add to
	 * @param from the from
	 * @param offset the offset
	 * @return the double[]
	 */
	public static double[] addDoubles(double[] addTo, double[] from, int offset) {
		for (int i = offset; i < from.length + offset; ++i) {
			addTo[i] = from[i - offset];
		}
		return addTo;
	}

	/**
	 * Converts an array of strings to an array of doubles *.
	 *
	 * @param strings the strings
	 * @return the double[]
	 */
	public static double[] stringToDouble(String[] strings) {
		double[] doubles = new double[strings.length];
		for (int i = 0; i < doubles.length; ++i) {
			doubles[i] = Double.parseDouble(strings[i]);
		}
		return doubles;
	}

	/**
	 * Convert Byte ArrayList to byte array *.
	 *
	 * @param bList the b list
	 * @return the byte[]
	 */
	public static byte[] arrayListToByte(ArrayList<Byte> bList) {
		byte[] bytes = new byte[bList.size()];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = bList.get(i);
		}
		return bytes;
	}
	
	/**
	 * Convert double ArrayList to double array *.
	 *
	 * @param aList the a list
	 * @return the double[]
	 */
	public static double[] arrayListToDouble(ArrayList<Double> aList) {
		double[] doubles = new double[aList.size()];
		for (int i = 0; i < doubles.length; ++i) {
			doubles[i] = aList.get(i);
		}
		return doubles;
	}
	
	/**
	 * Convert float ArrayList to float array *.
	 *
	 * @param aList the a list
	 * @return the float[]
	 */
	public static float[] arrayListToFloat(ArrayList<Float> aList) {
		float[] floats = new float[aList.size()];
		for (int i = 0; i < floats.length; ++i) {
			floats[i] = aList.get(i);
		}
		return floats;
	}
	
	/**
	 * Convert long ArrayList to long array *.
	 *
	 * @param aList the a list
	 * @return the long[]
	 */
	public static long[] arrayListToLong(ArrayList<Long> aList) {
		long[] longs = new long[aList.size()];
		for (int i = 0; i < longs.length; ++i) {
			longs[i] = aList.get(i);
		}
		return longs;
	}
	
	/**
	 * Convert int ArrayList to int array *.
	 *
	 * @param aList the a list
	 * @return the int[]
	 */
	public static int[] arrayListToInt(ArrayList<Integer> aList) {
		int[] ints = new int[aList.size()];
		for (int i = 0; i < ints.length; ++i) {
			ints[i] = aList.get(i);
		}
		return ints;
	}
	
	/**
	 * Convert char ArrayList to char array *.
	 *
	 * @param aList the a list
	 * @return the char[]
	 */
	public static char[] arrayListToChar(ArrayList<Character> aList) {
		char[] chars = new char[aList.size()];
		for (int i = 0; i < chars.length; ++i) {
			chars[i] = aList.get(i);
		}
		return chars;
	}

	/**
	 * Conversion from Byte[] to byte[] *.
	 *
	 * @param bytes the bytes
	 * @return the byte[]
	 */
	public static byte[] bigByteTobyte(Byte[] bytes) {
		byte[] nb = new byte[bytes.length];
		for (int i = 0; i < bytes.length; ++i) {
			nb[i] = bytes[i];
		}
		return nb;
	}
	
	/**
	 * Conversion from Double[] to double[] *.
	 *
	 * @param doubles the doubles
	 * @return the double[]
	 */
	public static double[] bigDoubleTodouble(Double[] doubles) {
		double[] nb = new double[doubles.length];
		for (int i = 0; i < doubles.length; ++i) {
			nb[i] = doubles[i];
		}
		return nb;
	}
	
	/**
	 * Conversion from Float[] to float[] *.
	 *
	 * @param floats the floats
	 * @return the float[]
	 */
	public static float[] bigFloatTofloat(Float[] floats) {
		float[] nb = new float[floats.length];
		for (int i = 0; i < floats.length; ++i) {
			nb[i] = floats[i];
		}
		return nb;
	}
	
	/**
	 * Conversion from Long[] to long[] *.
	 *
	 * @param longs the longs
	 * @return the long[]
	 */
	public static long[] bigLongTolong(Long[] longs) {
		long[] nb = new long[longs.length];
		for (int i = 0; i < longs.length; ++i) {
			nb[i] = longs[i];
		}
		return nb;
	}
	
	/**
	 * Conversion from Integer[] to int[] *.
	 *
	 * @param ints the ints
	 * @return the int[]
	 */
	public static int[] bigIntegerToint(Integer[] ints) {
		int[] nb = new int[ints.length];
		for (int i = 0; i < ints.length; ++i) {
			nb[i] = ints[i];
		}
		return nb;
	}
	
	/**
	 * Conversion from Character[] to char[] *.
	 *
	 * @param chars the chars
	 * @return the char[]
	 */
	public static char[] bigCharacterTochar(Character[] chars) {
		char[] nb = new char[chars.length];
		for (int i = 0; i < chars.length; ++i) {
			nb[i] = chars[i];
		}
		return nb;
	}

	/**
	 * return the percentage similarity between two byte arrays (based on
	 * hamming distance).
	 *
	 * @param byte1 the byte1
	 * @param byte2 the byte2
	 * @return the double
	 */
	public static double byteSimilarity(byte[] byte1, byte[] byte2) {
		if (byte1.length < byte2.length) {
			byte1 = ArrayMethods.extend(byte1, byte2.length);
		}
		if (byte2.length < byte1.length) {
			byte2 = ArrayMethods.extend(byte2, byte1.length);
		}
		// get total
		double total = byte1.length * 8;
		return compareBytesHamm(byte1, byte2) / total;
	}

	/**
	 * Convert byte to a bit char array (e.g. 32 -> 00100000) *
	 *
	 * @param b the b
	 * @return the char[]
	 */
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
		// flip
		if (negative) {
			for (int i = 0; i < 8; ++i) {
				str[i] = (str[i] == '0') ? '1' : '0';
			}
		}
		return str;
	}

	/**
	 * Convert byte to a bit string (e.g. 00100000) *
	 *
	 * @param b the b
	 * @return the string
	 */
	public static String byteToBitString(byte b) {
		return String.valueOf(byteToBitString(b));
	}

	/**
	 * Converts a double array to an arraylist *.
	 *
	 * @param array the array
	 * @return the array list
	 */
	public static ArrayList<Double> doubleToArrayList(double[] array) {
		ArrayList<Double> al = new ArrayList<Double>(array.length);
		for (double d : array) {
			al.add(d);
		}
		return al;
	}
	
	/**
	 * Converts a float array to an arraylist *.
	 *
	 * @param array the array
	 * @return the array list
	 */
	public static ArrayList<Float> floatToArrayList(float[] array) {
		ArrayList<Float> al = new ArrayList<Float>(array.length);
		for (float d : array) {
			al.add(d);
		}
		return al;
	}
	
	/**
	 * Converts a int array to an arraylist *.
	 *
	 * @param array the array
	 * @return the array list
	 */
	public static ArrayList<Integer> intToArrayList(int[] array) {
		ArrayList<Integer> al = new ArrayList<Integer>(array.length);
		for (int d : array) {
			al.add(d);
		}
		return al;
	}
	
	/**
	 * Converts a long array to an arraylist *.
	 *
	 * @param array the array
	 * @return the array list
	 */
	public static ArrayList<Long> longToArrayList(long[] array) {
		ArrayList<Long> al = new ArrayList<Long>(array.length);
		for (long d : array) {
			al.add(d);
		}
		return al;
	}
	
	/**
	 * Converts a char array to an arraylist *.
	 *
	 * @param array the array
	 * @return the array list
	 */
	public static ArrayList<Character> charToArrayList(char[] array) {
		ArrayList<Character> al = new ArrayList<Character>(array.length);
		for (char d : array) {
			al.add(d);
		}
		return al;
	}

	/**
	 * get the bitwise hamming distance between two byte arrays *.
	 *
	 * @param byte1 the byte1
	 * @param byte2 the byte2
	 * @return the int
	 */
	public static int compareBytesHamm(byte[] byte1, byte[] byte2) {
		int c = 0;
		for (int i = 0; i < byte1.length; ++i) {
			c += hamm(byteToBitChars(byte1[i]), byteToBitChars(byte2[i]));
		}
		Log.d("Hamming distance: " + byte2.length + " " + c);
		return c;
	}

	/**
	 * Convert double array to int array *.
	 *
	 * @param vals the vals
	 * @return the int[]
	 */
	public static int[] doubleToInt(double[] vals) {
		int[] nv = new int[vals.length];
		for (int i = 0; i < vals.length; ++i) {
			nv[i] = (int) Math.rint(vals[i]);
		}
		return nv;
	}

	/**
	 * Convert double array to long array *.
	 *
	 * @param vals the vals
	 * @return the long[]
	 */
	public static long[] doubleToLong(double[] vals) {
		long[] nv = new long[vals.length];
		for (int i = 0; i < vals.length; ++i) {
			nv[i] = (long) Math.rint(vals[i]);
		}
		return nv;
	}

	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the byte[]
	 */
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
	
	
	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the long[]
	 */
	public static long[] extend(long[] values, int newLength) {
		long[] na = new long[newLength];
		for (int i = 0; i < newLength; ++i) {
			if (i < values.length) {
				na[i] = values[i];
			} else {
				na[i] = 0;
			}
		}
		return na;
	}
	
	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the float[]
	 */
	public static float[] extend(float[] values, int newLength) {
		float[] na = new float[newLength];
		for (int i = 0; i < newLength; ++i) {
			if (i < values.length) {
				na[i] = values[i];
			} else {
				na[i] = 0;
			}
		}
		return na;
	}
	
	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the int[]
	 */
	public static int[] extend(int[] values, int newLength) {
		int[] na = new int[newLength];
		for (int i = 0; i < newLength; ++i) {
			if (i < values.length) {
				na[i] = values[i];
			} else {
				na[i] = 0;
			}
		}
		return na;
	}
	
	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the char[]
	 */
	public static char[] extend(char[] values, int newLength) {
		char[] na = new char[newLength];
		for (int i = 0; i < newLength; ++i) {
			if (i < values.length) {
				na[i] = values[i];
			} else {
				na[i] = 0;
			}
		}
		return na;
	}
	

	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening. Will only work for rectangular arrays
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the byte[][]
	 */
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
	
	
	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening. Will only work for rectangular arrays
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the float[][]
	 */
	public static float[][] extend(float[][] values, int newLength) {
		float[][] na = new float[newLength][values[0].length];
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
	
	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening. Will only work for rectangular arrays
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the long[][]
	 */
	public static long[][] extend(long[][] values, int newLength) {
		long[][] na = new long[newLength][values[0].length];
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
	
	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening. Will only work for rectangular arrays
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the int[][]
	 */
	public static int[][] extend(int[][] values, int newLength) {
		int[][] na = new int[newLength][values[0].length];
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
	
	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening. Will only work for rectangular arrays
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the char[][]
	 */
	public static char[][] extend(char[][] values, int newLength) {
		char[][] na = new char[newLength][values[0].length];
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

	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the double[]
	 */
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

	/**
	 * Extends the array to length x. Adding 0s after. Will also work for
	 * shortening. Will only work for rectangular arrays
	 *
	 * @param values the values
	 * @param newLength the new length
	 * @return the double[][]
	 */
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

	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @return the double[][]
	 */
	public static double[][] flip(double[][] table) {
		return flip(table, false);
	}

	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @param verbose the verbose
	 * @return the double[][]
	 */
	public static double[][] flip(double[][] table, boolean verbose) {
		// assume first row has correct column amount
		int columnNo = table[0].length;
		if (verbose) {
			System.out.println("Column No.: " + columnNo);
		}
		double[][] flipTable = new double[columnNo][table.length];
		int i = 0;
		for (double[] row : table) {
			int j = 0;
			for (double val : row) {
				flipTable[j][i] = val;
				j++;
			}
			i++;
		}
		return flipTable;
	}
	
	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @return the float[][]
	 */
	public static float[][] flip(float[][] table) {
		return flip(table, false);
	}

	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @param verbose the verbose
	 * @return the float[][]
	 */
	public static float[][] flip(float[][] table, boolean verbose) {
		// assume first row has correct column amount
		int columnNo = table[0].length;
		if (verbose) {
			System.out.println("Column No.: " + columnNo);
		}
		float[][] flipTable = new float[columnNo][table.length];
		int i = 0;
		for (float[] row : table) {
			int j = 0;
			for (float val : row) {
				flipTable[j][i] = val;
				j++;
			}
			i++;
		}
		return flipTable;
	}
	
	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @return the long[][]
	 */
	public static long[][] flip(long[][] table) {
		return flip(table, false);
	}

	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @param verbose the verbose
	 * @return the long[][]
	 */
	public static long[][] flip(long[][] table, boolean verbose) {
		// assume first row has correct column amount
		int columnNo = table[0].length;
		if (verbose) {
			System.out.println("Column No.: " + columnNo);
		}
		long[][] flipTable = new long[columnNo][table.length];
		int i = 0;
		for (long[] row : table) {
			int j = 0;
			for (long val : row) {
				flipTable[j][i] = val;
				j++;
			}
			i++;
		}
		return flipTable;
	}
	
	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @return the int[][]
	 */
	public static int[][] flip(int[][] table) {
		return flip(table, false);
	}

	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @param verbose the verbose
	 * @return the int[][]
	 */
	public static int[][] flip(int[][] table, boolean verbose) {
		// assume first row has correct column amount
		int columnNo = table[0].length;
		if (verbose) {
			System.out.println("Column No.: " + columnNo);
		}
		int[][] flipTable = new int[columnNo][table.length];
		int i = 0;
		for (int[] row : table) {
			int j = 0;
			for (int val : row) {
				flipTable[j][i] = val;
				j++;
			}
			i++;
		}
		return flipTable;
	}
	
	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @return the char[][]
	 */
	public static char[][] flip(char[][] table) {
		return flip(table, false);
	}

	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @param verbose the verbose
	 * @return the char[][]
	 */
	public static char[][] flip(char[][] table, boolean verbose) {
		// assume first row has correct column amount
		int columnNo = table[0].length;
		if (verbose) {
			System.out.println("Column No.: " + columnNo);
		}
		char[][] flipTable = new char[columnNo][table.length];
		int i = 0;
		for (char[] row : table) {
			int j = 0;
			for (char val : row) {
				flipTable[j][i] = val;
				j++;
			}
			i++;
		}
		return flipTable;
	}

	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @return the object[][]
	 */
	public static Object[][] flip(Object[][] table) {
		return flip(table, false);
	}

	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @param verbose the verbose
	 * @return the object[][]
	 */
	public static Object[][] flip(Object[][] table, boolean verbose) {
		// assume first row has correct column amount
		int columnNo = table[0].length;
		if (verbose) {
			System.out.println("Column No.: " + columnNo);
		}
		Object[][] flipTable = new Object[columnNo][table.length];
		int i = 0;
		for (Object[] row : table) {
			int j = 0;
			for (Object val : row) {
				flipTable[j][i] = val;
				j++;
			}
			i++;
		}
		return flipTable;
	}

	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @return the string[][]
	 */
	public static String[][] flip(String[][] table) {
		return flip(table, false);
	}

	/**
	 * Convert columns to rows and rows to columns *.
	 *
	 * @param table the table
	 * @param verbose the verbose
	 * @return the string[][]
	 */
	public static String[][] flip(String[][] table, boolean verbose) {
		// assume first row has correct column amount
		int columnNo = table[0].length;
		if (verbose) {
			System.out.println("Column No.: " + columnNo);
		}
		String[][] flipTable = new String[columnNo][table.length];
		int i = 0;
		for (String[] row : table) {
			int j = 0;
			for (String val : row) {
				flipTable[j][i] = val;
				j++;
			}
			i++;
		}
		return flipTable;
	}

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the min
	 */
	public static double getMin(double[] values) {
		double min = Double.POSITIVE_INFINITY;
		for (double d : values) {
			min = (d < min) ? d : min;
		}
		return min;
	}
	
	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the min
	 */
	public static double getMin(double[][] values) {
		double min = Double.POSITIVE_INFINITY;
		for (double[] row : values) {
			for (double d : row) {
				min = (d < min) ? d : min;
			}
		}
		return min;
	}

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the max
	 */
	public static double getMax(double[] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double d : values) {
			max = (d > max) ? d : max;
		}
		return max;
	}

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the max
	 */
	public static double getMax(double[][] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double[] d : values) {
			double c = getMax(d);
			max = (c > max) ? c : max;
		}
		return max;
	}
	
	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the min
	 */
	public static int getMin(int[] values) {
		int min = Integer.MAX_VALUE;
		for (int d : values) {
			min = (d < min) ? d : min;
		}
		return min;
	}
	
	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the min
	 */
	public static int getMin(int[][] values) {
		int min = Integer.MAX_VALUE;
		for (int[] row : values) {
			for (int d : row) {
				min = (d < min) ? d : min;
			}
		}
		return min;
	}

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the min
	 */
	public static char getMin(char[] values) {
		char min = Character.MAX_VALUE;
		for (char d : values) {
			min = (d < min) ? d : min;
		}
		return min;
	}
	
	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the min
	 */
	public static char getMin(char[][] values) {
		char min = Character.MAX_VALUE;
		for (char[] row : values) {
			for (char d : row) {
				min = (d < min) ? d : min;
			}
		}
		return min;
	}

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the max
	 */
	public static char getMax(char[] values) {
		char max = Character.MIN_VALUE;
		for (char d : values) {
			max = (d > max) ? d : max;
		}
		return max;
	}

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the max
	 */
	public static char getMax(char[][] values) {
		char max = Character.MIN_VALUE;
		for (char[] d : values) {
			char c = getMax(d);
			max = (c > max) ? c : max;
		}
		return max;
	}

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the max
	 */
	public static int getMax(int[][] values) {
		int max = Integer.MAX_VALUE;
		for (int[] d : values) {
			int c = getMax(d);
			max = (c > max) ? c : max;
		}
		return max;
	}
	
	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the min
	 */
	public static float getMin(float[] values) {
		float min = Float.POSITIVE_INFINITY;
		for (float d : values) {
			min = (d < min) ? d : min;
		}
		return min;
	}
	
	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the min
	 */
	public static float getMin(float[][] values) {
		float min = Float.POSITIVE_INFINITY;
		for (float[] row : values) {
			for (float d : row) {
				min = (d < min) ? d : min;
			}
		}
		return min;
	}

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the max
	 */
	public static float getMax(float[] values) {
		float max = Float.NEGATIVE_INFINITY;
		for (float d : values) {
			max = (d > max) ? d : max;
		}
		return max;
	}

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the max
	 */
	public static float getMax(float[][] values) {
		float max = Float.NEGATIVE_INFINITY;
		for (float[] d : values) {
			float c = getMax(d);
			max = (c > max) ? c : max;
		}
		return max;
	}
	
	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the min
	 */
	public static long getMin(long[] values) {
		long min = Long.MAX_VALUE;
		for (long d : values) {
			min = (d < min) ? d : min;
		}
		return min;
	}
	
	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the min
	 */
	public static long getMin(long[][] values) {
		long min = Long.MAX_VALUE;
		for (long[] row : values) {
			for (long d : row) {
				min = (d < min) ? d : min;
			}
		}
		return min;
	}

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the max
	 */
	public static long getMax(long[] values) {
		long max = Long.MIN_VALUE;
		for (long d : values) {
			max = (d > max) ? d : max;
		}
		return max;
	}
	

	/**
	 * return the maximum value in array *.
	 *
	 * @param values the values
	 * @return the max
	 */
	public static int getMax(int[] values) {
		int max = Integer.MIN_VALUE;
		for (int d : values) {
			max = (d > max) ? d : max;
		}
		return max;
	}

	/**
	 * get the maximum value in an array *.
	 *
	 * @param vals the vals
	 * @return the max
	 */
	public static long getMax(long[][] vals) {
		long max = 0;
		for (long[] l : vals) {
			for (int i = 0; i < l.length; ++i) {
				if (Math.abs(l[i]) > max) {
					max = Math.abs(l[i]);
				}
			}
		}
		return max;
	}

	/**
	 * get the absolute maximum value in an array *.
	 *
	 * @param values the values
	 * @return the max abs
	 */
	public static double getMaxAbs(double[] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double d : values) {
			max = (Math.abs(d) > max) ? Math.abs(d) : max;
		}
		return max;
	}

	/**
	 * get the absolute maximum value in an array *.
	 *
	 * @param values the values
	 * @return the max abs
	 */
	public static double getMaxAbs(double[][] values) {
		double max = Double.NEGATIVE_INFINITY;
		for (double[] d : values) {
			double c = getMaxAbs(d);
			max = (c > max) ? c : max;
		}
		return max;
	}
	
	/**
	 * get the absolute maximum value in an array *.
	 *
	 * @param values the values
	 * @return the max abs
	 */
	public static float getMaxAbs(float[] values) {
		float max = Float.NEGATIVE_INFINITY;
		for (float d : values) {
			max = (Math.abs(d) > max) ? Math.abs(d) : max;
		}
		return max;
	}

	/**
	 * get the absolute maximum value in an array *.
	 *
	 * @param values the values
	 * @return the max abs
	 */
	public static float getMaxAbs(float[][] values) {
		float max = Float.NEGATIVE_INFINITY;
		for (float[] d : values) {
			float c = getMaxAbs(d);
			max = (c > max) ? c : max;
		}
		return max;
	}
	
	/**
	 * get the absolute maximum value in an array *.
	 *
	 * @param values the values
	 * @return the max abs
	 */
	public static int getMaxAbs(int[] values) {
		int max = Integer.MIN_VALUE;
		for (int d : values) {
			max = (Math.abs(d) > max) ? Math.abs(d) : max;
		}
		return max;
	}

	/**
	 * get the absolute maximum value in an array *.
	 *
	 * @param values the values
	 * @return the max abs
	 */
	public static int getMaxAbs(int[][] values) {
		int max = Integer.MIN_VALUE;
		for (int[] d : values) {
			int c = getMaxAbs(d);
			max = (c > max) ? c : max;
		}
		return max;
	}
	
	/**
	 * get the absolute maximum value in an array *.
	 *
	 * @param values the values
	 * @return the max abs
	 */
	public static char getMaxAbs(char[] values) {
		char max = Character.MIN_VALUE;
		for (char d : values) {
			max = (char) ((Math.abs(d) > max) ? Math.abs(d) : max);
		}
		return max;
	}

	/**
	 * get the absolute maximum value in an array *.
	 *
	 * @param values the values
	 * @return the max abs
	 */
	public static char getMaxAbs(char[][] values) {
		char max = Character.MIN_VALUE;
		for (char[] d : values) {
			char c = getMaxAbs(d);
			max = (c > max) ? c : max;
		}
		return max;
	}

	/**
	 * Get a subset of an array *.
	 *
	 * @param bytes the bytes
	 * @param start the start
	 * @return the subset
	 */
	public static byte[] getSubset(byte[] bytes, int start) {
		return getSubset(bytes, start, bytes.length - 1);
	}

	/**
	 * Get a subset of array *.
	 *
	 * @param bytes the bytes
	 * @param start the start
	 * @param end the end
	 * @return the subset
	 */
	public static byte[] getSubset(byte[] bytes, int start, int end) {
		byte[] newBytes = new byte[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = bytes[start + i];
		}
		return newBytes;
	}
	
	/**
	 * Get a subset of an array *.
	 *
	 * @param longs the longs
	 * @param start the start
	 * @return the subset
	 */
	public static long[] getSubset(long[] longs, int start) {
		return getSubset(longs, start, longs.length - 1);
	}

	/**
	 * Get a subset of array *.
	 *
	 * @param longs the longs
	 * @param start the start
	 * @param end the end
	 * @return the subset
	 */
	public static long[] getSubset(long[] longs, int start, int end) {
		long[] newBytes = new long[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = longs[start + i];
		}
		return newBytes;
	}
	
	/**
	 * Get a subset of an array *.
	 *
	 * @param ints the ints
	 * @param start the start
	 * @return the subset
	 */
	public static int[] getSubset(int[] ints, int start) {
		return getSubset(ints, start, ints.length - 1);
	}

	/**
	 * Get a subset of array *.
	 *
	 * @param ints the ints
	 * @param start the start
	 * @param end the end
	 * @return the subset
	 */
	public static int[] getSubset(int[] ints, int start, int end) {
		int[] newBytes = new int[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = ints[start + i];
		}
		return newBytes;
	}
	
	/**
	 * Get a subset of an array *.
	 *
	 * @param floats the floats
	 * @param start the start
	 * @return the subset
	 */
	public static float[] getSubset(float[] floats, int start) {
		return getSubset(floats, start, floats.length - 1);
	}

	/**
	 * Get a subset of array *.
	 *
	 * @param floats the floats
	 * @param start the start
	 * @param end the end
	 * @return the subset
	 */
	public static float[] getSubset(float[] floats, int start, int end) {
		float[] newBytes = new float[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = floats[start + i];
		}
		return newBytes;
	}
	
	/**
	 * Get a subset of an array *.
	 *
	 * @param chars the chars
	 * @param start the start
	 * @return the subset
	 */
	public static char[] getSubset(char[] chars, int start) {
		return getSubset(chars, start, chars.length - 1);
	}

	/**
	 * Get a subset of array *.
	 *
	 * @param chars the chars
	 * @param start the start
	 * @param end the end
	 * @return the subset
	 */
	public static char[] getSubset(char[] chars, int start, int end) {
		char[] newBytes = new char[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = chars[start + i];
		}
		return newBytes;
	}

	/**
	 * Get a subset of array *.
	 *
	 * @param doubles the doubles
	 * @param start the start
	 * @param end the end
	 * @return the subset
	 */
	public static double[] getSubset(double[] doubles, int start, int end) {
		double[] newBytes = new double[(end + 1) - start];
		for (int i = 0; i <= (end - start); ++i) {
			newBytes[i] = doubles[start + i];
		}
		return newBytes;
	}

	/**
	 * Get a subset of an array *.
	 *
	 * @param doubles the doubles
	 * @param start the start
	 * @return the subset
	 */
	public static double[] getSubset(double[] doubles, int start) {
		return getSubset(doubles, start, doubles.length - 1);
	}

	/**
	 * calculate hamming distance for a bit char array *.
	 *
	 * @param s1 the s1
	 * @param s2 the s2
	 * @return the int
	 */
	private static int hamm(char[] s1, char[] s2) {
		int c = 0;
		for (int i = 0; i < s1.length; ++i) {
			if (s1[i] == s2[i]) {
				c++;
			}
		}
		return c;
	}

	/**
	 * Normailse to a ceiling value *.
	 *
	 * @param values the values
	 * @param ceiling the ceiling
	 * @return the double[][]
	 */
	public static double[][] normaliseDouble(double[][] values, double ceiling) {
		double multi = ceiling / getMax(values);
		for (int i = 0; i < values.length; ++i) {
			for (int j = 0; j < values[i].length; ++j) {
				values[i][j] *= multi;
			}
		}
		return values;
	}

	/**
	 * normalise to a ceiling value *.
	 *
	 * @param values the values
	 * @param ceiling the ceiling
	 * @return the double[]
	 */
	public static double[] normalizeDouble(double[] values, double ceiling) {
		double multi = ceiling / getMax(values);
		for (int i = 0; i < values.length; ++i) {
			values[i] *= multi;
		}
		return values;
	}
	
	/**
	 * Normailse to a ceiling value *.
	 *
	 * @param values the values
	 * @param ceiling the ceiling
	 * @return the float[][]
	 */
	public static float[][] normaliseDouble(float[][] values, float ceiling) {
		float multi = ceiling / getMax(values);
		for (int i = 0; i < values.length; ++i) {
			for (int j = 0; j < values[i].length; ++j) {
				values[i][j] *= multi;
			}
		}
		return values;
	}

	/**
	 * normalise to a ceiling value *.
	 *
	 * @param values the values
	 * @param ceiling the ceiling
	 * @return the float[]
	 */
	public static float[] normalizeDouble(float[] values, float ceiling) {
		float multi = ceiling / getMax(values);
		for (int i = 0; i < values.length; ++i) {
			values[i] *= multi;
		}
		return values;
	}

	/**
	 * changes a 2d array to a long 1d array *.
	 *
	 * @param table the table
	 * @return the byte[]
	 */
	public static byte[] tableToLongRow(byte[][] table) {
		// get length
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
	
	/**
	 * changes a 2d array to a long 1d array *.
	 *
	 * @param table the table
	 * @return the long[]
	 */
	public static long[] tableToLongRow(long[][] table) {
		// get length
		int l = 0;
		for (long[] row : table) {
			l += row.length;
		}
		long[] lr = new long[l];
		int c = 0;
		for (int i = 0; i < table.length; ++i) {
			for (int j = 0; j < table[i].length; ++j) {
				lr[c] = table[i][j];
				c++;
			}
		}
		return lr;
	}
	
	/**
	 * changes a 2d array to a long 1d array *.
	 *
	 * @param table the table
	 * @return the int[]
	 */
	public static int[] tableToLongRow(int[][] table) {
		// get length
		int l = 0;
		for (int[] row : table) {
			l += row.length;
		}
		int[] lr = new int[l];
		int c = 0;
		for (int i = 0; i < table.length; ++i) {
			for (int j = 0; j < table[i].length; ++j) {
				lr[c] = table[i][j];
				c++;
			}
		}
		return lr;
	}
	
	/**
	 * changes a 2d array to a long 1d array *.
	 *
	 * @param table the table
	 * @return the char[]
	 */
	public static char[] tableToLongRow(char[][] table) {
		// get length
		int l = 0;
		for (char[] row : table) {
			l += row.length;
		}
		char[] lr = new char[l];
		int c = 0;
		for (int i = 0; i < table.length; ++i) {
			for (int j = 0; j < table[i].length; ++j) {
				lr[c] = table[i][j];
				c++;
			}
		}
		return lr;
	}

	/**
	 * changes a 2d array to a long 1d array *.
	 *
	 * @param table the table
	 * @return the string[]
	 */
	public static String[] tableToLongRow(String[][] table) {
		// get length
		int l = 0;
		for (String[] row : table) {
			l += row.length;
		}
		String[] lr = new String[l];
		int c = 0;
		for (int i = 0; i < table.length; ++i) {
			for (int j = 0; j < table[i].length; ++j) {
				lr[c] = table[i][j];
				c++;
			}
		}
		return lr;
	}

	/**
	 * changes a 2d array to a long 1d array *.
	 *
	 * @param table the table
	 * @return the double[]
	 */
	public static double[] tableToLongRow(double[][] table) {
		// get length
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

	/**
	 * Linearly approximates (interpolates)
	 * the continous value between two discrete values *.
	 *
	 * @param array the array
	 * @param distance the distance
	 * @return the double
	 */
	public static double linearApproximate(double[] array, double distance) {
		/*
		 * Obviously we can't round the array lengths so I've decided to
		 * slightly extend the new one by one sample. Probably better than
		 * reducing the information
		 */
		if ((int) distance >= array.length - 1) {
			return array[array.length - 1];
		} else {
			int r = (int) distance;
			double rd = array[r];
			double ru = array[r + 1];
			double f = distance - r;
			return rd + (ru - rd) * f;
		}
	}

	/**
	 * Get the average of a particular subset of an array *.
	 *
	 * @param array the array
	 * @param from the from
	 * @param to the to
	 * @return the average of subset
	 */
	public static double getAverageOfSubset(double[] array, int from, int to) {
		double average = 0;
		// Log.d(ArrayStuff.arrayToString(array));
		for (int i = from; i < to; ++i) {
			average += array[i];
		}
		average /= (to - from);
		return average;
	}

	/**
	 * Copies a 2d double table *.
	 *
	 * @param table the table
	 * @return the double[][]
	 */
	public static double[][] copy(double[][] table) {
		double[][] nt = new double[table.length][];
		for (int i = 0; i < table.length; ++i) {
			double[] nr = new double[table[i].length];
			for (int j = 0; j < table[i].length; ++j) {
				nr[j] = table[i][j];
			}
			nt[i] = nr;
		}
		return nt;
	}
	
	/**
	 * Copies a 2d float table *.
	 *
	 * @param table the table
	 * @return the float[][]
	 */
	public static float[][] copy(float[][] table) {
		float[][] nt = new float[table.length][];
		for (int i = 0; i < table.length; ++i) {
			float[] nr = new float[table[i].length];
			for (int j = 0; j < table[i].length; ++j) {
				nr[j] = table[i][j];
			}
			nt[i] = nr;
		}
		return nt;
	}
	
	/**
	 * Copies a 2d long table *.
	 *
	 * @param table the table
	 * @return the long[][]
	 */
	public static long[][] copy(long[][] table) {
		long[][] nt = new long[table.length][];
		for (int i = 0; i < table.length; ++i) {
			long[] nr = new long[table[i].length];
			for (int j = 0; j < table[i].length; ++j) {
				nr[j] = table[i][j];
			}
			nt[i] = nr;
		}
		return nt;
	}
	
	/**
	 * Copies a 2d int table *.
	 *
	 * @param table the table
	 * @return the int[][]
	 */
	public static int[][] copy(int[][] table) {
		int[][] nt = new int[table.length][];
		for (int i = 0; i < table.length; ++i) {
			int[] nr = new int[table[i].length];
			for (int j = 0; j < table[i].length; ++j) {
				nr[j] = table[i][j];
			}
			nt[i] = nr;
		}
		return nt;
	}
	
	/**
	 * Copies a 2d char table *.
	 *
	 * @param table the table
	 * @return the char[][]
	 */
	public static char[][] copy(char[][] table) {
		char[][] nt = new char[table.length][];
		for (int i = 0; i < table.length; ++i) {
			char[] nr = new char[table[i].length];
			for (int j = 0; j < table[i].length; ++j) {
				nr[j] = table[i][j];
			}
			nt[i] = nr;
		}
		return nt;
	}
	
	/**
	 * Copies a 2d byte table *.
	 *
	 * @param table the table
	 * @return the byte[][]
	 */
	public static byte[][] copy(byte[][] table) {
		byte[][] nt = new byte[table.length][];
		for (int i = 0; i < table.length; ++i) {
			byte[] nr = new byte[table[i].length];
			for (int j = 0; j < table[i].length; ++j) {
				nr[j] = table[i][j];
			}
			nt[i] = nr;
		}
		return nt;
	}

	/**
	 * Generates one long array of all input arrays *.
	 *
	 * @param bytes the bytes
	 * @return the byte[]
	 */
	public static byte[] concat(byte[]... bytes) {
		// get length
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
	
	/**
	 * Generates one long array of all input arrays *.
	 *
	 * @param doubles the doubles
	 * @return the double[]
	 */
	public static double[] concat(double[]... doubles) {
		// get length
		int length = 0;
		for (int i = 0; i < doubles.length; ++i) {
			length += doubles[i].length;
		}
		double[] newBytes = new double[length];
		int c = 0;
		for (int i = 0; i < doubles.length; ++i) {
			for (int j = 0; j < doubles[i].length; ++j) {
				newBytes[c] = doubles[i][j];
				c++;
			}
		}
		return newBytes;
	}
	
	/**
	 * Generates one long array of all input arrays *.
	 *
	 * @param floats the floats
	 * @return the float[]
	 */
	public static float[] concat(float[]... floats) {
		// get length
		int length = 0;
		for (int i = 0; i < floats.length; ++i) {
			length += floats[i].length;
		}
		float[] newBytes = new float[length];
		int c = 0;
		for (int i = 0; i < floats.length; ++i) {
			for (int j = 0; j < floats[i].length; ++j) {
				newBytes[c] = floats[i][j];
				c++;
			}
		}
		return newBytes;
	}
	
	/**
	 * Generates one long array of all input arrays *.
	 *
	 * @param longs the longs
	 * @return the long[]
	 */
	public static long[] concat(long[]... longs) {
		// get length
		int length = 0;
		for (int i = 0; i < longs.length; ++i) {
			length += longs[i].length;
		}
		long[] newBytes = new long[length];
		int c = 0;
		for (int i = 0; i < longs.length; ++i) {
			for (int j = 0; j < longs[i].length; ++j) {
				newBytes[c] = longs[i][j];
				c++;
			}
		}
		return newBytes;
	}
	
	/**
	 * Generates one long array of all input arrays *.
	 *
	 * @param ints the ints
	 * @return the int[]
	 */
	public static int[] concat(int[]... ints) {
		// get length
		int length = 0;
		for (int i = 0; i < ints.length; ++i) {
			length += ints[i].length;
		}
		int[] newBytes = new int[length];
		int c = 0;
		for (int i = 0; i < ints.length; ++i) {
			for (int j = 0; j < ints[i].length; ++j) {
				newBytes[c] = ints[i][j];
				c++;
			}
		}
		return newBytes;
	}
	
	/**
	 * Generates one long array of all input arrays *.
	 *
	 * @param chars the chars
	 * @return the char[]
	 */
	public static char[] concat(char[]... chars) {
		// get length
		int length = 0;
		for (int i = 0; i < chars.length; ++i) {
			length += chars[i].length;
		}
		char[] newBytes = new char[length];
		int c = 0;
		for (int i = 0; i < chars.length; ++i) {
			for (int j = 0; j < chars[i].length; ++j) {
				newBytes[c] = chars[i][j];
				c++;
			}
		}
		return newBytes;
	}

	/**
	 * Represent 2d array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(double[][] dd) {
		StringBuilder sb = new StringBuilder(dd.length * dd[0].length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			for (int j = 0; j < dd[i].length; ++j) {
				sb.append(Statistics.round(dd[i][j], 4));
				if (j != dd[i].length - 1) {
					sb.append(",");
				}

			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Represent 2d array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(float[][] dd) {
		StringBuilder sb = new StringBuilder(dd.length * dd[0].length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			for (int j = 0; j < dd[i].length; ++j) {
				sb.append(Statistics.round(dd[i][j], 4));
				if (j != dd[i].length - 1) {
					sb.append(",");
				}

			}
			sb.append("\n");
		}
		return sb.toString();
	}
	

	/**
	 * Represent 2d array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(String[][] dd) {
		StringBuilder sb = new StringBuilder(dd.length * dd[0].length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			for (int j = 0; j < dd[i].length; ++j) {
				sb.append(dd[i][j]);
				if (j != dd[i].length - 1) {
					sb.append(",");
				}

			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Represent 2d array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(String[] dd) {
		StringBuilder sb = new StringBuilder(dd.length * dd.length * 15);
		for (int i = 0; i < dd.length; ++i) {
			sb.append(dd[i]);
			sb.append(",");
		}
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Represent array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(double[] dd) {
		StringBuilder sb = new StringBuilder(dd.length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			sb.append(dd[i]);
			if (i != dd.length - 1) {
				sb.append(",");
			}
			// sb.append("}");
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Represent 2d array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(int[][] dd) {
		StringBuilder sb = new StringBuilder(dd.length * dd[0].length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			for (int j = 0; j < dd[i].length; ++j) {
				sb.append(dd[i][j]);
				if (j != dd[i].length - 1) {
					sb.append(",");
				}

			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Represent array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(int[] dd) {
		StringBuilder sb = new StringBuilder(dd.length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			sb.append(dd[i]);
			if (i != dd.length - 1) {
				sb.append(",");
			}
			// sb.append("}");
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Represent 2d array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(byte[][] dd) {
		StringBuilder sb = new StringBuilder(dd.length * dd[0].length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			for (int j = 0; j < dd[i].length; ++j) {
				sb.append(dd[i][j]);
				if (j != dd[i].length - 1) {
					sb.append(",");
				}

			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Represent array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(byte[] dd) {
		StringBuilder sb = new StringBuilder(dd.length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			sb.append(dd[i]);
			if (i != dd.length - 1) {
				sb.append(",");
			}
			// sb.append("}");
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Represent 2d array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(char[][] dd) {
		StringBuilder sb = new StringBuilder(dd.length * dd[0].length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			for (int j = 0; j < dd[i].length; ++j) {
				sb.append(dd[i][j]);
				if (j != dd[i].length - 1) {
					sb.append(",");
				}

			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Represent array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(char[] dd) {
		StringBuilder sb = new StringBuilder(dd.length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			sb.append(dd[i]);
			if (i != dd.length - 1) {
				sb.append(",");
			}
			// sb.append("}");
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Represent 2d array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(long[][] dd) {
		StringBuilder sb = new StringBuilder(dd.length * dd[0].length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			for (int j = 0; j < dd[i].length; ++j) {
				sb.append(dd[i][j]);
				if (j != dd[i].length - 1) {
					sb.append(",");
				}

			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Represent array as String *.
	 *
	 * @param dd the dd
	 * @return the string
	 */
	public static String toString(long[] dd) {
		StringBuilder sb = new StringBuilder(dd.length * 15);
		for (int i = 0; i < dd.length; ++i) {
			// sb.append("{");
			sb.append(dd[i]);
			if (i != dd.length - 1) {
				sb.append(",");
			}
			// sb.append("}");
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Reverse.
	 *
	 * @param strings the strings
	 * @return the string[]
	 */
	public static String[] reverse(String[] strings) {
		String[] ns = new String[strings.length];
		for (int i = 0; i < strings.length; ++i) {
			ns[i] = strings[strings.length - 1 - i];
		}
		return ns;
	}

}
