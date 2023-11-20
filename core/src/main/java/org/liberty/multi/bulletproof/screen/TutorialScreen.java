package org.liberty.multi.bulletproof.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.liberty.multi.bulletproof.BulletProof;

public class TutorialScreen implements Screen {

    private final OrthographicCamera camera;

    private BulletProof game;

    private Sprite tutorial1;

    private Texture tutorial1Texture;

    public TutorialScreen (final BulletProof game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, game.viewportWidth, game.viewportHeight);

        this.tutorial1Texture= game.assetManager.get("images/tut.png", Texture.class);

        tutorial1 = new Sprite(tutorial1Texture, 576, 947);
        tutorial1.setSize(game.viewportWidth, game.viewportHeight);

        Gdx.input.setInputProcessor(new InputProcessor() {

            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer,
                    int button) {
                game.setScreen(new GameScreen(game, null));
                dispose();
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer,
                    int button) {
                return false;
            }

            @Override
            public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        tutorial1.draw(game.batch);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        // Nothing
    }

    @Override
    public void show() {
        // Nothing
    }

    @Override
    public void hide() {
        // Nothing

    }

    @Override
    public void pause() {
        // Nothing

    }

    @Override
    public void resume() {
        // Nothing

    }

    @Override
    public void dispose() {
    }

}

