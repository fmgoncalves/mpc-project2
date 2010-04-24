package mpc.wifi.lib;

import java.io.IOException;
import java.util.List;

/**
 * Interface to collect wireless ap's addresses and signal
 */
public interface WiFiInfo {
	public List<SignalStrength> scan() throws IOException;
}
