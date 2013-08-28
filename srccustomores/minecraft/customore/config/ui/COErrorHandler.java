package customore.config.ui;

import java.awt.Frame;

import customore.util.COLogger;

public class COErrorHandler {

	public static boolean onConfigError(Throwable error)
	{
		COLogger.log.throwing("CustomOreGen.ServerState", "loadWorldConfig", error);
		Frame[] frames = Frame.getFrames();

		if (frames != null && frames.length > 0)
		{
			switch ((new ConfigErrorDialog()).showDialog(frames[0], error))
			{
			case 1:
				return true;

			case 2:
				return false;
			}
		}
		error.printStackTrace();
		return false;
	}

}
