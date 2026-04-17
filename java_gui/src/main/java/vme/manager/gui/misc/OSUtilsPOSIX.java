package vme.manager.gui.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OSUtilsPOSIX implements OSUtils {
    private static ProcessBuilder pb;
	private static BufferedReader reader;
	private static String lineRead;
    public void setEnableStartup(boolean state) throws IOException, URISyntaxException
    {
        String jarPath = new File(
            OSUtils.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI())
                             .getPath();

        String userHome = System.getProperty("user.home");
        String scriptPath = userHome + "/start_java_app.sh";

        // Crear script de inicio
        String scriptContent = "#!/bin/bash\n"
            + "sleep 10\n"
            + "cd " + new File(jarPath).getParent() + "\n"
            + "java -jar " + jarPath + " >> /tmp/java_app.log 2>&1\n";

        Files.write(Paths.get(scriptPath), scriptContent.getBytes());
        new File(scriptPath).setExecutable(state);

        // Agregar a crontab
        String crontabEntry = "@reboot " + scriptPath + "\n";
        String crontabContent = getCurrentCrontab();

        if (!crontabContent.contains(crontabEntry.trim())) {
            crontabContent += crontabEntry;
            updateCrontab(crontabContent);
            System.out.println("Instalado en crontab correctamente");
        } else {
            System.out.println("Ya está instalado en crontab");
        }
    }

    private static String getCurrentCrontab() throws IOException
    {
        Process process = Runtime.getRuntime().exec(new String[] { "crontab", "-l" });
        try (BufferedReader reader = new BufferedReader(
                 new InputStreamReader(process.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    private static void updateCrontab(String content) throws IOException
    {
        File tempFile = File.createTempFile("crontab", ".txt");
        Files.write(tempFile.toPath(), content.getBytes());
		System.out.println(tempFile.getAbsolutePath());
        Runtime.getRuntime().exec(new String[] { "crontab", tempFile.getAbsolutePath() });
        tempFile.delete();
    }

    public String getMemInf() throws IOException
    {
        pb = new ProcessBuilder("free", "-h");
		Process pr = pb.start();
        reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        StringBuilder builder = new StringBuilder();
        while ((lineRead = reader.readLine()) != null) {
            builder.append(lineRead + '\n');
        }
        return builder.toString();
    }
    public String getCPUInf() throws IOException
    {
        pb = new ProcessBuilder("lscpu");
		Process pr = pb.start();
        reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        StringBuilder builder = new StringBuilder();
		
        while ((lineRead = reader.readLine()) != null) {
            builder.append(lineRead + '\n');
        }
        return builder.toString();
    }
}
