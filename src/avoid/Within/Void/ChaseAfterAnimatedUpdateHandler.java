package avoid.Within.Void;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.modifier.MoveModifier;

public class ChaseAfterAnimatedUpdateHandler implements IUpdateHandler
{
	private final LiveAnimatedSprite chaser;
	private final ILive chased;
	private final float STEP_SPEED, DELTA_X, DELTA_Y;

	public ChaseAfterAnimatedUpdateHandler(LiveAnimatedSprite chaser, ILive chased, final float stepSpeed)
	{
		this.chaser = chaser;
		this.chased = chased;
		this.STEP_SPEED = stepSpeed;
		this.DELTA_X = Constants.RANDOM.nextInt((int)(chaser.MAX_VELOCITY_X * 0.5f));
		this.DELTA_Y = Constants.RANDOM.nextInt((int)(chaser.MAX_VELOCITY_Y * 0.5f));
	}

	@Override
	public void onUpdate(float pSecondsElapsed)
	{
		chaser.clearEntityModifiers();
		if(chased.isAlive())
		{
			MoveModifier modifier = new MoveModifier(STEP_SPEED, chaser.getX(), chased.getX(), chaser.getY(), chased.getY());
			modifier.setRemoveWhenFinished(true);
			chaser.registerEntityModifier(modifier);
		}
		else
		{
			if(chaser.directionX == Direction.RIGHT)
			{
				chaser.getPhysicsHandler().setVelocityX(chaser.getPhysicsHandler().getVelocityX() + 1f);
				if(chaser.getPhysicsHandler().getVelocityX() > DELTA_X)
					chaser.directionX = Direction.LEFT;
			}
			else
			{
				chaser.getPhysicsHandler().setVelocityX(chaser.getPhysicsHandler().getVelocityX() - 1f);
				if(chaser.getPhysicsHandler().getVelocityX() < -DELTA_X)
					chaser.directionX = Direction.RIGHT;
			}
			if(chaser.directionY == Direction.DOWN)
			{
				chaser.getPhysicsHandler().setVelocityY(chaser.getPhysicsHandler().getVelocityY() + 1f);
				if(chaser.getPhysicsHandler().getVelocityY() > DELTA_Y)
					chaser.directionY = Direction.UP;
			}
			else
			{
				chaser.getPhysicsHandler().setVelocityY(chaser.getPhysicsHandler().getVelocityY() - 1f);
				if(chaser.getPhysicsHandler().getVelocityY() < -DELTA_Y)
					chaser.directionY = Direction.DOWN;
			}
		}
	}

	@Override
	public void reset()
	{
		chaser.clearEntityModifiers();
	}
}