package ir.msob.jima.security.api.util;

public class BearerTokenExtractorUtil {

    public static String extract(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7).trim();
    }
}