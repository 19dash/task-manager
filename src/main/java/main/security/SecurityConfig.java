package main.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.
AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.DispatcherType;
import main.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean 
    public PasswordEncoder passwordEncoder() { 
       return new BCryptPasswordEncoder();
    }
   
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setUseReferer(true);
        return handler;
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/static/**");
    }
    @Bean
    public AuthenticationManager  authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(new UserService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) 
    		throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
			.requestMatchers("/registration","/resources/**",
					"/static/**","/css/**","/resources/static/css/**")
			.permitAll().anyRequest().authenticated()
		 )
        .formLogin(form -> form
        	.loginPage("/login")
        	.loginProcessingUrl("/login")
        	.defaultSuccessUrl("/addtask", true)
            .permitAll()
         )
         .logout((logout) -> logout.permitAll());
         return http.build();
    }

}
