package org.duckdns.bidbuy.global.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie, Authorization, refresh, access")
                .allowedOrigins("https://bidbuy.duckdns.org", "https://api-bidbuy.duckdns.org:5000" ,"http://localhost:3000", "http://localhost:5000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}