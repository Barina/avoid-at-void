package avoid.Within.Void;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import and.engine.AutoVerticalParallaxBackground;
import and.engine.ParallaxBackground2d.ParallaxBackground2dEntity;

public class BackgroundFactory
{
	private static Texture bgBric1Texture, bgStars1Texture;
	private static TextureRegion bgBric1TextureRegion, bgStars1TextureRegion;

	public static synchronized void loadResources(BaseGameActivity baseGameActivity)
	{
		bgBric1Texture = new Texture(512, 512, TextureOptions.BILINEAR);
		bgStars1Texture = new Texture(512, 1024, TextureOptions.BILINEAR);
		bgBric1TextureRegion = TextureRegionFactory.createFromAsset(bgBric1Texture, baseGameActivity, "gfx/bg_bric_1.png", 0, 0);
		bgStars1TextureRegion = TextureRegionFactory.createFromAsset(bgStars1Texture, baseGameActivity, "gfx/bg_stars_1.png", 0, 0);
		baseGameActivity.getEngine().getTextureManager().loadTextures(bgBric1Texture,bgStars1Texture);
		
	}
	
	public static synchronized AutoVerticalParallaxBackground getBgBric1AutoVerticalParallaxBackground(BaseGameActivity baseGameActivity, float speed)
	{
		final AutoVerticalParallaxBackground autoVerticalParallaxBackground = new AutoVerticalParallaxBackground(0, 0, 0, 5);
		autoVerticalParallaxBackground.addParallaxEntity(new ParallaxBackground2dEntity(0, speed, new Sprite(0, 0, bgBric1TextureRegion.clone())));
		return autoVerticalParallaxBackground;
	}

	public static synchronized AutoVerticalParallaxBackground getBgStars1AutoVerticalParallaxBackground(BaseGameActivity baseGameActivity, float speed)
	{
		final AutoVerticalParallaxBackground autoVerticalParallaxBackground = new AutoVerticalParallaxBackground(0, 0, 0, 5);
		autoVerticalParallaxBackground.addParallaxEntity(new ParallaxBackground2dEntity(0, speed, new Sprite(0, 0, bgStars1TextureRegion.clone())));
		return autoVerticalParallaxBackground;
	}
}