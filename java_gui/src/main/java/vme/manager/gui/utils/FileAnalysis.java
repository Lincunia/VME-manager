package vme.manager.gui.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import vme.manager.gui.misc.LanguageManager;

public class FileAnalysis extends ContainerUtil {
    private JButton buttonAnalyze;
    private JButton buttonClean;
    private JPanel panelGraphics;
    private JSplitPane splitPaneFiles;
    private DefaultListModel<String> defaultListModelFilesFound;
    private JList<String> jListFilesFound;
    private Random random;
    private JLabel statusLabel, titleLabel;
    private GraficoPanel graficoPanel;
    private String fileSearch;
	private LanguageManager langManager;

    public FileAnalysis(JTabbedPane jTabbedPane)
    {
        super(jTabbedPane);
        langManager = LanguageManager.getInstance();
        initComponents();
        setupLayout();
    }

    private void initComponents()
    {
        // Lista de archivos encontrados
        defaultListModelFilesFound = new DefaultListModel<>();
        jListFilesFound = new JList<>(defaultListModelFilesFound);

        // Panel para el gráfico
        graficoPanel = new GraficoPanel();
        panelGraphics = new JPanel(new BorderLayout());
        panelGraphics.add(graficoPanel, BorderLayout.CENTER);
        panelGraphics.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            langManager.getString("fileanalysis.disk.usage"),
            TitledBorder.LEFT,
            TitledBorder.TOP));

        // Split pane con proporción 40% lista, 60% gráfico
        splitPaneFiles = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneFiles.setLeftComponent(new JScrollPane(jListFilesFound));
        splitPaneFiles.setRightComponent(panelGraphics);
        splitPaneFiles.setResizeWeight(0.4); // 40% para la lista
        splitPaneFiles.setContinuousLayout(true);
        splitPaneFiles.setDividerLocation(0.4);

        // Botones
        buttonAnalyze = new JButton(langManager.getString("fileanalysis.button.analyze"));
        buttonAnalyze.addActionListener((ActionEvent evt) -> {
            analyzeSystem();
        });

        buttonClean = new JButton(langManager.getString("fileanalysis.button.clean"));
        buttonClean.setEnabled(false);
        buttonClean.addActionListener((ActionEvent evt) -> {
            getTabbedPaneParent().setSelectedIndex(3);
        });

        // Label de estado
        statusLabel = new JLabel(langManager.getString("fileanalysis.status.ready"));
        //        statusLabel.setFont(new Font("sans-serif", Font.ITALIC, 12));
    }

    private void setupLayout()
    {
        // Limpiar paneles antes de añadir componentes
        getPanelTop().removeAll();
        getPanelCenter().removeAll();
        getPanelBottom().removeAll();

        // Panel superior con título
        titleLabel = new JLabel(langManager.getString("fileanalysis.title"));
        //        titleLabel.setFont(new Font("sans-serif", Font.BOLD, 18));
        getPanelTop().add(titleLabel);

        // Panel central con el split pane
        getPanelCenter().setLayout(new BorderLayout());
        getPanelCenter().add(splitPaneFiles, BorderLayout.CENTER);

        // Panel inferior con botones y estado
        JPanel bottomButtonPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonAnalyze);
        buttonPanel.add(buttonClean);

        bottomButtonPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomButtonPanel.add(statusLabel, BorderLayout.SOUTH);

        getPanelBottom().setLayout(new BorderLayout());
        getPanelBottom().add(bottomButtonPanel, BorderLayout.CENTER);

        // Actualizar UI
        revalidate();
        repaint();
    }

    public void refreshTexts()
    {
        // Actualizar textos de los componentes
        buttonAnalyze.setText(langManager.getString("fileanalysis.button.analyze"));
        buttonClean.setText(langManager.getString("fileanalysis.button.clean"));
        statusLabel.setText(langManager.getString("fileanalysis.status.ready"));

        // Actualizar borde del panel gráfico
        panelGraphics.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            langManager.getString("fileanalysis.disk.usage"),
            TitledBorder.LEFT,
            TitledBorder.TOP));

        titleLabel.setText(langManager.getString("fileanalysis.title"));

        revalidate();
        repaint();
    }

    private void analyzeSystem()
    {
        defaultListModelFilesFound.clear();
        random = new Random();
        statusLabel.setText(langManager.getString("fileanalysis.status.analyzing"));
        buttonAnalyze.setEnabled(false);

        // Simular proceso de análisis
        javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground()
            {
                performAnalysis();
                return null;
            }

            @Override
            protected void done()
            {
                buttonAnalyze.setEnabled(true);
                statusLabel.setText(langManager.getString("fileanalysis.status.completed"));
            }
        };
        worker.execute();
    }

    private void performAnalysis()
    {
        boolean virusEncontrado = false;

        fileSearch = System.getProperty("user.home");
        if (fileSearch.isEmpty()) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                statusLabel.setText(langManager.getString("fileanalysis.status.error"));
            });
            return;
        }

        File folderStart = new File(fileSearch);

        if (!folderStart.exists() || !folderStart.isDirectory()) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Error: Directorio no válido: " + fileSearch);
            });
            return;
        }

        File[] files = folderStart.listFiles();
        if (files == null) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Error: No se pudo leer el directorio");
            });
            return;
        }

        for (File file : files) {
            try {
                Thread.sleep(10); // Simular trabajo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (random.nextInt(10) >= 2) {
                final String fileName = "[OK] " + file.getName();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    defaultListModelFilesFound.addElement(fileName);
                });
                continue;
            }
            final String fileName = "[PELIGRO] " + file.getName();
            javax.swing.SwingUtilities.invokeLater(() -> {
                defaultListModelFilesFound.addElement(fileName);
            });
            virusEncontrado = true;
        }

        final boolean finalVirusEncontrado = virusEncontrado;
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (finalVirusEncontrado) {
                JOptionPane.showMessageDialog(
                    FileAnalysis.this,
                    langManager.getString("fileanalysis.alert.message"),
                    langManager.getString("fileanalysis.alert.title"),
                    JOptionPane.WARNING_MESSAGE);
                buttonClean.setEnabled(true);
                statusLabel.setText("¡Atención! Se encontraron archivos sospechosos");
            } else {
                JOptionPane.showMessageDialog(
                    FileAnalysis.this,
                    langManager.getString("fileanalysis.info.message"),
                    langManager.getString("fileanalysis.info.title"),
                    JOptionPane.INFORMATION_MESSAGE);
                buttonClean.setEnabled(true);
                statusLabel.setText("fileanalysis.status.safe");
            }
            graficoPanel.repaint();
        });
    }

    private class GraficoPanel extends JPanel {
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            fileSearch = System.getProperty("user.home");
            if (fileSearch.isEmpty()) {
                g.setColor(Color.RED);
                g.drawString("No se pudo analizar el disco", 50, 150);
                return;
            }

            File disco = new File(fileSearch);
            if (!disco.exists()) {
                g.setColor(Color.RED);
                g.drawString("No se puede acceder al disco", 50, 150);
                return;
            }

            long total = disco.getTotalSpace();
            long libre = disco.getFreeSpace();
            long usado = total - libre;

            if (total == 0) {
                g.drawString("No se puede determinar el espacio del disco", 50, 150);
                return;
            }

            // Calcular ángulos
            double porcentajeUsado = (double)usado / total * 100;
            int anguloUsado = (int)(porcentajeUsado * 3.6);

            // Dibujar gráfico circular
            int x = 50, y = 50, size = 200;

            g.setColor(Color.RED);
            g.fillArc(x, y, size, size, 0, anguloUsado);

            g.setColor(Color.GREEN);
            g.fillArc(x, y, size, size, anguloUsado, 360 - anguloUsado);

            // Dibujar borde del círculo
            g.setColor(Color.BLACK);
            g.drawOval(x, y, size, size);

            // Texto de estadísticas
            //            g.setFont(new Font("sans-serif", Font.PLAIN, 12));
            g.setColor(Color.BLACK);

            String usadoStr = String.format(langManager.getString("fileanalysis.disk.used") + ": %.2f GB", usado / (1024.0 * 1024 * 1024));
            String libreStr = String.format(langManager.getString("fileanalysis.disk.free") + ": %.2f GB", libre / (1024.0 * 1024 * 1024));
            String totalStr = String.format(langManager.getString("fileanalysis.disk.total") + ": %.2f GB", total / (1024.0 * 1024 * 1024));
            String porcentajeStr = String.format(langManager.getString("fileanalysis.disk.percentage") + ": %.1f%%", porcentajeUsado);

            g.drawString(usadoStr, 50, 270);
            g.drawString(libreStr, 50, 285);
            g.drawString(totalStr, 50, 300);
            g.drawString(porcentajeStr, 50, 315);

            // Leyenda
            g.setColor(Color.RED);
            g.fillRect(260, 50, 20, 10);
            g.setColor(Color.BLACK);
            g.drawString(langManager.getString("fileanalysis.disk.used"), 285, 60);

            g.setColor(Color.GREEN);
            g.fillRect(260, 70, 20, 10);
            g.setColor(Color.BLACK);
            g.drawString(langManager.getString("fileanalysis.disk.free"), 285, 80);
        }

        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(400, 350);
        }

        @Override
        public Dimension getMinimumSize()
        {
            return new Dimension(300, 250);
        }
    }
}
