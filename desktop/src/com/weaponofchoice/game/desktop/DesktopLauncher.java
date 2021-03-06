package com.weaponofchoice.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.weaponofchoice.game.WeaponOfChoice;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Weapon Of Choice";
		config.useGL30 = true;
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new WeaponOfChoice(), config);
	}
}
