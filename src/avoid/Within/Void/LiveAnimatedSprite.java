package avoid.Within.Void;

import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class LiveAnimatedSprite extends AnimatedSprite implements ILive
{
	protected final int INIT_LIFE, MAX_LIFE;
	protected BaseGameActivity baseGameActivity;
	public final float HALF_WIDTH, HALF_HEIGHT,MAX_VELOCITY_X, MAX_VELOCITY_Y;
	protected Direction directionX, directionY;
	private boolean isAlive;
	private int life;
	private PhysicsHandler mPhysicsHandler;
	private final LiveEntityIdentity entityIdentity;

	public LiveAnimatedSprite(BaseGameActivity baseGameActivity,LiveEntityIdentity entityIdentity, int life, int maxLife, float pX, float pY,float maxVelocityX, float maxVelocityY, TiledTextureRegion pTiledTextureRegion)
	{
		super(pX, pY, pTiledTextureRegion);
		this.baseGameActivity = baseGameActivity;
		this.entityIdentity = entityIdentity;
		this.MAX_LIFE = maxLife;
		setLife(this.INIT_LIFE = life);
		this.HALF_WIDTH = getWidth() * 0.5f;
		this.HALF_HEIGHT = getHeight() * 0.5f;
		this.MAX_VELOCITY_X = maxVelocityX;
		this.MAX_VELOCITY_Y = maxVelocityY;
		reset();// seems to work only with that line.. do not remove
	}

	public LiveAnimatedSprite(BaseGameActivity baseGameActivity,LiveEntityIdentity entityIdentity, float pX, float pY,float maxVelocityX, float maxVelocityY, TiledTextureRegion pTiledTextureRegion)
	{
		this(baseGameActivity,entityIdentity, 100, 100, pX, pY,maxVelocityX, maxVelocityY, pTiledTextureRegion);
	}

	@Override
	public void reset()
	{
		super.reset();
		directionX = Direction.RIGHT;
		directionY = Direction.DOWN;
		setAlive(true);
	}

	public BaseGameActivity getBaseGameActivity()
	{
		return baseGameActivity;
	}

	public GameActivity getGameActivity()
	{
		return (GameActivity)baseGameActivity;
	}

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
		return getX() + HALF_WIDTH;
	}

	@Override
	public float getCenterY()
	{
		return getY() + HALF_HEIGHT;
	}

	@Override
	public PhysicsHandler getPhysicsHandler()
	{
		if(this.mPhysicsHandler == null)
			registerUpdateHandler(this.mPhysicsHandler = new PhysicsHandler(this));
		return this.mPhysicsHandler;
	}

	@Override
	public float getHalfWidth()
	{
		return HALF_WIDTH;
	}

	@Override
	public float getHalfHeight()
	{
		return HALF_HEIGHT;
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