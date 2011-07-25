package avoid.Within.Void;

public class Shooter
{
	private final float deltaX, deltaY, rotation;

	/**
	 * Represents an spot on the ship where to shot from
	 * 
	 * @param deltaX
	 *            The gap from center in X axis
	 * @param deltaY
	 *            The gap from center in Y axis
	 * @param rotation
	 *            The rotation of the bullet
	 */
	public Shooter(float deltaX, float deltaY, float rotation)
	{
		// its not really in the center so decreasing 25 points to the left..
		this.deltaX = deltaX - 25;
		this.deltaY = deltaY;
		this.rotation = rotation;
	}

	/**
	 * Fire new bullet with stored parameters.
	 */
	public void Shoot()
	{
		BulletFactory.addLittleBullet(deltaX, deltaY, rotation);
	}
}