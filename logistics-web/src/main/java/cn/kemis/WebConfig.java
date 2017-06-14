package cn.kemis;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${file.server.url.upload}")
    private String uploadFilePath;

    /*@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }*/

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return new BoneCPDataSource();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /*
    @Bean(initMethod = "init", destroyMethod = "close")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }*/

    /*@Autowired
    private SpringTemplateEngine templateEngine;

    @PostConstruct
    public void extension() {
        templateEngine.addDialect(new SpringSecurityDialect());
    }*/

    public BeanNameViewResolver beanNameViewResolver() {
        BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();
        beanNameViewResolver.setOrder(1);

        return beanNameViewResolver;
    }

    @Bean
    MultipartConfigElement createMultipartConfigElement()
    {
        MultipartConfigFactory mcf = new MultipartConfigFactory();
        /**
         * 设置最大上传文件的大小，默认是10MB
         */
        mcf.setMaxFileSize("300MB");
        return mcf.createMultipartConfig();
    }

    // web暴露静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //绝对路径需要加上file: 前缀
         registry.addResourceHandler("/file/**").
                 addResourceLocations("file:" + uploadFilePath + Constants.AVATAR_PATH)
        //registry.addResourceHandler("/files/**").
        //        addResourceLocations("classpath:/files/")
        .setCachePeriod(3600)
        .resourceChain(true)
        .addResolver(new GzipResourceResolver())
        .addResolver(new PathResourceResolver());
    }
}