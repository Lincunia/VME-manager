package vme.manager.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import vme.manager.gui.utils.*;

public class Frame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel titlePanel, bottomPanel;
    private static JTabbedPane tabbedPane;
    private JScrollPane
        scrollPaneFileAnalysis,
        scrollPaneNetworkState,
        scrollPaneHardwareIntegrity,
        scrollPaneDeviceCleaning,
        scrollPanePredictionOfDevice,
        scrollPaneOptimizationMethodology;
    private JButton
        buttonConfig,
        buttonLogin;
    public Frame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setPreferredSize(new Dimension(700, 400));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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

        tabbedPane.setFont(new Font("sans-serif", Font.PLAIN, 20));

        scrollPaneFileAnalysis = new JScrollPane(new FileAnalysis(tabbedPane));
        tabbedPane.addTab("Análisis de datos", setIcon("./resources/icons/tab_icon.png", 10), scrollPaneFileAnalysis);

        scrollPaneNetworkState = new JScrollPane(new NetworkState(tabbedPane));
        tabbedPane.addTab("Estado de la red", setIcon("./resources/icons/tab_icon.png", 10), scrollPaneNetworkState);

        scrollPaneHardwareIntegrity = new JScrollPane(new HardwareIntegrity(tabbedPane));
        tabbedPane.addTab("Integridad de hardware", setIcon("./resources/icons/tab_icon.png", 10), scrollPaneHardwareIntegrity);

        scrollPaneDeviceCleaning = new JScrollPane(new DeviceCleaning(tabbedPane));
        tabbedPane.addTab("Limpieza del dispositivo", setIcon("./resources/icons/tab_icon.png", 10), scrollPaneDeviceCleaning);

        /*
         * Estas son funcionalidades aún más complejas de lo que son, por lo
         * tanto son elementos que van apareciendo poco a poco dentro del
         * proyecto y que por ahora no de van a implementar
         */
        scrollPanePredictionOfDevice = new JScrollPane(new PredictionOfDevice(tabbedPane));
        tabbedPane.addTab("Pronósticos *", setIcon("./resources/icons/tab_icon.png", 10), scrollPanePredictionOfDevice);
        tabbedPane.setEnabledAt(4, false);

        scrollPaneOptimizationMethodology = new JScrollPane(new OptimizationMethodology(tabbedPane));
        tabbedPane.addTab("Optimización *", setIcon("./resources/icons/tab_icon.png", 10), scrollPaneOptimizationMethodology);
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
