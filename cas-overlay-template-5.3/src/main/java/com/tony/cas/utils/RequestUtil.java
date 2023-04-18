package com.tony.cas.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * request工具类
 * Created by William on 2017/1/6.
 */
public class RequestUtil {

    public static Logger logger = LoggerFactory.getLogger(RequestUtil.class);


    /**
     * 接收Post请求发来的数据（例如xml）
     */
    public static String getPostDataFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String inputLine;
        // 接收到的数据
        StringBuilder receiveData = new StringBuilder();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            while ((inputLine = in.readLine()) != null) {
                receiveData.append(inputLine);
            }
        } catch (IOException e) {
            logger.error("解析request返回内容失败!", e);

        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("关闭request返回内容失败!", e);
            }
        }
        return receiveData.toString();
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @return ip
     */
    private static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获得客户端真实IP地址
     *
     * @return ip地址
     */
    public static String getIpAddr() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return getIpAddr(request);
        } else {
            return "127.0.0.1";
        }
    }

//    public static void getRequestParams(JoinPoint joinPoint, StringBuilder sb) {
//        //获取执行方法的参数
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        if(requestAttributes==null) {
//            return;
//        }
//        sb.append("\n方法入参如下：\n");
//        //获取request
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//        String httpMethod = request.getMethod();
//        String params = "";
//        if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH".equals(httpMethod)) {
//            Object[] paramsArray = joinPoint.getArgs();
//            // java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
//            Object[] arguments  = new Object[paramsArray.length];
//            for (int i = 0; i < paramsArray.length; i++) {
//                if (paramsArray[i] instanceof ServletRequest || paramsArray[i] instanceof ServletResponse || paramsArray[i] instanceof MultipartFile) {
//                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
//                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
//                    continue;
//                }
//                arguments[i] = paramsArray[i];
//            }
//            params = JSONObject.toJSONString(arguments);
//        } else {
//            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//            Method method = signature.getMethod();
//            // 请求的方法参数值
//            Object[] args = joinPoint.getArgs();
//            // 请求的方法参数名称
//            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
//            String[] paramNames = u.getParameterNames(method);
//            if (args != null && paramNames != null) {
//                for (int i = 0; i < args.length; i++) {
//                    params += "  " + paramNames[i] + ": " + args[i];
//                }
//            }
//        }
//        sb.append(params);
//        sb.append("Request IP:").append(RequestUtil.getIpAddr()).append("\n");
//    }

    public static void getHttpParamLogInfo(StringBuilder sb) {
        //获取执行方法的参数
        sb.append("\n方法入参如下：\n");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            String value = request.getParameter(name);
            sb.append("参数").append(name).append(":").append(value).append("\n");
        }
        sb.append("Request IP:").append(RequestUtil.getIpAddr()).append("\n");
    }

    public static Map<String, String> getHttpParam() {
        //获取执行方法的参数
        Map<String, String> param = new HashMap<>();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            String value = request.getParameter(name);
            param.put(name, value);
        }
        return param;
    }

    public static String getBaseUrl() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String requestUrl = request.getRequestURL().toString();
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        return requestUrl.replace(requestUri, "").concat(contextPath).concat("/");
    }
}
