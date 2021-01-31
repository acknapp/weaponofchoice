package com.weaponofchoice.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.weaponofchoice.game.util.Constants;
import com.weaponofchoice.game.util.MusicSingleton;

public class WeaponOfChoice extends ApplicationAdapter {
    public static final String TAG = WeaponOfChoice.class.getName();

    // TODO 1: collision identification and detection
    // TODO 2: player movement smoothing and animation
	// TODO 3: enemy spawning
    // TODO 4: fix package crash when rotating music
	// TODO 5: fix music optimization

    private SpriteBatch spriteBatch;
    private Texture texture;
    private Sprite player;
    private Vector2 startPosition;

    private TmxMapLoader loader;
	private TiledMap  tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private float playerMovementSpeed = 10; // TODO: figure out how to make this smooth

	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;

	private Array<String> musicFiles;
	private MusicSingleton music;
	private int musicfileIndex;

	@Override
	public void create () {
//		float width = Gdx.graphics.getWidth(); // TODO: find out how to resize the application to 1024 x 768
//		float height = Gdx.graphics.getHeight();
		float width = 1024.0f;
		float height = 768.0f;
//		Gdx.app.log(TAG, "Width: " + String.valueOf(width)); // DEBUG
// 		Gdx.app.log(TAG, "Height: " + String.valueOf(height)); // DEBUG

		camera = new OrthographicCamera();
		viewport = new FitViewport(width, height, camera);
		batch = new SpriteBatch();

		loader = new TmxMapLoader();
		tiledMap = loader.load(Constants.LEVEL_MAP);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);

		camera.setToOrtho(false, width, height);
		camera.update();

		spriteBatch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal(Constants.PLAYER_STARTING_SPRITE)); //TODO: use the packed atlas
		player = new Sprite(texture);
		Object whatIsThis = tiledMap.getProperties().get("PlayerStart"); //TODO how to get this?
		player.setPosition(158.947f, 126.316f); // TODO: get start position from tmx file


		musicFiles = getMusicFiles();
		musicfileIndex = 0;
	}

	@Override
	public void dispose() {
		tiledMap.dispose();
	}

	@Override
	public void resize(int width, int height) {
	    viewport.update(width, height);
	}

	private Array<String> getMusicFiles() {
		/**
		 * Get all the music files from the assets folder
		 * Dynamic but it doesn't work for packaged distributions
		 */
		Array<String> retval = new Array<String>();
		// FileHandle dirHandle = Gdx.files.internal("music/");
		String[] allMusicFiles = {
				"music/GGJAM - ACTION 1.mp3",
				"music/GGJAM - AMBIENT 1.mp3",
				"music/GGJAM - CHILL 1.mp3",
				"music/GGJAM - CHILL_EPIC THEME IDEA 2 1.mp3",
				"music/GGJAM - CHILL_EPIC THEME IDEA 2.mp3",
				"music/GGJAM - DRONE 1.mp3",
				"music/GGJAM - DRONE THEME.mp3",
				"music/GGJAM - RETRO MESS.mp3",
				"music/Groove idea.mp3"
		};
		for(String entry: allMusicFiles) {
			retval.add(entry);
		}
		/*
		for(FileHandle entry: dirHandle.list()) {
			retval.add(entry.path());
		}
		 */
		return retval;
	}

	@Override
	public void render () {
	    // Sound loading
		// TODO: Set audio sampling and integration with streaming (Ideally we need it to be 44.1khz, 16 bit):
			//https://github.com/libgdx/libgdx/wiki/Streaming-music
			//https://github.com/libgdx/libgdx/wiki/Playing-pcm-audio
			//https://libgdx.badlogicgames.com/ci/nightlies/docs/api/
        	// or maybe need to use SongListenser? (See Libgdx cookbook)
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
        	if(music != null) {
				music.disposeSong();
			}
			if (musicfileIndex >= musicFiles.size) {
				musicfileIndex = 0;
			}
			String song = musicFiles.get(musicfileIndex);
			Gdx.app.log(TAG, "Playing song: " + song);
			music = new MusicSingleton(song);
			music.setSongVolume(0.4f);
			music.playSong();
			musicfileIndex++;
		}
        /*
		music = new MusicSingleton(Constants.MUSIC);
		music.setSongVolume(0.4f);
		music.playSong();

         */

        // Crude player movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        	float positionX = player.getX();
        	float positionY = player.getY();
        	player.setPosition(positionX, positionY + playerMovementSpeed);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			float positionX = player.getX();
			float positionY = player.getY();
			player.setPosition(positionX, positionY - playerMovementSpeed);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			float positionX = player.getX();
			float positionY = player.getY();
			player.setPosition(positionX + playerMovementSpeed, positionY);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			float positionX = player.getX();
			float positionY = player.getY();
			player.setPosition(positionX - playerMovementSpeed, positionY);
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		player.draw(spriteBatch);
		spriteBatch.end();
	}
}
