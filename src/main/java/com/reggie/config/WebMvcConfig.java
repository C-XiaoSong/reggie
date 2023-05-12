package com.reggie.config;

import com.reggie.utis.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/*
 * @author：陈晓松
 * @CLASS_NAME：WebMvcConfig
 * @date：2023/4/18 17:56
 * @注释：配置类
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 设置静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始映射静态资源");
        registry.addResourceHandler("/static/backend/**")
                .addResourceLocations("classpath:/static/backend");
        registry.addResourceHandler("/static/front/**")
                .addResourceLocations("classpath:/static/front");
    }

    /**
     * 扩展mvc消息的转换器
     * @param converters the list of configured converters to be extended
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 把long型的转换为字符串 就不会丢失精确度了
        log.info("扩展消息转换器");
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层使用Jackson将java对象转为JSON
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将上面的消息转换器对象追加到mvc框架的转换器集合中，index：0,优先使用自己的转换器
        converters.add(0,messageConverter);
    }
}
