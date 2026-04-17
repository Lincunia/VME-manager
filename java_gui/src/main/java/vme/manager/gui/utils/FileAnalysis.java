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

public class FileAnalysis extends ContainerUtil {
    private JButton buttonAnalyze;
    private JButton buttonClean;
    private JPanel panelGraphics;
    private JSplitPane splitPaneFiles;
    private DefaultListModel<String> defaultListModelFilesFound;
    private JList<String> jListFilesFound;
    private Random random;
    private JLabel statusLabel;
    private GraficoPanel graficoPanel;
	private String fileSearch;

    public FileAnalysis(JTabbedPane jTabbedPane)
    {
        super(jTabbedPane);

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
            "Uso de disco",
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
        buttonAnalyze = new JButton("Iniciar análisis");
        buttonAnalyze.addActionListener((ActionEvent evt) -> {
            analyzeSystem();
        });

        buttonClean = new JButton("Ir a Limpieza");
        buttonClean.setEnabled(false);
        buttonClean.addActionListener((ActionEvent evt) -> {
            getTabbedPaneParent().setSelectedIndex(3);
        });

        // Label de estado
        statusLabel = new JLabel("Listo para analizar");
//        statusLabel.setFont(new Font("sans-serif", Font.ITALIC, 12));
    }

    private void setupLayout()
    {
        // Limpiar paneles antes de añadir componentes
        getPanelTop().removeAll();
        getPanelCenter().removeAll();
        getPanelBottom().removeAll();

        // Panel superior con título
        JLabel titleLabel = new JLabel("Análisis de Archivos");
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

    private void analyzeSystem()
    {
        defaultListModelFilesFound.clear();
        random = new Random();
        statusLabel.setText("Analizando sistema...");
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
                statusLabel.setText("Análisis completado");
            }
        };
        worker.execute();
    }

    private void performAnalysis()
    {
        boolean virusEncontrado = false;

        fileSearch = System.getProperty("user.home");
        if (fileSearch.isEmpty()) {
            System.err.println("No se pudo analizar el punto de entrada: Sistema de archivos equivocado");
            javax.swing.SwingUtilities.invokeLater(() -> {
                statusLabel.setText("Error: No se pudo acceder al sistema de archivos");
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
                    "Se han encontrado archivos sospechosos.\nRevise la lista para más detalles.",
                    "Alerta",
                    JOptionPane.WARNING_MESSAGE);
                buttonClean.setEnabled(true);
                statusLabel.setText("¡Atención! Se encontraron archivos sospechosos");
            } else {
                JOptionPane.showMessageDialog(
                    FileAnalysis.this,
                    "No se encontraron archivos sospechosos.\nEl sistema parece estar seguro.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
                buttonClean.setEnabled(true);
                statusLabel.setText("Sistema seguro - No se encontraron problemas");
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

            String usadoStr = String.format("Usado: %.2f GB", usado / (1024.0 * 1024 * 1024));
            String libreStr = String.format("Libre: %.2f GB", libre / (1024.0 * 1024 * 1024));
            String totalStr = String.format("Total: %.2f GB", total / (1024.0 * 1024 * 1024));
            String porcentajeStr = String.format("Porcentaje usado: %.1f%%", porcentajeUsado);

            g.drawString(usadoStr, 50, 270);
            g.drawString(libreStr, 50, 285);
            g.drawString(totalStr, 50, 300);
            g.drawString(porcentajeStr, 50, 315);

            // Leyenda
            g.setColor(Color.RED);
            g.fillRect(260, 50, 20, 10);
            g.setColor(Color.BLACK);
            g.drawString("Usado", 285, 60);

            g.setColor(Color.GREEN);
            g.fillRect(260, 70, 20, 10);
            g.setColor(Color.BLACK);
            g.drawString("Libre", 285, 80);
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
