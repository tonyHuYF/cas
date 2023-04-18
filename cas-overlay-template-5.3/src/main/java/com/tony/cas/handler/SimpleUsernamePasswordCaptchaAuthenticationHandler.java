package com.tony.cas.handler;

import cn.hutool.core.util.ObjectUtil;
import com.tony.cas.contants.GeneralContants;
import com.tony.cas.contants.RedisContants;
import com.tony.cas.domain.UsernamePasswordCaptchaCredential;
import com.tony.cas.error.BusinessError;
import com.tony.cas.exception.MyAccountDisabledException;
import com.tony.cas.exception.MyAccountNotFoundException;
import com.tony.cas.service.UserService;
import com.tony.cas.utils.RequestUtil;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 自定义验证器
 */
public class SimpleUsernamePasswordCaptchaAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

    @Resource
    private UserService userService;

    private RedisTemplate<String, Integer> redisTemplate;


    public SimpleUsernamePasswordCaptchaAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order, RedisTemplate redisTemplate) {
        super(name, servicesManager, principalFactory, order);
        this.redisTemplate = redisTemplate;
    }


    @Override
    protected AuthenticationHandlerExecutionResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
        UsernamePasswordCaptchaCredential myCredential = (UsernamePasswordCaptchaCredential) credential;
        String requestCaptcha = myCredential.getCaptcha();
        //验证错误次数
        String ip = RequestUtil.getIpAddr();
        String key = ip + ":" + myCredential.getUsername();
        Integer ipErrorCount = redisTemplate.opsForValue().get(RedisContants.IP_ERROR_CACHE_NAME + ip);
        //如果此ip登录次数超过100次，禁止访问
        if (ObjectUtil.isNotEmpty(ipErrorCount) && ipErrorCount >= GeneralContants.IP_ERROR_COUNT) {
            throw new MyAccountDisabledException();
        }

        Integer errorCount = redisTemplate.opsForValue().get(RedisContants.ERROR_CACHE_NAME + key);
        if (ObjectUtil.isNotEmpty(errorCount) && errorCount >= GeneralContants.ACCOUNT_ERROR_COUNT) {
            throw new MyAccountDisabledException();
        }

        //验证验证码是否正确
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//        boolean flag = CaptchaUtil.verification(request, requestCaptcha, true);
        boolean flag =true;

        if (!flag) {
            throw new MyAccountNotFoundException(BusinessError.VERIFICATION_CODE_WRONG.getMessage());
        }

        String userCredential = myCredential.getUsername();

        if (ObjectUtil.isNotEmpty(credential) && (userCredential.contains("$")
                || userCredential.contains("jndi") || userCredential.contains("ldap"))) {
            myCredential.setUsername("");
            throw new CredentialException(BusinessError.USERNAME_OR_PASSWORD_ERROR.getMessage());
        }

        Map<String, Object> map = userService.findUserByNameOrMobile(userCredential);

        //如果找不到账户或者密码错误
        if (ObjectUtil.isEmpty(map) || !map.get("password").equals(myCredential.getPassword())) {
            //记录此账户的错误次数
            if (ObjectUtil.isNotNull(errorCount)) {
                errorCount += 1;
                redisTemplate.opsForValue().set(RedisContants.ERROR_CACHE_NAME + key, errorCount, 30, TimeUnit.MINUTES);
            } else {
                redisTemplate.opsForValue().set(RedisContants.ERROR_CACHE_NAME + key, 1, 30, TimeUnit.MINUTES);
            }

            //记录IP错误次数
            if (ObjectUtil.isNotNull(ipErrorCount)) {
                ipErrorCount += 1;
                redisTemplate.opsForValue().set(RedisContants.IP_ERROR_CACHE_NAME + ip, ipErrorCount, 1, TimeUnit.DAYS);
            } else {
                redisTemplate.opsForValue().set(RedisContants.IP_ERROR_CACHE_NAME + ip, 1, 1, TimeUnit.DAYS);
            }
            throw new CredentialException(BusinessError.USERNAME_OR_PASSWORD_ERROR.getMessage());
        }

        int status = (int) map.get(GeneralContants.STATUS);
        if (status != 0) {
            throw new MyAccountDisabledException(BusinessError.ACCOUNT_NOT_AVAILABLE_ERROR.getMessage());
        }

        //登录成功，刷新错误次数
        redisTemplate.opsForValue().set(RedisContants.ERROR_CACHE_NAME + key, 0, 30, TimeUnit.MINUTES);
        map.put("username", map.get("user_name"));

        return createHandlerResult(credential, this.principalFactory.createPrincipal(userCredential, map));
    }

    @Override
    public boolean supports(Credential credential) {
        return credential instanceof UsernamePasswordCredential;
    }
}
