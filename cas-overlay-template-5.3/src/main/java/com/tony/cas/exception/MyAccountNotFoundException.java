package com.tony.cas.exception;

import javax.security.auth.login.AccountException;
import java.io.Serializable;

/**
 * 用户没找到--异常
 */
public class MyAccountNotFoundException extends AccountException implements Serializable {
    private static final long serialVersionUID = -1655421127534314774L;

    public MyAccountNotFoundException() {
        super();
    }

    public MyAccountNotFoundException(String msg) {
        super(msg);
    }
}
