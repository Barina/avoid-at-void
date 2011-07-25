package avoid.Within.Void;

import java.util.ArrayList;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.pool.GenericPool;

public class BlastFactory
{
	static class BigBlastPool extends GenericPool<AnimatedSprite>
	{
		@Override
		protected AnimatedSprite onAllocatePoolItem()
		{
			return new AnimatedSprite(getBigBlastWidth(), getBigBlastHeight(), bigBlastTiledTextureRegion.clone());
		}
	}

	static class ExplodePool extends GenericPool<AnimatedSprite>
	{
		@Override
		protected AnimatedSprite onAllocatePoolItem()
		{
			return new AnimatedSprite(getExplodeWidth(), getExplodeHeight(), explodeTiledTextureRegion.clone());
		}
	}

	private static Texture bigBlastTexture, explodeTexture;
	private static TiledTextureRegion bigBlastTiledTextureRegion, explodeTiledTextureRegion;
	private static BigBlastPool bigBlastPool;
	private static ExplodePool explodePool;
	private static ArrayList<AnimatedSprite> bigBlastsAnimatedSprites, explodeAnimatedSprites;
	private static Sound bigBlastSound, explodeSound;

	public static void loadResources(GameActivity gameActivity)
	{
		bigBlastTexture = new Texture(1024, 512, TextureOptions.BILINEAR);
		bigBlastTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(bigBlastTexture, gameActivity, "gfx/big_blast_texture.png", 0, 0, 8, 3);
		explodeTexture = new Texture(512, 256, TextureOptions.BILINEAR);
		explodeTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(explodeTexture, gameActivity, "gfx/explode.png", 1, 1, 7, 3);
		gameActivity.getEngine().getTextureManager().loadTextures(bigBlastTexture, explodeTexture);
		bigBlastPool = new BigBlastPool();
		explodePool = new ExplodePool();
		bigBlastsAnimatedSprites = new ArrayList<AnimatedSprite>();
		explodeAnimatedSprites = new ArrayList<AnimatedSprite>();
		bigBlastSound = GameSoundsFactory.getSound(GameSoundsFactory.SOUND_BIG_BLAST);
		explodeSound = GameSoundsFactory.getSound(GameSoundsFactory.SOUND_EXPLODE);
	}

	public static void addBigBlast(final GameActivity gameActivity, float x, float y)
	{
		final AnimatedSprite sprite = bigBlastPool.obtainPoolItem();
		sprite.reset();
		sprite.setPosition(x - getBigBlastWidth() * 0.5f, y - getBigBlastHeight() * 0.5f);
//		gameActivity.getEngine().getScene().getLayer(GameScene.BLAST_LAYER).addEntity(sprite);
		gameActivity.getEngine().getScene().getChild(GameScene.BLAST_LAYER).attachChild(sprite);
		sprite.animate(60, false, new IAnimationListener()
		{
			@Override
			public void onAnimationEnd(final AnimatedSprite pAnimatedSprite)
			{
				gameActivity.runOnUpdateThread(new Runnable()
				{
					@Override
					public void run()
					{
//						gameActivity.getGameScene().getLayer(GameScene.BLAST_LAYER).removeEntity(pAnimatedSprite);
						gameActivity.getGameScene().getChild(GameScene.BLAST_LAYER).detachChild(pAnimatedSprite);
					}
				});
				bigBlastsAnimatedSprites.remove(pAnimatedSprite);
				bigBlastPool.recyclePoolItem(pAnimatedSprite);
			}
		});
		bigBlastsAnimatedSprites.add(sprite);
		bigBlastSound.play();
	}

	public static void addExplosion(final GameActivity gameActivity, float x, float y)
	{
		final AnimatedSprite sprite = explodePool.obtainPoolItem();
		sprite.reset();
		sprite.setPosition(x - getExplodeWidth() * 0.5f, y - getExplodeHeight() * 0.5f);
//		gameActivity.getEngine().getScene().getLayer(GameScene.BLAST_LAYER).addEntity(sprite);
		gameActivity.getEngine().getScene().getChild(GameScene.BLAST_LAYER).attachChild(sprite);
		sprite.animate(new long[]
		{60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60}, 0, 15, 0, new IAnimationListener()
		{
			@Override
			public void onAnimationEnd(final AnimatedSprite pAnimatedSprite)
			{
				gameActivity.runOnUpdateThread(new Runnable()
				{
					@Override
					public void run()
					{
//						gameActivity.getGameScene().getLayer(GameScene.BLAST_LAYER).removeEntity(pAnimatedSprite);
						gameActivity.getGameScene().getChild(GameScene.BLAST_LAYER).detachChild(pAnimatedSprite);
					}
				});
				explodeAnimatedSprites.remove(pAnimatedSprite);
				explodePool.recyclePoolItem(pAnimatedSprite);
			}
		});
		explodeAnimatedSprites.add(sprite);
		explodeSound.play();
	}

	public static void clearBlasts(final GameActivity gameActivity)
	{
		for(final AnimatedSprite blast : bigBlastsAnimatedSprites)
		{
			blast.stopAnimation();
			gameActivity.runOnUpdateThread(new Runnable()
			{
				@Override
				public void run()
				{
//					gameActivity.getEngine().getScene().getLayer(GameScene.BLAST_LAYER).removeEntity(blast);
					gameActivity.getEngine().getScene().getChild(GameScene.BLAST_LAYER).detachChild(blast);
				}
			});
			bigBlastPool.recyclePoolItem(blast);
		}
		bigBlastsAnimatedSprites.clear();
		for(final AnimatedSprite blast : explodeAnimatedSprites)
		{
			blast.stopAnimation();
			gameActivity.runOnUpdateThread(new Runnable()
			{
				@Override
				public void run()
				{
//					gameActivity.getEngine().getScene().getLayer(GameScene.BLAST_LAYER).removeEntity(blast);
					gameActivity.getEngine().getScene().getChild(GameScene.BLAST_LAYER).detachChild(blast);
				}
			});
			explodePool.recyclePoolItem(blast);
		}
		explodeAnimatedSprites.clear();
	}

	public static float getBigBlastWidth()
	{
		if(bigBlastTiledTextureRegion != null)
			return bigBlastTiledTextureRegion.getTileWidth();
		return 0;
	}

	public static float getBigBlastHeight()
	{
		if(bigBlastTiledTextureRegion != null)
			return bigBlastTiledTextureRegion.getTileHeight();
		return 0;
	}

	public static float getExplodeWidth()
	{
		if(explodeTiledTextureRegion != null)
			return explodeTiledTextureRegion.getTileWidth();
		return 0;
	}

	public static float getExplodeHeight()
	{
		if(explodeTiledTextureRegion != null)
			return explodeTiledTextureRegion.getTileHeight();
		return 0;
	}
}