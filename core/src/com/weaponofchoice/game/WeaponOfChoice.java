package com.weaponofchoice.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Timer;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.weaponofchoice.game.util.Constants;
import com.weaponofchoice.game.util.MusicSingleton;

public class WeaponOfChoice extends ApplicationAdapter {
    public static final String TAG = WeaponOfChoice.class.getName();

    // TODO 1: collision identification and detection - working with Thor to add coordinate information
	// TODO 2: enemy spawning
	// TODO 3: Enemy movement
    // TODO 4: Player Enemy interaction (with sound effects)
	// TODO 5: Player weapon found
	// TODO 6: Player exit
	// TODO 7: Game HUD
	// TODO 8: Win screen
	// TODO 9: player movement smoothing and animation
	// TODO 10: fix music optimization
	// TODO 11: Add song looping
	// TODO 12: Start Screen

    private SpriteBatch playerSpriteBatch;
    private Texture startingPlayerTexture;
    private Texture playerMoveUpTexture;
    private Texture playerMoveDownTexture;
    private Texture playerMoveRightTexture;
    private Texture playerMoveLeftTexture;
    private Sprite player;
	private Vector2 startPosition;

	private Texture enemyStartingTexture;
	private Sprite enemy;
    private float elapsedTime = 0;
   	private boolean enemyRespawn;
   	private Array<Vector2> enemySpawnPositions;
	private SpriteBatch enemySpriteBatch;
	private DelayedRemovalArray<Sprite> enemies;

    private TmxMapLoader loader;
	private TiledMap  tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private float playerMovementSpeed = 10; // TODO: figure out how to make this smooth

	private OrthographicCamera camera;
	private Viewport viewport;

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
		playerSpriteBatch = new SpriteBatch(); // Needed with atlas for player animation

		loader = new TmxMapLoader();
		tiledMap = loader.load(Constants.LEVEL_MAP);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, playerSpriteBatch);

		camera.setToOrtho(false, width, height);
		camera.update();

		playerSpriteBatch = new SpriteBatch();
		startingPlayerTexture = new Texture(Gdx.files.internal(Constants.PLAYER_STARTING_SPRITE)); //TODO: use the packed atlas
		playerMoveUpTexture = new Texture(Gdx.files.internal(Constants.PLAYER_MOVE_UP_SPRITE)); // Sprite anim sheets don't work :(
		playerMoveDownTexture = new Texture(Gdx.files.internal(Constants.PLAYER_MOVE_DOWN_SPRITE));
//		playerMoveRightTexture = new Texture(Gdx.files.internal(Constants.PLAYER_MOVE_RIGHT_SPRITE));
		playerMoveLeftTexture = new Texture(Gdx.files.internal(Constants.PLAYER_MOVE_LEFT_SPRITE));
		player = new Sprite(startingPlayerTexture);

		enemyStartingTexture = new Texture(Gdx.files.internal(Constants.ENEMY_STARTING_SPRITE));
		enemySpawnPositions = generateEnemySpawnPoints();
		enemySpriteBatch = new SpriteBatch();
		enemies = new DelayedRemovalArray<Sprite>();

		// Set music
		musicFiles = getMusicFiles();
		musicfileIndex = 0;
		music = new MusicSingleton(Constants.MUSIC);
		music.setSongVolume(0.4f);
		music.playSong();

		processMapMetadata();
		player.setPosition(158.947f, 126.316f); // TODO: get start position from tmx file
	}

	private Array<Vector2> generateEnemySpawnPoints() {
		// TODO: Change this to the doors for enemy spawn points
		Array<Vector2> retval = new Array<Vector2>();
		float middleWidth =  350.2f; // Gdx.graphics.getWidth() / 2;
		float middleheight = 350.1f; // Gdx.graphics.getHeight() / 2;

		Vector2 topSpawnLoc = new Vector2();
		float topSpawnLocY = 358.9f; // Gdx.graphics.getHeight() - 32;
		topSpawnLoc.set(middleWidth, topSpawnLocY);
		retval.add(topSpawnLoc);

		Vector2 bottomSpawnLoc = new Vector2();
		float bottomSpawnLocY = 126.316f; // Gdx.graphics.getHeight() + 32;
		bottomSpawnLoc.set(middleWidth, bottomSpawnLocY);
		retval.add(bottomSpawnLoc);

		Vector2 eastSpawnLoc = new Vector2();
		float eastSpawnLocX = 458.1f; // Gdx.graphics.getWidth() - 32;
		bottomSpawnLoc.set(eastSpawnLocX, middleheight);
		retval.add(eastSpawnLoc);

		// TODO: West spawn location

		return retval;
	}

	@Override
	public void dispose() {
		tiledMap.dispose();
		playerSpriteBatch.dispose();
		if (music != null) {
			music.disposeSong();
		}
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
		elapsedTime += Gdx.graphics.getDeltaTime();
		float everyTwenty = elapsedTime / 10 % 2; // Is this milliseconds or seconds?
		if (everyTwenty >= 1) {
			enemyRespawn = true;
			// TODO: Find doors
			int randSpawnLocIndex = MathUtils.random(0, enemySpawnPositions.size - 1);

			Sprite newEnemy = new Sprite(enemyStartingTexture);
			Vector2 enemySpawnLoc = enemySpawnPositions.get(randSpawnLocIndex);
			newEnemy.setPosition(enemySpawnLoc.x, enemySpawnLoc.y);
			enemies.add(newEnemy);
		}

		// TODO: Move enemy

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
			music = new MusicSingleton(song); // TODO: Add set song to the singleton
			music.setSongVolume(0.4f);
			music.playSong();
			musicfileIndex++;
		}

        //TODO: Crude player movement, abstract into a player class
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
        	float positionX = player.getX();
        	float positionY = player.getY();
        	player.setPosition(positionX, positionY + playerMovementSpeed);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			float positionX = player.getX();
			float positionY = player.getY();
			player.setPosition(positionX, positionY - playerMovementSpeed);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			float positionX = player.getX();
			float positionY = player.getY();
			player.setPosition(positionX + playerMovementSpeed, positionY);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			float positionX = player.getX();
			float positionY = player.getY();
			player.setPosition(positionX - playerMovementSpeed, positionY);
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		if(enemies.size > 0) {
			enemySpriteBatch.setProjectionMatrix(camera.combined);
		    for (Sprite enemy : enemies) {
				enemySpriteBatch.begin();
		    	// TODO: make enemy movement random and follow player if found
		    	if (enemy.getX() + 10 <= Gdx.graphics.getWidth() - 32) {
					enemy.setPosition(enemy.getX() - 10, enemy.getY());
					enemy.draw(enemySpriteBatch);
					continue;
				} else if(enemy.getX() <= 32) {
					enemy.setPosition(enemy.getX() + 10, enemy.getY());
					enemy.draw(enemySpriteBatch);
				} else if(enemy.getY() + 10 <= Gdx.graphics.getHeight() - 32) {
					enemy.setPosition(enemy.getX(), enemy.getY() - 10);
					enemy.draw(enemySpriteBatch);
				} else if(enemy.getY() <= 32) {
					enemy.setPosition(enemy.getX(), enemy.getY() + 10);
					enemy.draw(enemySpriteBatch);
				}
				enemy.draw(enemySpriteBatch);
				enemySpriteBatch.end();
			}
		}

		playerSpriteBatch.setProjectionMatrix(camera.combined);
		playerSpriteBatch.begin();
		player.draw(playerSpriteBatch);
		playerSpriteBatch.end();
	}

	private void processMapMetadata() {
		int mapLayers = tiledMap.getLayers().getCount();
		for (int i = 0; i < mapLayers; i++) {
			MapLayer mapLayer = tiledMap.getLayers().get(i);
			String name = mapLayer.getName();
			MapProperties mapProperties = mapLayer.getProperties();
			MapObjects mapObjects = mapLayer.getObjects();
			if (name.equals("wall") || name.equals("collision")) {
			    // TODO: set a collision identifier - and you need to look for something else instead of wall
				Gdx.app.log(TAG, "Found a wall or something impassable, now what?");
			}
			if (name.equals("StartingObjects")) {
			    MapObjects startingObjects = mapLayer.getObjects();
			    for(MapObject mapObject : startingObjects) {
			    	if (mapObject.getName().equals("PlayerStart")) {
			    		MapProperties playerStartProperties = mapObject.getProperties();
			    		// TODO: get the player start position and save it in a global to be retrieved and used
					}
				}

			}
		}
/*
		for (MapObject object : objects) {
			String name = object.getName();
			String[] parts = name.split("[.]");
			RectangleMapObject rectangleObject = (RectangleMapObject)object;
			Rectangle rectangle = rectangleObject.getRectangle();

			if (name.equals("PlayerStart")) {
				float playerStartX = Float.valueOf(object.getProperties().get("x").toString());
				float playerStartY = Float.valueOf(object.getProperties().get("y").toString());

			}

		}

 */
	}
}

