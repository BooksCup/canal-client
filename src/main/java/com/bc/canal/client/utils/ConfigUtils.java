package com.bc.canal.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.bc.canal.client.parser.DataParser;


/**
 * 配置工具类
 *
 * @author zhou
 */
public class ConfigUtils {
    private static final Logger logger = Logger.getLogger(DataParser.class);

    // 这样当多个线程同时与某个对象交互时，就必须要注意到要让线程及时的得到共享成员变量的变化。
    // Volatile修饰的成员变量在每次被线程访问时，都强迫从共享内存中重读该成员变量的值。而且，当成员变量发生变化时，强迫线程将变化值回写到共享内存。这样在任何时刻，两个不同的线程总是看到某个成员变量的同一个值。
    // volatile关键字就是提示VM：对于这个成员变量不能保存它的私有拷贝，而应直接与共享成员变量交互。
    // 在两个或者更多的线程访问的成员变量上使用volatile。当要访问的变量已在synchronized代码块中，或者为常量时，不必使用
    // 由于使用volatile屏蔽掉了VM中必要的代码优化，所以在效率上比较低，因此一定在必要时才使用此关键字。
    /**
     * PROPERTIES
     */
    private static volatile Properties PROPERTIES;

    public static String getProperty(String key, String defaultValue) {
        Properties properties = getProperties();
        String value = properties.getProperty(key);
        if (null != value) {
            return value.trim();
        }
        return defaultValue;
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        Properties properties = getProperties();
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.valueOf(value).booleanValue();
    }


    public static Properties getProperties() {
        if (null == PROPERTIES) {
            synchronized (ConfigUtils.class) {
                if (null == PROPERTIES) {

                    PROPERTIES = ConfigUtils.loadProperties();
                }
            }
        }
        return PROPERTIES;
    }

    public static Properties loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        try {
            try {
                String configPath = System.getProperty("user.dir") + "/conf/canal.properties";
                logger.info("Loading config file: " + configPath);
                input = new FileInputStream(new File(configPath));
                properties.load(input);
            } finally {
                input.close();
            }
        } catch (Throwable e) {
            logger.warn("Failed to load canal.properties");
        }
        return properties;
    }
}
