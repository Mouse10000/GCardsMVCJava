package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT u.username, u.password, 'true' as enabled FROM user u WHERE u.username=?")
                .authoritiesByUsernameQuery(
                        "SELECT u.username, r.name " +
                        "FROM user u " +
                        "INNER JOIN userRole ur ON u.id = ur.userId " +
                        "INNER JOIN role r ON r.id = ur.roleId " +
                        //"INNER JOIN roles r ON u.role_id = r.id " +
                        "WHERE u.username=?"
                )
                .passwordEncoder(passwordEncoder)
                .rolePrefix("");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)//для тестов - а так лучше убрать
                .authorizeRequests()
                .antMatchers("/cards").permitAll()
                .antMatchers("/home").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/profile").authenticated()
                .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/profile", true)
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll();

    }
    /*@Override
    public void configure(WebSecurity web) throws Exception {
        http
        .antMatchers("/resources/**", "/image/**");
    }*/
}
