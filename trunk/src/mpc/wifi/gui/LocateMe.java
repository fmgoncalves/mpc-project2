package mpc.wifi.gui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import mpc.wifi.lib.Pair;
import mpc.wifi.lib.SignalStrength;
import mpc.wifi.lib.WiFiInfo;
import mpc.wifi.lib.analysis.AvgErrorAnalysis;
import mpc.wifi.lib.analysis.BestSampleLocationPicker;
import mpc.wifi.lib.analysis.EuclidianDistance;
import mpc.wifi.lib.analysis.LocationPicker;
import mpc.wifi.lib.analysis.ManhattanDistance;
import mpc.wifi.lib.db.DatabaseConnection;
import mpc.wifi.lib.db.DatabaseError;
import mpc.wifi.lib.linux.LinuxWiFi;

public class LocateMe extends JFrame {

	private static final long serialVersionUID = 1L;

	private WiFiInfo wifinfo;
	private LocationPicker lp1;
	private LocationPicker lp2;
	private LocationPicker lp3;

	private JPanel resultPanel;

	public LocateMe() throws DatabaseError {
		super("Locate Me");

		wifinfo = new LinuxWiFi();

		resultPanel = new JPanel();

		JTextArea jta = new JTextArea("Wait...");
		jta.setEditable(false);

		resultPanel.add(jta);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					DatabaseConnection dbc = new DatabaseConnection();
					Map<Integer, Pair<String, List<SignalStrength>>> samples = dbc
							.loadSamples();
					lp1 = new BestSampleLocationPicker(new AvgErrorAnalysis(),
							samples);
					lp2 = new BestSampleLocationPicker(new ManhattanDistance(),
							samples);
					lp3 = new BestSampleLocationPicker(new EuclidianDistance(),
							samples);

					new Thread(new Runnable() {

						@Override
						public void run() {
							while (true) {
								try {

									List<SignalStrength> scanList = wifinfo
											.scan();
									resultPanel.removeAll();

									String location1 = lp1
											.guessLocation(scanList);
									String location2 = lp2
											.guessLocation(scanList);
									String location3 = lp3
											.guessLocation(scanList);

									System.out.println(location1+", "+location2+", "+location3);
									
									JTextArea jta = new JTextArea();

									jta.append("AvgError\t\t" + location1);
									jta.append("\n");
									jta.append("Manhattan\t\t" + location2);
									jta.append("\n");
									jta.append("Euclidian\t\t" + location3);

									jta.setEditable(false);
									resultPanel.add(jta);
									
//									resultPanel.repaint();
									resultPanel.updateUI();


								} catch (DatabaseError e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}

								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									System.err.println(e.getLocalizedMessage());
								}
							}
						}
					}).start();

				} catch (DatabaseError e) {
					System.err.println(e);
				}
			}
		}).start();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());

		add(resultPanel, BorderLayout.CENTER);

		setSize(500, 100);

		setVisible(true);

	}

	/**
	 * @param args
	 * @throws DatabaseError
	 */
	public static void main(String[] args) throws DatabaseError {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println(e.getClass() + " " + e.getMessage());
		}

		new LocateMe();
	}

}
