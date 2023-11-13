package org.liberty.multi.bulletproof;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.liberty.multi.bulletproof.BulletProof;
import org.liberty.multi.bulletproof.resolver.DummyScoreBoard;
import org.liberty.multi.bulletproof.resolver.DummyShareResolver;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("BulletProof");
		config.setWindowedMode(480, 800);

		new Lwjgl3Application(new BulletProof(new DummyScoreBoard(), new DummyShareResolver()), config);
	}
}
