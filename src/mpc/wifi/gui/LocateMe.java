package mpc.wifi.gui;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import mpc.wifi.lib.WiFiInfo;
import mpc.wifi.lib.analysis.BestSampleLocationPicker;
import mpc.wifi.lib.analysis.LocationPicker;
import mpc.wifi.lib.db.DatabaseConnection;
import mpc.wifi.lib.db.DatabaseError;
import mpc.wifi.lib.linux.LinuxWiFi;

public class LocateMe extends JFrame {

	private static final long serialVersionUID = 1L;

	private WiFiInfo wifinfo;
	private LocationPicker lp;

	private JPanel resultPanel;

	public LocateMe() throws DatabaseError {
		super("Locate Me");

		wifinfo = new LinuxWiFi();
		lp = new BestSampleLocationPicker(new DatabaseConnection());

		resultPanel = new JPanel();

		JTextArea jta = new JTextArea("Wait...");
		jta.setEditable(false);

		resultPanel.add(jta);

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					String location = lp.guessLocation(wifinfo.scan());
					System.out.println("guessed "+location);
					resultPanel.removeAll();
					
					JTextArea jta;
					if(!location.isEmpty())
						jta = new JTextArea("You are at: " + location);
					else jta = new JTextArea("Couldn't determine current location");
					jta.setEditable(false);
					resultPanel.add(jta);
					
					resultPanel.updateUI();

				} catch (DatabaseError e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					System.err.println(e.getLocalizedMessage());
				}
			}
		}).start();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new BorderLayout());

		add(resultPanel, BorderLayout.CENTER);

		setSize(200, 400);
		
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
