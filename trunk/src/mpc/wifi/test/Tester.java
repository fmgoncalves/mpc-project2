package mpc.wifi.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mpc.wifi.lib.Pair;
import mpc.wifi.lib.SignalStrength;
import mpc.wifi.lib.analysis.AvgErrorAnalysis;
import mpc.wifi.lib.analysis.BestSampleLocationPicker;
import mpc.wifi.lib.analysis.BestXLocationPicker;
import mpc.wifi.lib.analysis.EuclidianDistance;
import mpc.wifi.lib.analysis.LocationPicker;
import mpc.wifi.lib.analysis.ManhattanDistance;
import mpc.wifi.lib.db.DatabaseConnection;
import mpc.wifi.lib.db.DatabaseError;


public class Tester {

	
	private static final int TOTAL_TRIES = 40;

	public static void main(String[] args) throws DatabaseError {
		
		System.out.println("Loading test data");
		
		//prepare data
		DatabaseConnection dbc = new DatabaseConnection();
		
		Map<Integer, Pair<String, List<SignalStrength>>> samples = dbc.loadSamples();
		
		Random random = new Random();
		
		
		euclidianTest(samples, random);
		ManhattanTest(samples, random);
		AvgErrorTest(samples, random);
		
		euclidianTestX(samples, random);
		ManhattanTestX(samples, random);
		AvgErrorTestX(samples, random);
	}

	/**
	 * @param samples
	 * @param random
	 * @throws DatabaseError
	 */
	private static void euclidianTest(
			Map<Integer, Pair<String, List<SignalStrength>>> samples,
			Random random) throws DatabaseError {
		//test parameters
		int total_tries = TOTAL_TRIES;
		int correct_tries = 0;
		
		//stores frequency of mismatch for each failed sample
		Map<String,Integer> n_incorrect = new HashMap<String, Integer>();
		
//		//stores which results are obtained when wrong. format <Correct, <Incorrect, Freq>>
//		Map<String, Map<String, Integer>> q_incorrect = new HashMap<String, Map<String,Integer>>();
		
		
		//begin test
		System.out.println("Starting test execution (Euclidian Distance)");
		
		for (int i = 0; i < total_tries; i++) {
			int rint = random.nextInt(samples.size());
			int id = (Integer)samples.keySet().toArray()[rint];
			Pair<String, List<SignalStrength>> rsam = samples.remove(id);
			
			LocationPicker lp = new BestSampleLocationPicker(new EuclidianDistance(),
					samples);
			
			String guess = lp.guessLocation(rsam.getSecond());
			
			String real_loc = rsam.getFirst();
//			System.out.println(i+"\t"+guess+"\t"+real_loc);
			
			if(guess.equals(real_loc))
				correct_tries++;
			else {
				if(n_incorrect.containsKey(real_loc))
					n_incorrect.put(real_loc, n_incorrect.get(real_loc)+1);
				else
					n_incorrect.put(real_loc, 1);
			}
			
			samples.put(id, rsam);
			
		}
		
		//Display test results
		
		System.out.println("Test Completed");
		
		System.out.println("\n");
		
		System.out.println("Statistics:\n");
		System.out.println("\tTotal Runs\t"+total_tries);
		System.out.println("\tCorrect Guesses\t"+correct_tries+" ("+(correct_tries*100/total_tries)+"%)");
		System.out.println();
		System.out.println("Error Frequency per location");
		
		for(String s: n_incorrect.keySet()) {
			System.out.println("\t"+s+" failed "+n_incorrect.get(s)+" times.");
		}
	}
	
	/**
	 * @param samples
	 * @param random
	 * @throws DatabaseError
	 */
	private static void ManhattanTest(
			Map<Integer, Pair<String, List<SignalStrength>>> samples,
			Random random) throws DatabaseError {
		//test parameters
		int total_tries = TOTAL_TRIES;
		int correct_tries = 0;
		
		//stores frequency of mismatch for each failed sample
		Map<String,Integer> n_incorrect = new HashMap<String, Integer>();
		
//		//stores which results are obtained when wrong. format <Correct, <Incorrect, Freq>>
//		Map<String, Map<String, Integer>> q_incorrect = new HashMap<String, Map<String,Integer>>();
		
		
		//begin test
		System.out.println("Starting test execution (Manhattan Distance)");
		
		for (int i = 0; i < total_tries; i++) {
			int rint = random.nextInt(samples.size());
			int id = (Integer)samples.keySet().toArray()[rint];
			Pair<String, List<SignalStrength>> rsam = samples.remove(id);
			
			LocationPicker lp = new BestSampleLocationPicker(new ManhattanDistance(),
					samples);
			
			String guess = lp.guessLocation(rsam.getSecond());
			
			String real_loc = rsam.getFirst();
//			System.out.println(i+"\t"+guess+"\t"+real_loc);
			
			if(guess.equals(real_loc))
				correct_tries++;
			else {
				if(n_incorrect.containsKey(real_loc))
					n_incorrect.put(real_loc, n_incorrect.get(real_loc)+1);
				else
					n_incorrect.put(real_loc, 1);
			}
			
			samples.put(id, rsam);
			
		}
		
		//Display test results
		
		System.out.println("Test Completed");
		
		System.out.println("\n");
		
		System.out.println("Statistics:\n");
		System.out.println("\tTotal Runs\t"+total_tries);
		System.out.println("\tCorrect Guesses\t"+correct_tries+" ("+(correct_tries*100/total_tries)+"%)");
		System.out.println();
		System.out.println("Error Frequency per location");
		
		for(String s: n_incorrect.keySet()) {
			System.out.println("\t"+s+" failed "+n_incorrect.get(s)+" times.");
		}
	}

	/**
	 * @param samples
	 * @param random
	 * @throws DatabaseError
	 */
	private static void AvgErrorTest(
			Map<Integer, Pair<String, List<SignalStrength>>> samples,
			Random random) throws DatabaseError {
		//test parameters
		int total_tries = TOTAL_TRIES;
		int correct_tries = 0;
		
		//stores frequency of mismatch for each failed sample
		Map<String,Integer> n_incorrect = new HashMap<String, Integer>();
		
//		//stores which results are obtained when wrong. format <Correct, <Incorrect, Freq>>
//		Map<String, Map<String, Integer>> q_incorrect = new HashMap<String, Map<String,Integer>>();
		
		
		//begin test
		System.out.println("Starting test execution (Avg Error)");
		
		for (int i = 0; i < total_tries; i++) {
			int rint = random.nextInt(samples.size());
			int id = (Integer)samples.keySet().toArray()[rint];
			Pair<String, List<SignalStrength>> rsam = samples.remove(id);
			
			LocationPicker lp = new BestSampleLocationPicker(new AvgErrorAnalysis(),
					samples);
			
			String guess = lp.guessLocation(rsam.getSecond());
			
			String real_loc = rsam.getFirst();
//			System.out.println(i+"\t"+guess+"\t"+real_loc);
			
			if(guess.equals(real_loc))
				correct_tries++;
			else {
				if(n_incorrect.containsKey(real_loc))
					n_incorrect.put(real_loc, n_incorrect.get(real_loc)+1);
				else
					n_incorrect.put(real_loc, 1);
			}
			
			samples.put(id, rsam);
			
		}
		
		//Display test results
		
		System.out.println("Test Completed");
		
		System.out.println("\n");
		
		System.out.println("Statistics:\n");
		System.out.println("\tTotal Runs\t"+total_tries);
		System.out.println("\tCorrect Guesses\t"+correct_tries+" ("+(correct_tries*100/total_tries)+"%)");
		System.out.println();
		System.out.println("Error Frequency per location");
		
		for(String s: n_incorrect.keySet()) {
			System.out.println("\t"+s+" failed "+n_incorrect.get(s)+" times.");
		}
	}
	
	/**
	 * @param samples
	 * @param random
	 * @throws DatabaseError
	 */
	private static void euclidianTestX(
			Map<Integer, Pair<String, List<SignalStrength>>> samples,
			Random random) throws DatabaseError {
		//test parameters
		int total_tries = TOTAL_TRIES;
		int correct_tries = 0;
		
		//stores frequency of mismatch for each failed sample
		Map<String,Integer> n_incorrect = new HashMap<String, Integer>();
		
//		//stores which results are obtained when wrong. format <Correct, <Incorrect, Freq>>
//		Map<String, Map<String, Integer>> q_incorrect = new HashMap<String, Map<String,Integer>>();
		
		
		//begin test
		System.out.println("Starting test execution (Euclidian Distance)");
		
		for (int i = 0; i < total_tries; i++) {
			int rint = random.nextInt(samples.size());
			int id = (Integer)samples.keySet().toArray()[rint];
			Pair<String, List<SignalStrength>> rsam = samples.remove(id);
			
			LocationPicker lp = new BestXLocationPicker(new EuclidianDistance(),
					samples);
			
			String guess = lp.guessLocation(rsam.getSecond());
			
			String real_loc = rsam.getFirst();
//			System.out.println(i+"\t"+guess+"\t"+real_loc);
			
			if(guess.equals(real_loc))
				correct_tries++;
			else {
				if(n_incorrect.containsKey(real_loc))
					n_incorrect.put(real_loc, n_incorrect.get(real_loc)+1);
				else
					n_incorrect.put(real_loc, 1);
			}
			
			samples.put(id, rsam);
			
		}
		
		//Display test results
		
		System.out.println("Test Completed");
		
		System.out.println("\n");
		
		System.out.println("Statistics:\n");
		System.out.println("\tTotal Runs\t"+total_tries);
		System.out.println("\tCorrect Guesses\t"+correct_tries+" ("+(correct_tries*100/total_tries)+"%)");
		System.out.println();
		System.out.println("Error Frequency per location");
		
		for(String s: n_incorrect.keySet()) {
			System.out.println("\t"+s+" failed "+n_incorrect.get(s)+" times.");
		}
	}
	/**
	 * @param samples
	 * @param random
	 * @throws DatabaseError
	 */
	private static void ManhattanTestX(
			Map<Integer, Pair<String, List<SignalStrength>>> samples,
			Random random) throws DatabaseError {
		//test parameters
		int total_tries = TOTAL_TRIES;
		int correct_tries = 0;
		
		//stores frequency of mismatch for each failed sample
		Map<String,Integer> n_incorrect = new HashMap<String, Integer>();
		
//		//stores which results are obtained when wrong. format <Correct, <Incorrect, Freq>>
//		Map<String, Map<String, Integer>> q_incorrect = new HashMap<String, Map<String,Integer>>();
		
		
		//begin test
		System.out.println("Starting test execution (Manhattan Distance)");
		
		for (int i = 0; i < total_tries; i++) {
			int rint = random.nextInt(samples.size());
			int id = (Integer)samples.keySet().toArray()[rint];
			Pair<String, List<SignalStrength>> rsam = samples.remove(id);
			
			LocationPicker lp = new BestXLocationPicker(new ManhattanDistance(),
					samples);
			
			String guess = lp.guessLocation(rsam.getSecond());
			
			String real_loc = rsam.getFirst();
//			System.out.println(i+"\t"+guess+"\t"+real_loc);
			
			if(guess.equals(real_loc))
				correct_tries++;
			else {
				if(n_incorrect.containsKey(real_loc))
					n_incorrect.put(real_loc, n_incorrect.get(real_loc)+1);
				else
					n_incorrect.put(real_loc, 1);
			}
			
			samples.put(id, rsam);
			
		}
		
		//Display test results
		
		System.out.println("Test Completed");
		
		System.out.println("\n");
		
		System.out.println("Statistics:\n");
		System.out.println("\tTotal Runs\t"+total_tries);
		System.out.println("\tCorrect Guesses\t"+correct_tries+" ("+(correct_tries*100/total_tries)+"%)");
		System.out.println();
		System.out.println("Error Frequency per location");
		
		for(String s: n_incorrect.keySet()) {
			System.out.println("\t"+s+" failed "+n_incorrect.get(s)+" times.");
		}
	}
	/**
	 * @param samples
	 * @param random
	 * @throws DatabaseError
	 */
	private static void AvgErrorTestX(
			Map<Integer, Pair<String, List<SignalStrength>>> samples,
			Random random) throws DatabaseError {
		//test parameters
		int total_tries = TOTAL_TRIES;
		int correct_tries = 0;
		
		//stores frequency of mismatch for each failed sample
		Map<String,Integer> n_incorrect = new HashMap<String, Integer>();
		
//		//stores which results are obtained when wrong. format <Correct, <Incorrect, Freq>>
//		Map<String, Map<String, Integer>> q_incorrect = new HashMap<String, Map<String,Integer>>();
		
		
		//begin test
		System.out.println("Starting test execution (Avg Error)");
		
		for (int i = 0; i < total_tries; i++) {
			int rint = random.nextInt(samples.size());
			int id = (Integer)samples.keySet().toArray()[rint];
			Pair<String, List<SignalStrength>> rsam = samples.remove(id);
			
			LocationPicker lp = new BestXLocationPicker(new AvgErrorAnalysis(),
					samples);
			
			String guess = lp.guessLocation(rsam.getSecond());
			
			String real_loc = rsam.getFirst();
//			System.out.println(i+"\t"+guess+"\t"+real_loc);
			
			if(guess.equals(real_loc))
				correct_tries++;
			else {
				if(n_incorrect.containsKey(real_loc))
					n_incorrect.put(real_loc, n_incorrect.get(real_loc)+1);
				else
					n_incorrect.put(real_loc, 1);
			}
			
			samples.put(id, rsam);
			
		}
		
		//Display test results
		
		System.out.println("Test Completed");
		
		System.out.println("\n");
		
		System.out.println("Statistics:\n");
		System.out.println("\tTotal Runs\t"+total_tries);
		System.out.println("\tCorrect Guesses\t"+correct_tries+" ("+(correct_tries*100/total_tries)+"%)");
		System.out.println();
		System.out.println("Error Frequency per location");
		
		for(String s: n_incorrect.keySet()) {
			System.out.println("\t"+s+" failed "+n_incorrect.get(s)+" times.");
		}
	}

}
