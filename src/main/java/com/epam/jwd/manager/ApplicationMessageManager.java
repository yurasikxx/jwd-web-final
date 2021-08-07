package com.epam.jwd.manager;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * An {@code ApplicationMessageManager} class implements
 * {@code BaseApplicationMessageManager} interface and its methods.
 *
 * @see BaseApplicationMessageManager
 */
public class ApplicationMessageManager implements BaseApplicationMessageManager {

    private static final String APPLICATION_BUNDLE_NAME = "application";

    private static volatile ApplicationMessageManager instance;

    private ResourceBundle bundle;

    private ApplicationMessageManager() {
        this.bundle = ResourceBundle.getBundle(APPLICATION_BUNDLE_NAME);
    }

    public static ApplicationMessageManager getInstance() {
        if (instance == null) {
            synchronized (ApplicationMessageManager.class) {
                if (instance == null) {
                    instance = new ApplicationMessageManager();
                }
            }
        }

        return instance;
    }

    @Override
    public void setLocale(String locale) {
        bundle = ResourceBundle.getBundle(APPLICATION_BUNDLE_NAME, new Locale(locale));
    }

    @Override
    public String getString(String key) {
        return new String(bundle.getString(key).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

}