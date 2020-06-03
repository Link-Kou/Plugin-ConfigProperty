package com.linkkou.configproperty.local;

import com.linkkou.configproperty.ConfigImpl;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 获取到当前架包中资源文件夹下的Properties配置文件
 * Spring接口实现
 *
 * @author LK
 * @version 1.0
 * @data 2017-12-10 21:47
 */
public class LocalConfigProperty {

    private static Set<File> Files = new HashSet<File>();

    private static Pattern r = Pattern.compile(".*\\.properties$");

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

    static {
        LocalConfigProperty.loadProps();
    }

    /**
     * 手工加载配置
     */
    synchronized public static void loadProps() {
        Properties props = new Properties();
        try {
            //获取到当前项目中的资源文件（不会获取到Jar中的资源文件）
            final String path = Objects.requireNonNull(LocalConfigProperty.class.getClassLoader().getResource("")).getPath();
            listDirectory(new File(path));
            for (File file : Files) {
                try (InputStream in = new FileInputStream(file)) {
                    propertiesPersister.load(props, new InputStreamReader(in, DEFAULT_ENCODING));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            ConfigImpl.getCtxPropMap().put(keyStr, props.get(keyStr));
        }
    }

    /**
     * 遍历到以properties为后缀的文件
     *
     * @param dir 文件
     */
    private static void listDirectory(File dir) {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null && files.length > 0) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            //递归
                            listDirectory(file);
                        } else {
                            if (r.matcher(file.getName()).matches()) {
                                Files.add(file);
                            }
                        }
                    }
                }
            }
        }
    }

}