package avoid.Within.Void;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import android.view.KeyEvent;

public class GameActivity extends BaseGameActivity
{
	protected Camera camera;
	private GameScene gameScene;

	@Override
	public Engine onLoadEngine()
	{
//		Toast.makeText(getApplicationContext(), "Start at level: " + getIntent().getIntExtra("StartAtLevel", 1), Toast.LENGTH_LONG).show();
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, Constants.RESOLUTION_POLICY, getCamera()).setNeedsMusic(true).setNeedsSound(true));
	}

	private Camera getCamera()
	{
		if(camera == null)
			camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		return camera;
	}

	@Override
	public void onLoadResources()
	{
		GameSoundsFactory.onLoadResources(this);
		BackgroundFactory.loadResources(this);
		BlastFactory.loadResources(this);
		EnemyFactory.loadResources(this);
		BulletFactory.loadResources(this);
		getGameScene();
	}

	@Override
	public Scene onLoadScene()
	{
		this.mEngine.registerUpdateHandler(new FPSLogger());
		getGameScene().onLoadScene();
		return getGameScene();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_DPAD_CENTER:
				getGameScene().ship.setShield(!getGameScene().ship.getShield());
				getGameScene().gameHud.refreshHUD();
				return true;
			case KeyEvent.KEYCODE_MENU:
				if(!getGameScene().isGameRunning())
				{
					getGameScene().setGameRunning(true);
					GameSoundsFactory.resumeAll();
				}
				else
				{
					getGameScene().setGameRunning(false);
					GameSoundsFactory.pauseAll();
				}
				return true;
			case KeyEvent.KEYCODE_BACK:
				finish();
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onLoadComplete()
	{
		getGameScene().setGameRunning(true);
	}

	public GameScene getGameScene()
	{
		if(gameScene == null)
			gameScene = new GameScene(this);
		return gameScene;
	}

	public void resumeGame()
	{
		getGameScene().setGameRunning(true);
	}

	public void startNewGame()
	{
		getGameScene().reset();
		resumeGame();
	}

	@Override
	public void onUnloadResources()
	{
		getEngine().getTextureManager().unloadTextures(getGameScene().getTextures());
		super.onUnloadResources();
	}
}