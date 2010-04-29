package mpc.wifi.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import mpc.wifi.lib.SignalStrength;
import mpc.wifi.lib.WiFiInfo;
import mpc.wifi.lib.db.DatabaseConnection;
import mpc.wifi.lib.db.DatabaseError;
import mpc.wifi.lib.linux.LinuxWiFi;



public class ScanWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String[] places =
		{ "112/Ed.II", "114/Ed.II", "116/Ed.II", "120/Ed.II", "121/Ed.II", "132/Ed.II" };
	
	
	private JButton scanTrigger;
	private JPanel resultPanel;
	private JComboBox locationList;
	private JButton saveLocation;
	
	private WiFiInfo wifinfo;
	private DatabaseConnection dbc;
	private List<SignalStrength> results;
	
	public ScanWindow() {
		super("Fingerprint Collector");
		
		wifinfo = new LinuxWiFi();
		try {
			dbc = new DatabaseConnection();
		} catch (DatabaseError e2) {
			System.err.println("No Database Connection");
		}
		
		results = new LinkedList<SignalStrength>();
		
		scanTrigger = new JButton("Scan");
		
		resultPanel = new JPanel();
		
		JTextArea jta = new JTextArea("No results to display.\n"+
		"Press \"Scan\" to obtain results");
		jta.setEnabled(false);
		resultPanel.add(jta);
		
		locationList = new JComboBox(places);
		locationList.setEditable(true);
	
		saveLocation = new JButton("Save");
		saveLocation.setEnabled(false);
		
		JPanel southPanel = new JPanel(new FlowLayout());
		southPanel.add(locationList);
		southPanel.add(saveLocation);
		
		scanTrigger.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					results = wifinfo.scan();
					
					if(results.isEmpty()) {
						JTextArea jta = new JTextArea("No results to display.\n"+
						"Press \"Scan\" to obtain results");
						jta.setEnabled(false);
					}else fillResultsTable();
					
					pack();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}

			
		});
		
		saveLocation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String location = (String)locationList.getSelectedItem();
				try {
					if(!dbc.isAlive())
						dbc = new DatabaseConnection();
					dbc.insertSample(location,results);
				} catch (DatabaseError e1) {
					System.err.println("Failed to insert sample to db: "+e1.getLocalizedMessage());
				}
			}
		});
		

		locationList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		
		add(scanTrigger, BorderLayout.NORTH);
		add(resultPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		pack();
		
		setVisible(true);
	}
	
	
	private void fillResultsTable() {
		
		Object[][] oa = new Object[results.size()][2];
		
		for(int i = 0; i < results.size(); i++) {
			SignalStrength ss = results.get(i);
			oa[i][0] = ss.getBssid();
			oa[i][1] = ss.getSignal();
		}
		
		String[] columns = {"BSSID", "Signal Strength (dBm)"};
		JTable resTable = new JTable(oa, columns );

		resTable.getColumnModel().getColumn(0).setPreferredWidth(140);
		resTable.getColumnModel().getColumn(1).setPreferredWidth(50);
		
		resTable.setEnabled(false);
		
		resultPanel.removeAll();
		resultPanel.add(resTable);
		resultPanel.updateUI();
		if(dbc != null)
			saveLocation.setEnabled(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println(e.getClass() + " " + e.getMessage());
		}
		
		new ScanWindow();
		
	}

}