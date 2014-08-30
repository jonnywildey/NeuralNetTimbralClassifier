package com.util.fileReading;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class CSVReader.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class CSVReader {
	/* Took some stuff from Graham Roberts */

	private String filename = "";
	private BufferedReader reader = null;
	private boolean eof = false;
	private char separate = ',';
	private char except = '\"';
	private List<List<String>> bufferArray = new ArrayList<List<String>>();
	/*
	 * So I had this totally sweet idea of how to determine what format we want
	 * the data in. We use an INDEX!!!! 0 is string, 1 is int, 2 is double, 3 is
	 * float
	 */
	private int typeIndex;
	private boolean fileRead = false;

	/**
	 * Instantiates a new cSV reader.
	 * 
	 * @param fname
	 *            the fname
	 */
	public CSVReader(String fname) {
		filename = fname;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			error("Can't open file: " + filename);
		}

	}

	/* explicit separator constructor */
	/**
	 * Instantiates a new cSV reader.
	 * 
	 * @param fname
	 *            the fname
	 * @param separate
	 *            the separate
	 */
	public CSVReader(String fname, char separate) {
		this(fname);
		this.separate = separate;
		typeIndex = 0;
	}

	/**
	 * Close.
	 */
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			error("Can't close file: " + filename);
		}
	}

	/**
	 * Eof.
	 * 
	 * @return true, if successful
	 */
	public boolean eof() {
		return eof;
	}

	/**
	 * Error.
	 * 
	 * @param msg
	 *            the msg
	 */
	private void error(String msg) {
		System.out.println(msg);
		System.out.println("Unable to continue executing program.");
		System.exit(0);
	}

	/**
	 * Gets the new line.
	 * 
	 * @return the new line
	 */
	public String getNewLine() {
		String ss = "";
		try {
			ss = reader.readLine();
			if (ss == null) {
				eof = true;
			}
			return ss;
		} catch (IOException e) {
			error("Couldn't read line");
			return null;
		}

	}

	/**
	 * Make double array.
	 * 
	 * @return the double[][]
	 */
	public double[][] makeDoubleArray() {
		/* Converts the bufferArray to an double array */

		if (fileRead == false) {
			error("Have not read file yet, cannot convert to double. Exiting...");
		}

		double[][] doubleList = new double[bufferArray.size()][];
		// loop through array making new double array
		for (int i = 0; i < bufferArray.size(); ++i) {
			double[] rArray = new double[bufferArray.get(i).size()];
			for (int j = 0; j < bufferArray.get(i).size(); ++j) {
				rArray[j] = stringToDouble(bufferArray.get(i).get(j));
			}
			doubleList[i] = rArray;
		}

		return doubleList;
	}

	/**
	 * Make float array.
	 * 
	 * @return the float[][]
	 */
	public float[][] makeFloatArray() {
		/* Converts the bufferArray to an float array */

		if (fileRead == false) {
			error("Have not read file yet, cannot convert to float. Exiting...");
		}

		float[][] floatList = new float[bufferArray.size()][];
		// loop through array making new float array
		for (int i = 0; i < bufferArray.size(); ++i) {
			float[] rArray = new float[bufferArray.get(i).size()];
			for (int j = 0; j < bufferArray.get(i).size(); ++j) {
				rArray[j] = stringToFloat(bufferArray.get(i).get(j));
			}
			floatList[i] = rArray;
		}

		return floatList;
	}

	/**
	 * Make int array.
	 * 
	 * @return the int[][]
	 */
	public int[][] makeIntArray() {
		/* Converts the bufferArray to an int array */

		if (fileRead == false) {
			error("Have not read file yet, cannot convert to int. Exiting...");
		}

		int[][] intList = new int[bufferArray.size()][];
		// loop through array making new int array
		for (int i = 0; i < bufferArray.size(); ++i) {
			int[] rArray = new int[bufferArray.get(i).size()];
			for (int j = 0; j < bufferArray.get(i).size(); ++j) {
				rArray[j] = stringToInt(bufferArray.get(i).get(j));
			}
			intList[i] = rArray;
		}

		return intList;
	}

	/**
	 * Make string array.
	 * 
	 * @return the string[][]
	 */
	public String[][] makeStringArray() {
		/* Converts the bufferArray to an double array */

		if (fileRead == false) {
			error("Have not read file yet, cannot convert to double. Exiting...");
		}

		String[][] stringArray = new String[bufferArray.size()][];
		for (int i = 0; i < bufferArray.size(); ++i) {
			stringArray[i] = bufferArray.get(i).toArray(
					new String[bufferArray.get(i).size()]);
		}

		return stringArray;
	}

	/**
	 * Read file.
	 */
	public synchronized void readFile() {
		/*
		 * reads a file, separating by commas etc outputting as an array of
		 * stuff
		 */
		ArrayList<String> sArray = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		String ss = "";
		char sc = ' ';
		int currentPoint = 0;
		ss = getNewLine();
		while (eof() != true) {

			// Read line

			for (int i = 0; i < ss.length(); ++i) {
				sc = ss.charAt(i);

				// if char is a separator
				if (sc == (separate)) {

					sArray.add(sb.toString());
					sb.delete(0, sb.length());
				}
				// char is an except (normally quotes)
				else if (sc == except) {
					// I would avoid looking at this method if possible. Is
					// horrible
					CSVValue quoteVal = readQuotes(ss.substring(i));
					// Add to array
					sArray.add(quoteVal.getCSVString());
					// moves looper to correct place (hopefully)
					i += quoteVal.getOriginalLength();

				}
				// any other character
				else {
					sb.append(String.valueOf(sc));
				}

			}
			// end of line, add some of array to line
			// empties sb array
			if (sb.length() > 0) {
				sArray.add(sb.toString());
				sb.delete(0, sb.length());
			}
			bufferArray.add(new ArrayList<String>(sArray.subList(currentPoint,
					sArray.size())));
			// System.out.println(sArray.toString());
			currentPoint = sArray.size();
			// get new line. end of line loop
			ss = getNewLine();
		}
		// end of doc. print array and exit

		// System.out.print(bufferArray.toString());
		fileRead = true;
		this.close();

	}

	/**
	 * Read quotes.
	 * 
	 * @param ss
	 *            the ss
	 * @return the cSV value
	 */
	private CSVValue readQuotes(String ss) {
		/* the truly horrible method for dealing with quotes */
		char sc = ' ';
		// quote count. measures how many quotes there are joined together (eg
		// """""" = 5)
		int qcount = 0;
		// Basically determines whether we've found the end of the value
		boolean getout = false;
		// counter
		int counter = 0;
		// our parsed string buffer
		StringBuffer vb = new StringBuffer();

		// loops through whole line.
		for (int j = 0; j < ss.length(); ++j) {
			// nothing happens if getout = true...
			if (getout == false) {
				// add the counter

				sc = ss.charAt(j);

				if (sc == except) {
					++qcount;
				} else if (sc == separate) {
					if (qcount % 2 == 1) {
						getout = true;
						for (int i = 0; i < (qcount - 1) / 2; ++i) {
							vb.append(except);
						}
						qcount = 0;
						// vb.append(separate);
					} else {
						if (qcount == 0) {
							vb.append(separate);
						} else {
							for (int i = 0; i < qcount / 2; ++i) {
								vb.append(except);
							}
							qcount = 0;
						}
					}
				}
				// if qcount > 0 we have reached the end of a bunch of quotes
				// and need to write them
				else if (qcount > 0) {
					for (int i = 0; i < qcount / 2; ++i) {
						vb.append(except);
					}
					qcount = 0;
					vb.append(sc);
				} else {
					vb.append(sc);
					qcount = 0;
				}
				// increase counter
				++counter;

			}
		}
		// END of LOOPS

		// in case we have a remaining qcount at end of line

		if (qcount > 0) {
			for (int i = 0; i < (qcount - 1) / 2; ++i) {
				vb.append(except);
			}
		}

		// make csvObject with value and counter length
		return new CSVValue(vb, counter - 1);

	}

	/**
	 * Return raw list.
	 * 
	 * @return the list
	 */
	public List<List<String>> returnRawList() {
		return bufferArray;
	}

	/**
	 * Sets the double.
	 */
	public void setDouble() {
		typeIndex = 2;
	}

	/**
	 * Sets the float.
	 */
	public void setFloat() {
		typeIndex = 3;
	}

	/**
	 * Sets the int.
	 */
	public void setInt() {
		typeIndex = 1;
	}

	/**
	 * Sets the string.
	 */
	public void setString() {
		typeIndex = 0;
	}

	/**
	 * String to csv.
	 * 
	 * @param str
	 *            the str
	 */
	private void stringToCSV(String str) {

	}

	/**
	 * String to double.
	 * 
	 * @param str
	 *            the str
	 * @return the double
	 */
	private double stringToDouble(String str) {
		try {
			str = str.trim();
			if (str.isEmpty() == true) {
				return (double) 0;
			} else {
				return Double.parseDouble(str);
			}

		} catch (Exception e) {
			error("Couldn't convert to number");
			return (double) 0;
		}
	}

	/**
	 * String to float.
	 * 
	 * @param str
	 *            the str
	 * @return the float
	 */
	private float stringToFloat(String str) {
		try {
			str = str.trim();
			if (str.isEmpty() == true) {
				return (float) 0;
			} else {
				return Float.parseFloat(str);
			}
		} catch (Exception e) {
			error("Couldn't convert to number");
			return (float) 0;
		}
	}

	/**
	 * String to int.
	 * 
	 * @param str
	 *            the str
	 * @return the int
	 */
	private int stringToInt(String str) {
		try {
			str = str.trim();
			if (str.isEmpty() == true) {
				return 0;
			} else {
				return Integer.parseInt(str);
			}
		} catch (Exception e) {
			error("Couldn't convert " + str + " to int number");
			return 0;
		}
	}

}
