package mpc.wifi.lib.analysis;

import java.util.List;

import mpc.wifi.lib.SignalStrength;


public interface SampleAnalyser {

	/**
	 * Compares two lists of samples
	 * @param sample the original sample, to be compared with a stock one.
	 * @param other the stock sample, preobtained and verified.
	 * @return
	 */
	public double evaluate(List<SignalStrength> sample, List<SignalStrength> other);
	
}
