package org.matveyvs.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@UtilityClass
//@Component
@Log4j2
public class LinkCreatorUtil {
    public String createLink(String reqUri, Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return reqUri;
        }

        StringBuilder link = new StringBuilder(reqUri);
        link.append("?");

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();

            if (paramName != null && paramValue != null) {
                link.append(URLEncoder.encode(paramName, StandardCharsets.UTF_8));
                link.append("=");
                link.append(URLEncoder.encode(paramValue, StandardCharsets.UTF_8));
                link.append("&");
            }
        }

        if (link.charAt(link.length() - 1) == '&') {
            link.deleteCharAt(link.length() - 1);
        }

        return link.toString();
    }

    public String createLink(String reqUri, String parameter, String value) {
        if (parameter == null || value == null) {
            return reqUri;
        }
        StringBuilder link = new StringBuilder(reqUri);
        link.append("?");
        link.append(parameter);
        link.append("=");
        link.append(value);
        return link.toString();
    }
}
