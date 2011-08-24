package com.googlecode.blacken.examples;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Helper class to manage the example strings
 * 
 * @author yam655
 */
public class Messages {
    private static final String BUNDLE_NAME =
            "com.googlecode.blacken.examples.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE =
            ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Messages
     */
    private Messages() {
        // do nothing
    }

    /**
     * Get a string.
     * 
     * @param key key string
     * @return resource string
     */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
