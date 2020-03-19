package com.plugin.configproperty.spring;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 获取到当前架包中资源文件夹下的Properties配置文件
 * Spring接口实现
 * @author LK
 * @version 1.0
 * @data 2017-12-10 21:47
 */
public class ConfigMsgPropertyConfigurer extends PropertyPlaceholderConfigurer {

	private static Map<String,Object> ctxPropMap = new HashMap<>(16);;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		for (Object key : props.keySet()){
			String keyStr = key.toString();
			ctxPropMap.put(keyStr,props.get(keyStr));
		}
	}

	/**
	 * 获取配置
	 * @param name
	 * @return
	 */
	public static Object getCtxProp(String name) {
		return ctxPropMap.get(name);
	}

	/**
	 * 获取HashMap键值对
	 * @return
	 */
	public static Map<String, Object> getCtxPropMap() {
		return ctxPropMap;
	}

	/**
	 * 手工加载配置
	 * @param name 配置文件名称
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	synchronized static public Properties loadProps(String name) throws IOException {
		Properties props = new Properties();
		InputStream in = null;
		try {
			in = ConfigMsgPropertyConfigurer.class.getClassLoader().getResourceAsStream(name);
			props.load(in);
			return props;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != in) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		throw new IOException("查找不到配置文件");
	}

}