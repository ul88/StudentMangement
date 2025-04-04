package com.ul88.be.config;

import com.ul88.be.security.JwtUtil;
import com.ul88.be.security.JwtAuthenticationFilter;
import com.ul88.be.security.JwtUsernamePasswordAuthenticationFilter;
import com.ul88.be.service.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberDetailsService memberDetailsService;
    private final JwtUtil jwtUtil;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors
                .configurationSource(corsConfigurationSource));
        http.formLogin(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/logout").permitAll()
                .requestMatchers("/api/logout").authenticated()
                .requestMatchers("/api/admin").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.POST).hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.PUT).hasAnyRole("ADMIN","USER")
                .requestMatchers(HttpMethod.DELETE).hasAnyRole("ADMIN","USER")
                .anyRequest().permitAll()
        );
        http.addFilterBefore(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), jwtUtil), LogoutFilter.class);
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil,memberDetailsService), JwtUsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(memberDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = daoAuthenticationProvider();
        return new ProviderManager(daoAuthenticationProvider);
    }
}
