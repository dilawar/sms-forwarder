package com.dilawar;

import java.util.regex.Pattern;

public class Matcher {
    public static boolean matches(String wildcardString, String text) {
        Pattern pattern = Pattern.compile(wildcardToRegex(wildcardString));
        java.util.regex.Matcher m = pattern.matcher(text);
        return m.matches();
    }

    static String wildcardToRegex(String wildcardString) {
        // The 12 is arbitrary, you may adjust it to fit your needs depending
        // on how many special characters you expect in a single pattern.
        StringBuilder sb = new StringBuilder(wildcardString.length() + 12);
        sb.append('^');
        for (int i = 0; i < wildcardString.length(); ++i) {
            char c = wildcardString.charAt(i);
            if (c == '*') {
                sb.append(".*");
            } else
                if (c == '?') {
                    sb.append('.');
                } else
                    if ("\\.[]{}()+-^$|".indexOf(c) >= 0) {
                        sb.append('\\');
                        sb.append(c);
                    } else {
                        sb.append(c);
                    }
        }
        sb.append('$');
        return sb.toString();
    }
}
