package com.tony.cas.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wangsaichao
 * @date: 2018/8/2
 * @description: 配置默认跳转路径
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index2(HttpServletRequest request) {
        return "forward:/index";
    }


    @RequestMapping("/index")
    public String index(HttpServletRequest request) {

        //获取cas给我们传递回来的对象，这个东西放到了session中，session的 key是 _const_cas_assertion_
        //Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        //获取登录用户名
        //String loginName =assertion.getPrincipal().getName();
        //获取返回的数据  这些在jsp中写的  在这里并没有什么用
        //if (request.getUserPrincipal() != null) {
        //    AttributePrincipal principal  = (AttributePrincipal) request.getUserPrincipal();
        //    if (principal instanceof AttributePrincipal) {
        //        //cas传递过来的数据
        //        Map<String,Object> result =principal.getAttributes();
        //        for(Map.Entry<String, Object> entry :result.entrySet()) {
        //            String key = entry.getKey();
        //            Object val = entry.getValue();
        //        }
        //    }
        //}

        return "index";
    }

}
