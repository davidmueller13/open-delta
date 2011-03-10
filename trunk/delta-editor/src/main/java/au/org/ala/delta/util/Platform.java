package au.org.ala.delta.util;

public class Platform {
	
	public static boolean isWindowsAero() {		
		String os = System.getProperty("os.name");
		if (os != null && os.toLowerCase().contains("windows")) {
			String osversion = System.getProperty("os.version");	
			double v =Double.parseDouble(osversion);
			return v >= 6;
		}
		return false;
	}

}
