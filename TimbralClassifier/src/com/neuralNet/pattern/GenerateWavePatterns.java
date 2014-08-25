package com.neuralNet.pattern;

import java.io.File;

import com.util.Log;
import com.util.Serialize;

public class GenerateWavePatterns {

	public GenerateWavePatterns() {
		// TODO Auto-generated constructor stub
	}

	/** generates and serialises and return wave patterns **/
	public static WavePatterns regenerateAndBatchPatterns(File batchFolder, File fileOut, int genCount) {
		WavePatterns wp = new WavePatternsBatchRegen(batchFolder);
		wp = WavePatternsBatchRegen.genWaves(wp, 4, genCount);
		try {
		Serialize.writeJSON(wp, fileOut);
		Log.d("serialised!");
		} catch (Exception e){
			Log.d("not serialised");
		}
		return wp;
	}
	

	/** generates and serialises and return wave patterns **/
	public static WavePatterns regenerateAndBatchPatternsMono
	(WavePatternsBatchRegen wp, File fileOut, int genCount) {
		WavePatterns np = WavePatternsMonoBatchRegen.genWaves(wp, 4, genCount);
		Serialize.serialize(np, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}

	/** generates wave patterns from data in Wave, serialises wave patterns **/
	public static WavePatterns generatePatternsMono(File batchFolder, File fileOut) {
		WavePatterns wp = new WavePatternsMono(batchFolder);
		wp = WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}

	/** regenerates the FFT analysis for a folder. Multithreaded **/
	public static WavePatterns regeneratePatternsMono(File batchFolder, File fileOut) {
		WavePatterns wp = new WavePatternsMonoRegenerate(batchFolder);
		wp = WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}
	
	/** regenerates the FFT analysis for a folder and rewrites metadata. 
	 * Multithreaded**/
	public static WavePatterns regenerateRewritePatternsMono(File batchFolder, File fileOut) {
		WavePatterns wp = new WavePatternsRegenRewriteMono(batchFolder);
		wp = WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}
	
	/** generates and serialises and return wave patterns **/
	public static WavePatterns regenerateAndBatchPatternsMono(File batchFolder, File fileOut, int genCount) {
		WavePatterns wp = new WavePatternsMonoBatchRegen(batchFolder);
		wp = WavePatternsBatchRegen.genWaves(wp, 4, genCount);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}
	

	/** generates and serialises and return wave patterns **/
	public static WavePatterns regenerateAndBatchPatterns(WavePatternsBatchRegen wp, File fileOut, int genCount) {
		WavePatterns np = WavePatternsBatchRegen.genWaves(wp, 4, genCount);
		Serialize.serialize(np, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}

	/** generates wave patterns from data in Wave, serialises wave patterns **/
	public static WavePatterns generatePatterns(File batchFolder, File fileOut) {
		WavePatterns wp = new WavePatterns(batchFolder);
		wp = WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}

	/** regenerates the FFT analysis for a folder. Multithreaded **/
	public static WavePatterns regeneratePatterns(File batchFolder, File fileOut) {
		WavePatterns wp = new WavePatternsRegenerate(batchFolder);
		wp = WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}
	
	/** regenerates the FFT analysis for a folder and rewrites metadata. 
	 * Multithreaded**/
	public static WavePatterns regenerateRewritePatterns(File batchFolder, File fileOut) {
		WavePatterns wp = new WavePatternsRegenRewrite(batchFolder);
		wp = WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}

}
