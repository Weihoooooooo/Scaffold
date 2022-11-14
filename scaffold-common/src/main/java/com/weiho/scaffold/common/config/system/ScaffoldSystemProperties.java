package com.weiho.scaffold.common.config.system;

import com.weiho.scaffold.common.exception.CaptchaException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 系统配置类
 *
 * @author Weiho
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "scaffold")
public class ScaffoldSystemProperties {
    private String version;
    private String ip;
    private SwaggerProperties swaggerProperties;
    private RateLimiterProperties rateLimiterProperties;
    private JwtProperties jwtProperties;
    private CodeProperties codeProperties;
    private MonitorProperties monitorProperties;
    private TaskThreadPoolProperties threadPoolProperties;
    private RSAProperties rsaProperties;
    private RabbitMqProperties rabbitMqProperties;
    private EmailProperties emailProperties;
    private ResourcesProperties resourcesProperties;

    public ScaffoldSystemProperties(SwaggerProperties swaggerProperties, RateLimiterProperties rateLimiterProperties,
                                    JwtProperties jwtProperties, CodeProperties codeProperties,
                                    MonitorProperties monitorProperties, TaskThreadPoolProperties taskThreadPoolProperties,
                                    RSAProperties rsaProperties, RabbitMqProperties rabbitMqProperties,
                                    EmailProperties emailProperties, ResourcesProperties resourcesProperties) {
        this.swaggerProperties = swaggerProperties;
        this.rateLimiterProperties = rateLimiterProperties;
        this.jwtProperties = jwtProperties;
        this.codeProperties = codeProperties;
        this.monitorProperties = monitorProperties;
        this.threadPoolProperties = taskThreadPoolProperties;
        this.rsaProperties = rsaProperties;
        this.rabbitMqProperties = rabbitMqProperties;
        this.emailProperties = emailProperties;
        this.resourcesProperties = resourcesProperties;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "scaffold.swagger")
    public static class SwaggerProperties {
        //是否开启Swagger接口文档
        private Boolean enabled;
        //Swagger标题
        private String title;
        //描述
        private String description;
        //版本
        private String version;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "scaffold.limit")
    public static class RateLimiterProperties {
        //是否开启限流
        private boolean enabled = true;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "scaffold.jwt")
    public static class JwtProperties {
        //token的头部的属性key
        private String header;
        //令牌token的前缀
        private String tokenStartWith;
        //token加密的编码
        private String secret;
        //token过期的时间
        private Long tokenValidityInSeconds;
        //在线用户信息缓存中的Key前缀
        private String onlineKey;
        //登录后用户信息的Key前缀
        private String detailKey;
        //登录后存储用户Token的Key
        private String tokenKey;
        //是否允许用户单次登录
        private Boolean singleLogin;

        public String getTokenStartWith() {
            return tokenStartWith + " ";
        }
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "scaffold.code")
    public static class CodeProperties {
        //验证码缓存前缀
        private String codeKey;
        //验证码有效时间
        private Long expiration;
        //验证码长度
        private int height = 40;
        //验证码宽度
        private int width = 140;
        //验证码位数
        private int len = 2;
        //验证码类型
        private Class<?> type;

        public CodeProperties() {
            try {
                this.type = Class.forName("com.wf.captcha.ArithmeticCaptcha");
            } catch (ClassNotFoundException e) {
                throw new CaptchaException();
            }
        }
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "scaffold.monitor")
    public static class MonitorProperties {
        //是否开启系统参数监测
        private boolean systemMonitorEnabled = false;
        //是否开启实时控制台传输
        private boolean loggingMonitorEnabled = false;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "scaffold.thread-pool")
    public static class TaskThreadPoolProperties {
        //核心线程数
        private int corePoolSize;
        //最大线程数
        private int maxPoolSize;
        //队列大小
        private int queueCapacity;
        //活跃时间
        private int keepAliveSeconds;
        //线程名称前缀
        private String namePrefix;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "scaffold.rsa")
    public static class RSAProperties {
        //RSA私钥
        private String privateKey;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "scaffold.rabbitmq")
    public static class RabbitMqProperties {
        // 队列名称
        private String queueName;
        // 队列交换机名称
        private String exchangeName;
        // 路由Key名称
        private String routingKeyName;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "scaffold.email")
    public static class EmailProperties {
        // 邮箱验证码的长度
        private int codeLength;
        // 邮箱验证码的缓存前缀
        private String codeKey;
        // 邮箱验证码的过期时间(min)
        private Long expiration;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties(prefix = "scaffold.resources")
    public static class ResourcesProperties {
        // 本地资源路径
        private String localAddressPrefix;
        // 头像本地地址前缀
        private String avatarLocalAddressPrefix;
        // 头像服务器地址前缀
        private String avatarServerAddressPrefix;
        // 文件上传最大的大小
        private long storageUploadMaxSize;
        // 存储本地地址前缀
        private String storageLocalAddressPrefix;
        // 存储服务器地址前缀
        private String storageServerAddressPrefix;
    }
}
