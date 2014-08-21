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
		WavePatternsBatchRegen wp = new WavePatternsBatchRegen(batchFolder);
		WavePatternsBatchRegen.genWaves(wp, 4, genCount);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
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
		WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}

	/** regenerates the FFT analysis for a folder. Multithreaded **/
	public static WavePatterns regeneratePatternsMono(File batchFolder, File fileOut) {
		WavePatternsMonoRegenerate wp = new WavePatternsMonoRegenerate(batchFolder);
		WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}
	
	/** regenerates the FFT analysis for a folder and rewrites metadata. 
	 * Multithreaded**/
	public static WavePatterns regenerateRewritePatternsMono(File batchFolder, File fileOut) {
		WavePatternsRegenRewrite wp = new WavePatternsRegenRewriteMono(batchFolder);
		WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}
	
	/** generates and serialises and return wave patterns **/
	public static WavePatterns regenerateAndBatchPatternsMono(File batchFolder, File fileOut, int genCount) {
		WavePatternsMonoBatchRegen wp = new WavePatternsMonoBatchRegen(batchFolder);
		WavePatternsBatchRegen.genWaves(wp, 4, genCount);
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
		WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}

	/** regenerates the FFT analysis for a folder. Multithreaded **/
	public static WavePatterns regeneratePatterns(File batchFolder, File fileOut) {
		WavePatternsRegenerate wp = new WavePatternsRegenerate(batchFolder);
		WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}
	
	/** regenerates the FFT analysis for a folder and rewrites metadata. 
	 * Multithreaded**/
	public static WavePatterns regenerateRewritePatterns(File batchFolder, File fileOut) {
		WavePatternsRegenRewrite wp = new WavePatternsRegenRewrite(batchFolder);
		WavePatterns.genWaves(wp, 4);
		Serialize.serialize(wp, fileOut.getAbsolutePath());
		Log.d("serialised!");
		return wp;
	}

}
