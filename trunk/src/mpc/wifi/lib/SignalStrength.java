package mpc.wifi.lib;


public class SignalStrength {

	private String bssid;
	private int signal;
	
	public SignalStrength(String bssid, int signal) {
		this.bssid = bssid;
		this.signal = signal;
	}

	
	public String getBssid() {
		return bssid;
	}

	
	public int getSignal() {
		return signal;
	}
	
}
