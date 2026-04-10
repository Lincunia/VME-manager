package vme.manager.gui.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public class FileAnalysis extends ContainerUtil {
    private JButton buttonAnalyze;
    private JButton buttonClean;
    private JPanel panelGraphics;

    private JSplitPane splitPaneFiles;
    private DefaultListModel<String> defaultListModelFilesFound;
    private JList<String> jListFilesFound;

    private Random random;

    public FileAnalysis(JTabbedPane jTabbedPane) {
        super(jTabbedPane);

        
        defaultListModelFilesFound = new DefaultListModel<>();
        jListFilesFound = new JList<>(defaultListModelFilesFound);
		jListFilesFound.setPreferredSize(new Dimension(90, 300));

        panelGraphics = new JPanel();
        panelGraphics.setPreferredSize(new Dimension(400, 300));
        splitPaneFiles = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneFiles.setLeftComponent(new JScrollPane(jListFilesFound));
        splitPaneFiles.setRightComponent(new JScrollPane(panelGraphics));
        splitPaneFiles.setResizeWeight(0.5);
        splitPaneFiles.setContinuousLayout(true);

        getPanelCenter().add(splitPaneFiles);

        buttonAnalyze = setButton("Iniciar análisis");
        buttonAnalyze.addActionListener((ActionEvent evt) -> {
            mostrarGrafico();
            analizarSistema();
        }
        );
        getPanelBottom().add(buttonAnalyze);

        buttonClean = setButton("Ir a Limpieza");
        buttonClean.setEnabled(false);
        buttonClean.addActionListener((ActionEvent evt) -> {
            getTabbedPaneParent().setSelectedIndex(3);
        });
        getPanelBottom().add(buttonClean);
    }

    private void analizarSistema() {
        defaultListModelFilesFound.clear();
        random = new Random();

        boolean virusEncontrado = false;

        String fileSearch = getFileStart();
        if (fileSearch.isEmpty()) {
            System.err.println("No se pudo analizar el punto de entrada: Sistemma de archivos equivocado");
            return;
        }

        File carpeta = new File(fileSearch);

        for (File file : carpeta.listFiles()) {
            if (random.nextInt(10) >= 2) {
                defaultListModelFilesFound.addElement("[OK] " + file.getName());
                continue;
            }
            defaultListModelFilesFound.addElement("[PELIGRO] " + file.getName());
            virusEncontrado = true;
        }

        if (virusEncontrado) {
            JOptionPane.showMessageDialog(
                    null,
                    "Sistema en riesgo",
                    "Alerta",
                    JOptionPane.WARNING_MESSAGE);
            buttonClean.setEnabled(true);
            return;
        }
        JOptionPane.showMessageDialog(
                null,
                "Sistema seguro",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        buttonClean.setEnabled(true);
    }

    private void mostrarGrafico() {
        panelGraphics.removeAll();

        JPanel grafico = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String fileSearch = getFileStart();
                if (fileSearch.isEmpty()) {
                    System.err.println("No se pudo analizar el punto de entrada: Sistemma de archivos equivocado");
                    return;
                }
                File disco = new File(fileSearch);
                long total = disco.getTotalSpace();
                long libre = disco.getFreeSpace();
                long usado = total - libre;

                if (total == 0) {
                    return; // evitar error
                }
                int anguloUsado = (int) ((double) usado / total * 360);

                // Gráfico
                g.setColor(Color.RED);
                g.fillArc(50, 50, 200, 200, 0, anguloUsado);

                g.setColor(Color.GREEN);
                g.fillArc(50, 50, 200, 200, anguloUsado, 360 - anguloUsado);

                // Texto
                g.setColor(Color.BLACK);
                g.drawString("Rojo = Usado", 50, 270);
                g.drawString("Verde = Libre", 50, 290);
            }
        };

        grafico.setPreferredSize(panelGraphics.getSize());

        panelGraphics.setLayout(new java.awt.BorderLayout());
        panelGraphics.add(grafico, java.awt.BorderLayout.CENTER);

        panelGraphics.revalidate();
        panelGraphics.repaint();
    }

    private String getFileStart() {
        String osName = System.getProperty("os.name").toLowerCase();
        String fileSearch = "";
        if (osName.contains("win")) {
            fileSearch = "C:/Users";
        }
        if (osName.contains("mac")) {
            fileSearch = "/"; // Tengo que buscar el sistema de archivos de Apple XD
        }
        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            fileSearch = "/";
        }
        return fileSearch;
    }
}
