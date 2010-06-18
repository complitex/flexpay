package net.sourceforge.navigator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * General purpose abstract class that describes an API for retrieving
 * Locale-sensitive messages from underlying resource locations of an
 * unspecified design, and optionally utilizing the <code>MessageFormat</code>
 * class to produce internationalized messages with parametric replacement.
 * <p>
 * Calls to <code>getMessage()</code> variants without a <code>Locale</code>
 * argument are presumed to be requesting a message string in the default
 * <code>Locale</code> for this JVM.
 * <p>
 * Calls to <code>getMessage()</code> with an unknown key, or an unknown
 * <code>Locale</code> will return <code>null</code> if the
 * <code>returnNull</code> property is set to <code>true</code>.  Otherwise,
 * a suitable error message will be returned instead.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong> - Classes that extend this class
 * must be Serializable so that instances may be used in distributable
 * application server environments.
 */
public abstract class MessageResources implements Serializable {

	protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * The configuration parameter used to initialize this MessageResources.
     */
    protected String config = null;

    /**
     * The configuration parameter used to initialize this MessageResources.
     * @return parameter used to initialize this MessageResources
     */
    public String getConfig() {
        return (this.config);
    }

    /**
     * The default Locale for our environment.
     */
    protected Locale defaultLocale = Locale.getDefault();

    /**
     * The <code>MessageResourcesFactory</code> that created this instance.
     */
    protected MessageResourcesFactory factory = null;

    /**
     * The <code>MessageResourcesFactory</code> that created this instance.
     * @return <code>MessageResourcesFactory</code> that created instance
     */
    public MessageResourcesFactory getFactory() {
        return (this.factory);
    }

    /**
     * The set of previously created MessageFormat objects, keyed by the
     * key computed in <code>messageKey()</code>.
     */
    protected HashMap<String, MessageFormat> formats = new HashMap<String, MessageFormat>();

    /**
     * Indicate is a <code>null</code> is returned instead of an error message string
     * when an unknown Locale or key is requested.
     */
    protected boolean returnNull = false;

    /**
     * Indicates that a <code>null</code> is returned instead of an error message string
     * if an unknown Locale or key is requested.
     * @return true if null is returned if unknown key or locale is requested
     */
    public boolean getReturnNull() {
        return this.returnNull;
    }

    /**
     * Indicates that a <code>null</code> is returned instead of an error message string
     * if an unknown Locale or key is requested.
     * @param returnNull true Indicates that a <code>null</code> is returned
     * if an unknown Locale or key is requested.
     */
    public void setReturnNull(boolean returnNull) {
        this.returnNull = returnNull;
    }

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new MessageResources according to the specified parameters.
     *
     * @param factory The MessageResourcesFactory that created us
     * @param config The configuration parameter for this MessageResources
     */
    public MessageResources(MessageResourcesFactory factory, String config) {
        this(factory, config, false);
    }

    /**
     * Construct a new MessageResources according to the specified parameters.
     *
     * @param factory The MessageResourcesFactory that created us
     * @param config The configuration parameter for this MessageResources
     * @param returnNull The returnNull property we should initialize with
     */
    public MessageResources(MessageResourcesFactory factory, String config, boolean returnNull) {
        super();
        this.factory = factory;
        this.config = config;
        this.returnNull = returnNull;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Returns a text message for the specified key, for the default Locale.
     *
     * @param key The message key to look up
	 * @return String
     */
    public String getMessage(String key) {
        return this.getMessage((Locale) null, key, null);
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.
     *
     * @param key The message key to look up
     * @param args An array of replacement parameters for placeholders
	 * @return String
     */
    public String getMessage(String key, Object args[]) {
        return this.getMessage((Locale) null, key, args);
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.
     *
     * @param key The message key to look up
     * @param arg0 The replacement for placeholder {0} in the message
	 * @return String
     */
    public String getMessage(String key, Object arg0) {
        return this.getMessage((Locale) null, key, arg0);
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.
     *
     * @param key The message key to look up
     * @param arg0 The replacement for placeholder {0} in the message
     * @param arg1 The replacement for placeholder {1} in the message
	 * @return String
     */
    public String getMessage(String key, Object arg0, Object arg1) {
        return this.getMessage((Locale) null, key, arg0, arg1);
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.
     *
     * @param key The message key to look up
     * @param arg0 The replacement for placeholder {0} in the message
     * @param arg1 The replacement for placeholder {1} in the message
     * @param arg2 The replacement for placeholder {2} in the message
	 * @return String
     */
    public String getMessage(String key, Object arg0, Object arg1, Object arg2) {
        return this.getMessage((Locale) null, key, arg0, arg1, arg2);
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.
     *
     * @param key The message key to look up
     * @param arg0 The replacement for placeholder {0} in the message
     * @param arg1 The replacement for placeholder {1} in the message
     * @param arg2 The replacement for placeholder {2} in the message
     * @param arg3 The replacement for placeholder {3} in the message
	 * @return String
     */
    public String getMessage(String key, Object arg0, Object arg1, Object arg2, Object arg3) {
        return this.getMessage((Locale) null, key, arg0, arg1, arg2, arg3);
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.
     *
     * @param key The message key to look up
     * @param arg0 The replacement for placeholder {0} in the message
     * @param arg1 The replacement for placeholder {1} in the message
     * @param arg2 The replacement for placeholder {2} in the message
     * @param arg3 The replacement for placeholder {3} in the message
     * @param arg4 The replacement for placeholder {4} in the message
	 * @return String
     */
    public String getMessage(String key, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        return this.getMessage(null, key, arg0, arg1, arg2, arg3, arg4);
    }

    /**
     * Returns a text message for the specified key, for the default Locale.
     * A null string result will be returned by this method if no relevant
     * message resource is found for this key or Locale, if the
     * <code>returnNull</code> property is set.  Otherwise, an appropriate
     * error message will be returned.
     * <p>
     * This method must be implemented by a concrete subclass.
     *
     * @param locale The requested message Locale, or <code>null</code> for the system default Locale
     * @param key The message key to look up
	 * @return String
     */
    public abstract String getMessage(Locale locale, String key);

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will be returned by
     * this method if no resource bundle has been configured.
     *
     * @param locale The requested message Locale, or <code>null</code> for the system default Locale
     * @param key The message key to look up
     * @param args An array of replacement parameters for placeholders
	 * @return String
     */
    public String getMessage(Locale locale, String key, Object args[]) {

        // Cache MessageFormat instances as they are accessed
        if (locale == null) {
            locale = defaultLocale;
        }

        MessageFormat format;
        String formatKey = messageKey(locale, key);

        synchronized (formats) {
            format = formats.get(formatKey);
            if (format == null) {
                String formatString = getMessage(locale, key);

                if (formatString == null) {
                    return returnNull ? null : "???" + formatKey + "???";
                }

                format = new MessageFormat(escape(formatString));
                format.setLocale(locale);
                formats.put(formatKey, format);
            }

        }

        return format.format(args);
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will never be returned
     * by this method.
     *
     * @param locale The requested message Locale, or <code>null</code> for the system default Locale
     * @param key The message key to look up
     * @param arg0 The replacement for placeholder {0} in the message
	 * @return String
     */
    public String getMessage(Locale locale, String key, Object arg0) {
        return this.getMessage(locale, key, new Object[] { arg0 });
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will never be returned
     * by this method.
     *
     * @param locale The requested message Locale, or <code>null</code> for the system default Locale
     * @param key The message key to look up
     * @param arg0 The replacement for placeholder {0} in the message
     * @param arg1 The replacement for placeholder {1} in the message
	 * @return String
     */
    public String getMessage(Locale locale, String key, Object arg0, Object arg1) {
        return this.getMessage(locale, key, new Object[] { arg0, arg1 });
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will never be returned
     * by this method.
     *
     * @param locale The requested message Locale, or <code>null</code> for the system default Locale
     * @param key The message key to look up
     * @param arg0 The replacement for placeholder {0} in the message
     * @param arg1 The replacement for placeholder {1} in the message
     * @param arg2 The replacement for placeholder {2} in the message
	 * @return String
     */
    public String getMessage(Locale locale, String key, Object arg0, Object arg1, Object arg2) {
        return this.getMessage(locale, key, new Object[] { arg0, arg1, arg2 });
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will never be returned
     * by this method.
     *
     * @param locale The requested message Locale, or <code>null</code> for the system default Locale
     * @param key The message key to look up
     * @param arg0 The replacement for placeholder {0} in the message
     * @param arg1 The replacement for placeholder {1} in the message
     * @param arg2 The replacement for placeholder {2} in the message
     * @param arg3 The replacement for placeholder {3} in the message
	 * @return String
     */
    public String getMessage(Locale locale, String key, Object arg0, Object arg1, Object arg2, Object arg3) {
        return this.getMessage(locale, key, new Object[] { arg0, arg1, arg2, arg3 });
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.  A null string result will never be returned
     * by this method.
     *
     * @param locale The requested message Locale, or <code>null</code> for the system default Locale
     * @param key The message key to look up
     * @param arg0 The replacement for placeholder {0} in the message
     * @param arg1 The replacement for placeholder {1} in the message
     * @param arg2 The replacement for placeholder {2} in the message
     * @param arg3 The replacement for placeholder {3} in the message
     * @param arg4 The replacement for placeholder {4} in the message
	 * @return String
	 */
    public String getMessage(Locale locale, String key, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        return this.getMessage(locale, key, new Object[] { arg0, arg1, arg2, arg3, arg4 });
    }

    /**
     * Return <code>true</code> if there is a defined message for the specified
     * key in the system default locale.
     *
     * @param key The message key to look up
	 * @return boolean
     */
    public boolean isPresent(String key) {
        return this.isPresent(null, key);
    }

    /**
     * Return <code>true</code> if there is a defined message for the specified
     * key in the specified Locale.
     *
     * @param locale The requested message Locale, or <code>null</code>
     *  for the system default Locale
     * @param key The message key to look up
	 * @return boolean
     */
    public boolean isPresent(Locale locale, String key) {

        String message = getMessage(locale, key);

        if (message == null) {
            return false;
        } else if (message.startsWith("???") && message.endsWith("???")) {
            return false; // FIXME - Only valid for default implementation
        } else {
            return true;
        }

    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Escape any single quote characters that are included in the specified
     * message string.
     *
     * @param string The string to be escaped
	 * @return String
     */
    protected String escape(String string) {

        if (string == null || string.indexOf('\'') < 0) {
            return string;
        }

        int n = string.length();
        StringBuffer sb = new StringBuffer(n);

        for (int i = 0; i < n; i++) {
            char ch = string.charAt(i);

            if (ch == '\'') {
                sb.append('\'');
            }

            sb.append(ch);
        }

        return sb.toString();

    }

    /**
     * Compute and return a key to be used in caching information by a Locale.
     * <strong>NOTE</strong> - The locale key for the default Locale in our
     * environment is a zero length String.
     *
     * @param locale The locale for which a key is desired
	 * @return String
     */
    protected String localeKey(Locale locale) {
        return locale == null ? "" : locale.toString();
    }

    /**
     * Compute and return a key to be used in caching information
     * by Locale and message key.
     *
     * @param locale The Locale for which this format key is calculated
     * @param key The message key for which this format key is calculated
	 * @return String
     */
    protected String messageKey(Locale locale, String key) {
        return localeKey(locale) + "." + key;
    }

    /**
     * Compute and return a key to be used in caching information
     * by locale key and message key.
     *
     * @param localeKey The locale key for which this cache key is calculated
     * @param key The message key for which this cache key is calculated
	 * @return String
     */
    protected String messageKey(String localeKey, String key) {
        return localeKey + "." + key;
    }

    // --------------------------------------------------------- Static Methods

    /**
     * The default MessageResourcesFactory used to create MessageResources
     * instances.
     */
    protected static MessageResourcesFactory defaultFactory = null;

    /**
     * Create and return an instance of <code>MessageResources</code> for the
     * created by the default <code>MessageResourcesFactory</code>.
     *
     * @param config Configuration parameter for this message bundle.
     */
    public synchronized static MessageResources getMessageResources(String config) {

        if (defaultFactory == null) {
            defaultFactory = MessageResourcesFactory.createFactory();
        }

        return defaultFactory.createResources(config);
    }

    /**
     * Log a message to the Writer that has been configured for our use.
     *
     * @param message The message to be logged
     */
    public void log(String message) {
        log.debug(message);
    }

    /**
     * Log a message and exception to the Writer that has been configured
     * for our use.
     *
     * @param message The message to be logged
     * @param throwable The exception to be logged
     */
    public void log(String message, Throwable throwable) {
        log.debug(message, throwable);
    }

}
