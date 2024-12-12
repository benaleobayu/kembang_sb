package com.kembang.service.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class ClientInfoService {
    public String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    public String getBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String browser = "Unknown";

        if (userAgent != null) {
            if (userAgent.contains("MSIE")) {
                browser = "Internet Explorer";
            } else if (userAgent.contains("Firefox")) {
                browser = "Firefox";
            } else if (userAgent.contains("Chrome")) {
                browser = "Chrome";
            } else if (userAgent.contains("Safari") && userAgent.contains("Version")) {
                browser = "Safari";
            } else if (userAgent.contains("Opera") || userAgent.contains("OPR")) {
                browser = "Opera";
            }
        }
        return browser;
    }

    public String getBrowserVersion(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String version = "Unknown";

        if (userAgent != null) {
            if (userAgent.contains("MSIE")) {
                version = userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[1];
            } else if (userAgent.contains("Firefox")) {
                version = userAgent.substring(userAgent.indexOf("Firefox")).split("/")[1];
            } else if (userAgent.contains("Chrome")) {
                version = userAgent.substring(userAgent.indexOf("Chrome")).split("/")[1].split(" ")[0];
            } else if (userAgent.contains("Safari") && userAgent.contains("Version")) {
                version = userAgent.substring(userAgent.indexOf("Version")).split("/")[1].split(" ")[0];
            } else if (userAgent.contains("Opera") || userAgent.contains("OPR")) {
                version = userAgent.substring(userAgent.indexOf("Version")).split("/")[1].split(" ")[0];
            }
        }
        return version;
    }

    public String getDeviceId(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String deviceId = "Unknown";

        if (userAgent != null) {
            if (userAgent.toLowerCase().contains("mobile")) {
                deviceId = "Mobile";
            } else if (userAgent.toLowerCase().contains("tablet")) {
                deviceId = "Tablet";
            } else {
                deviceId = "Desktop";
            }
        }
        return deviceId;
    }

}
