package org.liberty.multi.bulletproof.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.liberty.multi.bulletproof.BulletProof;
import org.liberty.multi.bulletproof.DummyScoreBoard;
import org.liberty.multi.bulletproof.DummyShareResolver;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "BulletProof";
		cfg.width = 480;
		cfg.height = 800;
		
		new LwjglApplication(new BulletProof(new DummyScoreBoard(), new DummyShareResolver()), cfg);
	}
}
