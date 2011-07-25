package avoid.Within.Void;

import org.anddev.andengine.engine.handler.physics.PhysicsHandler;

public interface ILive
{
	public static enum LiveEntityIdentity
	{
		Bullet, LittleBullet, UserShip, Enemy_1, Enemy_2, Enemy_3, Enemy_4, Enemy_5;
	}

	boolean addToLife(int amount);

	boolean subtractFromLife(int amount);

	void setLife(int amount);

	int getLife();

	void resetLife();

	boolean isAlive();

	void setAlive(boolean isAlive);

	float getX();

	float getY();

	float getCenterX();

	float getCenterY();

	float getHalfWidth();

	float getHalfHeight();

	PhysicsHandler getPhysicsHandler();

	void initColisionWith(LiveSprite other);

	void initColisionWith(LiveAnimatedSprite other);
}