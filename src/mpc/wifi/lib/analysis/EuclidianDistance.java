package mpc.wifi.lib.analysis;

import java.util.List;

import mpc.wifi.lib.SignalStrength;


public class EuclidianDistance implements SampleAnalyser {

	private final int WEAK_SIGNAL = -100;
	
	@Override
	public double evaluate(List<SignalStrength> sample,
			List<SignalStrength> other) {
		
		double distance = 0;
				
		for (SignalStrength ss0 : sample)
			if(other.contains(ss0)) {
				SignalStrength ss1 =  other.get(other.indexOf(ss0));
				distance += Math.pow(ss0.getSignal() - ss1.getSignal(),2);
			} else //to account for signal present in sample but not in other
				distance += Math.pow(ss0.getSignal() - WEAK_SIGNAL,2);
		
		//to account for the signals present in other but not in sample
		for(SignalStrength ss1: other)
			if(!sample.contains(ss1))
				distance += Math.pow(ss1.getSignal() - WEAK_SIGNAL,2);
		
		
		return Math.sqrt(distance);
	}

}
