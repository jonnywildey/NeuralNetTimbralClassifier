package com.neuralNet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.DSP.waveProcess.filters.MultiLayerNet;
import com.neuralNet.matrix.ConfusionMatrix;
import com.neuralNet.pattern.TestPatterns;

public class MultiNNUtilities {

	public MultiNNUtilities() {
		// TODO Auto-generated constructor stub
	}

	public static void testCommittee(TestPatterns testPatterns,
			Committee committee) {
		ConfusionMatrix cm = committee.testPatterns(testPatterns.getTrainingPatterns());
		cm.makeGraph();
		cm = committee.testPatterns(testPatterns.getValidationPatterns());
		cm.makeGraph();
		cm = committee.testPatterns(testPatterns.getTestingPatterns());
		cm.makeGraph();
	}

	public static void runMultipleNeuron(TestPatterns testPatterns,
			int runCount, boolean verbose) {
		ManyNets mN1 = new ManyNets(null, runCount, 1, testPatterns, verbose);
		mN1.even = true;
		ManyNets mN2 = new ManyNets(null, runCount, 2, testPatterns, verbose);
		mN2.even = false;
		ManyNets[] mn = new ManyNets[]{mN1, mN2};
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		// define your jobs somehow
		for (ManyNets job : mn) {
		    // under the covers this creates a FutureTask instance
		    Future future = threadPool.submit(job);
		    // save the future if necessary in a collection or something
		}
		// once we have submitted all jobs to the thread pool, it should be shutdown
		threadPool.shutdown();
	}

	public static MultiLayerNet[] runPreTrain(TestPatterns pretrain,TestPatterns posttrain,
			int count, boolean verbose) {
		PretrainThreadNet[] mn = new PretrainThreadNet[count];
		for (int i = 0; i < count; ++i) {
			mn[i] = new PretrainThreadNet(null, pretrain, 
					posttrain,
					200, 30, 40, i, 
					true, verbose);
		}
		ArrayList<MultiLayerNet> aNets = MultiNNUtilities.runCallableThreads(
				count, mn, MultiLayerNet.class);
		return Committee.MultiLayerListToArray(aNets);
	}

	protected static MultiLayerNet[] runPreTrainGradual(TestPatterns pretrain,TestPatterns posttrain,
			int count, boolean verbose) {
		PretrainGradualThreadNet[] mn = new PretrainGradualThreadNet[count];
		for (int i = 0; i < count; ++i) {
			mn[i] = new PretrainGradualThreadNet(null, pretrain, 
					posttrain,
					100, 100, 50, i, 
					verbose);
		}
		ArrayList<MultiLayerNet> aNets = MultiNNUtilities.runCallableThreads(
				count, mn, MultiLayerNet.class);
		return Committee.MultiLayerListToArray(aNets);
	}

	public static <T,V extends Callable<T>> ArrayList<T> runCallableThreads(int count,
			V[] thingsToRun, Class<T> type) {
		ExecutorService threadPool = Executors.newFixedThreadPool(count);
		// define your jobs somehow
		ArrayList<T> nets = new ArrayList<T>(thingsToRun.length);
		Set<Future<T>> futures = new HashSet<Future<T>>
											 (thingsToRun.length);
		for (int i = 0; i < count; ++i) {
		    Future<T> f = threadPool.submit(thingsToRun[i]);
		    futures.add(f);
		}
		threadPool.shutdown();
		for (Future<T> f : futures) {
			try {
				nets.add(f.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		threadPool.shutdown();
		return nets;
	}

	/** MultiThreaded committee generation. generates a thread for every single run count, 
	 * so be careful with very large committees **/
	public static Committee createCommittee(TestPatterns testPatterns, 
			int runCount, int hiddenLayer, int epoch,
			boolean verbose) {
		Committee committee = new Committee();
		ThreadNet[] threadNets = MultiNNUtilities.generateThreadNets(runCount, testPatterns, hiddenLayer, epoch, verbose);
		ArrayList<MultiLayerNet> aNets = runCallableThreads(
				runCount, threadNets, MultiLayerNet.class);
		committee.setNets(Committee.MultiLayerListToArray(aNets));
		return committee;
	}

	public static ManyNets[] generateManyNets(TestPatterns testPatterns,
			int runCount, boolean verbose, File writePath) {
		ManyNets[] manyNets = new ManyNets[runCount];
		File name = null;
		for (int i = 0; i < runCount; ++i) {
			name = new File(writePath.getAbsolutePath() + i + ".csv");
			manyNets[i] = new ManyNets(name, runCount, i, testPatterns, verbose);
		}
		return manyNets;
	}

	public static ThreadNet[] generateThreadNets(int runCount, TestPatterns testPatterns,
			int hiddenLayer, int epoch, boolean verbose) {
		ThreadNet[] manyNets = new ThreadNet[runCount];
		for (int i = 0; i < runCount; ++i) {
			manyNets[i] = new ThreadNet(null, testPatterns, 
					hiddenLayer, epoch, i, verbose);
		}
		return manyNets;
	}

	/**Create Many Nets **/
	public static Set<ManyNets> generateManyNetsSet(TestPatterns testPatterns,
			int runCount, boolean verbose, File writePath) {
		Set<ManyNets> manyNets = new HashSet<ManyNets>(runCount);
		File name = null;
		for (int i = 0; i < runCount; ++i) {
			name = new File(writePath.getAbsolutePath() + i + ".csv");
			manyNets.add( new ManyNets(name, runCount, i, testPatterns, verbose));
		}
		return manyNets;
	}

}
