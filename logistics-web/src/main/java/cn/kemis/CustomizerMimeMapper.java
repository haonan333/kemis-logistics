package cn.kemis;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义 MIME 示例增加 xlsx 类型支持
 * @author 刘体阳 jefferlzu@gmail.com
 *         Created on 2016-08-30
 */
@Configuration
public class CustomizerMimeMapper implements EmbeddedServletContainerCustomizer {
    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        configurableEmbeddedServletContainer.setMimeMappings(mappings);
    }
}
