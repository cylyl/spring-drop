package com.github.cylyl.springdrop.acl.config;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableCaching
public class AclContext extends BaseAclContext {

}