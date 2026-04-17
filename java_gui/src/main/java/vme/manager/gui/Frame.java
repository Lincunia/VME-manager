package vme.manager.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import vme.manager.gui.misc.LanguageManager;
import vme.manager.gui.utils.*;

public class Frame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JPanel titlePanel, bottomPanel;
    private static final JTabbedPane tabbedPane = new JTabbedPane();
    private JScrollPane
        scrollPaneFileAnalysis,
        scrollPaneNetworkState,
        scrollPaneHardwareIntegrity,
        scrollPaneDeviceCleaning,
        scrollPanePredictionOfDevice,
        scrollPaneOptimizationMethodology;
    private static DeviceCleaning deviceCleaning;
    private static FileAnalysis fileAnalysis;
    private static HardwareIntegrity hardwareIntegrity;
    private static NetworkState networkState;
    private static OptimizationMethodology optimizationMethodology;
    private static PredictionOfDevice predictionOfDevice;
    private JButton buttonConfig;
    private JLabel titleProgram;
    private SettingsDialog settingsDialog;
    private LanguageManager langManager;

    public Frame()
    {
        langManager = LanguageManager.getInstance();

        settingsDialog = new SettingsDialog(this, true);
        settingsDialog.load();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle(langManager.getString("app.title"));
        setIconImage(new ImageIcon("./resources/icons/tab_icon.png").getImage());
        setLayout(new BorderLayout());

        initComponents();
        setupLayout();

        pack();
    }

    private void initComponents()
    {
        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        buttonConfig = new JButton(setIcon("./resources/icons/config.png", 20));
        buttonConfig.addActionListener((ActionEvent e) -> {
            settingsDialog.setLocationRelativeTo(this);
            settingsDialog.setVisible(true);
        });

        titleProgram = new JLabel(langManager.getString("app.title"));

        fileAnalysis = new FileAnalysis(tabbedPane);
        scrollPaneFileAnalysis = new JScrollPane(fileAnalysis);

        networkState = new NetworkState(tabbedPane);
        scrollPaneNetworkState = new JScrollPane(networkState);

        hardwareIntegrity = new HardwareIntegrity(tabbedPane);
        scrollPaneHardwareIntegrity = new JScrollPane(hardwareIntegrity);

        deviceCleaning = new DeviceCleaning(tabbedPane);
        scrollPaneDeviceCleaning = new JScrollPane(deviceCleaning);

        predictionOfDevice = new PredictionOfDevice(tabbedPane);
        scrollPanePredictionOfDevice = new JScrollPane(predictionOfDevice);

        optimizationMethodology = new OptimizationMethodology(tabbedPane);
        scrollPaneOptimizationMethodology = new JScrollPane(optimizationMethodology);

        bottomPanel = new JPanel();
    }

    private void setupLayout()
    {
        add(titlePanel, BorderLayout.NORTH);
        titlePanel.add(buttonConfig);

        titlePanel.add(titleProgram);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt)
            {
                if (tabbedPane.getSelectedIndex() == 3) {
                    deviceCleaning.scanGarbage();
                }
            }
        });
        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab(langManager.getString("tab.analysis"),
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPaneFileAnalysis);

        tabbedPane.addTab(langManager.getString("tab.network"),
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPaneNetworkState);

        tabbedPane.addTab(langManager.getString("tab.hardware"),
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPaneHardwareIntegrity);

        tabbedPane.addTab(langManager.getString("tab.cleaning"),
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPaneDeviceCleaning);

        add(bottomPanel, BorderLayout.SOUTH);
        /*
         * Estas son funcionalidades aún más complejas de lo que son, por lo
         * tanto son elementos que van apareciendo poco a poco dentro del
         * proyecto y que por ahora no de van a implementar
         */
        tabbedPane.addTab(langManager.getString("tab.forecast"),
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPanePredictionOfDevice);
        tabbedPane.setEnabledAt(4, false);

        tabbedPane.addTab(langManager.getString("tab.optimization"),
            setIcon("./resources/icons/tab_icon.png", 10),
            scrollPaneOptimizationMethodology);
        tabbedPane.setEnabledAt(5, false);
    }

    public void updateTabTitles()
    {
        tabbedPane.setTitleAt(0, langManager.getString("tab.analysis"));
        tabbedPane.setTitleAt(1, langManager.getString("tab.network"));
        tabbedPane.setTitleAt(2, langManager.getString("tab.hardware"));
        tabbedPane.setTitleAt(3, langManager.getString("tab.cleaning"));
        tabbedPane.setTitleAt(4, langManager.getString("tab.forecast"));
        tabbedPane.setTitleAt(5, langManager.getString("tab.optimization"));
    }

    public void refreshAllTexts()
    {
        setTitle(langManager.getString("app.title"));
        updateTabTitles();
		fileAnalysis.refreshTexts();
		networkState.refreshTexts();
		hardwareIntegrity.refreshTexts();
		deviceCleaning.refreshTexts();
        revalidate();
        repaint();
    }
    private ImageIcon setIcon(String path, int size)
    {
        ImageIcon imageIcon = new ImageIcon(path);
        Image img = imageIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static JTabbedPane getTabbedpane()
    {
        return tabbedPane;
    }
}
