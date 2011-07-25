package avoid.Within.Void;

import java.io.IOException;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.modifier.IModifier;
import android.content.Intent;

public class SplashActivity extends BaseGameActivity
{
	private Camera camera;
	private Texture mTexture;
	private TextureRegion mTextureRegion;
	private Sprite badgeSprite;
	private Sound logoSound;

	@Override
	public Engine onLoadEngine()
	{
		this.camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, Constants.SCREEN_ORIENTATION, Constants.RESOLUTION_POLICY, this.camera).setNeedsSound(true));
	}

	@Override
	public void onLoadResources()
	{
		TextureRegionFactory.setAssetBasePath("gfx/");
		try
		{
			this.logoSound = SoundFactory.createSoundFromAsset(getSoundManager(), this, "mfx/hard_bass.ogg");
		}
		catch(IOException e)
		{
			Debug.v("Error: " + e.getMessage(), e);
		}
		this.mTexture = new Texture(512, 512, TextureOptions.BILINEAR);
		this.mTextureRegion = TextureRegionFactory.createFromAsset(this.mTexture, this, "andengine_badge.png", 0, 0);
		final float centerX = (Constants.CAMERA_WIDTH - this.mTextureRegion.getWidth()) * 0.5f, centerY = (Constants.CAMERA_HEIGHT - this.mTextureRegion.getHeight()) * 0.5f;
		this.badgeSprite = new Sprite(centerX, centerY, this.mTextureRegion);
		this.badgeSprite.setScale(0f);
		this.badgeSprite.registerEntityModifier(new SequenceEntityModifier(new IEntityModifierListener()
		{
			@Override
			public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity)
			{
				SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainMenuActivity.class));
				SplashActivity.this.finish();
			}
		}, new ScaleModifier(0.3f, 0f, 0f), new ScaleModifier(0.1f, 0.75f, 1f)
		{
			boolean soundUsed;

			@Override
			protected void onManagedUpdate(float pSecondsElapsed, IEntity pItem)
			{
				if(!soundUsed && (soundUsed = badgeSprite.getScaleX() >= 0.9f))
					logoSound.play();
				super.onManagedUpdate(pSecondsElapsed, pItem);
			}
		}, new ScaleModifier(0.1f, 1f, 0.8f), new ScaleModifier(3.5f, 0.8f, 0.9f), new ScaleModifier(0.1f, 0.75f, 2f, 0.75f, 0.02f), new ScaleModifier(0.1f, 2f, 0.02f, 0.02f,
				0.02f), new ScaleModifier(0.1f, 0.02f, 0.02f), new ScaleModifier(0.1f, 0.2f, 0f), new ScaleModifier(0.2f, 0f, 0f)));
		this.getEngine().getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public void onLoadComplete()
	{}

	@Override
	public Scene onLoadScene()
	{
		Scene scene = new Scene(1);
//		scene.getTopLayer().addEntity(badgeSprite);
		scene.getLastChild().attachChild(badgeSprite);
		return scene;
	}
}