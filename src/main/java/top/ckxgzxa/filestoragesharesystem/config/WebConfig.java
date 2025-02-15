package top.ckxgzxa.filestoragesharesystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.ckxgzxa.filestoragesharesystem.interceptor.JwtInterceptor;

import javax.annotation.Resource;

/**
 * @author 赵希奥
 * @date 2023/3/19 18:12
 * @github https://github.com/CKXGZXA
 * @gitee https://gitee.com/ckxgzxa
 * @description:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register")
                .excludePathPatterns("/resetPassword/**")
                .excludePathPatterns("/static/**");
    }
}

