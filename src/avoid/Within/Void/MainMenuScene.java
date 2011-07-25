package avoid.Within.Void;

import java.io.IOException;
import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.font.StrokeFont;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.HorizontalAlign;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class MainMenuScene extends Scene
{
	public static final short LAYER_BOTTOM = 0, LAYER_MANU = 1, LAYER_TOP = 2;
	private MainMenuActivity mainMenuActivity;
	private Sprite menuBackground;
	private AnimatedSprite continueMenuItemSprite, newGameMenuItemSprite, settingsMenuItemSprite, helpMenuItemSprite, quitMenuItemSprite;
	protected StrokeFont strokeFont;
	private Text continueText, gameText, settingsText, helpText, quitText;
	protected Texture bgTexture, menuItemTexture, fontTexture;
	private TextureRegion bgTextureRegion;
	private TiledTextureRegion menuItemTiledTextureRegion;
	protected Sound itemSelectSound, itemElectricSelectSound;
	private Music bgStartMusic, bgLoopMusic;
	private AlphaModifier toSemiTransperentAlphaModifier;
	private boolean itemClicked;

	public MainMenuScene(MainMenuActivity mainMenuActivity)
	{
		super(LAYER_TOP + 1);
		this.mainMenuActivity = mainMenuActivity;
		toSemiTransperentAlphaModifier = new AlphaModifier(0.1f, 1f, 0.25f);
		try
		{
			this.itemSelectSound = SoundFactory.createSoundFromAsset(getMainMenuActivity().getEngine().getSoundManager(), mainMenuActivity, "mfx/menu_selection.ogg");
			this.bgStartMusic = MusicFactory.createMusicFromAsset(getMainMenuActivity().getEngine().getMusicManager(), mainMenuActivity, "mfx/menu_bg_start.ogg");
			this.bgStartMusic.setOnCompletionListener(new OnCompletionListener()
			{
				@Override
				public void onCompletion(MediaPlayer mp)
				{
					bgLoopMusic.play();
				}
			});
			this.bgLoopMusic = MusicFactory.createMusicFromAsset(getMainMenuActivity().getMusicManager(), mainMenuActivity, "mfx/menu_bg_loop.ogg");
			this.bgLoopMusic.setLooping(true);
		}
		catch(IOException e)
		{
			Debug.e("Error - " + e.getMessage(), e);
		}
		try
		{
			this.itemElectricSelectSound = SoundFactory.createSoundFromAsset(getMainMenuActivity().getEngine().getSoundManager(), mainMenuActivity,
					"mfx/menu_selection_electric.ogg");
		}
		catch(Exception e)
		{
			Debug.e("Error - " + e.getMessage(), e);
		}
		this.fontTexture = new Texture(512, 256, TextureOptions.BILINEAR);
		this.strokeFont = FontFactory.createStrokeFromAsset(fontTexture, getMainMenuActivity(), "font/BAUHS93.TTF", 64, true, Color.WHITE, 2, Color.BLACK);
		this.bgTexture = new Texture(512, 1024, TextureOptions.BILINEAR);
		this.menuItemTexture = new Texture(512, 256, TextureOptions.BILINEAR);
		this.bgTextureRegion = TextureRegionFactory.createFromAsset(this.bgTexture, mainMenuActivity, "gfx/menu_background.png", 0, 0);
		this.menuItemTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(this.menuItemTexture, mainMenuActivity, "gfx/menu_item.png", 0, 0, 1, 2);
		getMainMenuActivity().getEngine().getTextureManager().loadTextures(this.fontTexture, this.bgTexture, this.menuItemTexture);
		getMainMenuActivity().getEngine().getFontManager().loadFont(this.strokeFont);
		setTouchAreaBindingEnabled(true);
	}

	public MainMenuScene loadScene()
	{
		final int centerX = (int)((Constants.CAMERA_WIDTH - menuItemTiledTextureRegion.getWidth()) * 0.5f);
		this.menuBackground = new Sprite((Constants.CAMERA_WIDTH - bgTextureRegion.getWidth()) * 0.5f, (Constants.CAMERA_HEIGHT - bgTextureRegion.getHeight()) * 0.5f,
				bgTextureRegion);
		this.continueMenuItemSprite = new AnimatedSprite(centerX, 100, menuItemTiledTextureRegion)
		{
			@Override
			public boolean onAreaTouched(org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
				{
					if(Constants.isGameRunning())
					{
						if(itemClicked)
							return true;
						itemClicked = true;
						itemSelectSound.play();
						stopBackgroundMusic();
						newGameMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
						settingsMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
						helpMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
						quitMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
						gameText.setAlpha(0.25f);
						settingsText.setAlpha(0.25f);
						helpText.setAlpha(0.25f);
						quitText.setAlpha(0.25f);
						animate(60, 6, new IAnimationListener()
						{
							@Override
							public void onAnimationEnd(AnimatedSprite pAnimatedSprite)
							{
								setMenuVisability(false);
							}
						});
					}
					return true;
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.newGameMenuItemSprite = new AnimatedSprite(centerX, continueMenuItemSprite.getY() + 100, menuItemTiledTextureRegion.clone())
		{
			@Override
			public boolean onAreaTouched(org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
				{
					if(itemClicked)
						return true;
					itemClicked = true;
					itemSelectSound.play();
					stopBackgroundMusic();
					continueMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					settingsMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					helpMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					quitMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					continueText.setAlpha(0.25f);
					settingsText.setAlpha(0.25f);
					helpText.setAlpha(0.25f);
					quitText.setAlpha(0.25f);
					animate(60, 6, new IAnimationListener()
					{
						@Override
						public void onAnimationEnd(AnimatedSprite pAnimatedSprite)
						{
							// setMenuVisability(false);
							getMainMenuActivity().startActivityForResult(new Intent(getMainMenuActivity(), GameActivity.class).putExtra("StartAtLevel", 0), 1);
							normalize();
						}
					});
					return true;
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.settingsMenuItemSprite = new AnimatedSprite(centerX, newGameMenuItemSprite.getY() + 100, menuItemTiledTextureRegion.clone())
		{
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
				{
					if(itemClicked)
						return true;
					itemClicked = true;
					itemSelectSound.play();
					continueMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					newGameMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					helpMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					quitMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					continueText.setAlpha(0.25f);
					gameText.setAlpha(0.25f);
					helpText.setAlpha(0.25f);
					quitText.setAlpha(0.25f);
					animate(60, 6, new IAnimationListener()
					{
						@Override
						public void onAnimationEnd(AnimatedSprite pAnimatedSprite)
						{
							setMenuVisability(false);
							setChildScene(getMainMenuActivity().getSettingsScene());
						}
					});
					itemClicked = true;
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.helpMenuItemSprite = new AnimatedSprite(centerX, settingsMenuItemSprite.getY() + 100, menuItemTiledTextureRegion.clone())
		{
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
				{
					if(itemClicked)
						return true;
					itemClicked = true;
					itemSelectSound.play();
					continueMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					newGameMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					settingsMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					quitMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					continueText.setAlpha(0.25f);
					gameText.setAlpha(0.25f);
					settingsText.setAlpha(0.25f);
					quitText.setAlpha(0.25f);
					animate(60, 6, new IAnimationListener()
					{
						@Override
						public void onAnimationEnd(AnimatedSprite pAnimatedSprite)
						{
							setMenuVisability(false);
							setChildScene(getMainMenuActivity().getHelpScene());
						}
					});
					return true;
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.quitMenuItemSprite = new AnimatedSprite(centerX, helpMenuItemSprite.getY() + 100, menuItemTiledTextureRegion.clone())
		{
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY)
			{
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
				{
					if(itemClicked)
						return true;
					itemClicked = true;
					itemSelectSound.play();
					stopBackgroundMusic();
					newGameMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					continueMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					settingsMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					helpMenuItemSprite.registerEntityModifier(toSemiTransperentAlphaModifier.clone());
					gameText.setAlpha(0.25f);
					continueText.setAlpha(0.25f);
					settingsText.setAlpha(0.25f);
					helpText.setAlpha(0.25f);
					animate(60, 6, new IAnimationListener()
					{
						@Override
						public void onAnimationEnd(AnimatedSprite pAnimatedSprite)
						{
							setMenuVisability(false);
							getMainMenuActivity().finish();
						}
					});
					return true;
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		this.continueText = new Text(0, 0, strokeFont, "Continue", HorizontalAlign.CENTER);
		this.gameText = new Text(0, 0, strokeFont, "New Game", HorizontalAlign.CENTER);
		this.settingsText = new Text(0, 0, strokeFont, "Settings", HorizontalAlign.CENTER);
		this.helpText = new Text(0, 0, strokeFont, "Help", HorizontalAlign.CENTER);
		this.quitText = new Text(0, 0, strokeFont, "Quit", HorizontalAlign.CENTER);
		setBackground(BackgroundFactory.getBgBric1AutoVerticalParallaxBackground(mainMenuActivity, 20));
		initTextPositions();
		getChild(LAYER_BOTTOM).attachChild(menuBackground);
		getChild(LAYER_MANU).attachChild(this.continueMenuItemSprite);
		getChild(LAYER_MANU).attachChild(this.newGameMenuItemSprite);
		getChild(LAYER_MANU).attachChild(this.settingsMenuItemSprite);
		getChild(LAYER_MANU).attachChild(this.helpMenuItemSprite);
		getChild(LAYER_MANU).attachChild(this.quitMenuItemSprite);
		getChild(LAYER_MANU).attachChild(this.continueText);
		getChild(LAYER_MANU).attachChild(this.gameText);
		getChild(LAYER_MANU).attachChild(this.settingsText);
		getChild(LAYER_MANU).attachChild(this.helpText);
		getChild(LAYER_MANU).attachChild(this.quitText);
		registerTouchArea(this.continueMenuItemSprite);
		registerTouchArea(this.newGameMenuItemSprite);
		registerTouchArea(this.settingsMenuItemSprite);
		registerTouchArea(this.helpMenuItemSprite);
		registerTouchArea(this.quitMenuItemSprite);
		if(!Constants.isGameRunning())
		{
			this.continueMenuItemSprite.setAlpha(0.25f);
			this.continueText.setAlpha(0.25f);
		}
		normalize();
		return this;
	}

	private void setMenuVisability(boolean visable)
	{
		menuBackground.setVisible(visable);
		continueMenuItemSprite.setVisible(visable);
		newGameMenuItemSprite.setVisible(visable);
		settingsMenuItemSprite.setVisible(visable);
		helpMenuItemSprite.setVisible(visable);
		quitMenuItemSprite.setVisible(visable);
		gameText.setVisible(visable);
		continueText.setVisible(visable);
		settingsText.setVisible(visable);
		helpText.setVisible(visable);
		quitText.setVisible(visable);
	}

	private void initTextPositions()
	{
		this.continueText.setPosition((Constants.CAMERA_WIDTH - continueText.getWidth()) * 0.5f, continueMenuItemSprite.getY() + 10);
		this.gameText.setPosition((Constants.CAMERA_WIDTH - gameText.getWidth()) * 0.5f, newGameMenuItemSprite.getY() + 10);
		this.settingsText.setPosition((Constants.CAMERA_WIDTH - settingsText.getWidth()) * 0.5f, settingsMenuItemSprite.getY() + 10);
		this.helpText.setPosition((Constants.CAMERA_WIDTH - helpText.getWidth()) * 0.5f, helpMenuItemSprite.getY() + 10);
		this.quitText.setPosition((Constants.CAMERA_WIDTH - quitText.getWidth()) * 0.5f, quitMenuItemSprite.getY() + 10);
	}

	public MainMenuActivity getMainMenuActivity()
	{
		return this.mainMenuActivity;
	}

	public void startBackgroundMusic()
	{
		if(this.bgStartMusic != null && this.bgLoopMusic != null)
			if(!(this.bgStartMusic.isPlaying() || this.bgLoopMusic.isPlaying()))
				this.bgStartMusic.play();
	}

	public void stopBackgroundMusic()
	{
		if(this.bgStartMusic != null && this.bgLoopMusic != null)
		{
			if(this.bgStartMusic.isPlaying())
				this.bgStartMusic.stop();
			if(this.bgLoopMusic.isPlaying())
				this.bgLoopMusic.stop();
		}
	}

	public void normalize()
	{
		itemClicked = false;
		startBackgroundMusic();
		newGameMenuItemSprite.setCurrentTileIndex(0);
		continueMenuItemSprite.setCurrentTileIndex(0);
		settingsMenuItemSprite.setCurrentTileIndex(0);
		helpMenuItemSprite.setCurrentTileIndex(0);
		quitMenuItemSprite.setCurrentTileIndex(0);
		newGameMenuItemSprite.setAlpha(1);
		settingsMenuItemSprite.setAlpha(1);
		helpMenuItemSprite.setAlpha(1);
		quitMenuItemSprite.setAlpha(1);
		gameText.setAlpha(1);
		settingsText.setAlpha(1);
		helpText.setAlpha(1);
		quitText.setAlpha(1);
		if(!Constants.isGameRunning())
		{
			continueMenuItemSprite.setAlpha(0.25f);
			continueText.setAlpha(0.25f);
		}
		else
		{
			continueMenuItemSprite.setAlpha(1);
			continueText.setAlpha(1);
		}
		setMenuVisability(true);
	}

	@Override
	public void reset()
	{
		super.reset();
		if(this.bgStartMusic != null && this.bgLoopMusic != null)
		{
			if(this.bgStartMusic.isPlaying())
				this.bgStartMusic.stop();
			if(this.bgLoopMusic.isPlaying())
				this.bgLoopMusic.stop();
		}
		normalize();
		initTextPositions();
	}
}