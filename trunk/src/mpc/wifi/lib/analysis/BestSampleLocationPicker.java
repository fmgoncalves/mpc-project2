package mpc.wifi.lib.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mpc.wifi.lib.Pair;
import mpc.wifi.lib.SignalStrength;
import mpc.wifi.lib.db.DatabaseConnection;
import mpc.wifi.lib.db.DatabaseError;


public class BestSampleLocationPicker implements LocationPicker {
	
	private SampleAnalyser sa;
	private DatabaseConnection dbc;
	
	/**
	 * Stock samples from the database, to be loaded on the constructor.
	 * Map is of (sample id, sample results).
	 */
	private Map<Integer,Pair<String,List<SignalStrength>>> stock;
	
	public BestSampleLocationPicker() {
		this(new EuclidianDistance());
	}

	public BestSampleLocationPicker(SampleAnalyser sa){
		this.sa =sa;
		try {
			this.stock= dbc.loadSamples();
		} catch (DatabaseError e) {
			this.stock= new HashMap<Integer, Pair<String,List<SignalStrength>>>();
		}
	}
	
	public BestSampleLocationPicker(SampleAnalyser sa, Map<Integer,Pair<String,List<SignalStrength>>> stock) {
		this.sa = sa;
		this.stock = stock;
	}
	
	

	@Override
	public String guessLocation(List<SignalStrength> sample) throws DatabaseError {
		
		double best = Double.MAX_VALUE;
		
		String location = "";
		
		Iterator<Integer> keys = stock.keySet().iterator();
		
		while(keys.hasNext()) {
			int key = keys.next();
			Pair<String,List<SignalStrength>> plss = stock.get(key);
			List<SignalStrength> lss = plss.getSecond();
			
			double result = sa.evaluate(sample, lss);
//			System.out.println("Comp:\t"+key+"\t"+result);
			if(best >= result) {
				best = result;
				location = plss.getFirst();
			}
		}
		
		return location;
	}

}
