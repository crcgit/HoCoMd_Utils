package gov.howardcountymd.utils.misc;

import java.util.regex.Pattern;

public class EmailOps {

	// Email testing
	private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
	          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	          "\\@" +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	          "(" +
	          "\\." +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	          ")+"
	      );
	
	public static boolean isEmailValid(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}
	
}
