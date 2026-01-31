package org.fugerit.java.demo.micronaut.photobook.util;

public class SanitizeUtil {

    private SanitizeUtil() {}

    public static String sanitizeInputParameter( String param ) {
        return param.replaceAll("[\n\r]", "_");
    }

}
