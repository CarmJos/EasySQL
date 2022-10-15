package cc.carm.lib.easysql.api.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UUIDUtil {

    private static final Pattern COMPILE = Pattern.compile("-", Pattern.LITERAL);

    public static UUID random() {
        return UUID.randomUUID();
    }

    public static String toString(UUID uuid, boolean withDash) {
        if (withDash) return uuid.toString();
        else return COMPILE.matcher(uuid.toString()).replaceAll(Matcher.quoteReplacement(""));
    }

    public static UUID toUUID(String s) {
        if (s.length() == 36) {
            return UUID.fromString(s);
        } else {
            return UUID.fromString(s.substring(0, 8) + '-' + s.substring(8, 12) + '-' + s.substring(12, 16) + '-' + s.substring(16, 20) + '-' + s.substring(20));
        }
    }

}
