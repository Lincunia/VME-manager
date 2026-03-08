package vme.manager.gui;

import javax.swing.SwingUtilities;
/*
 * This site was created by using this website:
 * https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
 */
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
