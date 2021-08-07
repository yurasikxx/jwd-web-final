package com.epam.jwd.manager;

/**
 * A {@code BaseApplicationMessageManager} interface is engaged in
 * localization application inside.
 *
 * @see ApplicationMessageManager
 */
public interface BaseApplicationMessageManager {

    /**
     * Sets locale by accepting it.
     *
     * @param locale a given locale.
     */
    void setLocale(String locale);

    /**
     * Gets message by accepting message key and returns it.
     *
     * @param key a given message key.
     * @return a found message.
     */
    String getString(String key);

}