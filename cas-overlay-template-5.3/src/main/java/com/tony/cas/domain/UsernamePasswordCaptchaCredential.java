package com.tony.cas.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apereo.cas.authentication.UsernamePasswordCredential;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernamePasswordCaptchaCredential extends UsernamePasswordCredential {
    private static final long serialVersionUID = 5287579267386526826L;

    /**
     * 验证码
     */
    @Size(min = 5, max = 5, message = "require captcha")
    private String captcha;
}
