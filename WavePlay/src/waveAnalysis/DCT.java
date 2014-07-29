package waveAnalysis;

import riff.Signal;
import filemanager.ArrayStuff;
import filemanager.Log;

public class DCT {
	
	public static double[] dct(double[] signal) {
		//generate new signal
		double[] ns = alterRow(signal);
		Complex[] fft = FFT.cfft(Complex.doubleToComplex(ns));
		//remove half
		//fft = Complex.getSubset(fft, 0, (fft.length / 2) - 1);
		double[] dct = newDCT(fft);
		//dct = ArrayStuff.normalizeDouble(dct, 100000000);
		return dct;
	}
	
	public static double[][] getDCTAnalysis(Signal s) {
		double[] amps = FFT.getPowerArray(s.getSignal()[0]);
		double[] freqRow = ArrayStuff.extend(DCT.getFreqRow(s, amps.length), amps.length / 2);
		return new double[][]{freqRow, dct(amps)};
	}
	
	private static double[] alterRow(double[] signal) {
		double[] ns = new double[signal.length];
		for  (int i = 0; i < signal.length / 2; ++i) {
				ns[i] = signal[2 * i];
				ns[signal.length - 1 - i] = signal[2 * i + 1];
		}
		return ns;
	}
	
	/** return the frequencies **/
	public static double[] getFreqRow(Signal s, double frameSize) {
		double sr = s.getSampleRate() / frameSize / 2;
		double[] fr = new double[(int) frameSize];
		for (int i = 0; i < frameSize; ++i) {
			fr[i] = i * sr;
		}
		return fr;
	}
	
	/** do the * e to the power part **/
	private static double[] newDCT(Complex[] fft) {
		double[] dct = new double[fft.length];
		double e = 0;
		for (int i = 0; i < fft.length; ++i) {
			e = Math.pow(Math.E, -1d * (double)i * Math.PI / (double)(fft.length * 2));
			//Log.d( + " " + fft[i].re);
			dct[i] = e  * fft[i].re;
		}
		return dct;
	}
	
	/** do the * e to the power part **/
	private static Complex[] newIDCT(double[] s) {
		Complex[] dct = new Complex[s.length];
		double e = 0;
		for (int i = 0; i < s.length; ++i) {
			e = Math.pow(Math.E, -1d * (double)i * Math.PI / (double)(s.length * 2));
			//Log.d( + " " + fft[i].re);
			dct[i] = new Complex((double)s[i] / e);
		}
		return dct;
	}
	
	private static double[] irearrange(double[] vals) {
		double[] ns = new double[vals.length];
		for (int i = 0; i < vals.length / 2; ++i) {
			ns[2 * i] = vals[i];
			ns[2 * i + 1] = vals[vals.length - 1 - i];
		}
		return ns;
	}
	
	public static double[] idct(double[] s) {
		Complex[] s2 = newIDCT(s);
		double[] s3 = Complex.getReals(FFT.icfft(s2));
		s3 = irearrange(s3);
		return s3;
	}
	
}
