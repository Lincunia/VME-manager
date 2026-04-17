package vme.manager.gui.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class OSUtilsWin implements OSUtils {
    private ProcessBuilder pb;
    public void setEnableStartup(boolean state) throws IOException, URISyntaxException
    {
        if (!state) {
            pb = new ProcessBuilder("reg", "delete",
                "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run",
                "/v", "AntiVirus",
                "/f");
            pb.start();
            return;
        }
        String jarPath = new File(
            OSUtils.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI())
                             .getPath();
        pb = new ProcessBuilder("reg", "add",
            "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run",
            "/v", "AntiVirus",
            "/t", "REG_SZ",
            "/d", "\"" + jarPath + "\"");
        pb.start();

        /*
        String command = "reg add HKCU\\Software\\Microsoft\\Windows\\"
            + "CurrentVersion\\Run /v AntiVirus /t REG_SZ /d \"\\\""
            + jarPath
            + "\\\"\"";
        Runtime.getRuntime().exec(command); */
    }
    public String getMemInf() throws Exception
    {
        String inf = "";
        pb = new ProcessBuilder("wmic", "memorychip", "get");
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                pb
                    .start()
                    .getInputStream()));
        StringBuilder builder = new StringBuilder();
        while ((inf = reader.readLine()) != null) {
            builder.append(inf + '\n');
        }
        return builder.toString();
    }
    public String getCPUInf() throws Exception
    {
        String inf = "";
        pb = new ProcessBuilder("wmic", "cpu", "get");
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                pb
                    .start()
                    .getInputStream()));
        StringBuilder builder = new StringBuilder();
        while ((inf = reader.readLine()) != null) {
            builder.append(inf + '\n');
        }
        return builder.toString();
    }
}
