package com.proy.utp.backend_tambo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtTokenProvider, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configure(http))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                // PERMITIR SWAGGER
                .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml",
                        "/api-docs/**"
                ).permitAll()
                // PERMITIR RECURSOS ESTÁTICOS DE SWAGGER
                .requestMatchers(
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/public/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/pedidos/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/pedidos/**").authenticated()
                .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
        return http.build();
    }
}
