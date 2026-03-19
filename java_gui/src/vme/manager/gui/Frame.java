package vme.manager.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import vme.manager.gui.utils.*;

public class Frame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel titlePanel, bottomPanel;
    private JTabbedPane tabbedPane;
    private JPanel
        deviceCleaning,
        hardwareIntegrity,
        optimizationMethodology,
        fileAnalysis,
        networkState,
        predictionOfDevice;
    private JButton
        buttonConfig,
        buttonLogin;
    public Frame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(700, 400));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        titlePanel = new JPanel();
        add(titlePanel, BorderLayout.NORTH);
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        buttonConfig = new JButton(setIcon("./resources/icons/config.png", 20));
        buttonConfig.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(
                    null,
                    "En desarrollo",
                    "Configuración",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        titlePanel.add(buttonConfig);
        buttonLogin = new JButton(setIcon("./resources/icons/user.png", 20));
		buttonLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(
                    null,
                    "En desarrollo",
                    "Inicio de sesión",
                    JOptionPane.INFORMATION_MESSAGE);
            }
		});
        titlePanel.add(buttonLogin);
        JLabel titleProgram = new JLabel("VME-Manager");
        titleProgram.setFont(new Font("sans-serif", Font.BOLD, 32));
        titlePanel.add(titleProgram);

        fileAnalysis = new FileAnalysis();
        tabbedPane.setFont(new Font("sans-serif", Font.PLAIN, 20));

        tabbedPane.addTab("Análisis", setIcon("./resources/icons/tab_icon.png", 10), fileAnalysis);

        networkState = new NetworkState();
        tabbedPane.addTab("Red", setIcon("./resources/icons/tab_icon.png", 10), networkState);

        hardwareIntegrity = new HardwareIntegrity();
        tabbedPane.addTab("Hardware", setIcon("./resources/icons/tab_icon.png", 10), hardwareIntegrity);

        deviceCleaning = new DeviceCleaning();
        tabbedPane.addTab("Limpieza", setIcon("./resources/icons/tab_icon.png", 10), deviceCleaning);

        predictionOfDevice = new PredictionOfDevice();
        tabbedPane.addTab("Pronósticos *", setIcon("./resources/icons/tab_icon.png", 10), predictionOfDevice);
        tabbedPane.setEnabledAt(4, false);

        optimizationMethodology = new OptimizationMethodology();
        tabbedPane.addTab("Optimización *", setIcon("./resources/icons/tab_icon.png", 10), optimizationMethodology);
        tabbedPane.setEnabledAt(5, false);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        pack();
    }

    private ImageIcon setIcon(String path, int size)
    {
        ImageIcon imageIcon = new ImageIcon(path);
        Image img = imageIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
