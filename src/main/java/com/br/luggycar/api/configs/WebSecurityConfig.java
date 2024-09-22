package com.br.luggycar.api.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests( (authorize) -> authorize
                .requestMatchers("/api/vehicle").permitAll()
                .requestMatchers("/api/registration").hasAuthority("admin")
                .anyRequest().authenticated()
        );
        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// ref https://www.youtube.com/watch?v=t6prPki7daU

//insert into user values('e0eb1b19-3d58-4ad8-8e14-55a89303c8e5', '$2a$10$amnMHdrqcIyjJk.BH6Wc2OgOMGTsjOW47F22CKiA62KLxR9uVy4rO', 'admin');
//insert into user values('1abdeca8-34c7-4e4c-9357-94555f6a3780', '$2a$10$amnMHdrqcIyjJk.BH6Wc2OgOMGTsjOW47F22CKiA62KLxR9uVy4rO', 'user');

//insert into role values('bb1c6a69-08a8-4026-b9ca-ec0235f063af', 'ADMIN')
//insert into role values('913bec7f-197d-4461-9fae-70a914294726', 'USER')

//insert into users_roles values('uuid de user', 'uuid da role')




//        http
//                .httpBasic()
//                .and()
//                .authorizeHttpRequests()
//                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/api").hasAnyRole("USER","ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
//                .anyRequest()
//                .authenticated()
//                .and()
//                .csrf().disable();