package vme.manager.gui.misc;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LanguageManager {
    private static LanguageManager instance;
    private ResourceBundle bundle;
    private Locale currentLocale;
    private String currentLanguage;
    
    private LanguageManager() {
		currentLanguage = "es"; // español por defecto
        loadLanguage(currentLanguage);
    }
    
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
    
    public void loadLanguage(String languageCode) {
        try {
            currentLanguage = languageCode;

            currentLocale = ("en".equals(languageCode))
				? new Locale("en")
				: new Locale("es");
            bundle = ResourceBundle.getBundle("vme.manager.gui.i18n.messages", currentLocale);
        } catch (MissingResourceException e) {
            System.err.println("Resource bundle not found, using default");
            bundle = ResourceBundle.getBundle("./resources/i18n.messages", new Locale("es"));
        }
    }
    
    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }
    
    public String getString(String key, Object... args) {
        try {
            String pattern = bundle.getString(key);
            return MessageFormat.format(pattern, args);
        } catch (MissingResourceException e) {
            return key;
        }
    }
    
    public String getCurrentLanguage() {
        return currentLanguage;
    }
}
