package com.weaponofchoice.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import com.weaponofchoice.game.util.Constants;
import com.weaponofchoice.game.util.MusicSingleton;

public class WeaponOfChoice extends ApplicationAdapter {
    public static final String TAG = WeaponOfChoice.class.getName();

	TiledMap  tiledMap;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
	Array<String> musicFiles;

	MusicSingleton music;
	int musicfileIndex;
	
	@Override
	public void create () {
//		float width = Gdx.graphics.getWidth(); // TODO: find out how to load 1024 x 768
//		float height = Gdx.graphics.getHeight();
		float width = 1024.0f;
		float height = 768.0f;
//		Gdx.app.log(TAG, "Width: " + String.valueOf(width)); // DEBUG
// 		Gdx.app.log(TAG, "Height: " + String.valueOf(height)); // DEBUG

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		camera.update();
		tiledMap = new TmxMapLoader().load(Constants.LEVEL_MAP);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		musicFiles = getMusicFiles();
		musicfileIndex = 0;
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
			music.setSongVolume(0.2f);
			music.playSong();
		}

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
/*
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
 */
	}
}
