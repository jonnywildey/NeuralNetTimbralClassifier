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

}
