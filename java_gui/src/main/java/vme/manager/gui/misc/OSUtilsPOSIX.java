package vme.manager.gui.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OSUtilsPOSIX implements OSUtils {
    private ProcessBuilder pb;
    public void setEnableStartup(boolean state)
    {
        System.out.println(state);
    }
    public String getMemInf() throws IOException
    {
        String inf = "";
        pb = new ProcessBuilder("free", "-h");
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
    public String getCPUInf() throws IOException
    {
        String inf;
        pb = new ProcessBuilder("lscpu");
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
