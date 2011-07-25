package avoid.Within.Void;

import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class EnemySprite extends LiveSprite
{
	public EnemySprite(BaseGameActivity baseGameActivity, LiveEntityIdentity entityIdentity, float pX, float pY, float maxVelocityX, float maxVelocityY,
			TextureRegion pTextureRegion)
	{
		super(baseGameActivity, entityIdentity, pX, pY, maxVelocityX, maxVelocityY, pTextureRegion);
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