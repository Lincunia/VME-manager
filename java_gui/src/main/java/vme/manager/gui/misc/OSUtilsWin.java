package vme.manager.gui.misc;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class OSUtilsWin implements OSUtils {
    public void setEnableStartup(boolean state) throws IOException, URISyntaxException
    {
        if (!state) {
            ProcessBuilder pb = new ProcessBuilder("reg", "delete",
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
        ProcessBuilder pb = new ProcessBuilder("reg", "add",
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
		return inf;
	}
	public String getCPUInf() throws Exception {
		String inf = "";
		return inf;
	}
}
