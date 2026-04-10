package vme.manager.gui.misc;

public class OSFactory {
    public static OSUtils create()
    {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return new OSUtilsWin();
        }
        if (os.contains("mac")) {
            throw new UnsupportedOperationException("Uso del sistema " + os + " no implementados");
        }
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			return new OSUtilsPOSIX();
        }
        if (os.contains("sunos")) {
            throw new UnsupportedOperationException("Uso del sistema " + os + " no implementados");
        }
        throw new UnsupportedOperationException("Uso del sistema " + os + " no implementados");
    }
}
