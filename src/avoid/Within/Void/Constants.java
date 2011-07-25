package avoid.Within.Void;

import java.util.Random;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;

public class Constants
{
	public static final int CAMERA_WIDTH = 512, CAMERA_HEIGHT = 768;
	public static final ScreenOrientation SCREEN_ORIENTATION = ScreenOrientation.PORTRAIT;
	public static final IResolutionPolicy RESOLUTION_POLICY = new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT);
	private static boolean gameRunning = false;
	public static final Random RANDOM = new Random();

	public static synchronized void setGameRunning(boolean gameRunning)
	{
		Constants.gameRunning = gameRunning;
	}

	public static synchronized boolean isGameRunning()
	{
		return gameRunning;
	}
}