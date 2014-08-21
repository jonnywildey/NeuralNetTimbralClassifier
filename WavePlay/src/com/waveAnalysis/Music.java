package com.waveAnalysis;

import com.filemanager.Log;

public class Music {
	
	/**Music and music theory based utility methods**/
	public Music() {
		// TODO Auto-generated constructor stub
	}
	
	public static double middleAMIDI = 69;
	public static double middleA = 440;
	public static String[] notes = new String[]{"C","C#","D","Eb","E","F","F#","G","G#/Ab","A","Bb","B"};

	/**converts hz to fractional midi number */
	public static double hzToMidi(double hz) {
		double n = (Math.log(hz / middleA) / Math.log(2))* 12;
		return n + middleAMIDI;
	}

	/**converts hz to rounded int midi number */
	public static int hzToMidiInt(double hz) {
		return (int) Math.rint(hzToMidi(hz));
	}

	/** Converts a frequency to a pitch **/
	public static String hzToPitch(double hz) {
		int n = hzToMidiInt(hz);
		String str = notes[n % 12] + String.valueOf((n / 12) - 2) ;
		return str;
	}
	
	
	/** returns the semitone interval between two frequencies **/
	public static double getInterval(double a, double b) {
		return (Math.log((b /a)) / Math.log(2)) * 12;
	}
	
}
