package avoid.Within.Void;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.anddev.andengine.util.Debug;
import android.content.Context;

public class Settings
{
	public enum Parameter
	{
		SHIP__LAST_SHIP, INT__LAST_LEVEL, INT__LAST_SCORE, INT__LAST_LIVES_COUNT, BOOL__CURVE_IN, BOOL__SHAKE_ANGLE, BOOL__BOTH_CANONS, BOOL__SHOT_UP, BOOL__SHOT_DOWN, BOOL__SHOT_LEFT, BOOL__SHOT_RIGHT;
		@Override
		public String toString()
		{
			switch (Parameter.values()[ordinal()])
			{
				case SHIP__LAST_SHIP:
					return "Last Used Ship";
				case INT__LAST_LEVEL:
					return "Last level";
				case INT__LAST_SCORE:
					return "Last Score";
				case INT__LAST_LIVES_COUNT:
					return "Last Lives Count";
				case BOOL__CURVE_IN:
					return "Shots Curved in";
				case BOOL__SHAKE_ANGLE:
					return "Shake Angle of Shot";
				case BOOL__BOTH_CANONS:
					return "Shot from both cannons";
				case BOOL__SHOT_UP:
					return "Shot Up";
				case BOOL__SHOT_DOWN:
					return "Shot Down";
				case BOOL__SHOT_LEFT:
					return "Shot Left";
				case BOOL__SHOT_RIGHT:
					return "Shot Right";
				default:
					return "Parameter Error";
			}
		};
	}

	private static Context activityContext;

	public static void loadContext(Context context)
	{
		activityContext = context;
	}

	public static Object getSetting(Parameter parameter)
	{
		ObjectInputStream inputStream = null;
		try
		{
			inputStream = new ObjectInputStream(activityContext.openFileInput(parameter.name()));
			return inputStream.readObject();
		}
		catch(Exception e)
		{
			Debug.w("Faild to load setting." + e.getMessage(), e);
		}
		finally
		{
			if(inputStream != null)
				try
				{
					inputStream.close();
				}
				catch(IOException e)
				{
					Debug.w(e.getMessage(), e);
				}
		}
		return null;
	}

	public static void setSetting(Parameter parameter, Object setting)
	{
		ObjectOutputStream outputStream = null;
		try
		{
			outputStream = new ObjectOutputStream(activityContext.openFileOutput(parameter.name(), 0));
			outputStream.writeObject(setting);
			outputStream.flush();
		}
		catch(Exception e)
		{
			Debug.w("Faild to save setting. " + e.getMessage(), e);
		}
		finally
		{
			if(outputStream != null)
				try
				{
					outputStream.close();
				}
				catch(IOException e)
				{
					Debug.w(e.getMessage(), e);
				}
		}
	}
}