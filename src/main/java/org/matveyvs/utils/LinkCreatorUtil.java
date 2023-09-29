package org.matveyvs.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkCreatorUtil {
    public String createLink(String reqUri, String reqQueryString) {
        String parameters = "";
        if (reqQueryString != null) {
            parameters = "?" + reqQueryString;
        }
        return reqUri + parameters;
    }
}
