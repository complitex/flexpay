package org.flexpay.validatorregistry.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Util for reading and saving config values
 */
public class ConfigUtil {


    private static String CONFIG_RESOURCE_NAME = "..//config.properties";


    public static String getCONFIG_RESOURCE_NAME() {
        return CONFIG_RESOURCE_NAME;
    }

    /**
     * Save config value for key to default  config file
     *
     * @param key   config key
     * @param value key value
     */
    public static void saveConfigKey(String key, String value) {
        saveConfigKey(key, value, CONFIG_RESOURCE_NAME);
    }

    /**
     * Save config value for key
     *
     * @param key          config key
     * @param value        key value
     * @param resourceName resource name
     */
    public static void saveConfigKey(String key, String value, String resourceName) {
        try {
            Properties config = new Properties();
            config.load(ConfigUtil.class.getResourceAsStream(resourceName));

            config.setProperty(key, value);

            storeSortedProperties(resourceName, config);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void storeSortedProperties(String resourceName, Properties config) throws IOException, URISyntaxException {
        FileWriter configWriter = null;
        try {
            configWriter = new FileWriter(new File(ConfigUtil.class.getResource(resourceName).toURI()));

            TreeMap<String, String> oracleProps = new TreeMap<String, String>();
            TreeMap<String, String> zhekFileProps = new TreeMap<String, String>();
            TreeMap<String, String> zhekProps = new TreeMap<String, String>();
            TreeMap<String, String> sjskdbfProps = new TreeMap<String, String>();
            TreeMap<String, String> otherProps = new TreeMap<String, String>();

            for (String propertyName : config.stringPropertyNames()) {
                String propertyValue = config.getProperty(propertyName);
                propertyValue = propertyValue.replace("\\", "\\\\");

                if (propertyName.startsWith("oracle.")) {
                    oracleProps.put(propertyName, propertyValue);
                } else if (propertyName.startsWith("zhek.file.")) {
                    zhekFileProps.put(propertyName, propertyValue);
                } else if (propertyName.startsWith("zhek.")) {
                    zhekProps.put(propertyName, propertyValue);
                } else if (propertyName.startsWith("sjskdbf.")) {
                    sjskdbfProps.put(propertyName, propertyValue);
                } else {
                    otherProps.put(propertyName, propertyValue);
                }
            }

            appendProperties(configWriter, "oracle default jdbc info", oracleProps);
            appendProperties(configWriter, "file names", zhekFileProps);
            appendProperties(configWriter, "zhek configuration", zhekProps);
            appendProperties(configWriter, "sjskdbf", sjskdbfProps);
            appendProperties(configWriter, "other properties", otherProps);

        } finally {
            if (configWriter != null) {
                configWriter.close();
            }
        }
    }

    private static void appendProperties(Writer writer, String comment, Map<String, String> props) throws IOException {
        writer.write("# " + comment + "\r\n");

        for (String name : props.keySet()) {
            String value = props.get(name);

            writer.write(name + "=" + value + "\r\n");
        }

        writer.write("\r\n");
    }

    /**
     * get config key from default file
     *
     * @param key key name to read
     * @return value for given key name
     */
    public static String getConfigKey(String key) {
        return getConfigKey(key, CONFIG_RESOURCE_NAME);
    }

    /**
     * Read config key from config fail
     *
     * @param key          key name to read
     * @param resourceName the name of properties file
     * @return value for given config name
     */
    public static String getConfigKey(String key, String resourceName) {
        try {
            Properties config = new Properties();
            config.load(ConfigUtil.class.getResourceAsStream(resourceName));
            return (String) config.get(key);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}