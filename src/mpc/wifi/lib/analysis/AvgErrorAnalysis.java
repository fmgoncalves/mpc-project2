package mpc.wifi.lib.analysis;

import java.util.LinkedList;
import java.util.List;

import mpc.wifi.lib.SignalStrength;

public class AvgErrorAnalysis implements SampleAnalyser {

	//TODO test other values for this
	private final double MISMATCH_PENALTY = 20;
	
	@Override
	public double evaluate(List<SignalStrength> sample, List<SignalStrength> other) {

		int matches = 0;
		double errorSum = 0;

		for (SignalStrength ss0 : sample)
			for (SignalStrength ss1 : other)
				if (ss0.getBssid().equals(ss1.getBssid())) {
					matches++;
					double relativeError = Math.abs(ss0.getSignal()-ss1.getSignal());
					errorSum += relativeError;
				}

		int mismatches = sample.size() + other.size() - matches*2;
		double avgError = matches > 0?  errorSum / matches : Double.POSITIVE_INFINITY;
		
		return avgError + mismatches * MISMATCH_PENALTY;
	}
	
	public static void main(String[] args) {
		List<SignalStrength> s1 = new LinkedList<SignalStrength>();
		List<SignalStrength> s2 = new LinkedList<SignalStrength>();
		List<SignalStrength> s3 = new LinkedList<SignalStrength>();
		List<SignalStrength> s4 = new LinkedList<SignalStrength>();
		
		s1.add(new SignalStrength("a", -73));
		s1.add(new SignalStrength("b", -43));

		s2.add(new SignalStrength("a", -71));
		s2.add(new SignalStrength("b", -44));
		s2.add(new SignalStrength("c", -35));
		
		s3.add(new SignalStrength("a", -33));
		s3.add(new SignalStrength("b", -86));
		
		s4.add(new SignalStrength("a", -76));
		
		AvgErrorAnalysis aea = new AvgErrorAnalysis();
		
		System.out.printf("s1/s2\t%.2f\n", aea.evaluate(s1, s2));
		System.out.printf("s1/s3\t%.2f\n", aea.evaluate(s1, s3));
		System.out.printf("s1/s4\t%.2f\n", aea.evaluate(s1, s4));
		System.out.printf("s2/s3\t%.2f\n", aea.evaluate(s2, s3));
		
	}

}
