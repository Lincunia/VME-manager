package vme.manager.gui.misc;

public class OSUtilsPOSIX implements OSUtils {
    public void setEnableStartup(boolean state)
    {
        System.out.println(state);
    }
}
