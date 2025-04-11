package shop.mtcoding.blog._core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.mtcoding.blog._core.interceptor.LoginInterceptor;

// WebMvcConfigurer : 설정하고 싶은 것들이 들어있는
@Configuration // WebMvcConfigurer를 implements해서 WebMvcConfig를 IoC에 등록해야됨 (@Component 포함되어있음)
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/user/**") // 언제 interceptor가 발동하게 할 건지
                .addPathPatterns("/board/**")
                .addPathPatterns("/love/**") // 지워도 되지만 그냥 두고 하나 더 등록하는 게 낫다
                .addPathPatterns("/api/love/**")
                .addPathPatterns("/reply/**")
                .excludePathPatterns("/board/{id:\\d+}"); // 예외자리 - {id}는 정규표현식으로 처리
    }
}