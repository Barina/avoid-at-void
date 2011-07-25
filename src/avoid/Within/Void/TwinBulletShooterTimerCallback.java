package avoid.Within.Void;

import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.ease.EaseCircularIn;

public class TwinBulletShooterTimerCallback extends BulletShooterTimerCallback
{
	private boolean shotUp, shotDown, shotLeft, shotRight, shotCurveIn, shakeAngleRotation, spiralToRight, shotBothCanons, shotFromLeftCanon;

	public TwinBulletShooterTimerCallback(ShipSprite ship, boolean shotCurveIn, boolean shakeAngleRotation, boolean shotBothCanons, boolean shotUp, boolean shotDown,
			boolean shotLeft, boolean shotRight)
	{
		super(ship, 90, 0.250f);
		this.shotCurveIn = shotCurveIn;
		this.shakeAngleRotation = shakeAngleRotation;
		this.shotBothCanons = shotBothCanons;
		this.shotUp = shotUp;
		this.shotDown = shotDown;
		this.shotLeft = shotLeft;
		this.shotRight = shotRight;
	}

	public TwinBulletShooterTimerCallback(ShipSprite ship, boolean shotCurveIn, boolean shakeAngleRotation, boolean shotBothCanons)
	{
		this(ship, shotCurveIn, shakeAngleRotation, shotBothCanons, true, false, false, false);
	}

	public TwinBulletShooterTimerCallback(ShipSprite ship, boolean shotCurveIn, boolean shakeAngleRotation)
	{
		this(ship, shotCurveIn, shakeAngleRotation, true, true, false, false, false);
	}

	public TwinBulletShooterTimerCallback(ShipSprite ship, boolean shotCurveIn)
	{
		this(ship, shotCurveIn, false, false, true, false, false, false);
	}

	public TwinBulletShooterTimerCallback(ShipSprite ship)
	{
		this(ship, true, false, false, true, false, false, false);
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler)
	{
		if(!(getShip().isAlive() && getShip().getCanShoot()))
			return;
		shotFromLeftCanon = !shotFromLeftCanon;
		if(shakeAngleRotation)
		{
			if(spiralToRight)
			{
				if((bulletAngle += 10) >= 100)
					spiralToRight = !spiralToRight;
			}
			else
			{
				if((bulletAngle -= 10) <= 80)
					spiralToRight = !spiralToRight;
			}
		}
		if(shotFromLeftCanon)
		{
			if(shotUp)
			{
				addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() - 20, getShip().getCenterY() - 20, bulletAngle - 10), true);
				if(shotBothCanons)
					addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() + 20, getShip().getCenterY() - 20, bulletAngle + 10), false);
			}
			if(shotRight)
			{
				addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() + 20, getShip().getCenterY() - 20, bulletAngle + 80), true);
				if(shotBothCanons)
					addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() + 20, getShip().getCenterY() + 20, bulletAngle + 100), false);
			}
			if(shotDown)
			{
				addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() + 20, getShip().getCenterY() + 20, bulletAngle + 170), true);
				if(shotBothCanons)
					addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() - 20, getShip().getCenterY() + 20, bulletAngle + 190), false);
			}
			if(shotLeft)
			{
				addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() - 20, getShip().getCenterY() + 20, bulletAngle + 260), true);
				if(shotBothCanons)
					addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() - 20, getShip().getCenterY() - 20, bulletAngle + 280), false);
			}
		}
		else
		{
			if(shotUp)
			{
				addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() + 20, getShip().getCenterY() - 20, bulletAngle + 10), false);
				if(shotBothCanons)
					addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() - 20, getShip().getCenterY() - 20, bulletAngle - 10), true);
			}
			if(shotRight)
			{
				addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() + 20, getShip().getCenterY() + 20, bulletAngle + 100), false);
				if(shotBothCanons)
					addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() + 20, getShip().getCenterY() - 20, bulletAngle + 80), true);
			}
			if(shotDown)
			{
				addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() - 20, getShip().getCenterY() + 20, bulletAngle + 190), false);
				if(shotBothCanons)
					addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() + 20, getShip().getCenterY() + 20, bulletAngle + 170), true);
			}
			if(shotLeft)
			{
				addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() - 20, getShip().getCenterY() - 20, bulletAngle + 280), false);
				if(shotBothCanons)
					addModifiersToBullet(BulletFactory.addBullet(getShip().getCenterX() - 20, getShip().getCenterY() + 20, bulletAngle + 260), true);
			}
		}
	}

	private void addModifiersToBullet(final BulletSprite bullet, boolean leftCanon)
	{
		RotationModifier rotationModifier = null;
		if(shakeAngleRotation)
			rotationModifier = new RotationModifier(1, bullet.getRotation(), spiralToRight ? bullet.getRotation() - 45 : bullet.getRotation() + 45, EaseCircularIn.getInstance());
		else
			if(shotCurveIn)
				rotationModifier = new RotationModifier(1, bullet.getRotation(), leftCanon ? bullet.getRotation() + 45 : bullet.getRotation() - 45, EaseCircularIn.getInstance());
			else
				rotationModifier = new RotationModifier(1, bullet.getRotation(), leftCanon ? bullet.getRotation() - 45 : bullet.getRotation() + 45, EaseCircularIn.getInstance());
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