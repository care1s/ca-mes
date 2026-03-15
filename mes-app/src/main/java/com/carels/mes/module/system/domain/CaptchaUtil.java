package com.carels.mes.module.system.domain;

import lombok.Data;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 验证码工具类 - carels
 * 
 * @author carels
 * @version V1.0
 * @date 2026-03-15
 */
@Data
public class CaptchaUtil {
    
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    
    /**
     * 生成验证码
     * 
     * @return 验证码信息
     */
    public static CaptchaInfo generateCaptcha() {
        CaptchaInfo captchaInfo = new CaptchaInfo();
        
        // 生成4位随机验证码
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        captchaInfo.setCode(code.toString());
        
        // 生成验证码图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // 背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 干扰线
        g.setColor(new Color(124, 58, 237));
        for (int i = 0; i < 3; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }
        
        // 绘制文字
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(new Color(124, 58, 237));
        for (int i = 0; i < 4; i++) {
            g.rotate((random.nextDouble() - 0.5) * 0.3, 20 + i * 25, 25);
            g.drawString(String.valueOf(code.charAt(i)), 15 + i * 25, 28);
            g.rotate(-(random.nextDouble() - 0.5) * 0.3, 20 + i * 25, 25);
        }
        
        g.dispose();
        
        // 转换为Base64
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            captchaInfo.setImageBase64("data:image/png;base64," + base64);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return captchaInfo;
    }
    
    @Data
    public static class CaptchaInfo {
        /** 验证码code */
        private String code;
        /** 验证码图片Base64 */
        private String imageBase64;
    }
}
