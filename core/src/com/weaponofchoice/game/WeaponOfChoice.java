package com.weaponofchoice.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.weaponofchoice.game.util.Constants;
import com.weaponofchoice.game.util.MusicSingleton;

public class WeaponOfChoice extends ApplicationAdapter {
    public static final String TAG = WeaponOfChoice.class.getName();

	SpriteBatch batch;
	Texture img;

	TiledMap  tiledMap;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;

	MusicSingleton music;
	
	@Override
	public void create () {
//		float width = Gdx.graphics.getWidth(); // TODO: find out how to load 1024 x 768
//		float height = Gdx.graphics.getHeight();
		float width = 1024.0f;
		float height = 768.0f;
//		Gdx.app.log(TAG, "Width: " + String.valueOf(width)); // DEBUG
// 		Gdx.app.log(TAG, "Height: " + String.valueOf(height)); // DEBUG

//		batch = new SpriteBatch();
// 		img = new Texture("badlogic.jpg");

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		camera.update();
		tiledMap = new TmxMapLoader().load(Constants.LEVEL_MAP);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
	}

	@Override
	public void render () {
	    // Sound loading
		music = new MusicSingleton(Constants.MUSIC);
		music.setSongVolume(0.2f);
		music.playSong();

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
