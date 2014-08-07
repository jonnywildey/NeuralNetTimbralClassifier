package waveProcess;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.PatternSyntaxException;

import javax.naming.InvalidNameException;

import neuralNet.NNUtilities;
import neuralNet.Pattern;
import neuralNet.RunNetwork;
import neuralNet.WavePattern;
import neuralNet.WavePatterns;
import filemanager.ArrayMethods;
import filemanager.CSVString;
import filemanager.HexByte;
import filemanager.Log;
import filemanager.Serialize;
import riff.*;
import waveAnalysis.FFTBox;
import waveAnalysis.FrameFFT;
import waveAnalysis.SampleRateException;

/** Class for transforming samples and preparing them for neural net **/
public class Samples {
	
	/** Performs batch processing of all the audio files in a directory **/
	public static void batchFolder(File sampleDir, File newDir) {
		String instrument = sampleDir.getName();
		File[] files = Serialize.getActualFiles(sampleDir);
		Random pitchRand = new Random();
		Random noiseRand = new Random();
		Random hpRand = new Random();
		Random lpRand = new Random();
		
		for (File nf: files) {
			for (int i = 0; i < 15; ++i) {	
				Wave nw = new Wave(nf);
				Signal s1 = nw.getSignals();
				s1 = processSignalChain(pitchRand, noiseRand, hpRand, lpRand,
						s1);
				Wave newWave = addMetaData(s1, "IAS8", instrument);			
				newWave.writeFile(new File(newDir.getAbsolutePath() + 
						"/" + instrument + i + nf.getName()));
			}
		}
	}


	/** Performs the audio processing of a file **/
	public static Signal processSignalChain(Random pitchRand,
			Random noiseRand, Random hpRand, Random lpRand, Signal s1) {
		s1 = Gain.getMid(s1);
		s1 = randomPitch(s1, 12, pitchRand);
		s1 = Gain.volume(s1, -6);
		s1 = addNoise(s1, noiseRand);
		s1 = randomHP(s1, 0.1, hpRand);
		s1 = randomLP(s1, 0.1, lpRand);
		s1 = EQFilter.highPassFilter(s1, 40, 0.72);
		s1 = EQFilter.lowPassFilter(s1, 20000, 0.72);
		s1 = Gain.normalise(s1);
		return s1;
	}
	
	
	
	/**Applies a random pitch change to the file **/
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
	
	
	/**chance is a percentage. Normally 0.1 **/
	public static Signal randomHP(Signal s, double chance, Random r) {
		double freq = r.nextDouble() * 130 + 100;
		if (r.nextDouble() > chance) {
			s = EQFilter.highPassFilter(s, freq, 0.72);
		}
		return s;
	}
	
	/**chance is a percentage. Normally 0.1 **/
	public static Signal randomLP(Signal s, double chance, Random r) {
		double freq = 10000 - r.nextDouble() * 6000;
		if (r.nextDouble() > chance) {
			s = EQFilter.lowPassFilter(s, freq, 0.72);
		}
		return s;
	}
	
	
	
	/** Get Waves **/
	public static Wave[] getWavs(File dir) {
		File[] files = Serialize.getActualFiles(dir);
		Wave[] waves = new Wave[files.length];
		for (int i = 0; i < files.length; ++i) {
			waves[i] = new Wave(files[i]);
		}
		return waves;
	}
	
	/** Adds random noise to a signal **/
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
	
	/** Adds meta data to a Wave file **/
	public static Wave addMetaData(Signal signal, String type, String input) {
		Chunk fftChunk = getFFTData(signal);
		Chunk metaChunk = new Chunk();
		Chunk artist = new Chunk();
		InfoChunk infoChunk = new InfoChunk();
		Wave wav = new Wave();
		wav.setSignal(signal);
		try {
			artist.setName("IART");
			artist.setData("Jonny Wildey");
			metaChunk.setName(type);
			metaChunk.setData(input);
			infoChunk.addChunk(artist);
			infoChunk.addChunk(metaChunk);
			infoChunk.addChunk(fftChunk);
			wav.addChunk(infoChunk);
			//Log.d(infoChunk.toStringRecursive());
		} catch (InvalidNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wav;
	}
	
	
	/** Performs FFT analysis and attaches to a data chunk **/
	public static Chunk getFFTData(Signal s) {
		//4096 works well
		try {
			FrameFFT fft = new FrameFFT(s, 4096);
			FFTBox dd = fft.analyse(20, 20000);
			//fft.makeGraph();
			//dd = FrameFFT.getExponentTable(dd, 0.78); //rate
			dd = FFTBox.getSumTable(dd, 10);
			dd = FFTBox.getBarkedSubset(dd);
			//Log.d(ArrayStuff.arrayToString(dd));
			dd = FFTBox.normaliseTable(dd, 10);
			//dd = FrameFFT.convertTableToDecibels(fft.signal, dd, fft.frameSize);
			String str = ArrayMethods.toString(dd.getTable());
			//Log.d(str);
			Chunk chunk = new Chunk();
			chunk.setName("IAS7");
			chunk.setData(str);
			return chunk;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	public static void main(String[] args) {
		Log.setFilePath(new File("/Users/Jonny/Documents/Timbre/Logs/WaveCreate.log"));
		File[] batchDirs = {new File("/Users/Jonny/Documents/Timbre/Samples/Cello"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Harp"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Marimba"),
				new File("/Users/Jonny/Documents/Timbre/Samples/Trombone")};
		File batchFolder = new File("/Users/Jonny/Documents/Timbre/Samples/Batch");
		File combineDir = new File("/Volumes/Rickay/Timbre/Combine");
		//batchFromFolders(batchFolder, batchFiles); 
		//batchCombine(combineDir, batchDirs);
		File waveSerial = new File("/Users/Jonny/Documents/Timbre/WaveCombLoadsBarkPatterns.ser");
		WavePatterns wavePatterns = regeneratePatterns(combineDir, waveSerial);
		RunNetwork.main(new String[]{""});
		
	}
	


	/**Batch process the given files to the batch folder **/
	public static void batchFromFolders(File batch, File...files) {
		for (File f : files) {
			batchFolder(f, batch);
		}
	}
	
	/** processes and then mixes 2 audio files **/
	public static void batchCombine(File dirToWriteTo, File...dirs) {
		Random pitchRand = new Random();
		Random noiseRand = new Random();
		Random hpRand = new Random();
		Random lpRand = new Random();
		//get patterns from each of the dirs
		String[] instruments = new String[dirs.length];
		for (int i = 0; i < dirs.length; ++i) {
			instruments[i] = dirs[i].getName();
		}
		//start combining
		Wave wWave = null;
		Wave nWave = null;
		for (int i = 0; i < dirs.length; ++i) {
			//get all audio files in directory
			File[] wFiles = Serialize.getActualFiles(dirs[i]); 
			for (int j = 0; j < wFiles.length; ++j) {
				wWave = new Wave(wFiles[j]);
				for (int k = i; k < dirs.length; ++k) {
					File[] nFiles = Serialize.getActualFiles(dirs[k]); 
					for (int l = 0; l < nFiles.length; ++l) {
						nWave = new Wave(nFiles[l]);
						//Modify and combine
						combineWaves(pitchRand, noiseRand, hpRand, lpRand, 
								dirToWriteTo, wWave, nWave);
					}
				}
			}
		}
	}
	

	/**Combines and normalises waves from files **/
	public static void combineWaves(Random pitchRand,
			Random noiseRand, Random hpRand, Random lpRand, File dir, Wave...waves) {
		Signal[] signals = new Signal[waves.length];
		String newName = "";
		String instrument = "";
		String wName = "";
		Wave wave = null;
		for (int i = 0; i < waves.length; ++i) {
			//get signals
			wave = waves[i];
			signals[i] = wave.getSignals();
			signals[i] = processSignalChain(pitchRand,
					noiseRand, hpRand, lpRand, signals[i]);
			//get name of file
			wName = waves[i].getFilePath().getParentFile().getName();
			newName = renameCombine(newName, waves[i]);
			//get instrument name
			if (!wName.equals(instrument)) { //don't want cellocello
				instrument = instrument.concat(wName);
			}
		}
		sumAndWrite(dir, signals, newName, instrument);
	}

	/** Renames a combined file in a logical way **/
	private static String renameCombine(String str, Wave wave) {
		if (str.length() == 0 | str == null) {
			str = wave.getFilePath().getName();
		} else {
			str = str.substring(0, str.length() - 4).concat
					(wave.getFilePath().getName());
		}
		return str;
	}



	/**sum a wave, write metadata and write signals **/
	public static void sumAndWrite(File dir, Signal[] signals, String newName,
			String instrument) {
		Signal comb = null;
		//write the file
		try {
			comb = Gain.sumMono(signals);
			comb = Gain.normalise(comb);
			Wave newWave = addMetaData(comb, "IAS8", instrument);
			newWave.writeFile(new File(dir.getAbsolutePath() + "/" + newName));
		} catch (SampleRateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/** generates and serialises and return wave patterns **/
	public static WavePatterns generatePatterns(File batchFolder, File fileOut) {
		WavePatterns wp = new WavePatterns(batchFolder);
		wp.wavMetaToPattern();
		Log.d(wp.toString());
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}
	
	/** generates and serialises and return wave patterns **/
	public static WavePatterns regeneratePatterns(File batchFolder, File fileOut) {
		WavePatterns wp = new WavePatterns(batchFolder);
		wp.wavReFFTPattern();
		Log.d(wp.toString());
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}
	
	
	
}
