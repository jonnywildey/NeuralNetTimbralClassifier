package neuralNet;

import java.io.File;
import java.util.ArrayList;

import com.riff.Signal;
import com.waveAnalysis.FFTBox;
import com.waveAnalysis.FrameFFT;
import com.waveProcess.FFTChain;

public class WavePatternsMonoBatchRegen extends WavePatternsBatchRegen {

	public WavePatternsMonoBatchRegen() {
		super();
	}

	public WavePatternsMonoBatchRegen(File filePath) {
		super(filePath);
	}

	@Override
	protected ArrayList<InputShell> reFFT(Signal signals) {
		FFTBox dd = FFTChain.monoFFTChain(signals);
		return Pattern.doubleToInputShell(dd.getValues()[0]);
	}

}
