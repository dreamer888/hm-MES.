package com.dream.iot.utils;

public class UrlUtils {

    /**
     * 剔除参数的uri
     * @param url
     * @return
     */
    public static String uriCutParam(String url) {
        int indexOf = url.indexOf("?");
        return indexOf == -1 ? url : url.substring(0, indexOf);
    }

    public static String subUrlOfRootStart(String url) {
        return url.startsWith("/") ? url : "/"+url;
    }
}
