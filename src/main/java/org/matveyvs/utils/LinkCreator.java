package org.matveyvs.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkCreator {
    public String createLink(String link, String parameter, String id){
        return "/" + link + "?" + parameter + "=" + id;
    }
}
