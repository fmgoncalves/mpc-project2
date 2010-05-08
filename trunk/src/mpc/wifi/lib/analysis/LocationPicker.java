package mpc.wifi.lib.analysis;

import java.util.List;

import mpc.wifi.lib.SignalStrength;
import mpc.wifi.lib.db.DatabaseError;


public interface LocationPicker {

	/**
	 * 
	 * @param sample
	 * @return empty string if no location found, otherwise the guessed location.
	 * @throws DatabaseError
	 */
	public String guessLocation(List<SignalStrength> sample) throws DatabaseError;
}
