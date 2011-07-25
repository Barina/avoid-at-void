package avoid.Within.Void;

import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.MathUtils;
import android.util.FloatMath;

public class BulletSprite extends LiveSprite
{
	private float bulletSpeed;

	public BulletSprite(BaseGameActivity baseBaseGameActivity, LiveEntityIdentity entityIdentity, float pX, float pY, float bulletSpeed, TextureRegion pTextureRegion)
	{
		super(baseBaseGameActivity, entityIdentity, pX, pY, bulletSpeed, bulletSpeed, pTextureRegion);
		setBulletSpeed(bulletSpeed);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed)
	{
		final float r = getRotation();
		getPhysicsHandler().setVelocity(-FloatMath.cos(MathUtils.degToRad(r)) * bulletSpeed, -FloatMath.sin(MathUtils.degToRad(r)) * bulletSpeed);
		super.onManagedUpdate(pSecondsElapsed);
		if(getX() < -100 || getX() > (Constants.CAMERA_WIDTH + 100) || getY() < -100 || getY() > (Constants.CAMERA_HEIGHT + 100))
			setAlive(false);
	}

	public void setBulletSpeed(float bulletSpeed)
	{
		this.bulletSpeed = bulletSpeed;
	}
}