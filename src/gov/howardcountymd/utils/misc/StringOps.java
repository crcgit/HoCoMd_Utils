package gov.howardcountymd.utils.misc;

import java.util.Locale;

public class StringOps {

	// Use to ensure caps don't mess up database keys, etc
	public static String getKeyForName(String name) {
		String key = name.trim().toLowerCase(Locale.US);
		return key;
	}
	
}
