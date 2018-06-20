package com.teasoft.rydeon.auth;

import com.teasoft.auth.sec.LoginFilter;
import com.teasoft.auth.sec.MyPasswordEncoder;
import com.teasoft.auth.sec.TokenAuthFilter;
import com.teasoft.auth.sec.TokenAuthService;
import com.teasoft.auth.sec.UserDetailsService;
import javax.inject.Inject;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@Order(1)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("token.secret")
    public String SECRET;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    TokenAuthService tokenAuthService;

    public SecurityConfig() {
        super(true);
    }
    
    @Inject private SecurityProperties securityProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .anonymous().and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/src/**").permitAll()
                .antMatchers("/bower_components/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/style.css").permitAll()
                .antMatchers("/websocket.js").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/licence.html").permitAll()
                .antMatchers("/health").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(new TokenAuthFilter(tokenAuthService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilter("/auth/login", authenticationManager(), tokenAuthService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    protected Filter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addExposedHeader("Location");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new MyPasswordEncoder());
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }
    
    
}
