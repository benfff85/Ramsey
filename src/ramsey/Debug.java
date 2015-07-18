package ramsey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Debug {

	public static void write(String inputString) {
		String debugString;
		
		if (Config.DEBUG_MODE) {
			debugString = "[" + getDateTimeStamp() + "] " + getMethodName(3) + ": " + inputString;
			System.out.println(debugString);
		}

	}
	
	

	/**
	 * Get the method name for a depth in call stack. <br/>
	 * Utility function
	 * 
	 * @param depth depth in the call stack (0 means current method, 1 means
	 *        call method, ...)
	 * @return method name
	 */
	private static String getMethodName(final int depth)
	{
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		return ste[depth].getMethodName();
	}
	
	
	/**
	 * This is used for generating the date time stamps.
	 * 
	 * @return String value of the current date time.
	 */
	private static String getDateTimeStamp(){
		DateFormat df = new SimpleDateFormat("MM-dd-yy HH:mm:ss");
		return df.format(new Date());
	}

}