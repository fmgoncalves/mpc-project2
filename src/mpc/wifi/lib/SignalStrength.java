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

	@Override
	public boolean equals(Object obj) {
		return obj instanceof SignalStrength ? ((SignalStrength) obj)
				.getBssid().equals(bssid)
				&& ((SignalStrength) obj).getSignal() == signal : false;
	}

}
