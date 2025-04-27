package com.wanted.cqrs.config.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class LogAspectConfig {
    @Bean
    @Profile("dev || prod")
    public LogAspect LogAspect(Environment env) { return new LogAspect(env); }
}
