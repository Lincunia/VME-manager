package vme.manager.gui.utils;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class DeviceCleaning extends ContainerUtil {
    private JLabel labelCleaning, labelFreed;
    private JButton buttonDelete;
    private JProgressBar progressBarCleanning;
    private DefaultListModel<String> defaultListModelTempFiles;

    private JList<String> jListGarbageFiles;
    public DeviceCleaning(JTabbedPane jTabbedPane)
    {
        super(jTabbedPane);
        initComponents();
    }
    private void initComponents()
    {
        defaultListModelTempFiles = new DefaultListModel<>();

		jListGarbageFiles = new JList<>(defaultListModelTempFiles);
		jListGarbageFiles.setVisibleRowCount(-1);
		getPanelCenter().setLayout(new GridLayout(1, 1));
		getPanelCenter().add(new JScrollPane(jListGarbageFiles));

        getPanelBottom().setLayout(new FlowLayout());

		buttonDelete = new JButton("Eliminar");
        buttonDelete.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar archivos temporales?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_NO_OPTION) {
                return;
            }
            int deletedFiles = deleteGarbage();
            JOptionPane.showMessageDialog(this,
                "Archivos eliminados: " + deletedFiles);
            checkStorage();
        });
        getPanelBottom().add(buttonDelete);

		labelFreed = new JLabel();
        getPanelBottom().add(labelFreed);

		labelCleaning = new JLabel();
        getPanelBottom().add(labelCleaning);

        progressBarCleanning = new JProgressBar();
        getPanelBottom().add(progressBarCleanning);
    }

    public void scanGarbage()
    {
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }

                int progreso = i;

                javax.swing.SwingUtilities.invokeLater(() -> {
                    progressBarCleanning.setValue(progreso);
                    labelCleaning.setText("Escaneando... " + progreso + "%");
                });
            }

            // Al terminar el escaneo
            javax.swing.SwingUtilities.invokeLater(() -> {
                checkStorage();
            });
        }).start();
    }

    public long loadGarbage()
    {
        long total = 0;
        defaultListModelTempFiles.clear();

        String tempPath = System.getProperty("java.io.tmpdir");
        File folder = new File(tempPath);

        for (File f : folder.listFiles()) {
            defaultListModelTempFiles.addElement(f.getAbsolutePath());
            total += f.length();
        }
        return total;
    }

    public int deleteGarbage()
    {
        int deletedFiles = 0;
        File fileRef;
		int dlmtfSize = defaultListModelTempFiles.size();

        for (int i = 0; i < dlmtfSize; i++) {
            fileRef = new File(defaultListModelTempFiles.getElementAt(i));
            if (fileRef.isFile() && fileRef.delete()) {
                deletedFiles++;
            }
            if (fileRef.isDirectory()) {
                deletedFiles += deleteDirectory(fileRef);
            }
        }
        return deletedFiles;
    }

    public int deleteDirectory(File directory)
    {
        int deletedDirectories = 0;
        for (File file : directory.listFiles()) {
            if (file.delete()) {
                deletedDirectories++;
            }
            if (file.isDirectory()) {
                deletedDirectories += deleteDirectory(file);
            }
        }
        if (directory.delete()) {
            deletedDirectories++;
        }
        return deletedDirectories;
    }

    public void checkStorage()
    {
		/*
        double storageToFree = (loadGarbage() / (1024 * 1024));
		System.out.println(storageToFree);
		*/
        labelFreed.setText("Espacio a liberar: " + loadGarbage() + " B");
        labelCleaning.setText("Escaneo completado");
    }
}
