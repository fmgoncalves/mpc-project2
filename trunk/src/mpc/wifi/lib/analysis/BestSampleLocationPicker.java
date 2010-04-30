package mpc.wifi.lib.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	private Map<Integer,List<SignalStrength>> stock;
	
	public BestSampleLocationPicker(DatabaseConnection dbc) {
		this(new AvgErrorAnalysis(), dbc);
	}

	BestSampleLocationPicker(SampleAnalyser sa, DatabaseConnection dbc){
		this.sa = sa;
		this.dbc = dbc;
		
		try {
			this.stock = dbc.loadSamples();
		} catch (DatabaseError e) {
			this.stock = new HashMap<Integer, List<SignalStrength>>();
		}
	}

	@Override
	public String guessLocation(List<SignalStrength> sample) throws DatabaseError {
		double best = Double.MAX_VALUE;
		
		//valor de bestKey não interessa, é só porque precisa ser inicializada
		int bestKey = 0;
		
		Iterator<Integer> keys = stock.keySet().iterator();
		
		while(keys.hasNext()) {
			int key = keys.next();
			List<SignalStrength> lss = stock.get(key);
			
			double result = sa.evaluate(sample, lss);
//			System.out.println("Comp:\t"+key+"\t"+result);
			if(best >= result) {
				best = result;
				bestKey = key;
			}
		}
//		
//		System.out.println("Picked sample "+bestKey);
		
		//fetch location name from database using bestKey
		if(!dbc.isAlive())
			dbc = new DatabaseConnection();
		
		String location = dbc.locationFromSample(bestKey);
		
		return location;
	}

}
