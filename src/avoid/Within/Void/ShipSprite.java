package avoid.Within.Void;

import javax.microedition.khronos.opengles.GL10;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.anddev.andengine.entity.particle.initializer.AlphaInitializer;
import org.anddev.andengine.entity.particle.initializer.ColorInitializer;
import org.anddev.andengine.entity.particle.initializer.RotationInitializer;
import org.anddev.andengine.entity.particle.initializer.VelocityInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class ShipSprite extends LiveAnimatedSprite
{
	private GlideAtPlaceAnimatedUpdateHandler glideAtPlaceUpdateListener;
	private boolean shield, canShoot;
	private TimerHandler bulletShooterTimerHandler;
	private ParticleSystem spaceDuelSmokeParticleSystem, shieldParticleSystem;
	private CircleOutlineParticleEmitter spaceDuelSmokeParticleEmitter, shieldCircleOutlineParticleEmitter;
	private Texture spaceDuelParticleTexture, shieldTexture;
	private TextureRegion spaceDuelParticleTextureRegion, shieldTextureRegion;
	// private SpiralBulletShooterTimerCallback
	// spiralBulletShooterTimerCallback;
//	private TwinBulletShooterTimerCallback twinBulletShooterTimerCallback;
	private BasicBulletShooterTimerCallback basicBulletShooterTimerCallback;
	private int extraLife;

	public ShipSprite(final GameActivity gameActivity, TiledTextureRegion pTiledTextureRegion, float initX, float initY)
	{
		super(gameActivity, LiveEntityIdentity.UserShip, initX, initY, 100, 100,pTiledTextureRegion);
		// reset();
		// spiralBulletShooterTimerCallback = new
		// SpiralBulletShooterTimerCallback(this,true,true);
		// bulletShooterTimerHandler = new
		// TimerHandler(spiralBulletShooterTimerCallback.bulletInterval, true,
		// spiralBulletShooterTimerCallback);
		
//		boolean shotCurveIn = false, shakeAngleRotation = false, shotBothCanons = true, shotUp = true, shotDown = true, shotLeft = true, shotRight = true;
//		Object object = Settings.getSetting(Parameter.BOOL__CURVE_IN);
//		if(object != null)
//			shotCurveIn = (Boolean)object;
//		object = Settings.getSetting(Parameter.BOOL__SHAKE_ANGLE);
//		if(object != null)
//			shakeAngleRotation = (Boolean)object;
//		object = Settings.getSetting(Parameter.BOOL__BOTH_CANONS);
//		if(object != null)
//			shotBothCanons = (Boolean)object;
//		object = Settings.getSetting(Parameter.BOOL__SHOT_UP);
//		if(object != null)
//			shotUp = (Boolean)object;
//		object = Settings.getSetting(Parameter.BOOL__SHOT_DOWN);
//		if(object != null)
//			shotDown = (Boolean)object;
//		object = Settings.getSetting(Parameter.BOOL__SHOT_LEFT);
//		if(object != null)
//			shotLeft = (Boolean)object;
//		object = Settings.getSetting(Parameter.BOOL__SHOT_RIGHT);
//		if(object != null)
//			shotRight = (Boolean)object;
		this.extraLife = 3;
		shieldTexture = new Texture(128, 128, TextureOptions.BILINEAR);
		spaceDuelParticleTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		spaceDuelParticleTextureRegion = TextureRegionFactory.createFromAsset(spaceDuelParticleTexture, getGameActivity(), "gfx/particle_point.png", 0, 0);
		shieldTextureRegion = TextureRegionFactory.createFromAsset(shieldTexture, getGameActivity(), "gfx/ship_space_duel_normal.png", 0, 0);
		getGameActivity().getEngine().getTextureManager().loadTextures(shieldTexture, spaceDuelParticleTexture);
//		twinBulletShooterTimerCallback = new TwinBulletShooterTimerCallback(this, shotCurveIn, shakeAngleRotation, shotBothCanons, shotUp, shotDown, shotLeft, shotRight);
		basicBulletShooterTimerCallback = new BasicBulletShooterTimerCallback(this,-35, -40, 85);
		basicBulletShooterTimerCallback.addShooter(35, -40, 95);
		basicBulletShooterTimerCallback.addShooter(0, 40, 270);
		bulletShooterTimerHandler = new TimerHandler(basicBulletShooterTimerCallback.bulletInterval, true, basicBulletShooterTimerCallback);
		registerUpdateHandler(getGlideAtPlaceUpdateListener());
	}

	public CircleOutlineParticleEmitter getSpaceDuelSmokeParticleEmitter(float centerX, float centerY)
	{
		if(spaceDuelSmokeParticleEmitter == null)
			spaceDuelSmokeParticleEmitter = new CircleOutlineParticleEmitter(centerX, centerY, 10);
		return spaceDuelSmokeParticleEmitter;
	}

	public CircleOutlineParticleEmitter getShieldCircleOutlineParticleEmitter(float centerX, float centerY)
	{
		if(shieldCircleOutlineParticleEmitter == null)
			shieldCircleOutlineParticleEmitter = new CircleOutlineParticleEmitter(centerX, centerY, 10);
		return shieldCircleOutlineParticleEmitter;
	}

	public ParticleSystem getSpaceDuelSmokeParticleSystem()
	{
		if(spaceDuelSmokeParticleSystem == null)
		{
			spaceDuelSmokeParticleSystem = new ParticleSystem(getSpaceDuelSmokeParticleEmitter(getX(), getY()), 40, 60, 360, spaceDuelParticleTextureRegion);
			spaceDuelSmokeParticleSystem.addParticleInitializer(new ColorInitializer(0.5f, 0.5f, 0.5f));
			spaceDuelSmokeParticleSystem.addParticleInitializer(new AlphaInitializer(0.2f));
			spaceDuelSmokeParticleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			spaceDuelSmokeParticleSystem.addParticleInitializer(new VelocityInitializer(-15, 15, 80, 120));
			spaceDuelSmokeParticleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
			spaceDuelSmokeParticleSystem.addParticleModifier(new ScaleModifier(0.5f, 2.5f, 0, 0.75f));
			spaceDuelSmokeParticleSystem.addParticleModifier(new ColorModifier(0.5f, 1, 0.5f, 1, 0.5f, 1, 0, 0.75f));
			spaceDuelSmokeParticleSystem.addParticleModifier(new AlphaModifier(0.2f, 0, 0.5f, 0.75f));
			spaceDuelSmokeParticleSystem.addParticleModifier(new ExpireModifier(0.5f, 0.75f));
		}
		return spaceDuelSmokeParticleSystem;
	}

	public ParticleSystem getShieldParticleSystem()
	{
		if(shieldParticleSystem == null)
		{
			shieldParticleSystem = new ParticleSystem(getShieldCircleOutlineParticleEmitter(getX(), getY()), 40, 60, 360, shieldTextureRegion);
			shieldParticleSystem.addParticleInitializer(new ColorInitializer(1f, 1f, 1f));
			shieldParticleSystem.addParticleInitializer(new AlphaInitializer(0f));
			shieldParticleSystem.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_COLOR);
			shieldParticleSystem.addParticleInitializer(new VelocityInitializer(0, 0, 0, 0));
			shieldParticleSystem.addParticleModifier(new AlphaModifier(0f, 0.5f, 0f, 0.1f));
			shieldParticleSystem.addParticleModifier(new AlphaModifier(0.5f, 0f, 0.1f, 0.2f));
			shieldParticleSystem.addParticleModifier(new ScaleModifier(1f, 1.5f, 0f, 0.2f));
			shieldParticleSystem.addParticleModifier(new ExpireModifier(0.2f));
			shieldParticleSystem.setParticlesSpawnEnabled(false);
		}
		return shieldParticleSystem;
	}

	private GlideAtPlaceAnimatedUpdateHandler getGlideAtPlaceUpdateListener()
	{
		if(glideAtPlaceUpdateListener == null)
			glideAtPlaceUpdateListener = new GlideAtPlaceAnimatedUpdateHandler(this, 10 + Constants.RANDOM.nextInt(20), 10 + Constants.RANDOM.nextInt(20));		
		return glideAtPlaceUpdateListener;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed)
	{
		getSpaceDuelSmokeParticleEmitter(0, 0).setCenter(getCenterX() - 15, getCenterY() - 20);
		if(getShield())
			getShieldCircleOutlineParticleEmitter(0, 0).setCenter(getX(), getY());
		super.onManagedUpdate(pSecondsElapsed);
	}

	public void setShield(boolean shield)
	{
		getShieldParticleSystem().setParticlesSpawnEnabled(this.shield = shield);
		setExtraLife(getExtraLife()+1);
		if(getExtraLife()>11)
			setExtraLife(0);
	}

	public boolean getShield()
	{
		return shield;
	}

	public void setCanShoot(boolean canShoot)
	{
		this.canShoot = canShoot;
	}

	public boolean getCanShoot()
	{
		return canShoot;
	}

	public void resetBulletShooterTimerHandler()
	{
		// spiralBulletShooterTimerCallback.setBulletAngle(90);
		bulletShooterTimerHandler.reset();
	}

	public TimerHandler getBulletShooterTimerHandler()
	{
		return bulletShooterTimerHandler;
	}

	public int getExtraLife()
	{
		return extraLife;
	}

	public void setExtraLife(int extraLife)
	{
		this.extraLife = extraLife;
	}
}