package util;
/*
 * output debug info
 */
public class LineInfo {
	public static String getLineInfo(){
		 StackTraceElement ste = new Throwable().getStackTrace()[1];
	     return ste.getFileName() + ": Line " + ste.getLineNumber();
	}
}
