package customore.config.ui;

import customore.util.COLogger;


public class COErrorHandler {

	public static boolean onConfigError(Throwable error)
	{
		COLogger.log.throwing("CustomOre", "onConfigError", error);
		error.printStackTrace();
		return false;
	}

}
