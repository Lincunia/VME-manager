package vme.manager.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import vme.manager.gui.utils.*;

public class Frame extends JFrame {
  private JPanel titlePanel, bottomPanel;
  private JTabbedPane tabbedPane;
  private JPanel deviceCleaning, hardwareIntegrity, optimizationMethodology,
      fileAnalysis, networkState, predictionOfDevice;
  public Frame() {
    setTitle("VME Manager");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    titlePanel = new JPanel();
    add(titlePanel, BorderLayout.NORTH);

    tabbedPane = new JTabbedPane();
    add(tabbedPane, BorderLayout.CENTER);

	fileAnalysis = new FileAnalysis();
    tabbedPane.addTab("Análisis", fileAnalysis);
	
	hardwareIntegrity = new FileAnalysis();
    tabbedPane.addTab("Hardware", hardwareIntegrity);

	networkState = new NetworkState();
	tabbedPane.addTab("Red", networkState);

	deviceCleaning = new DeviceCleaning();
    tabbedPane.addTab("Limpieza", deviceCleaning);

	predictionOfDevice = new PredictionOfDevice();
    tabbedPane.addTab("Pronósticos", predictionOfDevice);

	optimizationMethodology = new OptimizationMethodology();
    tabbedPane.addTab("Optimización", optimizationMethodology);

    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

    bottomPanel = new JPanel();
    add(bottomPanel, BorderLayout.SOUTH);

    pack();
  }
}
