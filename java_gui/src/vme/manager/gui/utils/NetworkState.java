package vme.manager.gui.utils;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class NetworkState extends ContainerBoilerPlate {
    private JPanel
        panelLatency,
        panelDownload,
        panelUpload;
    private JButton
        buttonMeasureMetrics;
    public NetworkState(JTabbedPane jTabbedPane)
    {
        super("sans-serif", 20, jTabbedPane);

        getPanelCenter().setLayout(new BoxLayout(
            getPanelCenter(),
            BoxLayout.Y_AXIS));
        measureMetrics();
        measureDownloads();
        measureUploads();
    }
    public void measureMetrics()
    {
        panelLatency = new JPanel();
        panelLatency.setLayout(new GridLayout(8, 1));

        panelLatency.add(setLabel("Métricas de latencia: "));
        JTextField textFieldHost = setTextField(10);
        textFieldHost.setText("google.com");
        panelLatency.add(textFieldHost);

        panelLatency.add(setLabel("Puerto: "));
        JTextField textFieldPort = setTextField(10);
        textFieldPort.setText("80");
        panelLatency.add(textFieldPort);

        panelLatency.add(setLabel("Timeout (ms): "));
        JTextField textFieldTimeout = setTextField(10);
        textFieldTimeout.setText("2000");
        panelLatency.add(textFieldTimeout);

        panelLatency.add(setLabel("Conteo de pings: "));
        JTextField textFieldCount = setTextField(10);
        textFieldCount.setText("5");
        panelLatency.add(textFieldCount);
        buttonMeasureMetrics = setButton("Medir métricas");
        buttonMeasureMetrics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt)
            {
                String host = textFieldHost.getText();
                int port = parseFields(textFieldPort.getText());
                int timeoutMs = parseFields(textFieldTimeout.getText());
                int count = parseFields(textFieldCount.getText());

                double avgLatency = averagePing(host, port, timeoutMs, count);
                if (avgLatency != -1) {
                    System.out.printf("%nAverage Latency to %s:%d: %.2f ms%n", host, port, avgLatency);
                } else {
                    System.out.println("All pings failed.");
                }
            }
        });

        getPanelCenter().add(panelLatency);
    }
    public void measureDownloads()
    {
        panelDownload = new JPanel();
        panelDownload.add(setLabel("Métricas de descarga: "));
        getPanelCenter().add(panelDownload);
    }
    public void measureUploads()
    {
        panelUpload = new JPanel();
        panelUpload.add(setLabel("Métricas de envío: "));
        getPanelCenter().add(panelUpload);
    }

    public static long ping(String host, int port, int timeoutMs)
    {
        long startTime = System.currentTimeMillis();
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMs);
            long endTime = System.currentTimeMillis();
            return endTime - startTime;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                e.getMessage(),
                "Conexión fallida",
                JOptionPane.WARNING_MESSAGE);
            return -1;
        }
    }

    public static double averagePing(String host, int port, int timeoutMs, int count)
    {
        long totalLatency = 0;
        int successfulPings = 0;

        for (int i = 0; i < count; i++) {
            long latency = ping(host, port, timeoutMs);
            if (latency != -1) {
                totalLatency += latency;
                successfulPings++;
                System.out.printf("Ping %d: %d ms%n", i + 1, latency);
            } else {
                System.out.printf("Ping %d: Failed%n", i + 1);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (successfulPings == 0)
            return -1;
        return (double)totalLatency / successfulPings;
    }

    private int parseFields(String str)
    {
        return (!Pattern.matches("^[1-9]\\d*$", str)) ? 0 : Integer.parseInt(str);
    }
}
