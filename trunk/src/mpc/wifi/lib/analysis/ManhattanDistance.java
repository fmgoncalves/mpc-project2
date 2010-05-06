package mpc.wifi.lib.analysis;

import java.util.List;

import mpc.wifi.lib.SignalStrength;


public class ManhattanDistance implements SampleAnalyser {

	private final int WEAK_SIGNAL = -100;
	
	@Override
	public double evaluate(List<SignalStrength> sample,
			List<SignalStrength> other) {
		
		double distance = 0;
				
		for (SignalStrength ss0 : sample)
			if(other.contains(ss0)) {
				SignalStrength ss1 =  other.get(other.indexOf(ss0));
				distance += Math.abs(ss0.getSignal() - ss1.getSignal());
			} else //to account for signal present in sample but not in other
				distance += Math.abs(ss0.getSignal() - WEAK_SIGNAL);
		
		//to account for the signals present in other but not in sample
		for(SignalStrength ss1: other)
			if(!sample.contains(ss1))
				distance += Math.abs(ss1.getSignal() - WEAK_SIGNAL);
		
		
		return distance;
	}

}
