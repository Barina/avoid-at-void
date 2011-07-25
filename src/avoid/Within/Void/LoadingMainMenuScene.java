package avoid.Within.Void;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class LoadingMainMenuScene extends Scene
{
	private Texture loadingTexture;
	private TiledTextureRegion loadingTextureRegion;
	private final LiveAnimatedSprite loadingSprite;

	public LoadingMainMenuScene(final MainMenuActivity mainMenuActivity)
	{
		super(1);
		this.setBackground(new ColorBackground(0, 0, 0));
		this.loadingTexture = new Texture(512, 512, TextureOptions.BILINEAR);
		this.loadingTextureRegion = TextureRegionFactory.createTiledFromAsset(loadingTexture, mainMenuActivity, "gfx/loading.png", 0, 0, 1, 8);
		mainMenuActivity.getEngine().getTextureManager().loadTexture(loadingTexture);
		loadingSprite = new LiveAnimatedSprite(mainMenuActivity, null, 0, 0, 0, 0, loadingTextureRegion);
		loadingSprite.setPosition(0, Constants.CAMERA_HEIGHT - loadingSprite.getHeightScaled());
		loadingSprite.animate(new long[]
		{300, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, new int[]
		{0, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1}, 10);
		this.getLastChild().attachChild(loadingSprite);
//		mainMenuActivity.runOnUpdateThread(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				mainMenuActivity.loadResources();
//				mainMenuActivity.getMainMenuScene().loadScene().normalize();
//				mainMenuActivity.getHelpScene().loadScene();
//				mainMenuActivity.getSettingsScene().loadScene();
//				mainMenuActivity.getEngine().setScene(mainMenuActivity.getMainMenuScene());
//			}
//		});
		new AsyncTaskLoader().execute(new IAsyncCallback()
		{
			@Override
			public void workToDo()
			{
				mainMenuActivity.loadResources();
				mainMenuActivity.getMainMenuScene().loadScene().normalize();
				mainMenuActivity.getHelpScene().loadScene();
				mainMenuActivity.getSettingsScene().loadScene();
			}

			@Override
			public void onComplete()
			{
				mainMenuActivity.getEngine().setScene(mainMenuActivity.getMainMenuScene());
			}
		});
	}
}