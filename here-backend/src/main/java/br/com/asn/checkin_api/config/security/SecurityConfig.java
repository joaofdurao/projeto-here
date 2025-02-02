package br.com.asn.checkin_api.config.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private JWTFilter jwtFilter;
        private UserDetailsService userDetailsService;

        public SecurityConfig(JWTFilter jwtFilter, UserDetailsService userDetailsService) {
                this.jwtFilter = jwtFilter;
                this.userDetailsService = userDetailsService;
        }

        private static String[] ROUTE_WL = {
                        "/usuarios",
                        "/usuarios/login"
        };

        private static String[] HOSTS_WL = {
                        "http://localhost:4200",
                        "http://34.74.179.238"
        };

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                return http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(request -> request.requestMatchers(ROUTE_WL).permitAll()
                                                .anyRequest().authenticated())
                                .httpBasic(Customizer.withDefaults())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();

        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList(HOSTS_WL));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                configuration.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
                provider.setUserDetailsService(userDetailsService);

                return provider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();

        }

}
