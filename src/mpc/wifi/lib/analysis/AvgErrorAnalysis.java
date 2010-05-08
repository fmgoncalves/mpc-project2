package mpc.wifi.lib.analysis;

import java.util.LinkedList;
import java.util.List;

import mpc.wifi.lib.SignalStrength;

public class AvgErrorAnalysis implements SampleAnalyser {

	private static final int WEAK_SIGNAL = -100;

	@Override
	public double evaluate(List<SignalStrength> sample, List<SignalStrength> other) {

		double errorSum = 0;
		int n = 0;
		
//		System.out.println("Stage1: "+sample.size()+" "+other.size());
		
		for (SignalStrength ss0 : sample)
			if(other.contains(ss0)) {
				n++;
				SignalStrength ss1 =  other.get(other.indexOf(ss0));
				errorSum += Math.abs(ss0.getSignal() - ss1.getSignal());
			} else {//to account for signal present in sample but not in other
				n++;
				errorSum += Math.abs(ss0.getSignal() - WEAK_SIGNAL);
			}
		
		//to account for the signals present in other but not in sample
		for(SignalStrength ss1: other)
			if(!sample.contains(ss1)) {
				n++;
				errorSum += Math.abs(ss1.getSignal() - WEAK_SIGNAL);
			}
		double avgError = n > 0?  errorSum / n : Double.POSITIVE_INFINITY;
		
		return avgError;
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
