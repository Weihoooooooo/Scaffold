package com.jcweiho.scaffold.common.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * IP工具类
 *
 * @author Weiho
 */
@UtilityClass
public class IpUtils extends NetUtil {
    /**
     * 获取客户端IP
     *
     * @param request 请求对象
     * @return IP地址
     */
    public String getIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : getMultistageReverseProxyIp(ip);
    }

    /**
     * 获取IP地址
     *
     * @return 本地IP地址
     */
    public String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
        }
        return "127.0.0.1";
    }

    /**
     * 获取主机名
     *
     * @return 本地主机名
     */
    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }
        return "未知";
    }

    /**
     * 根据ip获取详细地址
     */
    public String getCityInfo(String ip) {
        if (StringUtils.isBlank(ip)) {
            return null;
        }
        if (ping("www.hutool.cn")) {
            String api = UrlBuilder.ofHttp("whois.pconline.com.cn")
                    .addPath("/ipJson.jsp").addQuery("ip", ip).addQuery("json", "true").build();
            String result = HttpUtil.get(api, 1000);
            JSONObject object = JSONUtil.parseObj(result);
            return object.get("addr", String.class);
        } else {
            return "无网络";
        }
    }

    /**
     * 获取用户的浏览器
     *
     * @param request 请求
     * @return String
     */
    public String getBrowser(HttpServletRequest request) {
        return UserAgentUtil.parse(request.getHeader("User-Agent")).getBrowser().getName();
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return HttpServletRequest
     */
    public HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
