package kr.co.finote.backend.global.config;

import kr.co.finote.backend.global.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new SessionInterceptor())
                .addPathPatterns("/users/**") // TODO https 설정 후 글쓰기 권한 확인 필요
                .excludePathPatterns("/users/auth/google", "/users/validation/**");
    }
}
