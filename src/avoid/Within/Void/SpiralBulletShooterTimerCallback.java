package avoid.Within.Void;

import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.ease.EaseCircularIn;

public class SpiralBulletShooterTimerCallback extends BulletShooterTimerCallback
{
	private final float bulletAngleGap = 22.5f;
	private boolean spiralToRight, swapAtEnd;

	public SpiralBulletShooterTimerCallback(ShipSprite ship, boolean spiralToRight, boolean swapAtEnd)
	{
		super(ship, 90, 0.0625f);
		this.spiralToRight = spiralToRight;
		this.swapAtEnd = swapAtEnd;
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler)
	{
		if(getShip().isAlive() && getShip().getCanShoot())
		{
			final BulletSprite bullet = BulletFactory.addBullet(ship.getCenterX(), ship.getCenterY(), bulletAngle);
			if(spiralToRight)
			{
				if((bulletAngle += bulletAngleGap) >= 450)
				{
					if(swapAtEnd)
						spiralToRight = !spiralToRight;
					else
						bulletAngle = 90;
				}
			}
			else
			{
				if((bulletAngle -= bulletAngleGap) <= 90)
				{
					if(swapAtEnd)
						spiralToRight = !spiralToRight;
					else
						bulletAngle = 450;
				}
			}
			final RotationModifier rotationModifier = new RotationModifier(1, bullet.getRotation(), spiralToRight ? bullet.getRotation() + 90 : bullet.getRotation() - 90,
					EaseCircularIn.getInstance());
			rotationModifier.setRemoveWhenFinished(true);
			final ParallelEntityModifier modifier = new ParallelEntityModifier(new IEntityModifierListener()
			{
				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem)
				{
					bullet.setAlive(false);
				}
			}, getAlphaModifier(), rotationModifier);
			modifier.setRemoveWhenFinished(true);
			bullet.registerEntityModifier(modifier);
		}
	}

	public void setBulletAngle(final float angle)
	{
		this.bulletAngle = angle;
	}
}