package com.carels.mes.common.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Configuration
@MapperScan("com.carels.mes.**.mapper")
public class MyBatisConfig {
    
}
