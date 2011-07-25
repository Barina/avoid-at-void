package avoid.Within.Void;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import android.view.KeyEvent;

public class MainMenuActivity extends BaseGameActivity
{
	protected Camera camera;
	private MainMenuScene mainMenuScene;
	private HelpScene helpScene;
	private SettingsScene settingsScene;

	@Override
	public Engine onLoadEngine()
	{
		Settings.loadContext(getApplicationContext());
		this.camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, Constants.SCREEN_ORIENTATION, Constants.RESOLUTION_POLICY, this.camera).setNeedsMusic(true).setNeedsSound(true));
	}

	@Override
	public void onLoadResources()
	{
		loadResources();
	}

	public void loadResources()
	{
		GameSoundsFactory.onLoadResources(this);
		BackgroundFactory.loadResources(this);
		EnemyFactory.loadResources(this);
	}

	@Override
	public Scene onLoadScene()
	{
		getMainMenuScene().loadScene();
		getHelpScene().loadScene();
		getSettingsScene().loadScene();
		return getMainMenuScene();
		// return new LoadingMainMenuScene(this);
	}

	@Override
	public void onLoadComplete()
	{}

	public MainMenuScene getMainMenuScene()
	{
		if(mainMenuScene == null)
			mainMenuScene = new MainMenuScene(this);
		return mainMenuScene;
	}

	public HelpScene getHelpScene()
	{
		if(helpScene == null)
			helpScene = new HelpScene(this);
		return helpScene;
	}

	public SettingsScene getSettingsScene()
	{
		if(settingsScene == null)
			settingsScene = new SettingsScene(this);
		return settingsScene;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
				if(getMainMenuScene().hasChildScene())
				{
					getMainMenuScene().back();
					getMainMenuScene().normalize();
				}
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}