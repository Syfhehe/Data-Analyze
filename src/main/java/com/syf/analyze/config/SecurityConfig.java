package com.syf.analyze.config;


import com.syf.analyze.service.DataAnalyzeUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@ComponentScan("sample.service")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataAnalyzeUserDetailService userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/css/**", "/data/**", "/bootstrap/**", "/bootstrap-social/**", "/datatables/**", "/datatables-plugins/**", "/datatables-responsive/**", "/flot/**", "/flot-tooltip/**", "/font-awesome/**", "/jquery/**", "/metisMenu/**", "/morrisjs/**", "/raphael/**", "/fonts/**", "/js/**").permitAll().anyRequest().fullyAuthenticated().and().formLogin().loginPage("/login").failureUrl("/login?error").permitAll().and().logout().permitAll();
        http.csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailService);
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("salt");
        authProvider.setSaltSource(saltSource);
        auth.authenticationProvider(authProvider);
    }

    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }

}
