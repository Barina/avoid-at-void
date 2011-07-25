package avoid.Within.Void;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import android.graphics.Color;

public class HUDSprite extends Sprite
{
	private GameActivity gameActivity;
	private final ShipSprite ship;
	private Texture fontTexture, uiExtraLifeTexture;
	private TextureRegion uiExtraLifeTextureRegion;
	private Font font;
	private ChangeableText levelText, scoreText, extraLifeText;
	private int currentScore, currentLvl, currentExtraLife;
	private Sprite uiExtra1LifeSprite, uiExtra2LifeSprite, uiExtra3LifeSprite;

	public HUDSprite(GameActivity gameActivity, TextureRegion pTextureRegion, final ShipSprite ship)
	{
		super(0, Constants.CAMERA_HEIGHT - pTextureRegion.getHeight(), pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion);
		this.gameActivity = gameActivity;
		this.ship = ship;
		this.fontTexture = new Texture(256, 256, TextureOptions.BILINEAR);
		this.font = FontFactory.createFromAsset(fontTexture, getGameActivity(), "font/BAUHS93.TTF", 24, true, Color.WHITE);
		this.uiExtraLifeTexture = new Texture(32, 32, TextureOptions.BILINEAR);
		this.uiExtraLifeTextureRegion = TextureRegionFactory.createFromAsset(uiExtraLifeTexture, gameActivity, "gfx/ui_life_space_duel.png", 0, 0);
		getGameActivity().getEngine().getTextureManager().loadTextures(this.fontTexture, this.uiExtraLifeTexture);
		getGameActivity().getEngine().getFontManager().loadFont(this.font);
	}

	public void onLoadScene()
	{
		this.levelText = new ChangeableText(15, Constants.CAMERA_HEIGHT - 45, this.font, "Lvl 1", 10);
		this.scoreText = new ChangeableText(Constants.CAMERA_WIDTH / 2 - 90, Constants.CAMERA_HEIGHT - 35, this.font, "Score 0", 15);
		this.extraLifeText = new ChangeableText(Constants.CAMERA_WIDTH - 70, Constants.CAMERA_HEIGHT - 45, this.font, "X00", 3);
		this.extraLifeText.setVisible(false);
		this.uiExtra1LifeSprite = new Sprite(Constants.CAMERA_WIDTH - uiExtraLifeTextureRegion.getWidth() - 74, Constants.CAMERA_HEIGHT - uiExtraLifeTextureRegion.getHeight() - 10,
				uiExtraLifeTextureRegion);
		this.uiExtra1LifeSprite.setVisible(false);
		this.uiExtra2LifeSprite = new Sprite(Constants.CAMERA_WIDTH - uiExtraLifeTextureRegion.getWidth() - 42, Constants.CAMERA_HEIGHT - uiExtraLifeTextureRegion.getHeight() - 10,
				uiExtraLifeTextureRegion);
		this.uiExtra2LifeSprite.setVisible(false);
		this.uiExtra3LifeSprite = new Sprite(Constants.CAMERA_WIDTH - uiExtraLifeTextureRegion.getWidth() - 10, Constants.CAMERA_HEIGHT - uiExtraLifeTextureRegion.getHeight() - 10,
				uiExtraLifeTextureRegion);
		this.uiExtra3LifeSprite.setVisible(false);
		// getGameActivity().getGameScene().getLayer(GameScene.HUD_LAYER).addEntity(this.levelText);
		// getGameActivity().getGameScene().getLayer(GameScene.HUD_LAYER).addEntity(this.scoreText);
		getGameActivity().getGameScene().getChild(GameScene.HUD_LAYER).attachChild(this.levelText);
		getGameActivity().getGameScene().getChild(GameScene.HUD_LAYER).attachChild(this.scoreText);
		getGameActivity().getGameScene().getChild(GameScene.HUD_LAYER).attachChild(this.extraLifeText);
		getGameActivity().getGameScene().getChild(GameScene.HUD_LAYER).attachChild(this.uiExtra1LifeSprite);
		getGameActivity().getGameScene().getChild(GameScene.HUD_LAYER).attachChild(this.uiExtra2LifeSprite);
		getGameActivity().getGameScene().getChild(GameScene.HUD_LAYER).attachChild(this.uiExtra3LifeSprite);
		currentLvl = 0;
		currentScore = 0;
		updateHUD(currentScore, currentLvl);
		refreshHUD();
	}

	public void updateHUD(int score, int level)
	{
		updateHUDScore(score);
		updateHUDLevel(level);
	}

	public void updateHUDScore(int score)
	{
		currentScore = score;
		this.scoreText.setText("Score " + currentScore);
	}

	public void updateHUDLevel(int level)
	{
		currentLvl = level;
		this.levelText.setText("Lvl " + currentLvl);
	}

	public int incrementLevel()
	{
		updateHUDLevel(currentLvl + 1);
		return currentLvl;
	}

	public void addToScore(int score)
	{
		updateHUDScore(currentScore + score);
	}

	public GameActivity getGameActivity()
	{
		return gameActivity;
	}

	public void refreshHUD()
	{
		this.extraLifeText.setVisible(false);
		this.uiExtra1LifeSprite.setVisible(false);
		this.uiExtra2LifeSprite.setVisible(false);
		this.uiExtra3LifeSprite.setVisible(false);
		if((this.currentExtraLife = ship.getExtraLife()) > 3)
		{
			this.extraLifeText.setText("x" + this.currentExtraLife);
			this.extraLifeText.setVisible(true);
			this.uiExtra1LifeSprite.setVisible(true);
			this.uiExtra2LifeSprite.setVisible(false);
			this.uiExtra3LifeSprite.setVisible(false);
		}
		else
		{
			switch (this.currentExtraLife)
			{
				case 3:
					this.uiExtra1LifeSprite.setVisible(true);
				case 2:
					this.uiExtra2LifeSprite.setVisible(true);
				case 1:
					this.uiExtra3LifeSprite.setVisible(true);
			}
		}
	}

	@Override
	public void reset()
	{
		super.reset();
		if(this.levelText == null || this.scoreText == null)
			onLoadScene();
		this.levelText.setText("Lvl 001");
		this.scoreText.setText("Score 00000000");
		refreshHUD();
	}
}