package cn.kemis.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by xuhailong on 16/8/8.
 */
@Component
public class PropertiesUtil {

    private static Properties properties;

    public PropertiesUtil() {
        YamlPropertiesFactoryBean yamlProperties = new YamlPropertiesFactoryBean();
        yamlProperties.setResources(new ClassPathResource("application.yml"));
        properties = yamlProperties.getObject();
    }

    /**
     * 获取属性
     * @param key
     * @return
     */
    public static String getProperty(String key){

        return properties == null ? null :  properties.getProperty(key);

    }

    /**
     * 获取属性
     * @param key 属性key
     * @param defaultValue 属性value
     * @return
     */
    public static String getProperty(String key,String defaultValue){

        return properties == null ? null : properties.getProperty(key, defaultValue);

    }

}
