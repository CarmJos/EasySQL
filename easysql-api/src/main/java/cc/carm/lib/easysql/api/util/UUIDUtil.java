package cc.carm.lib.easysql.api.util;

import java.util.UUID;

public class UUIDUtil {

	public static UUID toUUID(String s) {
		if (s.length() == 36) {
			return UUID.fromString(s);
		} else {
			return UUID.fromString(s.substring(0, 8) + '-' + s.substring(8, 12) + '-' + s.substring(12, 16) + '-' + s.substring(16, 20) + '-' + s.substring(20));
		}
	}

}
