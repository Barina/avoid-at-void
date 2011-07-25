package avoid.Within.Void;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.util.modifier.ease.EaseCircularIn;

public abstract class BulletShooterTimerCallback implements ITimerCallback
{
	public final float bulletInterval;
	private final AlphaModifier alphaModifier = new AlphaModifier(1, 1, 0, EaseCircularIn.getInstance());
	protected final ShipSprite ship;
	protected float bulletAngle = 90;

	protected BulletShooterTimerCallback(ShipSprite ship, float bulletAngle, float bulletInterval)
	{
		this.bulletInterval = bulletInterval;
		this.ship = ship;
		this.bulletAngle = bulletAngle;
		this.alphaModifier.setRemoveWhenFinished(true);
	}

	public ShipSprite getShip()
	{
		return ship;
	}

	public float getBulletAngle()
	{
		return bulletAngle;
	}

	public AlphaModifier getAlphaModifier()
	{
		return alphaModifier.clone();
	}
}