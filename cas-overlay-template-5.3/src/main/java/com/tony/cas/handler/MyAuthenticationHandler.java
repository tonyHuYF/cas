package com.tony.cas.handler;

import cn.hutool.core.util.ObjectUtil;
import com.tony.cas.service.UserService;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;

import javax.annotation.Resource;
import javax.security.auth.login.AccountNotFoundException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 自定义验证器
 */
public class MyAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

    @Resource
    private UserService userService;


    public MyAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential, String originalPassword) throws GeneralSecurityException, PreventedException {
        Map<String, Object> map = userService.findUserByNameOrMobile(credential.getUsername());

        if (ObjectUtil.isNotEmpty(map) && map.get("password").equals(credential.getPassword())) {
            return createHandlerResult(credential, this.principalFactory.createPrincipal(credential.getUsername()),
                    new ArrayList<>(0));
        } else {
            throw new AccountNotFoundException("必须是admin用户才允许通过!");
        }

    }

    @Override
    public boolean supports(Credential credential) {
        return super.supports(credential);
    }
}
