package mpc.wifi.lib.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mpc.wifi.lib.Pair;
import mpc.wifi.lib.SignalStrength;

public class DatabaseConnection {

	private static final String host_address = "84.90.92.185";
	private static final String database_name = "mpc";

	private static final String user = "filipe";
	private static final String password = "bivuce";

	private Connection conn;

	public DatabaseConnection() throws DatabaseError {

		try {
			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://" + host_address + "/" + database_name;

			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);

		} catch (Exception e) {
			throw new DatabaseError(e);
		}
	}

	public boolean isAlive() {
		try {
			return conn.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	public Map<Integer, Pair<String,List<SignalStrength>>> loadSamples()
			throws DatabaseError {

		Map<Integer, Pair<String,List<SignalStrength>>> samples = new HashMap<Integer, Pair<String,List<SignalStrength>>>();

		try {

			Statement stmt = conn.createStatement();
			if (stmt.execute("SELECT DISTINCT(id), loc FROM samples NATURAL JOIN location;")) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next())
					samples.put(rs.getInt("id"), new Pair<String,List<SignalStrength>>(rs.getString("loc"),new LinkedList<SignalStrength>()));
				if (stmt.execute("SELECT * FROM samples;")) {
					rs = stmt.getResultSet();
					while (rs.next()) {
						String bssid = rs.getString("bssid");
						int signal = rs.getInt("signal");
						int id = rs.getInt("id");
						samples.get(id).getSecond().add(new SignalStrength(bssid, signal));
					}
				}
			}

		} catch (SQLException e) {
			throw new DatabaseError(e);
		}

		return samples;
	}

	public String locationFromSample(int id) {
		String result = "";
		try {
			conn.setAutoCommit(true);
			Statement stmt = conn.createStatement();
			String sql = "SELECT loc FROM location WHERE id =" + id + ";";
			
//			System.out.println(sql);
			if (stmt.execute(sql)) {
				ResultSet rs = stmt.getResultSet();
				rs.next();
				result = rs.getString("loc");
			}

			conn.setAutoCommit(false);
		} catch (SQLException e) {}
		return result;
	}

	public void insertSample(String location, List<SignalStrength> sample)
			throws DatabaseError {
		try {
			Statement stmt = conn.createStatement();

			String sql = "INSERT INTO samples(bssid, signal) VALUES ('"
					+ sample.get(0).getBssid() + "', "
					+ sample.get(0).getSignal() + ")";
			System.out.println(sql);
			stmt.addBatch(sql);

			for (int i = 1; i < sample.size(); i++) {
				sql = "INSERT INTO samples(id, bssid, signal) VALUES ( LAST_INSERT_ID(), '"
						+ sample.get(i).getBssid()
						+ "', "
						+ sample.get(i).getSignal() + ");";
				System.out.println(sql);
				stmt.addBatch(sql);
			}
			stmt.executeBatch();

			sql = "INSERT INTO location(id, loc) VALUES ( LAST_INSERT_ID(), '"
					+ location + "');";
			stmt.execute(sql);

			conn.commit();

		} catch (SQLException e) {
			throw new DatabaseError(e);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		DatabaseConnection dbc = new DatabaseConnection();

		dbc.conn.setAutoCommit(false);

		Statement stmt = dbc.conn.createStatement();

		String sql = "SELECT * FROM location;";

		if (stmt.execute(sql))
			System.out.println(stmt.getResultSet().getFetchSize());
	}

}
