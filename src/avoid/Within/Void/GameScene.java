package avoid.Within.Void;

import java.util.Random;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.modifier.ease.EaseQuadOut;

public class GameScene extends Scene
{
	public static final short BACKGROUND_LAYER = 0, PARTICLE_BOTTOM_LAYER = 1, SHIPS_LAYER = 2, USER_SHIP_LAYER = 3, UPGRADE_LAYER = 4, PARTICLE_TOP_LAYER = 5, BLAST_LAYER = 6,
			HUD_LAYER = 7, MESSAGE_LAYER = 8;
	protected final GameActivity gameActivity;
	protected final Texture shipTexture, hudTexture;
	protected final TextureRegion hudTextureRegion;
	protected final TiledTextureRegion shipTiledTextureRegion;
	protected float colideInterval = 1, lastColide = 0.1f;
	protected final ShipSprite ship;
	protected final HUDSprite gameHud;
	private boolean gameRunning;

	public GameScene(final GameActivity gameActivity)
	{
		super(MESSAGE_LAYER + 1);
		this.gameActivity = gameActivity;
		this.hudTexture = new Texture(512, 64, TextureOptions.BILINEAR);
		this.hudTextureRegion = TextureRegionFactory.createFromAsset(this.hudTexture, gameActivity, "gfx/game_hud.png", 0, 0);
		this.shipTexture = new Texture(256, 128, TextureOptions.BILINEAR);
		this.shipTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(this.shipTexture, gameActivity, "gfx/ship_tile_space_duel_jet.png", 1, 1, 3, 1);
		gameActivity.getEngine().getTextureManager().loadTextures(this.hudTexture, this.shipTexture);
		setBackground(BackgroundFactory.getBgStars1AutoVerticalParallaxBackground(gameActivity, 120));
		final int WIDTH = Constants.CAMERA_WIDTH, HEIGHT = Constants.CAMERA_HEIGHT;
		ship = new ShipSprite(gameActivity, shipTiledTextureRegion, (WIDTH - shipTiledTextureRegion.getWidth()) * 0.5f, (HEIGHT - shipTiledTextureRegion.getHeight()) * 0.5f);
		gameHud = new HUDSprite(gameActivity, hudTextureRegion,this.ship);
		registerUpdateHandler(ship.getBulletShooterTimerHandler());
		registerTouchArea(ship);
		setTouchAreaBindingEnabled(true);
		registerUpdateHandler(new IUpdateHandler()
		{
			@Override
			public void reset()
			{}

			@Override
			public void onUpdate(float pSecondsElapsed)
			{
				if(!isGameRunning())
					return;
				colideInterval += pSecondsElapsed;
				if(colideInterval >= lastColide)
				{
					for(EnemySprite enemy : EnemyFactory.getEnemy1Sprites())
					{
						if(enemy.isAlive())
						{
							if(ship.collidesWith(enemy))
							{
								colideInterval = 0;
								BlastFactory.addExplosion(gameActivity, enemy.getCenterX(), enemy.getCenterY());
								enemy.setAlive(false);
								enemy.setVisible(false);
								gameHud.addToScore(5);
								return;
							}
							for(final BulletSprite bullet : BulletFactory.getBulletSprites())
							{
								if(bullet.isAlive())
								{
									if(enemy.collidesWith(bullet))
									{
										BlastFactory.addExplosion(gameActivity, enemy.getCenterX(), enemy.getCenterY());
										enemy.setAlive(false);
										enemy.setVisible(false);
										bullet.setAlive(false);
										bullet.setVisible(false);
										gameHud.addToScore(10);
									}
								}
								else
								{
									bullet.setAlive(false);
									bullet.setVisible(false);
								}
							}
							for(final BulletSprite bullet : BulletFactory.getLittleBulletSprites())
							{
								if(bullet.isAlive())
								{
									if(enemy.collidesWith(bullet))
									{
										BlastFactory.addExplosion(gameActivity, enemy.getCenterX(), enemy.getCenterY());
										enemy.setAlive(false);
										enemy.setVisible(false);
										bullet.setAlive(false);
										bullet.setVisible(false);
										gameHud.addToScore(1);
										BulletFactory.showSpark(bullet.getX(), bullet.getY(), bullet.getRotation());
									}
								}
								else
								{
									bullet.setAlive(false);
									bullet.setVisible(false);
								}
							}
						}
						else
						{
							enemy.setAlive(false);
							enemy.setVisible(false);
						}
					}
					for(EnemyAnimatedSprite enemy : EnemyFactory.getEnemySprites(3))
					{
						for(final BulletSprite bullet : BulletFactory.getLittleBulletSprites())
						{
							if(bullet.isAlive())
							{
								if(enemy.collidesWith(bullet))
								{
									bullet.setAlive(false);
									bullet.setVisible(false);
									gameHud.addToScore(1);
									BulletFactory.showSpark(bullet.getX(), bullet.getY(), bullet.getRotation());
								}
							}
							else
							{
								bullet.setAlive(false);
								bullet.setVisible(false);
							}
						}
					}
					EnemyFactory.clearAllDeadEnemies();
				}
			}
		});
		registerUpdateHandler(new TimerHandler(0.1f, true, new ITimerCallback()
		{
			Random rnd = new Random();
			int x, y, count = 10;

			@Override
			public void onTimePassed(TimerHandler pTimerHandler)
			{
				if(EnemyFactory.getLiveEnemiesCount() > 11)
					return;
				if(count++ >= 10)
				{
					count = 0;
					x = 10 + rnd.nextInt(Constants.CAMERA_WIDTH - 20);
					y = 10 + rnd.nextInt(Constants.CAMERA_HEIGHT - 30);
				}
				else
				{
					final EnemySprite enemy = EnemyFactory.addEnemy(x, y);
					enemy.registerUpdateHandler(new ChaseAfterUpdateHandler(enemy, ship, 2f));
				}

				if(EnemyFactory.getEnemySprites(3).size() <= 0)
				{
					EnemyAnimatedSprite sprite = EnemyFactory.addEnemy(3, (Constants.CAMERA_WIDTH - 30) * 0.5f, 250);
					sprite.registerUpdateHandler(new GlideAtPlaceAnimatedUpdateHandler(sprite, 10 + Constants.RANDOM.nextInt(20), 10 + Constants.RANDOM.nextInt(20)));
				}
				
//				EnemyAnimatedSprite enemyAnimatedSprite = EnemyFactory.addEnemy(2, 10+rnd.nextInt(Constants.CAMERA_WIDTH-20), 10+rnd.nextInt(Constants.CAMERA_HEIGHT-30));
//				enemyAnimatedSprite.registerUpdateHandler(new ChaseAfterAnimatedUpdateHandler(enemyAnimatedSprite, ship, 2f));
				
//				enemyAnimatedSprite = EnemyFactory.addEnemy(3, 10+rnd.nextInt(Constants.CAMERA_WIDTH-20), 10+rnd.nextInt(Constants.CAMERA_HEIGHT-30));
//				enemyAnimatedSprite.registerUpdateHandler(new ChaseAfterAnimatedUpdateHandler(enemyAnimatedSprite, ship, 2f));
					
//				enemyAnimatedSprite = EnemyFactory.addEnemy(4, 10+rnd.nextInt(Constants.CAMERA_WIDTH-20), 10+rnd.nextInt(Constants.CAMERA_HEIGHT-30));
//				enemyAnimatedSprite.registerUpdateHandler(new ChaseAfterAnimatedUpdateHandler(enemyAnimatedSprite, ship, 2f));
				
//				enemyAnimatedSprite = EnemyFactory.addEnemy(5, 10+rnd.nextInt(Constants.CAMERA_WIDTH-20), 10+rnd.nextInt(Constants.CAMERA_HEIGHT-30));
//				enemyAnimatedSprite.registerUpdateHandler(new ChaseAfterAnimatedUpdateHandler(enemyAnimatedSprite, ship, 2f));
				
			}
		}));
		setGameRunning(false);
	}

	public void onLoadScene()
	{
		getChild(PARTICLE_TOP_LAYER).attachChild(ship.getSpaceDuelSmokeParticleSystem());
		getChild(PARTICLE_TOP_LAYER).attachChild(ship.getShieldParticleSystem());
		getChild(HUD_LAYER).attachChild(gameHud);
		getChild(USER_SHIP_LAYER).attachChild(ship);
		gameHud.onLoadScene();
		setGameRunning(true);
	}

	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent)
	{
		if(isGameRunning() && ship.isAlive())
		{
			if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
			{
				ship.setCanShoot(true);
				GameSoundsFactory.play(GameSoundsFactory.SOUND_GUN_LOOP);
			}
			else
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
				{
					ship.setCanShoot(false);
					ship.resetBulletShooterTimerHandler();
					GameSoundsFactory.pause(GameSoundsFactory.SOUND_GUN_LOOP);
				}
			if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN || pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE)
			{
				float x = pSceneTouchEvent.getX() - ship.getWidth() * 0.5f;
				float y = pSceneTouchEvent.getY() - ship.getHeight() * 0.5f;
				if(y > Constants.CAMERA_HEIGHT - 100)
					y = Constants.CAMERA_HEIGHT - 100;
				ship.clearEntityModifiers();
				ship.registerEntityModifier(new MoveModifier(0.5f, ship.getX(), x, ship.getY(), y, EaseQuadOut.getInstance()));
			}
			return true;
		}
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}

	public boolean isGameRunning()
	{
		return gameRunning;
	}

	public void setGameRunning(boolean run)
	{
		if(this.gameRunning = run)
			GameSoundsFactory.resumeAll();
		else
			GameSoundsFactory.pauseAll();
		setIgnoreUpdate(!gameRunning);
	}

	@Override
	public synchronized void reset()
	{
		super.reset();
		BlastFactory.clearBlasts(gameActivity);
		EnemyFactory.clearAllEnemies();
		GameSoundsFactory.stopAll();
		ship.reset();
		gameHud.reset();
	}

	public Texture[] getTextures()
	{
		return new Texture[]
		{this.hudTexture, this.shipTexture};
	}
}