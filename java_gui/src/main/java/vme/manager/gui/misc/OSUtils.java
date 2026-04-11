package vme.manager.gui.misc;

public interface OSUtils {
	void setEnableStartup(boolean state) throws Exception;
	String getMemInf() throws Exception;
	String getCPUInf() throws Exception;
}
