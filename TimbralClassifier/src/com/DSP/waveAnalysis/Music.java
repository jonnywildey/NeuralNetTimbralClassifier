package com.DSP.waveAnalysis;

/**
 * Music and music theory based utility methods*.
 * 
 * @author Jonny Wildey
 * @version 1.0
 */
public class Music {

	public static double middleAMIDI = 69;

	public static double middleA = 440;
	public static String[] notes = new String[] { "C", "C#", "D", "Eb", "E",
		"F", "F#", "G", "G#/Ab", "A", "Bb", "B" };
	/**
	 * returns the semitone interval between two frequencies *.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the interval
	 */
	public static double getInterval(double a, double b) {
		return (Math.log((b / a)) / Math.log(2)) * 12;
	}

	/**
	 * converts hz to fractional midi number.
	 * 
	 * @param hz
	 *            the hz
	 * @return the double
	 */
	public static double hzToMidi(double hz) {
		double n = (Math.log(hz / middleA) / Math.log(2)) * 12;
		return n + middleAMIDI;
	}

	/**
	 * converts hz to rounded int midi number.
	 * 
	 * @param hz
	 *            the hz
	 * @return the int
	 */
	public static int hzToMidiInt(double hz) {
		return (int) Math.rint(hzToMidi(hz));
	}

	/**
	 * Converts a frequency to a pitch *.
	 * 
	 * @param hz
	 *            the hz
	 * @return the string
	 */
	public static String hzToPitch(double hz) {
		int n = hzToMidiInt(hz);
		String str = notes[n % 12] + String.valueOf((n / 12) - 2);
		return str;
	}

	/**
	 * Instantiates a new music.
	 */
	public Music() {
	}

}
