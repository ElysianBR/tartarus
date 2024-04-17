package br.com.elysian.tartarus.utils;

import org.owasp.encoder.Encode;

public class StringUtils {

    public static String format(String s, Object... args) {
        return "";
    }

    public static String formatXSS(String s, Object... args) {
        Object[] encodedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            encodedArgs[i] = Encode.forHtml(args[i].toString());
        }
        return String.format(s, encodedArgs);
    }

}
