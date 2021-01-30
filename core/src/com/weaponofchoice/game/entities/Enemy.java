package com.weaponofchoice.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import com.weaponofchoice.game.util.Utils;

public class Enemy {

    public Vector2 position;
    public int health;
//    private Direction direction; //TODO

    public Enemy() {
        position = new Vector2(); // TODO

    }

    public void update(float delta) {

    }

    public void render(SpriteBatch batch) {
        final TextureRegion region = new TextureRegion(); // TODO set from assets
        Utils.drawTextureRegion(batch, region, position); // TODO add enemy center

    }

}
