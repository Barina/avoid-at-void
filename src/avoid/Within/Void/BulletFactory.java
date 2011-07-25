package avoid.Within.Void;

import java.util.ArrayList;
import java.util.Random;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.pool.GenericPool;
import avoid.Within.Void.ILive.LiveEntityIdentity;

public class BulletFactory
{
	public static class BulletPool extends GenericPool<BulletSprite>
	{
		@Override
		public synchronized BulletSprite obtainPoolItem()
		{
			final BulletSprite bullet = super.obtainPoolItem();
			bullet.reset();
			bullet.clearEntityModifiers();
			bullet.setAlive(true);
			return bullet;
		}

		@Override
		public synchronized void recyclePoolItem(BulletSprite bullet)
		{
			bullet.setAlive(false);
			super.recyclePoolItem(bullet);
		}

		@Override
		protected BulletSprite onAllocatePoolItem()
		{
			BulletSprite bullet = new BulletSprite(gameActivity,LiveEntityIdentity.Bullet, 0, 0, 400, bulletTextureRegion.clone());
			gameActivity.getGameScene().getChild(GameScene.PARTICLE_TOP_LAYER).attachChild(bullet);
			return bullet;
		}
	}

	public static class LittleBulletPool extends GenericPool<BulletSprite>
	{
		@Override
		public synchronized BulletSprite obtainPoolItem()
		{
			final BulletSprite bullet = super.obtainPoolItem();
			bullet.reset();
			bullet.clearEntityModifiers();
			bullet.setAlive(true);
			return bullet;
		}

		@Override
		public synchronized void recyclePoolItem(BulletSprite bullet)
		{
			bullet.setAlive(false);
			super.recyclePoolItem(bullet);
		}

		@Override
		protected BulletSprite onAllocatePoolItem()
		{
			BulletSprite bullet = new BulletSprite(gameActivity,LiveEntityIdentity.LittleBullet, 0, 0, 600, littleBulletTextureRegion.clone());
			gameActivity.getGameScene().getChild(GameScene.PARTICLE_TOP_LAYER).attachChild(bullet);
			return bullet;
		}
	}

	public static class SparkPool extends GenericPool<AnimatedSprite>
	{
		private Random rnd = new Random();

		@Override
		public synchronized AnimatedSprite obtainPoolItem()
		{
			AnimatedSprite sprite = super.obtainPoolItem();
			sprite.setCurrentTileIndex(rnd.nextInt(3));
			return sprite;
		}

		@Override
		public synchronized void recyclePoolItem(AnimatedSprite sprite)
		{
			sprite.setVisible(false);
			super.recyclePoolItem(sprite);
		}

		@Override
		protected AnimatedSprite onAllocatePoolItem()
		{
			AnimatedSprite sprite = new AnimatedSprite(0, 0, littleBulletSparkTiledTextureRegion.clone());
			gameActivity.getGameScene().getChild(GameScene.BLAST_LAYER).attachChild(sprite);
			return sprite;
		}
	}

	public static class GunFlashPool extends GenericPool<Sprite>
	{
		@Override
		public synchronized void recyclePoolItem(Sprite sprite)
		{
			sprite.setVisible(false);
			sprite.clearUpdateHandlers();
			super.recyclePoolItem(sprite);
		}

		@Override
		protected Sprite onAllocatePoolItem()
		{
			Sprite sprite = new Sprite(0, 0, gunFlashTextureRegion.clone());
			gameActivity.getGameScene().getChild(GameScene.BLAST_LAYER).attachChild(sprite);
			return sprite;
		}
	}

	protected static GameActivity gameActivity;
	private static Texture bulletTexture, littleBulletSparkTexture, gunFlashTexture;
	private static TextureRegion bulletTextureRegion, littleBulletTextureRegion, gunFlashTextureRegion;
	private static TiledTextureRegion littleBulletSparkTiledTextureRegion;
	private static BulletPool bulletPool;
	private static LittleBulletPool littleBulletPool;
	private static SparkPool littleBulletSparkPool;
	private static GunFlashPool gunFlashPool;
	private static ArrayList<BulletSprite> bullets, littleBullets;

	public static synchronized void loadResources(GameActivity gameActivity)
	{
		BulletFactory.gameActivity = gameActivity;
		bulletTexture = new Texture(64, 32, TextureOptions.BILINEAR);
		littleBulletSparkTexture = new Texture(128, 64, TextureOptions.BILINEAR);
		gunFlashTexture = new  Texture(64, 32, TextureOptions.BILINEAR);
		gunFlashTextureRegion = TextureRegionFactory.createFromAsset(gunFlashTexture, gameActivity, "gfx/gun_flash.png", 0, 0);
		bulletTextureRegion = TextureRegionFactory.createFromAsset(bulletTexture, gameActivity, "gfx/bullet.png", 0, 0);
		// littleBulletTexture = new Texture(64, 16, TextureOptions.BILINEAR);
		littleBulletTextureRegion = TextureRegionFactory.createFromAsset(bulletTexture, gameActivity, "gfx/little_bullet.png", 0, 16);
		littleBulletSparkTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(littleBulletSparkTexture, gameActivity, "gfx/little_bullet_sparks.png", 0, 0, 3, 1);
		gameActivity.getEngine().getTextureManager().loadTextures(bulletTexture, littleBulletSparkTexture, gunFlashTexture);
		bulletPool = new BulletPool();
		littleBulletPool = new LittleBulletPool();
		littleBulletSparkPool = new SparkPool();
		gunFlashPool = new GunFlashPool();
		bullets = new ArrayList<BulletSprite>();
		littleBullets = new ArrayList<BulletSprite>();
	}

	public static synchronized BulletSprite addBullet(final float x, final float y, final float rotation)
	{
		final BulletSprite bullet = bulletPool.obtainPoolItem();
		bullets.add(bullet);
		bullet.setPosition(x, y);
		bullet.setRotation(rotation);
		if(bullets.size() > 20)
			clearDeadBullets();
		return bullet;
	}

	public static synchronized BulletSprite addBullet(final float x, final float y, final float rotation, final IUpdateHandler updateHandler)
	{
		final BulletSprite bullet = addBullet(x, y, rotation);
		bullet.registerUpdateHandler(updateHandler);
		return bullet;
	}

	public static synchronized void removeBullet(final BulletSprite bullet)
	{
		bullets.remove(bullet);
		bulletPool.recyclePoolItem(bullet);
	}

	public static synchronized BulletSprite addLittleBullet(final float deltaX, final float deltaY, final float rotation)
	{
		final ShipSprite ship = gameActivity.getGameScene().ship;
		final BulletSprite bullet = littleBulletPool.obtainPoolItem();
		littleBullets.add(bullet);
		bullet.setPosition(ship.getCenterX() + deltaX, ship.getCenterY() + deltaY);
		bullet.setRotation(rotation);
		showGunFlash(ship, deltaX, deltaY, rotation);
		if(littleBullets.size() > 20)
			clearDeadBullets();
		return bullet;
	}

	public static synchronized BulletSprite addLittleBullet(final float x, final float y, final float rotation, final IUpdateHandler updateHandler)
	{
		final BulletSprite bullet = addLittleBullet(x, y, rotation);
		bullet.registerUpdateHandler(updateHandler);
		return bullet;
	}

	public static synchronized void removeLittleBullet(final BulletSprite bullet)
	{
		littleBullets.remove(bullet);
		littleBulletPool.recyclePoolItem(bullet);
	}

	public static synchronized void clearDeadBullets()
	{
		ArrayList<BulletSprite> deadBullets = new ArrayList<BulletSprite>();
		for(BulletSprite bullet : bullets)
			if(!bullet.isAlive())
			{
				deadBullets.add(bullet);
				bulletPool.recyclePoolItem(bullet);
			}
		bullets.removeAll(deadBullets);
		deadBullets = new ArrayList<BulletSprite>();
		for(BulletSprite bullet : littleBullets)
			if(!bullet.isAlive())
			{
				deadBullets.add(bullet);
				littleBulletPool.recyclePoolItem(bullet);
			}
		littleBullets.removeAll(deadBullets);
	}

	public static synchronized void clearBullets()
	{
		for(BulletSprite bullet : bullets)
			bulletPool.recyclePoolItem(bullet);
		bullets.clear();
		for(BulletSprite bullet : littleBullets)
			littleBulletPool.recyclePoolItem(bullet);
		littleBullets.clear();
	}

	public static synchronized ArrayList<BulletSprite> getBulletSprites()
	{
		return bullets;
	}

	public static synchronized ArrayList<BulletSprite> getLittleBulletSprites()
	{
		return littleBullets;
	}

	public static synchronized void showSpark(float x, float y, float rotation)
	{
		final AnimatedSprite sprite = littleBulletSparkPool.obtainPoolItem();
		sprite.setPosition(x, y - 25);
		sprite.setRotation(rotation);
		sprite.setVisible(true);
		sprite.registerEntityModifier(new SequenceEntityModifier(new IEntityModifier.IEntityModifierListener()
		{
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem)
			{
				littleBulletSparkPool.recyclePoolItem(sprite);
				sprite.setRotation(0);
			}
		}, new AlphaModifier(0.15f, 1, 1)));//, new AlphaModifier(0.01f, 1, 0)));
	}
	
	private static void showGunFlash(final ShipSprite ship, final float deltaX, final float deltaY, final float rotation)
	{
		final Sprite sprite = gunFlashPool.obtainPoolItem();
		final float y = deltaY - 25;
		sprite.setPosition(ship.getCenterX() + deltaX, ship.getCenterY() + y);
		sprite.setRotation(rotation);
		sprite.setVisible(true);
		sprite.registerEntityModifier(new SequenceEntityModifier(new IEntityModifier.IEntityModifierListener()
		{
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem)
			{
				gunFlashPool.recyclePoolItem(sprite);
				sprite.setRotation(0);
			}
		}, new AlphaModifier(0.15f, 1, 1)));//, new AlphaModifier(0.01f, 1, 0)));
		sprite.registerUpdateHandler(new IUpdateHandler()
		{
			@Override
			public void reset()
			{}

			@Override
			public void onUpdate(float pSecondsElapsed)
			{
				sprite.setPosition(ship.getCenterX() + deltaX, ship.getCenterY() + y);
			}
		});
	}
}