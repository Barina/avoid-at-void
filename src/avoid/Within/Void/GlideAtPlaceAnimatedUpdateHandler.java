package avoid.Within.Void;

import org.anddev.andengine.engine.handler.IUpdateHandler;

public class GlideAtPlaceAnimatedUpdateHandler implements IUpdateHandler
{
	private LiveAnimatedSprite sprite;
	private final float velocityX, velocityY;

	public GlideAtPlaceAnimatedUpdateHandler(LiveAnimatedSprite sprite, float velocityX, float velocityY)
	{
		this.sprite = sprite;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	@Override
	public void onUpdate(float pSecondsElapsed)
	{
		if(sprite.directionX == Direction.RIGHT)
		{
			sprite.getPhysicsHandler().setVelocityX(sprite.getPhysicsHandler().getVelocityX() + 1f);
			if(sprite.getPhysicsHandler().getVelocityX() > velocityX)
				sprite.directionX = Direction.LEFT;
		}
		else
		{
			sprite.getPhysicsHandler().setVelocityX(sprite.getPhysicsHandler().getVelocityX() - 1f);
			if(sprite.getPhysicsHandler().getVelocityX() < -velocityX)
				sprite.directionX = Direction.RIGHT;
		}
		if(sprite.directionY == Direction.DOWN)
		{
			sprite.getPhysicsHandler().setVelocityY(sprite.getPhysicsHandler().getVelocityY() + 1f);
			if(sprite.getPhysicsHandler().getVelocityY() > velocityY)
				sprite.directionY = Direction.UP;
		}
		else
		{
			sprite.getPhysicsHandler().setVelocityY(sprite.getPhysicsHandler().getVelocityY() - 1f);
			if(sprite.getPhysicsHandler().getVelocityY() < -velocityY)
				sprite.directionY = Direction.DOWN;
		}
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
	}
}