package com.weaponofchoice.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.weaponofchoice.game.util.Constants;
import com.weaponofchoice.game.util.MusicSingleton;

public class WeaponOfChoice extends ApplicationAdapter {
    public static final String TAG = WeaponOfChoice.class.getName();

    private static final float SCALE = 0.2916f;
    private static final int VIRTUAL_WIDTH = (int)(1024 * SCALE);
    private static final int VIRTUAL_HEIGHT = (int)(768 * SCALE);

    private static final float CAMERA_SPEED = 100.0f;

    SpriteBatch spriteBatch;
    Texture texture;
    Sprite player;
    Vector2 startPosition;

    TmxMapLoader loader;
	TiledMap  tiledMap;
	TiledMapRenderer tiledMapRenderer;

	OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;

	private Vector2 direction;

	Array<String> musicFiles;
	MusicSingleton music;
	int musicfileIndex;

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

		camera.setToOrtho(false, width, height);
		camera.update();

// 		tiledMap = new TmxMapLoader().load(Constants.LEVEL_MAP);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
		direction = new Vector2();

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
		 */
		Array<String> retval = new Array<String>();
		FileHandle dirHandle;
		dirHandle = Gdx.files.internal("music");
		for(FileHandle entry: dirHandle.list()) {
			retval.add(entry.path());
		}
		return retval;
	}

	@Override
	public void render () {
	    // Sound loading
		// TODO: Set audio sampling and integration with streaming (Ideally we need it to be 44.1khz, 16 bit):
			//https://github.com/libgdx/libgdx/wiki/Streaming-music
			//https://github.com/libgdx/libgdx/wiki/Playing-pcm-audio
			//https://libgdx.badlogicgames.com/ci/nightlies/docs/api/
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
        	if(music != null) {
				music.disposeSong();
			}
			musicfileIndex++;
			if (musicfileIndex >= musicFiles.size) {
				musicfileIndex = 0;
			}
			String song = musicFiles.get(musicfileIndex);
			Gdx.app.log(TAG, "Playing song: " + song);
			music = new MusicSingleton(song);
			music.setSongVolume(0.4f);
			music.playSong();
		}

        // Crude player movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
        	float positionX = player.getX();
        	float positionY = player.getY();
        	player.setPosition(positionX, positionY + 10);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			float positionX = player.getX();
			float positionY = player.getY();
			player.setPosition(positionX, positionY - 10);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			float positionX = player.getX();
			float positionY = player.getY();
			player.setPosition(positionX + 10, positionY);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			float positionX = player.getX();
			float positionY = player.getY();
			player.setPosition(positionX - 10, positionY);
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
