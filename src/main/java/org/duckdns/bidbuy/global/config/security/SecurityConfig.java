package org.duckdns.bidbuy.global.config.security;

import java.util.Arrays;
import java.util.Collections;

import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.global.auth.domain.RefreshTokenRepository;
import org.duckdns.bidbuy.global.auth.filter.LoginFilter;
import org.duckdns.bidbuy.global.auth.filter.LogoutFilterCustom;
import org.duckdns.bidbuy.global.auth.jwt.JWTFilter;
import org.duckdns.bidbuy.global.auth.jwt.JWTUtil;
import org.duckdns.bidbuy.global.auth.oauth.OAuthSuccessHandler;
import org.duckdns.bidbuy.global.auth.service.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

// import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;
    private final OAuth2UserService oAuth2UserService;
    private final OAuthSuccessHandler oAuthSuccessHandler;


    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Arrays.asList("https://bidbuy.duckdns.org","https://api-bidbuy.duckdns.org:5000","http://localhost:3000", "http://localhost:5000"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Arrays.asList("Set-Cookie","Authorization"));

                    return configuration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2Login((oauth2)->oauth2
                        .userInfoEndpoint((userInfo)->userInfo
                                .userService(oAuth2UserService))
                                .successHandler(oAuthSuccessHandler))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/login","/api/v1/oauth2/redirect/**" ,"/login/oauth2/code/**","/login","/","/user", "/join","/api/v1/auth/**", "api/v1/search/**" ).permitAll()
                        .requestMatchers("/admin").hasAuthority("ADMIN")  // hasRole("ADMIN") 대신 hasAuthority("ADMIN") 사용
                        .requestMatchers("/api/refreshToken").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling((exception) -> exception
                        .authenticationEntryPoint((request, response, authException) -> response.sendError(401))
                        .accessDeniedHandler((request, response, accessDeniedException) -> response.sendError(403)))
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(),jwtUtil,refreshTokenRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LogoutFilterCustom(jwtUtil, refreshTokenRepository), LogoutFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 임시로 h2-console, swagger-ui는 Spring Security 무시
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")
                                            , new AntPathRequestMatcher("/v3/api-docs/**")
                                            , new AntPathRequestMatcher("/swagger-ui/**"));
    }

    // "ROLE_" 접두사 제거
    @Bean
    SecurityExpressionHandler<FilterInvocation> expressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setDefaultRolePrefix("");
        return defaultWebSecurityExpressionHandler;
    }
}