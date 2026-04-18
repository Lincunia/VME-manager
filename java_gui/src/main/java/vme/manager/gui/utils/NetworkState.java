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
import vme.manager.gui.misc.LanguageManager;

public class NetworkState extends ContainerUtil {
    private static JButton buttonMsrLatency;
    private static JTextArea textAreaNetInt, textAreaLatency;
    private static JLabel
        labelHost,
        labelPort,
        labelTimeout,
        labelCount;
    private static JTextField
        textFieldHost,
        textFieldPort,
        textFieldTimeout,
        textFieldCount;
    private LanguageManager langManager;
    public NetworkState(JTabbedPane jTabbedPane)
    {
        super(jTabbedPane);
		langManager = LanguageManager.getInstance();
        initComponents();
        setupLayout();
    }

    private void initComponents()
    {
        textAreaNetInt = new JTextArea();
        textAreaNetInt.setEditable(false);
        textAreaNetInt.setText(getNetworkInterfaceAddresses());

        labelHost = new JLabel(langManager.getString("network.host"));

        textFieldHost = new JTextField(15);
        textFieldHost.setText("google.com");

        labelPort = new JLabel(langManager.getString("network.port"));

        textFieldPort = new JTextField(15);
        textFieldPort.setText("80");

        labelTimeout = new JLabel(langManager.getString("network.timeout"));

        textFieldTimeout = new JTextField(15);
        textFieldTimeout.setText("2000");

        labelCount = new JLabel(langManager.getString("network.count"));

        textFieldCount = new JTextField(15);
        textFieldCount.setText("5");

        buttonMsrLatency = new JButton(langManager.getString("network.button.start"));
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
			langManager.getString("network.interfaces")));
        getPanelLeft().add(new JScrollPane(textAreaNetInt));

        getPanelRight().setLayout(new BoxLayout(getPanelRight(), BoxLayout.Y_AXIS));
        getPanelRight().setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK),
			langManager.getString("network.latency")));

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

    public void refreshTexts()
    {
        buttonMsrLatency.setText(langManager.getString("network.button.start"));

        getPanelLeft().setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK),
			langManager.getString("network.interfaces")));

        getPanelRight().setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLACK),
			langManager.getString("network.latency")));
        /*
        textAreaNetInt.setText(langManager.getString("network."));
        textAreaLatency.setText(langManager.getString("network."));
        */
        labelHost = new JLabel(langManager.getString("network.host"));
        labelHost.setText(langManager.getString("network.host"));
        labelPort = new JLabel(langManager.getString("network.port"));
        labelPort.setText(langManager.getString("network.port"));
        labelTimeout = new JLabel(langManager.getString("network.timeout"));
        labelTimeout.setText(langManager.getString("network.timeout"));
        labelCount = new JLabel(langManager.getString("network.count"));
        labelCount.setText(langManager.getString("network.count"));
    }

    private void verifyLatency()
    {

        String host = textFieldHost.getText();
        int port = Integer.parseInt(textFieldPort.getText());
        int timeoutMs = Integer.parseInt(textFieldTimeout.getText());
        int count = Integer.parseInt(textFieldCount.getText());
        textAreaLatency.setText("");
        ;

        new Thread(() -> {
            long totalLatency = 0;
            int successfulPings = 0;
            String pingMessage = "";
            String baseStr;
            for (int i = 0; i < count; i++) {
                long latency = ping(host, port, timeoutMs);
                if (latency != -1) {
                    totalLatency += latency;
                    successfulPings++;
                    baseStr = langManager.getString("network.ping.success");
                    pingMessage = String.format(baseStr, i + 1, latency);
                    // pingMessage = "Ping " + (i + 1) + ": " + latency + " ms\n";
                } else {
                    baseStr = langManager.getString("network.ping.failed");
                    pingMessage = String.format(baseStr, i + 1);
                    // pingMessage = "Ping " + (i + 1) + ": fallido\n";
                }
                textAreaLatency.append(pingMessage + '\n');
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            if (successfulPings == 0) {
                textAreaLatency.append(langManager.getString("network.ping.allfailed"));
            } else {
                baseStr = langManager.getString("network.ping.average");
                textAreaLatency.append(String.format(baseStr, host, port, (double)totalLatency / successfulPings));
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
