package mpc.wifi.lib.mac;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import mpc.wifi.lib.SignalStrength;
import mpc.wifi.lib.WiFiInfo;


/**
 * Wrapper of the iwlist command
 */
public class MacWiFi implements WiFiInfo{
	
	/**
	 * Change to "" to avoid giving super user privileges to this application.
	 * Otherwise use "gksu".
	 */
/*	private static final String SU_PRIVILEGES = "gksu";
	
	private String wifi_interface = "ra0";*/

	public MacWiFi() {
	}
	
	public MacWiFi(String wifi_interface) {
		//this.wifi_interface = wifi_interface;
	}
	

	public List<SignalStrength> scan() throws IOException {
		Process p = Runtime.getRuntime().exec("/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport -s");
		
		InputStream is = p.getInputStream();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		int c;
		while((c= is.read()) != -1)
			bos.write(c);
		
		byte[] b = bos.toByteArray();
		
		return parseScanData(new String(b,"UTF-8"));
		
	}
	
	/**
	 * The provided String should come in the format returned by
	 * <code>iwlist \<interface\> scan</code> command.<br>
	 * Example:<br>
	 * <blockquote>
	 * ra0       Peers/Access-Points in range:
	 *     00:12:DA:AE:B2:A0 : Quality:96/100  Signal level:-52 dBm  Noise level:-81 dBm
	 *     00:12:DA:9E:30:32 : Quality:5/100  Signal level:-88 dBm  Noise level:-81 dBm
	 * </blockquote>
	 * This function parses that string and returns an array of <tt>SignalStrength</tt>.
	 * @param data
	 * @return an array of <tt>SignalStrength</tt>
	 */
	private List<SignalStrength> parseScanData(String data){
		List<SignalStrength> l = new LinkedList<SignalStrength>();
		
		//Divide by line breaks
		String[] nonbl = data.split("\n");
		
		//Parse each line to obtain BSSID and the corresponding signal strength
		for(int i = 1; i < nonbl.length; i++) {
			String line = nonbl[i];
			
			String[] rmws = line.split(" ");

			if(rmws.length <= 1) continue;
			int begin;
			for(begin = 0;begin < rmws.length; begin++)
				if(!rmws[begin].equals("")){
					break;
				}

			String bssid = rmws[begin+1];
			
			int signal = Integer.parseInt(rmws[begin+2]);
			
			l.add(new SignalStrength(bssid, signal));
			
		}
		
		
		return l;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		MacWiFi wi = new MacWiFi();
		
		for(SignalStrength ss : wi.scan())
			System.out.printf("Access Point %s\t%d\n", ss.getBssid(), ss.getSignal());
	}

}
