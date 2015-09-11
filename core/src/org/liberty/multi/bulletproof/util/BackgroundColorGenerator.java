package org.liberty.multi.bulletproof.util;

import com.badlogic.gdx.Gdx;

public class BackgroundColorGenerator {

    public static void setBgColor(int highLevel) {
        if (highLevel % 12 < 2) {
            Gdx.gl.glClearColor(0, 0, 0.1f + 0.1f * highLevel / 12, 1);
            return;
        }

        if (highLevel % 12 < 4) {
            Gdx.gl.glClearColor(0, 0.1f + 0.1f * highLevel / 12, 0, 1);
            return;
        }
        if (highLevel % 12 < 6) {
            Gdx.gl.glClearColor(0.1f + 0.1f * highLevel / 12, 0, 0, 1);
            return;
        }
        if (highLevel % 12 < 8) {
            Gdx.gl.glClearColor(0.0f, 0.07f + 0.07f * (highLevel % 12), 0.07f + 0.07f * (highLevel % 12), 1);
            return;
        }
        if (highLevel % 12 < 10) {
            Gdx.gl.glClearColor(0.07f + 0.07f * (highLevel % 12), 0, 0.07f + 0.07f * (highLevel % 12), 1);
            return;
        }
        if (highLevel % 12 < 12) {
            Gdx.gl.glClearColor(0, 0.07f + 0.07f * (highLevel % 12), 0.07f + 0.07f * (highLevel % 12), 1);
            return;
        }
    }
}
