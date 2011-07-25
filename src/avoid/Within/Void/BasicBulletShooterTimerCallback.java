package avoid.Within.Void;

import java.util.ArrayList;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

public class BasicBulletShooterTimerCallback extends BulletShooterTimerCallback
{
	private ArrayList<Shooter> shooters;
	private final Shooter defaultShooter;

	protected BasicBulletShooterTimerCallback(ShipSprite ship, Shooter defaultShooter)
	{
		super(ship, 90, 0.2f);
		shooters = new ArrayList<Shooter>();
		this.defaultShooter = defaultShooter;
	}

	protected BasicBulletShooterTimerCallback(ShipSprite ship, float defaultX, float defaultY, float defaultRotation)
	{
		this(ship, new Shooter(defaultX, defaultY, defaultRotation));
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler)
	{
		if(!(getShip().isAlive() && getShip().getCanShoot()))
			return;
		defaultShooter.Shoot();
		for(Shooter shooter : shooters)
			shooter.Shoot();
	}

	public Shooter addShooter(final float fromX, final float fromY, final float rotation)
	{
		Shooter shooter = new Shooter(fromX, fromY, rotation);
		this.shooters.add(shooter);
		return shooter;
	}

	public void removeShooter(Shooter shooter)
	{
		this.shooters.remove(shooter);
	}

	public void clearShooters()
	{
		shooters.clear();
	}
}