package site.keydeuk.store.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.keydeuk.store.common.security.authentication.*;
import site.keydeuk.store.common.security.authentication.token.TokenService;
import site.keydeuk.store.common.security.authorization.SecurityAccessDeniedHandler;
import site.keydeuk.store.common.security.authorization.SecurityAuthenticationEntryPoint;
import site.keydeuk.store.common.security.authorization.TokenAuthorityConfigurer;
import site.keydeuk.store.domain.oauth2.service.OAuth2UserService;
import site.keydeuk.store.domain.security.handler.CustomOAuth2LoginSuccessHandler;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Slf4j
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] PERMIT_ALL_URLS = new String[]{
            "/test/**",
            "/login",
            "/",
            "/logout",
    };

    private static final String[] PERMIT_ALL_GET_URLS = new String[]{
            "/login.html",
            "/favicon.ico",
            "/api-docs/**",
            "/swagger-ui/**",
            "/api/v1/product/**",
            "/api/v1/users/**",
            "/api/v1/oauth2/signUp",
            "/oauth2/signUp",
            "/api/v1/reviews",
            "/api/v1/community/**",
            "/api/v1/custom/get/random-option-products",
            "/api/v1/search",
            "/api/v1/search/all/products-name"
    };

    private static final String[] PERMIT_ALL_POST_URLS = new String[]{
            "/api/v1/users/**",
            "/api/v1/reissue"
    };

    private final ObjectMapper objectMapper;
    private final OAuth2UserService oAuth2UserService;
    private final UserDetailsService userDetailsService;

    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(createCorsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PERMIT_ALL_URLS).permitAll()
                        .requestMatchers(GET, PERMIT_ALL_GET_URLS).permitAll()
                        .requestMatchers(POST, PERMIT_ALL_POST_URLS).permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logoutConfigurer -> logoutConfigurer
                        .addLogoutHandler(createLogoutHandler())
                        .logoutSuccessHandler(createLogoutSuccessHandler())
                )
                .with(
                        new LoginAuthenticationConfigurer<>(createAuthenticationFilter()),
                        SecurityAuthenticationFilter -> SecurityAuthenticationFilter
                                .successHandler(createAuthenticationSuccessHandler())
                                .failureHandler(createAuthenticationFailureHandler())
                )
                .oauth2Login(oauth2Configurer -> oauth2Configurer
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService)) //로그인 완료 후 회원 정보 받기
                        .successHandler(customOAuth2LoginSuccessHandler()))
                .with(
                        new TokenAuthorityConfigurer(tokenService, userDetailsService),
                        Customizer.withDefaults()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(createAccessDeniedHandler())
                        .authenticationEntryPoint(createAuthenticationEntryPoint())
                );


        return http.getOrBuild();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                // error endpoint를 열어줘야 함, favicon.ico 추가!
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public CorsConfigurationSource createCorsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000", //프론트 주소
                "http://localhost:3001",
                "https://keydeuk.com/",
                "https://keydeuk-be.shop",
                "http://13.125.98.110:3000",
                "http://13.124.105.54:8080"
        ));
        corsConfiguration.setAllowedMethods(List.of(
                GET.name(),
                POST.name(),
                PUT.name(),
                PATCH.name(),
                DELETE.name(),
                OPTIONS.name()
        ));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(
                                "http://localhost",
                                "http://localhost:3000",
                                "http://localhost:*",
                                "http://43.201.71.50",
                                "http://43.201.71.50:*",
                                "http://13.124.105.54",
                                "http://13.124.105.54:*",
                                "http://13.124.105.54:8080",
                                "https://keydeuk-be.shop",
                                "http://keydeuk-be.shop"
                        )
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                        .allowCredentials(true)
                        .maxAge(3000);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private LogoutHandler createLogoutHandler() {
        return new LogoutTokenHandler(objectMapper, tokenService);
    }

    private LogoutSuccessHandler createLogoutSuccessHandler() {
        return new LogoutTokenSuccessHandler(objectMapper);
    }

    @Bean
    public CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler() {
        return new CustomOAuth2LoginSuccessHandler(objectMapper, tokenService);
    }

    private AbstractAuthenticationProcessingFilter createAuthenticationFilter() {
        return new LoginAuthenticationFilter(objectMapper);
    }

    private AuthenticationSuccessHandler createAuthenticationSuccessHandler() {
        return new LoginAuthenticationSuccessHandler(objectMapper, tokenService);
    }

    private AuthenticationFailureHandler createAuthenticationFailureHandler() {
        return new LoginAuthenticationFailureHandler(objectMapper);
    }

    private AccessDeniedHandler createAccessDeniedHandler() {
        return new SecurityAccessDeniedHandler();
    }

    private AuthenticationEntryPoint createAuthenticationEntryPoint() {
        return new SecurityAuthenticationEntryPoint(objectMapper);
    }
}
