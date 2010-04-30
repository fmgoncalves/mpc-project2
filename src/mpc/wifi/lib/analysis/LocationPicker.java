package mpc.wifi.lib.analysis;

import java.util.List;

import mpc.wifi.lib.SignalStrength;
import mpc.wifi.lib.db.DatabaseError;


public interface LocationPicker {

	public String guessLocation(List<SignalStrength> sample) throws DatabaseError;
}
