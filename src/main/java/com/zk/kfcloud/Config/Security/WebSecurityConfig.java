package com.zk.kfcloud.Config.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public  class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        // Spring会自动寻找实现接口的类注入,会找到我们的 UserServiceImpl  类
        @Autowired
        private UserDetailsService userDetailsService;

        /**
         * 使用自定义用户类（userDetailsService）作为AuthenticationManager中UserDetailsService实例
         * @param auth
         * @throws Exception
         */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
        }

        /**
         * 重点
         * @param http
         * @throws Exception
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()   // 取消csrf
                    .authorizeRequests()
                    .antMatchers(    // 允许对于网站静态资源的无授权访问
                            HttpMethod.GET,
                            "/",
                            "/*.html",
                            "/favicon.ico",
                            "/**/*.html",
                            "/**/*.css",
                            "/**/*.js",
                            "/webjars/**",
                            "/swagger-resources/**",
                            "/*/api-docs"
                    ).permitAll()
                    //允许微信发送的请求
                    .antMatchers("/access","/code","/redirect_uri","/isBrother").permitAll()
                    .antMatchers("/","/login","/index").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .logout()
                    .permitAll();
        }
}