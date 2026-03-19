package antivirus;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JFrame;

/*import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;*/

import javax.swing.DefaultListModel;
import java.io.File;
import java.util.Random;
import javax.swing.JPanel;

public class VistaAntivirus extends javax.swing.JFrame {

    public VistaAntivirus() {
        initComponents();
        setLocationRelativeTo(null);
        setMinimumSize(new java.awt.Dimension(800, 500));
        
        BtnLimpiar.setEnabled(false); // botón desactivado al inicio
        jListArchivos.setModel(modeloLista);
        jListBasura.setModel(modeloBasura);
        
        ImageIcon iconAjustes = new ImageIcon(getClass().getResource("/antivirus/icons/config.png"));
        Image imgAjustes = iconAjustes.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        BtnAjustes.setIcon(new ImageIcon(imgAjustes));

        ImageIcon iconUsuario = new ImageIcon(getClass().getResource("/antivirus/icons/user.png"));
        Image imgUsuario = iconUsuario.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        BtnUsuario.setIcon(new ImageIcon(imgUsuario));
        
        /*tablaBasura.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {},
        new String [] {
            "Nombre", "Ruta", "Tipo", "Estado"
            }
        ));*/
    }
    
    public void mostrarGrafico() {
        panelGrafico.removeAll();

        JPanel grafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                File disco = new File("C:/");
                long total = disco.getTotalSpace();
                long libre = disco.getFreeSpace();
                long usado = total - libre;

                if (total == 0) return; // evitar error

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

        grafico.setPreferredSize(panelGrafico.getSize());

        panelGrafico.setLayout(new java.awt.BorderLayout());
        panelGrafico.add(grafico, java.awt.BorderLayout.CENTER);

        panelGrafico.revalidate();
        panelGrafico.repaint();
    }
    
    public void analizarSistema() {
        modeloLista.clear();
        Random random = new Random();

        File carpeta = new File("C:/Users");
        File[] archivos = carpeta.listFiles();

        boolean virusEncontrado = false;

        if (archivos != null) {
            for (int i = 0; i < archivos.length && i < 20; i++) {
                File f = archivos[i];

                if (random.nextInt(10) < 2) {
                    modeloLista.addElement("[PELIGRO] " + f.getName());
                    virusEncontrado = true;
                } else {
                    modeloLista.addElement("[OK] " + f.getName());
                }
            }
        }

        if (virusEncontrado) {
            lblEstado.setText("Sistema en riesgo");
            BtnLimpiar.setEnabled(true);
        } else {
            lblEstado.setText("Sistema seguro");
            BtnLimpiar.setEnabled(false);
        }
    }
    
    public long buscarArchivosBasura() {
        modeloBasura.clear();

        String temp = System.getProperty("java.io.tmpdir");
        File carpetaTemp = new File(temp);

        File[] archivos = carpetaTemp.listFiles();

        long total = 0;

        if (archivos != null) {
            for (File f : archivos) {
                modeloBasura.addElement(f.getAbsolutePath());
                total += f.length();
            }
        }

        return total;
    }
    
    public boolean esBasura(File f) {
        String nombre = f.getName().toLowerCase();

        return nombre.endsWith(".tmp") ||
               nombre.endsWith(".log") ||
               nombre.endsWith(".bak") ||
               nombre.contains("cache");
    }
    
    public void cargarBasura() {
        modeloBasura.clear();

        String tempPath = System.getProperty("java.io.tmpdir");
        File carpeta = new File(tempPath);

        File[] archivos = carpeta.listFiles();

        if (archivos != null) {
            for (File f : archivos) {
                modeloBasura.addElement(f.getAbsolutePath());
            }
        }
    }
    
    public void escaneoConProgreso() {
        new Thread(() -> {

            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {}

                int progreso = i;

                javax.swing.SwingUtilities.invokeLater(() -> {
                    progresoLimpieza.setValue(progreso);
                    lblEstadoLimpieza.setText("Escaneando... " + progreso + "%");
                });
            }

            // Al terminar el escaneo
            javax.swing.SwingUtilities.invokeLater(() -> {
                cargarBasura();
                long espacio = buscarBasura();

                lblEspacioLiberado.setText(
                    "Espacio a liberar: " + (espacio / (1024 * 1024)) + " MB"
                );

                lblEstadoLimpieza.setText("Escaneo completado");
            });

        }).start();
    }
    
    public int eliminarTemporales() {
        String tempPath = System.getProperty("java.io.tmpdir");
        File carpeta = new File(tempPath);

        int eliminados = 0;

        File[] archivos = carpeta.listFiles();

        if (archivos != null) {
            for (File f : archivos) {

                if (f.isFile()) {
                    if (f.delete()) eliminados++;
                }

                if (f.isDirectory()) {
                    eliminados += eliminarCarpeta(f);
                }
            }
        }

        return eliminados;
    }
    
    public int eliminarCarpeta(File carpeta) {
        int eliminados = 0;

        File[] archivos = carpeta.listFiles();

        if (archivos != null) {
            for (File f : archivos) {

                if (f.isDirectory()) {
                    eliminados += eliminarCarpeta(f);
                }

                if (f.delete()) {
                    eliminados++;
                }
            }
        }

        if (carpeta.delete()) {
            eliminados++;
        }

        return eliminados;
    }
    
    public long buscarBasura() {
        long total = 0;

        String tempPath = System.getProperty("java.io.tmpdir");
        File carpeta = new File(tempPath);

        File[] archivos = carpeta.listFiles();

        if (archivos != null) {
            for (File f : archivos) {
                total += f.length();
            }
        }

        return total;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        VistaAntivirus = new javax.swing.JPanel();
        TpPestañas = new javax.swing.JTabbedPane();
        JpAnalisisDeDatos = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        panelGrafico = new javax.swing.JPanel();
        panelBasura = new javax.swing.JPanel();
        JsListaArchivos = new javax.swing.JScrollPane();
        jListArchivos = new javax.swing.JList<>();
        BtnLimpiar = new javax.swing.JButton();
        BtnAnalizar = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();
        JpEstadoDeLaRed = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        JpIntegridadDeHarware = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        JpLimpieza = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListBasura = new javax.swing.JList<>();
        BtnEliminarBasura = new javax.swing.JButton();
        lblEspacioLiberado = new javax.swing.JLabel();
        progresoLimpieza = new javax.swing.JProgressBar();
        lblEstadoLimpieza = new javax.swing.JLabel();
        JpPronóstico = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        JpOptimización = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        BtnAjustes = new javax.swing.JButton();
        BtnUsuario = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TpPestañas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TpPestañasStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelGraficoLayout = new javax.swing.GroupLayout(panelGrafico);
        panelGrafico.setLayout(panelGraficoLayout);
        panelGraficoLayout.setHorizontalGroup(
            panelGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 641, Short.MAX_VALUE)
        );
        panelGraficoLayout.setVerticalGroup(
            panelGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 385, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(panelGrafico);

        panelBasura.setLayout(new java.awt.BorderLayout());

        JsListaArchivos.setViewportView(jListArchivos);

        panelBasura.add(JsListaArchivos, java.awt.BorderLayout.CENTER);

        BtnLimpiar.setText("Ir a Limpieza");
        BtnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnLimpiarActionPerformed(evt);
            }
        });
        panelBasura.add(BtnLimpiar, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setLeftComponent(panelBasura);

        BtnAnalizar.setText("Iniciar análisis");
        BtnAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAnalizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JpAnalisisDeDatosLayout = new javax.swing.GroupLayout(JpAnalisisDeDatos);
        JpAnalisisDeDatos.setLayout(JpAnalisisDeDatosLayout);
        JpAnalisisDeDatosLayout.setHorizontalGroup(
            JpAnalisisDeDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
            .addGroup(JpAnalisisDeDatosLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(BtnAnalizar))
        );
        JpAnalisisDeDatosLayout.setVerticalGroup(
            JpAnalisisDeDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpAnalisisDeDatosLayout.createSequentialGroup()
                .addComponent(jSplitPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JpAnalisisDeDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnAnalizar)
                    .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        TpPestañas.addTab("Análisis de Datos", JpAnalisisDeDatos);

        jLabel3.setText("En Desarrollo2");

        javax.swing.GroupLayout JpEstadoDeLaRedLayout = new javax.swing.GroupLayout(JpEstadoDeLaRed);
        JpEstadoDeLaRed.setLayout(JpEstadoDeLaRedLayout);
        JpEstadoDeLaRedLayout.setHorizontalGroup(
            JpEstadoDeLaRedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpEstadoDeLaRedLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel3)
                .addContainerGap(626, Short.MAX_VALUE))
        );
        JpEstadoDeLaRedLayout.setVerticalGroup(
            JpEstadoDeLaRedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpEstadoDeLaRedLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addContainerGap(392, Short.MAX_VALUE))
        );

        TpPestañas.addTab("Estado de la Red", JpEstadoDeLaRed);

        jLabel4.setText("En Desarrollo3");

        javax.swing.GroupLayout JpIntegridadDeHarwareLayout = new javax.swing.GroupLayout(JpIntegridadDeHarware);
        JpIntegridadDeHarware.setLayout(JpIntegridadDeHarwareLayout);
        JpIntegridadDeHarwareLayout.setHorizontalGroup(
            JpIntegridadDeHarwareLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpIntegridadDeHarwareLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel4)
                .addContainerGap(626, Short.MAX_VALUE))
        );
        JpIntegridadDeHarwareLayout.setVerticalGroup(
            JpIntegridadDeHarwareLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpIntegridadDeHarwareLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addContainerGap(392, Short.MAX_VALUE))
        );

        TpPestañas.addTab("Integrida de Hardware", JpIntegridadDeHarware);

        jScrollPane1.setViewportView(jListBasura);

        BtnEliminarBasura.setText("Eliminar");
        BtnEliminarBasura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEliminarBasuraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JpLimpiezaLayout = new javax.swing.GroupLayout(JpLimpieza);
        JpLimpieza.setLayout(JpLimpiezaLayout);
        JpLimpiezaLayout.setHorizontalGroup(
            JpLimpiezaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpLimpiezaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JpLimpiezaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(JpLimpiezaLayout.createSequentialGroup()
                        .addComponent(BtnEliminarBasura)
                        .addGap(18, 18, 18)
                        .addComponent(lblEspacioLiberado, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEstadoLimpieza, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(progresoLimpieza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 92, Short.MAX_VALUE)))
                .addContainerGap())
        );
        JpLimpiezaLayout.setVerticalGroup(
            JpLimpiezaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpLimpiezaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JpLimpiezaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BtnEliminarBasura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEspacioLiberado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progresoLimpieza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEstadoLimpieza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        TpPestañas.addTab("Limpieza", JpLimpieza);

        jLabel6.setText("En Desarrollo5");

        javax.swing.GroupLayout JpPronósticoLayout = new javax.swing.GroupLayout(JpPronóstico);
        JpPronóstico.setLayout(JpPronósticoLayout);
        JpPronósticoLayout.setHorizontalGroup(
            JpPronósticoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpPronósticoLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel6)
                .addContainerGap(626, Short.MAX_VALUE))
        );
        JpPronósticoLayout.setVerticalGroup(
            JpPronósticoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpPronósticoLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel6)
                .addContainerGap(392, Short.MAX_VALUE))
        );

        TpPestañas.addTab("Pronóstico", JpPronóstico);

        jLabel7.setText("En Desarrollo6");

        javax.swing.GroupLayout JpOptimizaciónLayout = new javax.swing.GroupLayout(JpOptimización);
        JpOptimización.setLayout(JpOptimizaciónLayout);
        JpOptimizaciónLayout.setHorizontalGroup(
            JpOptimizaciónLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpOptimizaciónLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel7)
                .addContainerGap(626, Short.MAX_VALUE))
        );
        JpOptimizaciónLayout.setVerticalGroup(
            JpOptimizaciónLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpOptimizaciónLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel7)
                .addContainerGap(392, Short.MAX_VALUE))
        );

        TpPestañas.addTab("Optimización", JpOptimización);

        BtnAjustes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAjustesActionPerformed(evt);
            }
        });

        BtnUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUsuarioActionPerformed(evt);
            }
        });

        jLabel1.setText("VME manager");

        javax.swing.GroupLayout VistaAntivirusLayout = new javax.swing.GroupLayout(VistaAntivirus);
        VistaAntivirus.setLayout(VistaAntivirusLayout);
        VistaAntivirusLayout.setHorizontalGroup(
            VistaAntivirusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VistaAntivirusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VistaAntivirusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(VistaAntivirusLayout.createSequentialGroup()
                        .addComponent(BtnAjustes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(BtnUsuario)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(TpPestañas, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        VistaAntivirusLayout.setVerticalGroup(
            VistaAntivirusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, VistaAntivirusLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(VistaAntivirusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnUsuario)
                    .addComponent(BtnAjustes)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TpPestañas)
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(VistaAntivirus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(VistaAntivirus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnAjustesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAjustesActionPerformed
        javax.swing.JOptionPane.showMessageDialog(this, "Contenido en desarrollo");
    }//GEN-LAST:event_BtnAjustesActionPerformed

    private void BtnUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUsuarioActionPerformed
        javax.swing.JOptionPane.showMessageDialog(this, "Contenido en desarrollo");
    }//GEN-LAST:event_BtnUsuarioActionPerformed

    private void BtnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLimpiarActionPerformed
        TpPestañas.setSelectedIndex(3);
    }//GEN-LAST:event_BtnLimpiarActionPerformed

    private void BtnAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAnalizarActionPerformed
        mostrarGrafico();
        analizarSistema();
    }//GEN-LAST:event_BtnAnalizarActionPerformed

    private void TpPestañasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TpPestañasStateChanged
        if (TpPestañas.getSelectedIndex() == 3) {
        escaneoConProgreso();
    }
    }//GEN-LAST:event_TpPestañasStateChanged

    private void BtnEliminarBasuraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEliminarBasuraActionPerformed
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "¿Eliminar archivos temporales?",
            "Confirmación",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

        int eliminados = eliminarTemporales();

        javax.swing.JOptionPane.showMessageDialog(this,
            "Archivos eliminados: " + eliminados);

        cargarBasura();

        long espacio = buscarBasura();

        lblEspacioLiberado.setText("Espacio a liberar: " + (espacio / (1024 * 1024)) + " MB");
    }//GEN-LAST:event_BtnEliminarBasuraActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaAntivirus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaAntivirus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaAntivirus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaAntivirus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaAntivirus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAjustes;
    private javax.swing.JButton BtnAnalizar;
    private javax.swing.JButton BtnEliminarBasura;
    private javax.swing.JButton BtnLimpiar;
    private javax.swing.JButton BtnUsuario;
    private javax.swing.JPanel JpAnalisisDeDatos;
    private javax.swing.JPanel JpEstadoDeLaRed;
    private javax.swing.JPanel JpIntegridadDeHarware;
    private javax.swing.JPanel JpLimpieza;
    private javax.swing.JPanel JpOptimización;
    private javax.swing.JPanel JpPronóstico;
    private javax.swing.JScrollPane JsListaArchivos;
    private javax.swing.JTabbedPane TpPestañas;
    private javax.swing.JPanel VistaAntivirus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jListArchivos;
    private javax.swing.JList<String> jListBasura;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblEspacioLiberado;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblEstadoLimpieza;
    private javax.swing.JPanel panelBasura;
    private javax.swing.JPanel panelGrafico;
    private javax.swing.JProgressBar progresoLimpieza;
    // End of variables declaration//GEN-END:variables
    DefaultListModel<String> modeloLista = new DefaultListModel<>();
    DefaultListModel<String> modeloBasura = new DefaultListModel<>();
    long espacioTotal = 0;
}
