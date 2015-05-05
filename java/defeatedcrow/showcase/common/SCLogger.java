package defeatedcrow.showcase.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SCLogger {
	
	public static Logger logger = LogManager.getLogger("DCsShowcase");
	
	public static void debugInfo(String msg) {
		if (ShowcaseCore.debugMode) {
			SCLogger.logger.info("Debug: " + msg);
		}
	}

}
