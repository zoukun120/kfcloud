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
                            "/","/**",
                            "/favicon.ico",
                            "/jquery/**",
                            "/bootstrap-3.3.7-dist/**",
                            "/layui/**",
                            "/webjars/**",
                            "/settings/**"
                    ).permitAll()
                    //允许微信发送的请求
                    .antMatchers("/access","/code","/redirect_uri","/isBrother","/createMenu","/state","/alarmtimeon").permitAll()
                    .antMatchers("/","/login","/index").permitAll()
                    .antMatchers("/subMenu","/current","/history","/anal/**","/settings/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .logout()
                    .permitAll();
        }


}