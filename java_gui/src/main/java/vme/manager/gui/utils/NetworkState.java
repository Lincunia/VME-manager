package vme.manager.gui.utils;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NetworkState extends ContainerUtil {
    private static JButton buttonMsrLatency;
    private static JTextArea textAreaNetInt, textAreaLatency;
    private static JTextField
        textFieldHost,
        textFieldPort,
        textFieldTimeout,
        textFieldCount;
    public NetworkState(JTabbedPane jTabbedPane)
    {
        super(jTabbedPane);

        initComponents();
    }

    private void initComponents()
    {
        getPanelBottom().setLayout(new BoxLayout(getPanelBottom(), BoxLayout.X_AXIS));

        getPanelLeft().setLayout(new BoxLayout(getPanelLeft(), BoxLayout.Y_AXIS));
        getPanelLeft().setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            "Interfaces de red del dispositivo"));

        getPanelRight().setLayout(new BoxLayout(getPanelRight(), BoxLayout.Y_AXIS));
        getPanelRight().setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            "Medición de latencia"));

        textAreaNetInt = new JTextArea();
        textAreaNetInt.setEditable(false);
        textAreaNetInt.setText(getNetworkInterfaceAddresses());
        getPanelLeft().add(new JScrollPane(textAreaNetInt));

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado
        gbc.anchor = GridBagConstraints.EAST; // Alinear etiquetas a la derecha
        // Etiquetas (columna 0)
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Host:"), gbc);

        gbc.gridy = 1;
        panelForm.add(new JLabel("Puerto:"), gbc);

        gbc.gridy = 2;
        panelForm.add(new JLabel("Timeout (ms):"), gbc);

        gbc.gridy = 3;
        panelForm.add(new JLabel("Conteo:"), gbc);

        // Campos de texto (columna 1)
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST; // Alinear campos a la izquierda
        gbc.fill = GridBagConstraints.HORIZONTAL; // Expandir horizontalmente
        gbc.weightx = 1.0; // Ocupar espacio extra

        gbc.gridy = 0;
        textFieldHost = new JTextField(15);
        textFieldHost.setText("google.com");
        panelForm.add(textFieldHost, gbc);

        gbc.gridy = 1;
        textFieldPort = new JTextField(15);
        textFieldPort.setText("80");
        panelForm.add(textFieldPort, gbc);

        gbc.gridy = 2;
        textFieldTimeout = new JTextField(15);
        textFieldTimeout.setText("2000");
        panelForm.add(textFieldTimeout, gbc);

        gbc.gridy = 3;
        textFieldCount = new JTextField(15);
        textFieldCount.setText("5");
        panelForm.add(textFieldCount, gbc);

        getPanelRight().add(panelForm);

        buttonMsrLatency = new JButton("Empezar la medición");
        buttonMsrLatency.addActionListener((ActionEvent evt) -> {
            String host = textFieldHost.getText();
            int port = Integer.parseInt(textFieldPort.getText());
            int timeoutMs = Integer.parseInt(textFieldTimeout.getText());
            int count = Integer.parseInt(textFieldCount.getText());

            String output = "";
            double avgLatency = averagePing(host, port, timeoutMs, count);
            if (avgLatency != -1) {
                output = "Latencia promedio de " + host + ":" + port + " es"
                    + "\n" + avgLatency;
            } else {
                output = "Los pings fallaron";
            }
			textAreaLatency.append("\n" + output);
        });
        getPanelRight().add(buttonMsrLatency);

        textAreaLatency = new JTextArea();
        textAreaLatency.setEditable(false);
        getPanelRight().add(new JScrollPane(textAreaLatency));
    }

    public static double averagePing(String host, int port, int timeoutMs, int count)
    {
        long totalLatency = 0;
        int successfulPings = 0;
        String pingMessage = "";

        for (int i = 0; i < count; i++) {
            long latency = ping(host, port, timeoutMs);
            if (latency != -1) {
                totalLatency += latency;
                successfulPings++;
                pingMessage = "Ping " + (i + 1) + ": " + latency + " ms\n";
            } else {
                pingMessage = "Ping " + (i + 1) + ": fallido\n";
            }
            textAreaLatency.append(pingMessage);
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

    public static long ping(String host, int port, int timeoutMs)
    {
        long startTime = System.currentTimeMillis();
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMs);
            long endTime = System.currentTimeMillis();
            return endTime - startTime; // Latency in ms
        } catch (SocketTimeoutException e) {
            JOptionPane.showMessageDialog(
                null,
                e.getMessage(),
                "Ping rebasó el tiempo",
                JOptionPane.WARNING_MESSAGE);
            return -1;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                e.getMessage(),
                "Conexion fallida",
                JOptionPane.WARNING_MESSAGE);
            return -1;
        }
    }

    public String getNetworkInterfaceAddresses()
    {
        String interfaces = "";
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                interfaces += getInterfacesInfo(netint);
                interfaces += getSubInterfacesInfo(netint);
                interfaces += "\n==========================\n";
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return interfaces;
    }

    public String getInterfacesInfo(NetworkInterface netInt) throws SocketException
    {
        String interfaceStr = "";
        interfaceStr = "Nombre de la interfaz: " + netInt.getDisplayName()
            + "\nNombre real de la interfaz: " + netInt.getName();
        Enumeration<InetAddress> inetAddresses = netInt.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            interfaceStr += "\nDirección: " + inetAddress;
        }
        return interfaceStr;
    }

    public String getSubInterfacesInfo(NetworkInterface netIf) throws SocketException
    {
        String subInterefaceStr = "";
        Enumeration<NetworkInterface> subIfs = netIf.getSubInterfaces();

        // Iterating over sub networks list
        for (NetworkInterface subIf : Collections.list(subIfs)) {
            subInterefaceStr = "\tNombre de la subinterfaz: " + subIf.getDisplayName()
                + "\n\tNombre real de la subinterfaz: " + subIf.getName() + "\n";
        }
        return subInterefaceStr;
    }
}
