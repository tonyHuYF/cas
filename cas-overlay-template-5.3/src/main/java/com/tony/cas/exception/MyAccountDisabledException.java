package com.tony.cas.exception;

import javax.security.auth.login.AccountException;
import java.io.Serializable;

/**
 * 账号不可用exception
 */
public class MyAccountDisabledException extends AccountException implements Serializable {

    private static final long serialVersionUID = -860860643786582709L;

    public MyAccountDisabledException() {
        super();
    }

    public MyAccountDisabledException(String msg) {
        super(msg);
    }
}
