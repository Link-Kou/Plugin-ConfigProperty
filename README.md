# Plugin-ConfigProperty

### Plugin-ConfigProperty 能做什么？

> Spring环境中@Value具备非常强大的功能。希望能在非加载容器内的类提供类似的功能

- 基于Spring,读取Properties文件
- 提供读取配置注入的单一功能

> Java的编译时注解;继承AbstractProcessor进行代码构建
> 替换常量的方式的配置

- @ConfigValue注解注入

---
### 使用环境

    JAVA1.8
    Maven
    
### 使用教程

1. @ConfigValue会实现构建，在Spring环境中也可以使用

```java：

public class TestDemo {

   @ConfigValue(@Value("${Globalparam.SYS_AUTH_DEV_NAME}"))
   private Config<String> SYS_AUTH_DEV_NAME;

}

```
2. 在Spring环境XML配置

```xml：

    <!--配置读取-->
    <bean class="com.linkkou.configproperty.spring.ConfigMsgPropertyConfigurer">
        <property name="locations">
            <list>
                <value>classpath*:**/JsonResultMsgCode.properties</value>
                <value>classpath*:config/properties/globalparam.properties</value>
                <value>classpath*:config/properties/RedisKeyName.properties</value>
            </list>
        </property>
        <property name="fileEncoding">
            <value>utf-8</value>
        </property>
    </bean> 
            
```