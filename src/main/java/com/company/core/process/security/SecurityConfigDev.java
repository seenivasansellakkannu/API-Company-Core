/**
 * 
 */
package com.company.core.process.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Seenivasan S
 *
 */
@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfigDev extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
    	log.info("Bypassing Security in DEVELOPMENT Environment");
    	
    	http.csrf().disable().authorizeRequests().antMatchers("**/**").authenticated();
		
    }
    
}