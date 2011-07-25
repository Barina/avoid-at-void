package avoid.Within.Void;

import java.io.IOException;
import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class GameSoundsFactory
{
	public static final short MUSIC_THEME_LOOP = 0, SOUND_BIG_BLAST = 1, SOUND_GUN_SHOT = 2, SOUND_GUN_LOOP = 3, SOUND_EXPLODE = 4;
	protected static Music themeLoopMusic;
	protected static Sound bigBlastSound, gunShotSound, gunLoopSound, explodeSound;

	public static synchronized void onLoadResources(BaseGameActivity baseGameActivity)
	{
		try
		{
			themeLoopMusic = MusicFactory.createMusicFromAsset(baseGameActivity.getEngine().getMusicManager(), baseGameActivity, "mfx/theme_loop.ogg");
			themeLoopMusic.setLooping(false);
			themeLoopMusic.setOnCompletionListener(new OnCompletionListener()
			{
				@Override
				public void onCompletion(MediaPlayer mp)
				{
					play(MUSIC_THEME_LOOP);
				}
			});
		}
		catch(final IOException e)
		{
			Debug.e("Error - " + e.getMessage(), e);
		}
		try
		{
			bigBlastSound = SoundFactory.createSoundFromAsset(baseGameActivity.getEngine().getSoundManager(), baseGameActivity, "mfx/explosion_fragments.ogg");
		}
		catch(IOException e)
		{
			Debug.e("Error - " + e.getMessage(), e);
		}
		try
		{
			gunShotSound = SoundFactory.createSoundFromAsset(baseGameActivity.getEngine().getSoundManager(), baseGameActivity, "mfx/gun_shot.ogg");
		}
		catch(IOException e)
		{
			Debug.e("Error - " + e.getMessage(), e);
		}
		try
		{
			gunLoopSound = SoundFactory.createSoundFromAsset(baseGameActivity.getEngine().getSoundManager(), baseGameActivity, "mfx/gun_loop.ogg");
			gunLoopSound.setLooping(true);
		}
		catch(IOException e)
		{
			Debug.e("Error - " + e.getMessage(), e);
		}
		try
		{
			explodeSound = SoundFactory.createSoundFromAsset(baseGameActivity.getEngine().getSoundManager(), baseGameActivity, "mfx/blast_soft.ogg");
		}
		catch(IOException e)
		{
			Debug.e("Error - " + e.getMessage(), e);
		}
	}

	public static synchronized void pauseAll()
	{
		if(themeLoopMusic.isPlaying())
			themeLoopMusic.pause();
		bigBlastSound.pause();
		gunShotSound.pause();
		gunLoopSound.pause();
		explodeSound.pause();
	}

	public static synchronized void resumeAll()
	{
		// themeLoopMusic.resume();
		bigBlastSound.resume();
		gunShotSound.resume();
		gunLoopSound.resume();
		explodeSound.resume();
	}

	public static synchronized void stopAll()
	{
		if(themeLoopMusic.isPlaying())
			themeLoopMusic.stop();
		bigBlastSound.stop();
		gunShotSound.stop();
		gunLoopSound.stop();
		explodeSound.stop();
	}

	public static synchronized void play(short id)
	{
		switch (id)
		{
			case MUSIC_THEME_LOOP:
				stop(MUSIC_THEME_LOOP);
				themeLoopMusic.play();
				break;
			case SOUND_BIG_BLAST:
				bigBlastSound.play();
				break;
			case SOUND_GUN_SHOT:
				gunShotSound.play();
				break;
			case SOUND_GUN_LOOP:
				gunLoopSound.play();
				break;
			case SOUND_EXPLODE:
				explodeSound.play();
		}
	}

	public static synchronized void stop(short id)
	{
		switch (id)
		{
			case MUSIC_THEME_LOOP:
				if(themeLoopMusic.isPlaying())
					themeLoopMusic.stop();
				break;
			case SOUND_BIG_BLAST:
				bigBlastSound.stop();
				break;
			case SOUND_GUN_SHOT:
				gunShotSound.stop();
				break;
			case SOUND_GUN_LOOP:
				gunLoopSound.stop();
				break;
			case SOUND_EXPLODE:
				explodeSound.stop();
		}
	}

	public static synchronized void pause(short id)
	{
		switch (id)
		{
			case MUSIC_THEME_LOOP:
				if(themeLoopMusic.isPlaying())
					themeLoopMusic.pause();
				break;
			case SOUND_BIG_BLAST:
				bigBlastSound.pause();
				break;
			case SOUND_GUN_SHOT:
				gunShotSound.pause();
				break;
			case SOUND_GUN_LOOP:
				gunLoopSound.pause();
				break;
			case SOUND_EXPLODE:
				explodeSound.pause();
		}
	}

	public static synchronized void resume(short id)
	{
		switch (id)
		{
			case MUSIC_THEME_LOOP:
				themeLoopMusic.resume();
				break;
			case SOUND_BIG_BLAST:
				bigBlastSound.resume();
				break;
			case SOUND_GUN_SHOT:
				gunShotSound.resume();
				break;
			case SOUND_GUN_LOOP:
				gunLoopSound.resume();
				break;
			case SOUND_EXPLODE:
				explodeSound.resume();
		}
	}

	public static synchronized Sound getSound(int id)
	{
		switch (id)
		{
			case SOUND_BIG_BLAST:
				return bigBlastSound;
			case SOUND_GUN_SHOT:
				return gunShotSound;
			case SOUND_GUN_LOOP:
				return gunLoopSound;
			case SOUND_EXPLODE:
				return explodeSound;
		}
		return null;
	}

	public static synchronized Music getMusic(int id)
	{
		switch (id)
		{
			case MUSIC_THEME_LOOP:
				return themeLoopMusic;
		}
		return null;
	}
}
