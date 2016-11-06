package com.yutong.framework.utils;

import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PropertiesUtils {

    private static Logger logger = LogManager.getLogger(PropertiesUtils.class);


    public static String getString(String propertiesPath, String key) {
        String string = "";
        try {
            Properties properties = new Properties();
            properties.load(FileUtils.getFileInputStream(propertiesPath));
            if (properties.containsKey(key)) {
                string = properties.getProperty(key);
            }
        }
        catch (Exception e) {
            logger.error(e);
        }
        return string;
    }

}
