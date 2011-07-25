package avoid.Within.Void;

import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class LiveSprite extends Sprite implements ILive
{
	protected final int INIT_LIFE, MAX_LIFE;
	protected BaseGameActivity baseGameActivity;
	public final float halfWidth, halfHeight, MAX_VELOCITY_X, MAX_VELOCITY_Y;
	protected Direction directionX, directionY;
	private boolean isAlive;
	private int life;
	private PhysicsHandler mPhysicsHandler;
	private final LiveEntityIdentity entityIdentity;

	public LiveSprite(BaseGameActivity baseGameActivity, LiveEntityIdentity entityIdentity, int life, int maxLife, float pX, float pY, float maxVelocityX, float maxVelocityY,
			TextureRegion pTextureRegion)
	{
		super(pX, pY, pTextureRegion);
		this.baseGameActivity = baseGameActivity;
		this.entityIdentity = entityIdentity;
		this.MAX_LIFE = maxLife;
		setLife(this.INIT_LIFE = life);
		this.halfWidth = pTextureRegion.getWidth() * 0.5f;
		this.halfHeight = pTextureRegion.getHeight() * 0.5f;
		this.MAX_VELOCITY_X = maxVelocityX;
		this.MAX_VELOCITY_Y = maxVelocityY;
	}

	public LiveSprite(BaseGameActivity baseGameActivity, LiveEntityIdentity entityIdentity, float pX, float pY, float maxVelocityX, float maxVelocityY, TextureRegion pTextureRegion)
	{
		this(baseGameActivity, entityIdentity, 100, 100, pX, pY, maxVelocityX, maxVelocityY, pTextureRegion);
	}

	@Override
	public void reset()
	{
		super.reset();
		directionX = Direction.RIGHT;
		directionY = Direction.DOWN;
		setAlive(true);
	}

	public PhysicsHandler getPhysicsHandler()
	{
		if(mPhysicsHandler == null)
			registerUpdateHandler(this.mPhysicsHandler = new PhysicsHandler(this));
		return mPhysicsHandler;
	}

	public BaseGameActivity getBaseGameActivity()
	{
		return baseGameActivity;
	}

	/**
	 * Also setting the
	 * {@link #org.anddev.andengine.entity.Entity.setVisible(boolean)}.
	 * 
	 * @see org.anddev.andengine.entity.Entity#setVisible(boolean)
	 * @see avoid.Within.Void.ILive#setAlive(boolean)
	 */
	@Override
	public void setAlive(boolean isAlive)
	{
		setIgnoreUpdate(!(this.isAlive = isAlive));
		setVisible(isAlive);
	}

	@Override
	public boolean isAlive()
	{
		return isAlive;
	}

	@Override
	public float getCenterX()
	{
		return getX() + halfWidth;
	}

	@Override
	public float getCenterY()
	{
		return getY() + halfHeight;
	}

	@Override
	public float getHalfWidth()
	{
		return halfWidth;
	}

	@Override
	public float getHalfHeight()
	{
		return halfHeight;
	}

	@Override
	public boolean addToLife(int amount)
	{
		if(isAlive() && this.life < MAX_LIFE)
			this.life += amount;
		if(this.life < 0)
		{
			setAlive(false);
			return false;
		}
		if(this.life > MAX_LIFE)
			setLife(MAX_LIFE);
		return true;
	}

	@Override
	public boolean subtractFromLife(int amount)
	{
		return addToLife(-amount);
	}

	@Override
	public void setLife(int amount)
	{
		if(amount > MAX_LIFE)
			this.life = MAX_LIFE;
		else
			this.life = amount;
	}

	@Override
	public int getLife()
	{
		return this.life;
	}

	@Override
	public void resetLife()
	{
		this.life = INIT_LIFE;
	}

	@Override
	public void initColisionWith(LiveSprite other)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void initColisionWith(LiveAnimatedSprite other)
	{
		// TODO Auto-generated method stub
	}

	public LiveEntityIdentity getEntityIdentity()
	{
		return entityIdentity;
	}
}