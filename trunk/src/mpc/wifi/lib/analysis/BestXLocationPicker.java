package mpc.wifi.lib.analysis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mpc.wifi.lib.Pair;
import mpc.wifi.lib.SignalStrength;
import mpc.wifi.lib.db.DatabaseConnection;
import mpc.wifi.lib.db.DatabaseError;

public class BestXLocationPicker implements LocationPicker {

	private int X = 3;

	private SampleAnalyser sa;

	/**
	 * Stock samples from the database, to be loaded on the constructor. Map is
	 * of (sample id, sample results).
	 */
	private Map<Integer, Pair<String, List<SignalStrength>>> stock;

	public BestXLocationPicker() {
		this(new EuclidianDistance());
	}

	public BestXLocationPicker(SampleAnalyser sa) {
		this.sa = sa;

		try {
			this.stock = new DatabaseConnection().loadSamples();
		} catch (DatabaseError e) {
			this.stock = new HashMap<Integer, Pair<String, List<SignalStrength>>>();
		}
	}

	public BestXLocationPicker(SampleAnalyser sa,
			Map<Integer, Pair<String, List<SignalStrength>>> stockSamples) {
		this.sa = sa;
		this.stock = stockSamples;
	}

	@Override
	public String guessLocation(List<SignalStrength> sample)
			throws DatabaseError {

		String location = "";

		Iterator<Integer> keys = stock.keySet().iterator();

		List<SSResult> resultList = new LinkedList<SSResult>();

		while (keys.hasNext()) {
			int key = keys.next();
			Pair<String, List<SignalStrength>> plss = stock.get(key);
			List<SignalStrength> lss = plss.getSecond();

			double result = sa.evaluate(sample, lss);

			resultList.add(new SSResult(key, result));
		}

		//Calculate best top X
		Collections.sort(resultList);

//		for (int i = 0; i < X; i++) {
//			System.out.println(i + "\tpossible\t"
//					+ stock.get(resultList.get(i).key).getFirst() + "t"
//					+ resultList.get(i).result);
//		}

		//maps key->frequency
		Map<Integer, Integer> freq = new HashMap<Integer, Integer>();
		for (int i = 0; i < X; i++) {
			SSResult ssr = resultList.get(i);
			if (freq.containsKey(ssr.key))
				freq.put(ssr.key, freq.get(ssr.key) + 1);
			else
				freq.put(ssr.key, 1);
		}

		int bestKey = -1;
		int bestFreq = 0;
		for (int key : freq.keySet())
			if (freq.get(key) > bestFreq) bestKey = key;

		if (bestKey > -1) location = stock.get(bestKey).getFirst();

		//		System.out.println("\tand came out"+location);

		return location;
	}

	private class SSResult implements Comparable<SSResult> {

		private int key;
		private double result;

		public SSResult(int key, double result) {
			this.key = key;
			this.result = result;
		}

		@Override
		public int compareTo(SSResult o) {
			int res = 0;
			if (o.result - this.result > 0)
				res =  -1;
			else if (o.result - this.result < 0)
				res = 1;
//			System.out.println("PIM "+res);
			return res;
		}

	}

}
