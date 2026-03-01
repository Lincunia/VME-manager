package vme.manager.gui;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Frame frame = new Frame();
				frame.setVisible(true);
			}
		});
	}
}
