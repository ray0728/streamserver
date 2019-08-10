package com.rcircle.service.stream.utils;

import java.io.File;

public class Toolkit {
    public static String encodeForUrl(String str) {
        str = str.replace("+", "%2B");
        str = str.replaceAll(" ", "%20");
        str = str.replaceAll("/", "%2F");
        str = str.replace("?", "%3F");
        str = str.replaceAll("%", "%25");
        str = str.replaceAll("#", "%23");
        str = str.replaceAll("&", "%26");
        str = str.replaceAll("=", "%3D");
        return str;
    }

    public static String decodeFromUrl(String str) {
        str = str.replace("%2B", "+");
        str = str.replaceAll("%20", " ");
        str = str.replaceAll("%2F", "/");
        str = str.replace("%3F", "?");
        str = str.replaceAll("%25", "%");
        str = str.replaceAll("%23", "#");
        str = str.replaceAll("%26", "&");
        str = str.replaceAll("%3D", "=");
        return str;
    }

    public static String assembleAbsoluteFilePath(String filepath, String filename) {
        if (filepath.lastIndexOf(File.separatorChar) == filepath.length() - 1) {
            return filepath + filename;
        }
        return filepath + File.separatorChar + filename;
    }

    public static String assembleBaseUrl(String baseurl, int id, String name) {
        if (baseurl.endsWith("/")) {
            baseurl = baseurl.substring(0, baseurl.length() - 1);
        }
        return String.format("%s/%d/%s/", baseurl, id, name);
    }
}
