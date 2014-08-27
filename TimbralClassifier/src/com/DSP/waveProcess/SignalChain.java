package com.DSP.waveProcess;

import java.io.File;
import java.util.Random;

import com.riff.Signal;
import com.util.Log;

/**
 * Signal processing chains *.
 *
 * @author Jonny Wildey
 * @version 1.0
 */
public class SignalChain {

	/**
	 * Performs the audio processing of a file *.
	 *
	 * @param pitchRand the pitch rand
	 * @param noiseRand the noise rand
	 * @param hpRand the hp rand
	 * @param lpRand the lp rand
	 * @param s1 the s1
	 * @return the signal
	 */
	public static Signal processSignalChain(Random pitchRand,
			Random noiseRand, Random hpRand, Random lpRand, Signal s1) {
		s1 = Gain.getMid(s1);
		s1 = SignalChain.randomPitch(s1, 12, pitchRand);
		s1 = Gain.volume(s1, -6);
		s1 = SignalChain.addNoise(s1, noiseRand);
		s1 = SignalChain.randomHP(s1, 0.1, hpRand);
		s1 = SignalChain.randomLP(s1, 0.1, lpRand);
		s1 = EQFilter.highPassFilter(s1, 40, 0.72);
		s1 = EQFilter.lowPassFilter(s1, 20000, 0.72);
		s1 = Gain.normalise(s1);
		return s1;
	}
	
	/**
	 * get mid of signal and normalise *.
	 *
	 * @param s1 the s1
	 * @return the signal
	 */
	public static Signal midAndNormalise(Signal s1) {
		s1 = Gain.getMid(s1);
		s1 = Gain.normalise(s1);
		return s1;
	}

	/**
	 * Applies a random pitch change to the file *.
	 *
	 * @param s the s
	 * @param range the range
	 * @param r the r
	 * @return the signal
	 */
	public static Signal randomPitch(Signal s, double range, Random r) {
		double semi = (range * r.nextDouble()) - (range * 0.5);
		Signal ns = Pitch.pitchShift(s, semi);
		if (!Pitch.isFundamentalHearable(ns)) { //BAD PITCH CHANGE
			Log.d("Bad pitch change, trying again");
			return randomPitch(s, range, r);
		} else {
			return ns;
		}
	}

	/**
	 * chance is a percentage. Normally 0.1 *
	 *
	 * @param s the s
	 * @param chance the chance
	 * @param r the r
	 * @return the signal
	 */
	public static Signal randomHP(Signal s, double chance, Random r) {
		double freq = r.nextDouble() * 130 + 100;
		if (r.nextDouble() > chance) {
			s = EQFilter.highPassFilter(s, freq, 0.72);
		}
		return s;
	}

	/**
	 * chance is a percentage. Normally 0.1 *
	 *
	 * @param s the s
	 * @param chance the chance
	 * @param r the r
	 * @return the signal
	 */
	public static Signal randomLP(Signal s, double chance, Random r) {
		double freq = 10000 - r.nextDouble() * 6000;
		if (r.nextDouble() > chance) {
			s = EQFilter.lowPassFilter(s, freq, 0.72);
		}
		return s;
	}

	/**
	 * Adds random noise to a signal *.
	 *
	 * @param s the s
	 * @param nr the nr
	 * @return the signal
	 */
	public static Signal addNoise(Signal s, Random nr) {
		double nc = nr.nextDouble();
		double maxLoud = -90;
		double loudness = maxLoud - (nr.nextDouble() * 12);
		//Log.d(loudness);
		try {
			//pick which
			if (nc < 0.3333) {
				Signal s2 = Gen.pinkNoise(s.getLength(), 0, s.getSampleRate(), s.getBit());
				s2 = Gain.volumeRMS(s2, loudness);
				s = Gain.sumMono(s, s2);
			} else if(nc < 0.6666) {
				Signal s2 = Gen.whiteNoise(s.getLength(), 0, s.getSampleRate(), s.getBit());
				s2 = Gain.volumeRMS(s2, loudness);
				s = Gain.sumMono(s, s2);
			} else {
				Signal s2 = Gen.tapeHiss(s.getLength(), 0, s.getSampleRate(), s.getBit());
				s2 = Gain.volumeRMS(s2, loudness);
				s = Gain.sumMono(s, s2);
			}
		} catch (Exception e) {
			
		}
		return s;
	}

}
