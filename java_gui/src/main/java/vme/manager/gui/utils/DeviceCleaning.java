package vme.manager.gui.utils;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import vme.manager.gui.misc.LanguageManager;

public class DeviceCleaning extends ContainerUtil {
    private JLabel labelCleaning, labelFreed;
    private JButton buttonDelete;
    private JProgressBar progressBarCleanning;
    private DefaultListModel<String> defaultListModelTempFiles;
    private JList<String> jListGarbageFiles;
	private LanguageManager langManager;

    public DeviceCleaning(JTabbedPane jTabbedPane)
    {
        super(jTabbedPane);
		langManager = LanguageManager.getInstance();
        initComponents();
        setupLayout();
    }

    private void initComponents()
    {
        defaultListModelTempFiles = new DefaultListModel<>();
        jListGarbageFiles = new JList<>(defaultListModelTempFiles);
        jListGarbageFiles.setVisibleRowCount(-1);

        buttonDelete = new JButton(langManager.getString("devicecleaning.button.delete"));
        buttonDelete.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
				langManager.getString("devicecleaning.confirm.message"),
				langManager.getString("devicecleaning.files.deleted"),
                JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_NO_OPTION) {
                return;
            }
            int deletedFiles = deleteGarbage();
            JOptionPane.showMessageDialog(this,
					langManager.getString("devicecleaning.files.deleted") + deletedFiles);
            checkStorage();
        });

        labelFreed = new JLabel();
        labelCleaning = new JLabel();
        progressBarCleanning = new JProgressBar();
    }

    private void setupLayout()
    {
        getPanelCenter().setLayout(new GridLayout(1, 1));
        getPanelCenter().add(new JScrollPane(jListGarbageFiles));

        getPanelBottom().setLayout(new FlowLayout());
        getPanelBottom().add(buttonDelete);
        getPanelBottom().add(labelFreed);
        getPanelBottom().add(labelCleaning);
        getPanelBottom().add(progressBarCleanning);
    }

	public void refreshTexts(){
        labelFreed.setText(langManager.getString("devicecleaning.space.free") + loadGarbage() + " B");
        labelCleaning.setText(langManager.getString("devicecleaning.completed"));
        buttonDelete.setText(langManager.getString("devicecleaning.button.delete"));
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
                    labelCleaning.setText(
							langManager.getString("devicecleaning.scanning")
							+ progreso + "%");
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
		if(directory.listFiles() == null){
			return deletedDirectories;
		}
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
        labelFreed.setText(langManager.getString("devicecleaning.space.free")
				+ loadGarbage() + " B");
        labelCleaning.setText(langManager.getString("devicecleaning.completed"));
    }
}
