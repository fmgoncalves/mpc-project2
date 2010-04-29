package mpc.wifi.lib.analysis;

import java.util.List;

import mpc.wifi.lib.SignalStrength;


public interface LocationPicker {

	public String guessLocation(List<SignalStrength> sample);
}
