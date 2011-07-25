package avoid.Within.Void;

import java.util.ArrayList;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.pool.GenericPool;
import avoid.Within.Void.ILive.LiveEntityIdentity;

public class EnemyFactory
{
	private static class Enemy1Pool extends GenericPool<EnemySprite>
	{
		@Override
		public synchronized void recyclePoolItem(final EnemySprite sprite)
		{
			sprite.setAlive(false);
			super.recyclePoolItem(sprite);
		}

		@Override
		public synchronized EnemySprite obtainPoolItem()
		{
			EnemySprite sprite = super.obtainPoolItem();
			sprite.reset();
			sprite.setAlive(true);
			return sprite;
		}

		@Override
		protected EnemySprite onAllocatePoolItem()
		{
			final EnemySprite sprite = new EnemySprite(baseGameActivity, LiveEntityIdentity.Enemy_1, 0, 0, 100, 100, getEnemy1TextureRegionClone());
			((GameActivity)baseGameActivity).getGameScene().getChild(GameScene.SHIPS_LAYER).attachChild(sprite);
			return sprite;
		}
	}

	private static abstract class LiveAnimatedPool extends GenericPool<EnemyAnimatedSprite>
	{
		@Override
		public synchronized void recyclePoolItem(final EnemyAnimatedSprite sprite)
		{
			sprite.setAlive(false);
			super.recyclePoolItem(sprite);
		}

		@Override
		public synchronized EnemyAnimatedSprite obtainPoolItem()
		{
			EnemyAnimatedSprite sprite = super.obtainPoolItem();
			sprite.reset();
			sprite.setAlive(true);
			return sprite;
		}
	}

	private static class Enemy2AnimatedPool extends LiveAnimatedPool
	{
		@Override
		protected EnemyAnimatedSprite onAllocatePoolItem()
		{
			final EnemyAnimatedSprite sprite = new EnemyAnimatedSprite(baseGameActivity, LiveEntityIdentity.Enemy_2, 0, 0, 100, 100, getEnemyTiledTextureRegionClone(2));
			((GameActivity)baseGameActivity).getGameScene().getChild(GameScene.SHIPS_LAYER).attachChild(sprite);
			return sprite;
		}
	}

	private static class Enemy3AnimatedPool extends LiveAnimatedPool
	{
		@Override
		protected EnemyAnimatedSprite onAllocatePoolItem()
		{
			final EnemyAnimatedSprite sprite = new EnemyAnimatedSprite(baseGameActivity, LiveEntityIdentity.Enemy_3, 0, 0, 200, 200, getEnemyTiledTextureRegionClone(3));
			((GameActivity)baseGameActivity).getGameScene().getChild(GameScene.SHIPS_LAYER).attachChild(sprite);
			return sprite;
		}
	}

	private static class Enemy4AnimatedPool extends LiveAnimatedPool
	{
		@Override
		protected EnemyAnimatedSprite onAllocatePoolItem()
		{
			final EnemyAnimatedSprite sprite = new EnemyAnimatedSprite(baseGameActivity, LiveEntityIdentity.Enemy_4, 0, 0, 400, 400, getEnemyTiledTextureRegionClone(4));
			((GameActivity)baseGameActivity).getGameScene().getChild(GameScene.SHIPS_LAYER).attachChild(sprite);
			return sprite;
		}
	}

	private static class Enemy5AnimatedPool extends LiveAnimatedPool
	{
		@Override
		protected EnemyAnimatedSprite onAllocatePoolItem()
		{
			final EnemyAnimatedSprite sprite = new EnemyAnimatedSprite(baseGameActivity, LiveEntityIdentity.Enemy_5, 0, 0, 150, 150, getEnemyTiledTextureRegionClone(5));
			((GameActivity)baseGameActivity).getGameScene().getChild(GameScene.SHIPS_LAYER).attachChild(sprite);
			return sprite;
		}
	}

	private static BaseGameActivity baseGameActivity;
	private static Texture enemyTexture, enemyAnimatedTexture;
	private static TextureRegion enemy1TextureRegion;
	private static TiledTextureRegion enemy2TiledTextureRegion, enemy3TiledTextureRegion, enemy4TiledTextureRegion, enemy5TiledTextureRegion;
	private static Enemy1Pool enemy1Pool;
	private static Enemy2AnimatedPool enemy2Pool;
	private static Enemy3AnimatedPool enemy3Pool;
	private static Enemy4AnimatedPool enemy4Pool;
	private static Enemy5AnimatedPool enemy5Pool;
	private static ArrayList<EnemySprite> enemy1Sprites;
	private static ArrayList<EnemyAnimatedSprite> enemy2Sprites, enemy3Sprites, enemy4Sprites, enemy5Sprites;

	public static synchronized void loadResources(BaseGameActivity baseGameActivity)
	{
		EnemyFactory.baseGameActivity = baseGameActivity;
		enemyTexture = new Texture(32, 64, TextureOptions.BILINEAR);
		enemy1TextureRegion = TextureRegionFactory.createFromAsset(enemyTexture, baseGameActivity, "gfx/enemy_1.png", 0, 0);
		enemyAnimatedTexture = new Texture(256, 256, TextureOptions.BILINEAR);
		enemy2TiledTextureRegion = TextureRegionFactory.createTiledFromAsset(enemyAnimatedTexture, baseGameActivity, "gfx/enemy_2.png", 0, 0, 2, 1);
		enemy3TiledTextureRegion = TextureRegionFactory.createTiledFromAsset(enemyAnimatedTexture, baseGameActivity, "gfx/enemy_3.png", 0, 91, 2, 1);
		enemy4TiledTextureRegion = TextureRegionFactory.createTiledFromAsset(enemyAnimatedTexture, baseGameActivity, "gfx/enemy_4.png", 120, 53, 2, 1);
		enemy5TiledTextureRegion = TextureRegionFactory.createTiledFromAsset(enemyAnimatedTexture, baseGameActivity, "gfx/enemy_5.png", 120, 0, 2, 1);
		baseGameActivity.getEngine().getTextureManager().loadTextures(enemyTexture, enemyAnimatedTexture);
		enemy1Pool = new Enemy1Pool();
		enemy2Pool = new Enemy2AnimatedPool();
		enemy3Pool = new Enemy3AnimatedPool();
		enemy4Pool = new Enemy4AnimatedPool();
		enemy5Pool = new Enemy5AnimatedPool();
		enemy1Sprites = new ArrayList<EnemySprite>();
		enemy2Sprites = new ArrayList<EnemyAnimatedSprite>();
		enemy3Sprites = new ArrayList<EnemyAnimatedSprite>();
		enemy4Sprites = new ArrayList<EnemyAnimatedSprite>();
		enemy5Sprites = new ArrayList<EnemyAnimatedSprite>();
	}

	public static synchronized EnemySprite addEnemy(float pX, float pY)
	{
		final EnemySprite sprite = enemy1Pool.obtainPoolItem();
		sprite.clearEntityModifiers();
		sprite.clearUpdateHandlers();
		enemy1Sprites.add(sprite);
		sprite.setPosition(pX, pY);
		return sprite;
	}

	public static synchronized EnemyAnimatedSprite addEnemy(final int index, float pX, float pY)
	{
		ArrayList<EnemyAnimatedSprite> enemies = null;
		LiveAnimatedPool pool = null;
		switch (index)
		{
			case 2:
				enemies = enemy2Sprites;
				pool = enemy2Pool;
				break;
			case 3:
				enemies = enemy3Sprites;
				pool = enemy3Pool;
				break;
			case 4:
				enemies = enemy4Sprites;
				pool = enemy4Pool;
				break;
			case 5:
				enemies = enemy5Sprites;
				pool = enemy5Pool;
				break;
		}
		final EnemyAnimatedSprite sprite = pool.obtainPoolItem();
		sprite.clearEntityModifiers();
		sprite.clearUpdateHandlers();
		enemies.add(sprite);
		sprite.setPosition(pX, pY);
		return sprite;
	}

	public static synchronized void clearDeadEnemies(final int index)
	{
		if(index == 1)
		{
			final ArrayList<EnemySprite> deadEnemies = new ArrayList<EnemySprite>();
			for(final EnemySprite enemy : enemy1Sprites)
				if(!enemy.isAlive())
				{
					deadEnemies.add(enemy);
					enemy1Pool.recyclePoolItem(enemy);
				}
			enemy1Sprites.removeAll(deadEnemies);
		}
		else
		{
			final ArrayList<EnemyAnimatedSprite> deadEnemies = new ArrayList<EnemyAnimatedSprite>();
			ArrayList<EnemyAnimatedSprite> enemies = null;
			LiveAnimatedPool enemiesPool = null;
			switch (index)
			{
				case 2:
					enemies = enemy2Sprites;
					enemiesPool = enemy2Pool;
				case 3:
					enemies = enemy3Sprites;
					enemiesPool = enemy3Pool;
				case 4:
					enemies = enemy4Sprites;
					enemiesPool = enemy4Pool;
				case 5:
					enemies = enemy5Sprites;
					enemiesPool = enemy5Pool;
					break;
				default:
					enemies = new ArrayList<EnemyAnimatedSprite>();
			}
			for(final EnemyAnimatedSprite enemy : enemies)
				if(!enemy.isAlive())
				{
					deadEnemies.add(enemy);
					enemiesPool.recyclePoolItem(enemy);
				}
			enemies.removeAll(deadEnemies);
		}
	}

	public static void clearAllDeadEnemies()
	{
		for(int i = 1 ; i <= 5 ; i++)
			clearDeadEnemies(i);
	}

	public static synchronized void clearEnemies(final int index)
	{
		if(index == 1)
		{
			for(final EnemySprite sprite : enemy1Sprites)
				enemy1Pool.recyclePoolItem(sprite);
			enemy1Sprites.clear();
		}
		else
		{
			ArrayList<EnemyAnimatedSprite> enemies = null;
			LiveAnimatedPool enemiesPool = null;
			switch (index)
			{
				case 2:
					enemies = enemy2Sprites;
					enemiesPool = enemy2Pool;
					break;
				case 3:
					enemies = enemy3Sprites;
					enemiesPool = enemy3Pool;
					break;
				case 4:
					enemies = enemy4Sprites;
					enemiesPool = enemy4Pool;
					break;
				case 5:
					enemies = enemy5Sprites;
					enemiesPool = enemy5Pool;
					break;
				default:
					enemies = new ArrayList<EnemyAnimatedSprite>();
			}
			for(final EnemyAnimatedSprite sprite : enemies)
				enemiesPool.recyclePoolItem(sprite);
			enemies.clear();
		}
	}

	public static synchronized void clearAllEnemies()
	{
		for(int i = 1 ; i <= 5 ; i++)
			clearEnemies(i);
	}

	@Deprecated
	public static synchronized void removeEnemy(final EnemySprite sprite)
	{
		enemy1Pool.recyclePoolItem(sprite);
		enemy1Sprites.remove(sprite);
	}

	public static synchronized ArrayList<EnemySprite> getEnemy1Sprites()
	{
		return enemy1Sprites;
	}

	public static synchronized ArrayList<EnemyAnimatedSprite> getEnemySprites(final int index)
	{
		switch (index)
		{
			case 2:
				return enemy2Sprites;
			case 3:
				return enemy3Sprites;
			case 4:
				return enemy4Sprites;
			case 5:
				return enemy5Sprites;
			default:
				return null;
		}
	}

	public static synchronized ArrayList<EnemyAnimatedSprite> getAllEnemyAnimatedSprites()
	{
		ArrayList<EnemyAnimatedSprite> result = new ArrayList<EnemyAnimatedSprite>(enemy2Sprites);
		result.addAll(enemy3Sprites);
		result.addAll(enemy4Sprites);
		result.addAll(enemy5Sprites);
		return result;
	}

	public static float getEnemyWidth(final int index)
	{
		switch (index)
		{
			case 1:
				return enemy1TextureRegion.getWidth();
			case 2:
				return enemy2TiledTextureRegion.getTileWidth();
			case 3:
				return enemy3TiledTextureRegion.getTileWidth();
			case 4:
				return enemy4TiledTextureRegion.getTileWidth();
			case 5:
				return enemy5TiledTextureRegion.getTileWidth();
			default:
				return 0;
		}
	}

	public static float getEnemyHeight(final int index)
	{
		switch (index)
		{
			case 1:
				return enemy1TextureRegion.getHeight();
			case 2:
				return enemy2TiledTextureRegion.getTileHeight();
			case 3:
				return enemy3TiledTextureRegion.getTileHeight();
			case 4:
				return enemy4TiledTextureRegion.getTileHeight();
			case 5:
				return enemy5TiledTextureRegion.getTileHeight();
			default:
				return 0;
		}
	}

	public static synchronized TextureRegion getEnemy1TextureRegionClone()
	{
		return enemy1TextureRegion.clone();
	}

	public static synchronized TiledTextureRegion getEnemyTiledTextureRegionClone(final int index)
	{
		switch (index)
		{
			case 2:
				return enemy2TiledTextureRegion.clone();
			case 3:
				return enemy3TiledTextureRegion.clone();
			case 4:
				return enemy4TiledTextureRegion.clone();
			case 5:
				return enemy5TiledTextureRegion.clone();
			default:
				return null;
		}
	}

	public static synchronized int getLiveEnemiesCount()
	{
		return enemy1Sprites.size() + enemy2Sprites.size() + enemy3Sprites.size() + enemy4Sprites.size() + enemy5Sprites.size();
	}
}