package vme.manager.gui.utils;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
        setupLayout();
    }

    private void initComponents()
    {
        textAreaNetInt = new JTextArea();
        textAreaNetInt.setEditable(false);
        textAreaNetInt.setText(getNetworkInterfaceAddresses());

        textFieldHost = new JTextField(15);
        textFieldHost.setText("google.com");

        textFieldPort = new JTextField(15);
        textFieldPort.setText("80");

        textFieldTimeout = new JTextField(15);
        textFieldTimeout.setText("2000");

        textFieldCount = new JTextField(15);
        textFieldCount.setText("5");

        buttonMsrLatency = new JButton("Empezar la medición");
        buttonMsrLatency.addActionListener(e -> verifyLatency());

        textAreaLatency = new JTextArea();
        textAreaLatency.setEditable(false);
    }

    private void setupLayout()
    {
        getPanelBottom().setLayout(new BoxLayout(getPanelBottom(), BoxLayout.X_AXIS));

        getPanelLeft().setLayout(new BoxLayout(getPanelLeft(), BoxLayout.Y_AXIS));
        getPanelLeft().setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            "Interfaces de red del dispositivo"));
        getPanelLeft().add(new JScrollPane(textAreaNetInt));

        getPanelRight().setLayout(new BoxLayout(getPanelRight(), BoxLayout.Y_AXIS));
        getPanelRight().setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            "Medición de latencia"));

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;

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
        panelForm.add(textFieldHost, gbc);

        gbc.gridy = 1;
        panelForm.add(textFieldPort, gbc);

        gbc.gridy = 2;
        panelForm.add(textFieldTimeout, gbc);

        gbc.gridy = 3;
        panelForm.add(textFieldCount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelForm.add(buttonMsrLatency, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.ipady = 100;
        panelForm.add(new JScrollPane(textAreaLatency), gbc);
        getPanelRight().add(panelForm);
    }

	public void refreshTexts(){
		System.out.println("Lo traduciré LOL");
	}

    private void verifyLatency()
    {

        String host = textFieldHost.getText();
        int port = Integer.parseInt(textFieldPort.getText());
        int timeoutMs = Integer.parseInt(textFieldTimeout.getText());
        int count = Integer.parseInt(textFieldCount.getText());
        textAreaLatency.setText("");;

        new Thread(() -> {
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
            if (successfulPings == 0) {
                textAreaLatency.append("Los pings fallaron");
            } else {
                textAreaLatency.append("Latencia promedio de " + host + ":" + port + " es"
                    + "\n" + (double)totalLatency / successfulPings);
            }
        }).start();
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
