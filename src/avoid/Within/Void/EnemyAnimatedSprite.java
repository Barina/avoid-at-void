package avoid.Within.Void;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class EnemyAnimatedSprite extends LiveAnimatedSprite
{
	public EnemyAnimatedSprite(BaseGameActivity baseGameActivity, LiveEntityIdentity entityIdentity, float pX, float pY, float maxVelocityX, float maxVelocityY,
			TiledTextureRegion pTiledTextureRegion)
	{
		super(baseGameActivity, entityIdentity, pX, pY, maxVelocityX, maxVelocityY, pTiledTextureRegion);
		animate(new long[]
		{900, 100}, 0, 1, true);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed)
	{
		synchronized (EnemyFactory.class)
		{
			if(getX() < -200 || getX() > Constants.CAMERA_WIDTH + 200 || getY() < -200 || getY() > Constants.CAMERA_HEIGHT + 200)
				setAlive(false);
		}
		super.onManagedUpdate(pSecondsElapsed);
	}
}