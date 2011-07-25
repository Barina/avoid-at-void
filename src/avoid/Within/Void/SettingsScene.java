package avoid.Within.Void;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.font.StrokeFont;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.HorizontalAlign;
import android.graphics.Color;
import avoid.Within.Void.Settings.Parameter;

public class SettingsScene extends Scene
{
	private final Texture radioButtonTexture, fontTexture;
	final StrokeFont font;
	private final TiledTextureRegion radioButtonTiledTextureRegion;
	private LiveAnimatedSprite isCurveInAnimatedSprite, isShakeAngleAnimatedSprite, isBothCanonsAnimatedSprite, isShotUpAnimatedSprite, isShotDownAnimatedSprite,
			isShotLeftAnimatedSprite, isShotRightAnimatedSprite;
	private boolean clickAllowed, isCurveIn, isShakeAngle, isBothCanons, isShotUp, isShotDown, isShotLeft, isShotRight;
	private float lastClicked = 0;
	private final Sound itemSelectSound;
	private Text shotModesText, curveInText, shakeAngleText, bothCanonsText, shotUpText, shotDownText, shotLeftText, shotRightText;
	private MainMenuActivity mainMenuActivity;

	public SettingsScene(MainMenuActivity mainMenuActivity)
	{
		super(1);
		this.mainMenuActivity = mainMenuActivity;
		this.itemSelectSound = mainMenuActivity.getMainMenuScene().itemElectricSelectSound;
		this.fontTexture = new Texture(512, 256, TextureOptions.BILINEAR);
		this.font = FontFactory.createStrokeFromAsset(fontTexture, mainMenuActivity, "font/BAUHS93.TTF", 48, true, Color.WHITE, 2, Color.GRAY);
		this.shotModesText = new Text(10, 10, font, "Ship shot modes", HorizontalAlign.LEFT);
		this.curveInText = new Text(10, 100, font, "Curve in", HorizontalAlign.LEFT);
		this.shakeAngleText = new Text(10, 190, font, "Shake angle", HorizontalAlign.LEFT);
		this.bothCanonsText = new Text(10, 280, font, "Shot both canons", HorizontalAlign.LEFT);
		this.shotUpText = new Text(10, 370, font, "Shot up", HorizontalAlign.LEFT);
		this.shotDownText = new Text(10, 460, font, "Shot down", HorizontalAlign.LEFT);
		this.shotLeftText = new Text(10, 550, font, "Shot left", HorizontalAlign.LEFT);
		this.shotRightText = new Text(10, 640, font, "Shot right", HorizontalAlign.LEFT);
		radioButtonTexture = new Texture(256, 256, TextureOptions.BILINEAR);
		radioButtonTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(radioButtonTexture, mainMenuActivity, "gfx/radio_button.png", 1, 1, 2, 2);
		mainMenuActivity.getEngine().getTextureManager().loadTextures(radioButtonTexture, fontTexture);
		mainMenuActivity.getEngine().getFontManager().loadFont(this.font);
		setBackgroundEnabled(false);
	}

	public void loadScene()
	{
		final float x = Constants.CAMERA_WIDTH - 10 - radioButtonTiledTextureRegion.getTileWidth();
		this.isCurveInAnimatedSprite = new LiveAnimatedSprite(mainMenuActivity, null, x, 100, 0, 0, radioButtonTiledTextureRegion.clone())
		{
			public boolean onAreaTouched(org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(clickAllowed)
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
					{
						lastClicked = 0;
						itemSelectSound.play();
						isCurveIn = !isCurveIn;
						saveSettings();
						return true;
					}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.isShakeAngleAnimatedSprite = new LiveAnimatedSprite(mainMenuActivity, null, x, 190, 0, 0, radioButtonTiledTextureRegion.clone())
		{
			public boolean onAreaTouched(org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(clickAllowed)
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
					{
						lastClicked = 0;
						itemSelectSound.play();
						isShakeAngle = !isShakeAngle;
						saveSettings();
						return true;
					}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.isBothCanonsAnimatedSprite = new LiveAnimatedSprite(mainMenuActivity, null, x, 280, 0, 0, radioButtonTiledTextureRegion.clone())
		{
			public boolean onAreaTouched(org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(clickAllowed)
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
					{
						lastClicked = 0;
						itemSelectSound.play();
						isBothCanons = !isBothCanons;
						saveSettings();
						return true;
					}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.isShotUpAnimatedSprite = new LiveAnimatedSprite(mainMenuActivity, null, x, 370, 0, 0, radioButtonTiledTextureRegion.clone())
		{
			public boolean onAreaTouched(org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(clickAllowed)
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
					{
						lastClicked = 0;
						itemSelectSound.play();
						isShotUp = !isShotUp;
						saveSettings();
						return true;
					}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.isShotDownAnimatedSprite = new LiveAnimatedSprite(mainMenuActivity, null, x, 460, 0, 0, radioButtonTiledTextureRegion.clone())
		{
			public boolean onAreaTouched(org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(clickAllowed)
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
					{
						lastClicked = 0;
						itemSelectSound.play();
						isShotDown = !isShotDown;
						saveSettings();
						return true;
					}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.isShotLeftAnimatedSprite = new LiveAnimatedSprite(mainMenuActivity, null, x, 550, 0, 0, radioButtonTiledTextureRegion.clone())
		{
			public boolean onAreaTouched(org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(clickAllowed)
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
					{
						lastClicked = 0;
						itemSelectSound.play();
						isShotLeft = !isShotLeft;
						saveSettings();
						return true;
					}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.isShotRightAnimatedSprite = new LiveAnimatedSprite(mainMenuActivity, null, x, 640, 0, 0, radioButtonTiledTextureRegion.clone())
		{
			public boolean onAreaTouched(org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(clickAllowed)
					if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
					{
						lastClicked = 0;
						itemSelectSound.play();
						isShotRight = !isShotRight;
						saveSettings();
						return true;
					}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		getLastChild().attachChild(this.shotModesText);
		getLastChild().attachChild(this.curveInText);
		getLastChild().attachChild(this.shakeAngleText);
		getLastChild().attachChild(this.bothCanonsText);
		getLastChild().attachChild(this.shotUpText);
		getLastChild().attachChild(this.shotDownText);
		getLastChild().attachChild(this.shotLeftText);
		getLastChild().attachChild(this.shotRightText);
		getLastChild().attachChild(this.isCurveInAnimatedSprite);
		getLastChild().attachChild(this.isShakeAngleAnimatedSprite);
		getLastChild().attachChild(this.isBothCanonsAnimatedSprite);
		getLastChild().attachChild(this.isShotUpAnimatedSprite);
		getLastChild().attachChild(this.isShotDownAnimatedSprite);
		getLastChild().attachChild(this.isShotLeftAnimatedSprite);
		getLastChild().attachChild(this.isShotRightAnimatedSprite);
		registerTouchArea(this.isCurveInAnimatedSprite);
		registerTouchArea(this.isShakeAngleAnimatedSprite);
		registerTouchArea(this.isBothCanonsAnimatedSprite);
		registerTouchArea(this.isShotUpAnimatedSprite);
		registerTouchArea(this.isShotDownAnimatedSprite);
		registerTouchArea(this.isShotLeftAnimatedSprite);
		registerTouchArea(this.isShotRightAnimatedSprite);
		checkSettings();
	}

	private void checkSettings()
	{
		Object object = Settings.getSetting(Parameter.BOOL__CURVE_IN);
		if(object != null)
			isCurveIn = (Boolean)object;
		object = Settings.getSetting(Parameter.BOOL__SHAKE_ANGLE);
		if(object != null)
			isShakeAngle = (Boolean)object;
		object = Settings.getSetting(Parameter.BOOL__BOTH_CANONS);
		if(object != null)
			isBothCanons = (Boolean)object;
		object = Settings.getSetting(Parameter.BOOL__SHOT_UP);
		if(object != null)
			isShotUp = (Boolean)object;
		object = Settings.getSetting(Parameter.BOOL__SHOT_DOWN);
		if(object != null)
			isShotDown = (Boolean)object;
		object = Settings.getSetting(Parameter.BOOL__SHOT_LEFT);
		if(object != null)
			isShotLeft = (Boolean)object;
		object = Settings.getSetting(Parameter.BOOL__SHOT_RIGHT);
		if(object != null)
			isShotRight = (Boolean)object;
		updateUi();
	}

	private void saveSettings()
	{
		Settings.setSetting(Parameter.BOOL__CURVE_IN, isCurveIn);
		Settings.setSetting(Parameter.BOOL__SHAKE_ANGLE, isShakeAngle);
		Settings.setSetting(Parameter.BOOL__BOTH_CANONS, isBothCanons);
		Settings.setSetting(Parameter.BOOL__SHOT_UP, isShotUp);
		Settings.setSetting(Parameter.BOOL__SHOT_DOWN, isShotDown);
		Settings.setSetting(Parameter.BOOL__SHOT_LEFT, isShotLeft);
		Settings.setSetting(Parameter.BOOL__SHOT_RIGHT, isShotRight);
		updateUi();
	}

	private void updateUi()
	{
		isCurveInAnimatedSprite.setCurrentTileIndex(isCurveIn ? 2 : 0);
		isShakeAngleAnimatedSprite.setCurrentTileIndex(isShakeAngle ? 2 : 0);
		isBothCanonsAnimatedSprite.setCurrentTileIndex(isBothCanons ? 2 : 0);
		isShotUpAnimatedSprite.setCurrentTileIndex(isShotUp ? 2 : 0);
		isShotDownAnimatedSprite.setCurrentTileIndex(isShotDown ? 2 : 0);
		isShotLeftAnimatedSprite.setCurrentTileIndex(isShotLeft ? 2 : 0);
		isShotRightAnimatedSprite.setCurrentTileIndex(isShotRight ? 2 : 0);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed)
	{
		clickAllowed = (lastClicked += pSecondsElapsed) >= 0.5f;
		super.onManagedUpdate(pSecondsElapsed);
	}
}