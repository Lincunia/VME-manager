package vme.manager.gui.misc;

public interface OSUtils {
	final String programName = "VME-manager";
	void setEnableStartup(boolean state) throws Exception;
	String getMemInf() throws Exception;
	String getCPUInf() throws Exception;
}
