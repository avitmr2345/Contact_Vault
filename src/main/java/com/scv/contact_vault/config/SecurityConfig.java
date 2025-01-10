package com.scv.contact_vault.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.scv.contact_vault.services.CustomSecurityUserDetailService;

@Configuration
public class SecurityConfig {

    @SuppressWarnings("unused")
    private CustomSecurityUserDetailService userDetailService;

    private OAuthAuthenticationSuccessHandler handler;

    public SecurityConfig(CustomSecurityUserDetailService theCustomSecurityUserDetailService,
            OAuthAuthenticationSuccessHandler theOAuthAuthenticationSuccessHandler) {

        userDetailService = theCustomSecurityUserDetailService;
        handler = theOAuthAuthenticationSuccessHandler;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        http.formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.defaultSuccessUrl("/user/profile", true);
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
        }).csrf(csrf -> csrf.disable());

        http.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        http.logout(logout -> logout.permitAll());

        return http.build();
    }
}
