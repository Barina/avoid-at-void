package avoid.Within.Void;

import java.util.Random;
import javax.microedition.khronos.opengles.GL10;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.anddev.andengine.entity.particle.initializer.AlphaInitializer;
import org.anddev.andengine.entity.particle.initializer.ColorInitializer;
import org.anddev.andengine.entity.particle.initializer.RotationInitializer;
import org.anddev.andengine.entity.particle.initializer.VelocityInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.font.StrokeFont;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import android.graphics.Color;

public class HelpScene extends Scene
{
	public static int LAYER_BOTTOM = 0, LAYER_PARTICLE_BOTTOM = 1, LAYER_ENTITIES = 2, LAYER_PARTICLE_TOP = 3, LAYER_INFO = 4;
	private MainMenuActivity mainMenuActivity;
	private EnemySprite enemySprite;
	private Texture fingerParticleTexture, littleWindowTexture;
	private TextureRegion fingerParticleTextureRegion, littleWindowTextureRegion;
	private Sprite fingerSprite, selectedSprite, littleWindowSprite;
	private CircleOutlineParticleEmitter fingerParticleEmitter;
	private boolean spriteSelected;
	private float lastClicked = 0;
	private boolean clickAllowed = true;
	private ParticleSystem particleSystem;
	private Texture fontTexture;
	private StrokeFont strokeFont;
	private ChangeableText infoText;
	private org.anddev.andengine.entity.modifier.AlphaModifier alphaIn, alphaOut;
	private String enemyInfoString;

	public HelpScene(MainMenuActivity mainMenuActivity)
	{
		super(1 + LAYER_INFO);
		this.mainMenuActivity = mainMenuActivity;
		this.fingerParticleTexture = new Texture(32, 32, TextureOptions.BILINEAR);
		this.fingerParticleTextureRegion = TextureRegionFactory.createFromAsset(fingerParticleTexture, mainMenuActivity, "gfx/particle_star.png", 0, 0);
		this.littleWindowTexture = new Texture(512, 512, TextureOptions.BILINEAR);
		this.littleWindowTextureRegion = TextureRegionFactory.createFromAsset(littleWindowTexture, mainMenuActivity, "gfx/little_window.png", 0, 0);
		this.fontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
		this.strokeFont = FontFactory.createStrokeFromAsset(fontTexture, mainMenuActivity, "font/BAUHS93.TTF", 32, true, Color.WHITE, 2, Color.BLACK);
		mainMenuActivity.getEngine().getTextureManager().loadTextures(fingerParticleTexture, littleWindowTexture, fontTexture);
		mainMenuActivity.getEngine().getFontManager().loadFont(this.strokeFont);
		spriteSelected = false;
		setBackgroundEnabled(false);
	}

	public HelpScene loadScene()
	{
		fingerSprite = new Sprite(-100, -100, fingerParticleTextureRegion);
		littleWindowSprite = new Sprite((Constants.CAMERA_WIDTH - littleWindowTextureRegion.getWidth()) * 0.5f, Constants.CAMERA_HEIGHT - littleWindowTextureRegion.getHeight()
				- 20, littleWindowTextureRegion);
		littleWindowSprite.setAlpha(0);
		getChild(LAYER_INFO).attachChild(littleWindowSprite);
		this.infoText = new ChangeableText(littleWindowSprite.getX() + 20, littleWindowSprite.getY() + 15, strokeFont, "",175);
		infoText.setAlpha(0);
		getChild(LAYER_INFO).attachChild(infoText);
		getParticleSystem().setParticlesSpawnEnabled(false);
		initStrings();
		getEnemySprite();
		return this;
	}

	private void initStrings()
	{
		enemyInfoString = "The most basic, easy\nto kill enemy.\nyou wont have to avoid it\nso much...\na lot of them wil score you\na lot of points!";
	}

	public org.anddev.andengine.entity.modifier.AlphaModifier getAlphaIn()
	{
		if(alphaIn == null)
		{
			alphaIn = new org.anddev.andengine.entity.modifier.AlphaModifier(0.5f, 0, 1);
			alphaIn.setRemoveWhenFinished(true);
		}
		return alphaIn.clone();
	}

	public org.anddev.andengine.entity.modifier.AlphaModifier getAlphaOut()
	{
		if(alphaOut == null)
		{
			alphaOut = new org.anddev.andengine.entity.modifier.AlphaModifier(0.5f, 1, 0);
			alphaOut.setRemoveWhenFinished(true);
		}
		return alphaOut.clone();
	}

	@Override
	public void reset()
	{
		super.reset();
		getEnemySprite().setPosition(100, 100);
		getEnemySprite().setScale(1);
		getParticleSystem().setParticlesSpawnEnabled(false);
		littleWindowSprite.setAlpha(0);
		infoText.setAlpha(0);
		spriteSelected = false;
	}

	public CircleOutlineParticleEmitter getFingerParticleEmitter()
	{
		if(fingerParticleEmitter == null)
			fingerParticleEmitter = new CircleOutlineParticleEmitter(-100, -100, 5);
		return fingerParticleEmitter;
	}

	public ParticleSystem getParticleSystem()
	{
		if(particleSystem == null)
		{
			particleSystem = new ParticleSystem(getFingerParticleEmitter(), 10, 60, 500, fingerParticleTextureRegion);
			particleSystem.addParticleInitializer(new ColorInitializer(0.5f, 0.5f, 1));
			particleSystem.addParticleInitializer(new AlphaInitializer(1));
			particleSystem.setBlendFunction(GL10.GL_LIGHT0, GL10.GL_ONE);
			particleSystem.addParticleInitializer(new VelocityInitializer(-100, 100, -150, 150));
			particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
			particleSystem.addParticleModifier(new org.anddev.andengine.entity.particle.modifier.RotationModifier(0, 100, 0, 1));
			particleSystem.addParticleModifier(new org.anddev.andengine.entity.particle.modifier.ScaleModifier(3, 0.1f, 0, 1f));
			particleSystem.addParticleModifier(new ColorModifier(0.5f, 0.9f, 0.5f, 0.9f, 1f, 1f, 0, 0.5f));
			particleSystem.addParticleModifier(new ColorModifier(0.9f, 1, 0.9f, 1, 1f, 1, 0.5f, 1f));
			particleSystem.addParticleModifier(new AlphaModifier(1, 0, 0, 1f));
			particleSystem.addParticleModifier(new ExpireModifier(1f));
			getChild(LAYER_PARTICLE_BOTTOM).attachChild(particleSystem);
		}
		return particleSystem;
	}

	private EnemySprite getEnemySprite()
	{
		if(enemySprite == null)
		{
			// TODO maybe exception here...
			enemySprite = new EnemySprite(mainMenuActivity, null, 100, 100, 100, 100, EnemyFactory.getEnemy1TextureRegionClone());
			Random rnd = new Random();
			enemySprite.registerUpdateHandler(new GlideAtPlaceUpdateHandler(enemySprite, 10 + rnd.nextInt(20), 10 + rnd.nextInt(20)));
			getChild(LAYER_ENTITIES).attachChild(enemySprite);
		}
		return enemySprite;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed)
	{
		super.onManagedUpdate(pSecondsElapsed);
		if(spriteSelected)
			getFingerParticleEmitter().setCenter(selectedSprite.getX(), selectedSprite.getY());
		clickAllowed = (lastClicked += pSecondsElapsed) >= 0.5f;
	}

	private SequenceEntityModifier getSelectionMadeModifier(Sprite selectedSprite)
	{
		final SequenceEntityModifier modifier = new SequenceEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.1f, 1f, 5f), new ParallelEntityModifier(new ScaleModifier(
				0.5f, 5f, 3f), new MoveModifier(0.5f, selectedSprite.getX(), 256, selectedSprite.getY(), 150))));
		modifier.setRemoveWhenFinished(true);
		return modifier;
	}

	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent)
	{
		if(!clickAllowed)
			return super.onSceneTouchEvent(pSceneTouchEvent);
		lastClicked = 0;
		fingerSprite.setPosition(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
		if(fingerSprite.collidesWith(getEnemySprite()))
		{
			getEnemySprite().registerEntityModifier(getSelectionMadeModifier(getEnemySprite()));
			getParticleSystem().setPosition(getEnemySprite());
			getParticleSystem().setParticlesSpawnEnabled(true);
			infoText.setText(enemyInfoString);
			if(!spriteSelected)
			{
				littleWindowSprite.registerEntityModifier(getAlphaIn());
				infoText.registerEntityModifier(getAlphaIn());
			}
			spriteSelected = true;
			selectedSprite = getEnemySprite();
			return true;
		}
		if(spriteSelected)
		{
			littleWindowSprite.registerEntityModifier(getAlphaOut());
			infoText.registerEntityModifier(getAlphaOut());
			getParticleSystem().setParticlesSpawnEnabled(false);
			spriteSelected = false;
			if(selectedSprite != null)
			{
				float x = 0, y = 0;
				if(selectedSprite instanceof EnemySprite)
				{
					EnemySprite sprite = ((EnemySprite)selectedSprite);
					x = sprite.getInitialX();
					y = sprite.getInitialY();
				}
				selectedSprite.registerEntityModifier(new ParallelEntityModifier(new ScaleModifier(0.1f, 5f, 1f), new MoveModifier(0.1f, selectedSprite.getX(), x, selectedSprite.getY(),
				y)));
				selectedSprite = null;
			}
			return true;
		}
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}
}