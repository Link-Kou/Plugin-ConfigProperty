package com.linkkou.configproperty.spring;


import com.linkkou.configproperty.ConfigImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 获取到当前架包中资源文件夹下的Properties配置文件
 * Spring接口实现
 *
 * @author LK
 * @version 1.0
 * @data 2017-12-10 21:47
 */
public class ConfigMsgPropertyConfigurer implements BeanFactoryPostProcessor {

    private Resource[] locations;

    private String fileEncoding = "UTF-8";

    public void setLocations(Resource... locations) {
        this.locations = locations;
    }

    public void setFileEncoding(String encoding) {
        this.fileEncoding = encoding;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        Properties result = new Properties();
        if (null != locations) {
            for (Resource resource : locations) {
                Properties properties = new Properties();
                try {
                    final InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream(), fileEncoding);
                    properties.load(inputStreamReader);
                    CollectionUtils.mergePropertiesIntoMap(properties, result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (Object key : result.keySet()) {
                String keyStr = key.toString();
                ConfigImpl.getCtxPropMap().put(keyStr, result.get(keyStr));
            }
        }

    }
}