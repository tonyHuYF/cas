package com.tony.cas.utils;

import cn.hutool.core.util.ObjectUtil;
import com.tony.cas.config.LoginCodeProperties;
import com.tony.cas.error.BusinessError;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;

@Component
public class CaptchaUtil {
    public static final String SESSION_KEY = "happy-captcha";

    /**
     * 获取验证码生产类
     */
    public Captcha getCaptcha(LoginCodeProperties properties) {
        if (ObjectUtil.isNull(properties)) {
            properties = new LoginCodeProperties();
        }
        return switchCaptcha(properties);
    }

    /**
     * 依据配置信息生产验证码
     */
    public Captcha switchCaptcha(LoginCodeProperties properties) {
        Captcha captcha;
        synchronized (this) {
            switch (properties.getCodeType()) {
                case arithmetic:
                    //算术类型 https://gitee.com/whvse/EasyCaptcha
                    captcha = new FixedArithmeticCaptcha(properties.getWidth(), properties.getHeight());
                    //几位数运算，默认是两位
                    captcha.setLen(properties.getLength());
                    break;
                case chinese:
                    captcha = new ChineseCaptcha(properties.getWidth(), properties.getHeight());
                    captcha.setLen(properties.getLength());
                    break;
                case chinese_gif:
                    captcha = new ChineseGifCaptcha(properties.getWidth(), properties.getHeight());
                    captcha.setLen(properties.getLength());
                    break;
                case gif:
                    captcha = new GifCaptcha(properties.getWidth(), properties.getHeight());
                    captcha.setLen(properties.getLength());
                    break;
                case spec:
                    captcha = new SpecCaptcha(properties.getWidth(), properties.getHeight());
                    captcha.setLen(properties.getLength());
                    break;
                default:
                    throw new RuntimeException(BusinessError.CODE_CONFIG_ERROR.getMessage());
            }
        }
        if (ObjectUtil.isNotEmpty(properties.getFontName())) {
            captcha.setFont(new Font(properties.getFontName(), Font.PLAIN, properties.getFontSize()));
        }
        return captcha;
    }


    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            int n1 = num(1, 10);
            int n2 = num(1, 10);
            int opt = num(3);

            //计算结果
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            //转换为字符运算符
            char optChar = "+-x".charAt(opt);

            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);

            return chars.toCharArray();
        }
    }

    /**
     * 校验验证码
     */
    public static boolean verification(HttpServletRequest request, String code, boolean ignoreCase) {
        if (code == null || code.trim().equals("")) {
            return false;
        }
        String captcha = (String) request.getSession().getAttribute(SESSION_KEY);
        return ignoreCase ? code.equalsIgnoreCase(captcha) : code.equals(captcha);
    }

    /**
     * 去除session中的验证码
     */
    public static void remove(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_KEY);
    }

}
