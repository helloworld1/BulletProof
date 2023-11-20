package org.liberty.multi.bulletproof.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import org.liberty.multi.bulletproof.BulletProof;
import org.liberty.multi.bulletproof.logic.GameEventManager;
import org.liberty.multi.bulletproof.logic.GameRecorder;
import org.liberty.multi.bulletproof.logic.GameRecorder.BulletData;
import org.liberty.multi.bulletproof.logic.GameTimer;
import org.liberty.multi.bulletproof.object.GameInfo;
import org.liberty.multi.bulletproof.sprite.Aircraft;
import org.liberty.multi.bulletproof.sprite.Bullet;
import org.liberty.multi.bulletproof.util.AircraftControlGestureListener;


public class GameScreen implements Screen {
    private final BulletProof game;
    private final OrthographicCamera camera;

    private GameInfo gameInfo;

    private GameEventManager gameEventManager;

    private GameTimer timer;

    private Array<Bullet> bullets;

    private Pool<Bullet> bulletPool =  new Pool<Bullet>()  {
        @Override
        protected Bullet newObject() {
            return new Bullet(game);
        }
    };

    private Aircraft aircraft;

    private Sprite background;

    private ParticleEffect explosionEffect;

    private GameState gameState = GameState.Normal;

    private Sound dingSound;

    private Sound explodeSound;

    private GameRecorder replayRecorder;

    public GameScreen(final BulletProof game, final GameRecorder replayRecorder) {
        this.game = game;

        this.gameInfo = new GameInfo();

        if (replayRecorder != null) {
            this.replayRecorder = replayRecorder;
            replayRecorder.resetRecordPlayingState();
            this.gameState = GameState.Replay;
            this.gameInfo.gameRecorder = replayRecorder;
        } else {
            this.gameState = GameState.Normal;
        }

        timer = new GameTimer();

        this.gameEventManager = new GameEventManager(gameInfo);

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, game.viewportWidth, game.viewportHeight);

        this.bullets = new Array<Bullet>();
        this.aircraft = new Aircraft(game);

        this.background = new Sprite(game.assetManager.get("images/earth-bg.etc1", Texture.class), 307, 512);
        this.background.setSize(game.viewportWidth, game.viewportHeight);

        this.explosionEffect = game.assetManager.get("effects/explosion.p", ParticleEffect.class);

        this.dingSound = game.assetManager.get("audio/ding.mp3", Sound.class);
        this.explodeSound = game.assetManager.get("audio/explode.mp3", Sound.class);

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                // The stage can not be disposed within the loop
                if (Gdx.input.isKeyPressed(Keys.BACK)) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            game.setScreen(new MainMenuScreen(game));
                            dispose();
                        }
                    });
                    return true;
                }
                return false;
            }
        };
        InputProcessor gestureProcessor = new GestureDetector(new AircraftControlGestureListener(this.aircraft, game.viewportWidth, game.viewportHeight));
        InputMultiplexer multiplexer = new InputMultiplexer(backProcessor, gestureProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
    }

    private void spawnBullets(GameEventManager.EventType event) {
        for (int i = bullets.size ; i < gameInfo.getMaxBulletCount(); i++) {
            Bullet bullet = bulletPool.obtain();
            bullet.initRandom(aircraft.getCenterX(), aircraft.getCenterY(), gameInfo.getBulletVelocity());
            gameInfo.gameRecorder.recordBullet(bullet, gameInfo.getGameElapseTimeSec());
            bullets.add(bullet);
        }


        if (event == GameEventManager.EventType.FastBullet) {
            for (int i = 0; i < gameInfo.getMaxBulletCount() / 2; i++) {
                Bullet bullet = new Bullet(game);
                bullet.initRandom(aircraft.getCenterX(), aircraft.getCenterY(), gameInfo.getBulletVelocity() * 1.25f);
                gameInfo.gameRecorder.recordBullet(bullet, gameInfo.getGameElapseTimeSec());
                bullets.add(bullet);
            }
        }

        if (event == GameEventManager.EventType.SuperFastBullet) {
            for (int i = 0; i < gameInfo.getMaxBulletCount() / 2; i++) {
                Bullet bullet = new Bullet(game);
                bullet.initRandom(aircraft.getCenterX(), aircraft.getCenterY(), gameInfo.getBulletVelocity() * 1.66f);
                gameInfo.gameRecorder.recordBullet(bullet, gameInfo.getGameElapseTimeSec());
                bullets.add(bullet);
            }
        }
        if (event == GameEventManager.EventType.UltraFastBullet) {
            for (int i = 0; i < gameInfo.getMaxBulletCount() / 2; i++) {
                Bullet bullet = new Bullet(game);
                bullet.initRandom(aircraft.getCenterX(), aircraft.getCenterY(), gameInfo.getBulletVelocity() * 2f);
                gameInfo.gameRecorder.recordBullet(bullet, gameInfo.getGameElapseTimeSec());
                bullets.add(bullet);
            }
        }
    }

    private void handleBullets() {
        for (int i = 0; i < bullets.size; i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.isOutOfBound()) {
                bullets.removeIndex(i);
                gameInfo.increaseBulletDodgedCount();
                bulletPool.free(bullet);
            }

            // Hit by bullet, game over!
            if (bullet.getBoundingRectangle().overlaps(aircraft.getCollisionBoundingBox())) {
                gameOver();
            }
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        timer.update(delta);

        gameInfo.update(delta);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        this.background.draw(game.batch);
        drawScore();
        GameEventManager.EventType event = gameEventManager.retrieveEvent();

        for (Bullet bullet : bullets) {
            bullet.move(delta);
            bullet.draw(game.batch);
        }

        if (gameState == GameState.Replay) {
            replayBullets();
            replayAircraft();
            this.aircraft.draw(game.batch);
            handleBullets();
        }

        if (gameState == GameState.Normal) {
            spawnBullets(event);
            this.gameInfo.gameRecorder.recordAircraft(this.aircraft.getX(), this.aircraft.getY(), gameInfo.getGameElapseTimeSec());
            this.aircraft.draw(game.batch);
            handleBullets();
        }

        if (gameState == GameState.AircraftExploding) {
            explosionEffect.draw(game.batch, delta);
        }

        playSoundEffect(event);

        game.batch.end();
    }

    private void playSoundEffect(GameEventManager.EventType event) {
        if (event == GameEventManager.EventType.LevelUp && game.soundEnabled) {
            float pitch = (float) Math.min(2.0f, 1f + (gameInfo.getCurrentLevel() - 1.0f) / 20.0f);
            dingSound.play(1, pitch, 0);
        }
    }

    private void drawScore() {
        game.skin.getFont("small_font").draw(game.batch, "Time: " + gameInfo.getGameElapseTimeSecString(), 400, 800);
        game.skin.getFont("small_font").draw(game.batch, "Bullets: " + gameInfo.getBulletDodgedCount(), 400, 780);
    }

    private void gameOver() {
        gameInfo.pause();
        if (gameState == GameState.Replay) {
            gameState = GameState.AircraftExploding;
        } else {
            gameState = GameState.AircraftExploding;
        }
        explosionEffect.reset();
        explosionEffect.setPosition(aircraft.getCenterX(), aircraft.getCenterY());
        explosionEffect.start();

        explodeSound.play();

        timer.schedule(new Runnable() {
			@Override
			public void run() {
				game.setScreen(new GameOverScreen(game, gameInfo));
				dispose();
			}
		}, 0.8f);
    }

    private void replayBullets() {
        Array<BulletData> bulletDataArray = replayRecorder.getBullet(gameInfo.getGameElapseTimeSec());

        if (bulletDataArray != null) {
            for (BulletData bulletData : bulletDataArray) {
                Bullet bullet = bulletPool.obtain();
                bullet.init(bulletData.getX(), bulletData.getY(), bulletData.getVelocityX(), bulletData.getVelocityY());
                bullets.add(bullet);
            }
        }
    }

    private void replayAircraft() {
        Vector3 aircraftPosition = replayRecorder.getAircraftPosition(gameInfo.getGameElapseTimeSec());
        if (aircraftPosition != null) {
            aircraft.setX(aircraftPosition.x);
            aircraft.setY(aircraftPosition.y);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        timer.start();
    }

    @Override
    public void hide() {
        timer.stop();
    }

    @Override
    public void resume() {
        timer.start();
    }

    @Override
    public void pause() {
        gameInfo.pause();
        timer.stop();
    }

    @Override
    public void dispose() {
        this.dingSound.stop();
        this.explodeSound.stop();
    }

    private enum GameState {
        Replay,
        Normal,
        AircraftExploding;
    }
}

