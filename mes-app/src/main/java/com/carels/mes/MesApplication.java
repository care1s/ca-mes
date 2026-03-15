package com.carels.mes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * MES制造执行系统启动类
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 * @copyright carels版权所有
 */
@SpringBootApplication
@EnableScheduling
public class MesApplication {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║     MES制造执行系统 - carels版权所有                          ║");
        System.out.println("║     文档版本: V9.0                                           ║");
        System.out.println("║     编写日期: 2026-03-15                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        SpringApplication.run(MesApplication.class, args);
    }
}
