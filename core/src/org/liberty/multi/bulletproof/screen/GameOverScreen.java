package org.liberty.multi.bulletproof.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import org.liberty.multi.bulletproof.BulletProof;
import org.liberty.multi.bulletproof.object.GameInfo;
import org.liberty.multi.bulletproof.util.BackgroundColorGenerator;
import org.liberty.multi.bulletproof.util.GameStringUtils;
import org.liberty.multi.bulletproof.value.PreferenceKeys;

public class GameOverScreen implements Screen {

    private final BulletProof game;

    private Stage stage;

    private Table table;

    private GameInfo stat;

    private TextButton retryButton;
    private TextButton shareButton;
    private TextButton replayButton;
    private TextButton mainScreenButton;
    private Label gameOverLabel;
    private Label timeLabel;
    private Label highTimeLabel;
    private Label bulletCountLabel;
    private Label highBulletCountLabel;

    private Preferences preferences = Gdx.app.getPreferences(PreferenceKeys.SETTING_NAME);

    public GameOverScreen(final BulletProof game, final GameInfo stat) {
        this.game = game;
        this.stage = new Stage();
        this.stat = stat;

        this.stage.setViewport(new StretchViewport(game.viewportWidth, game.viewportHeight));

        saveHighscore();
        submitHighscore();

        final String time = stat.getGameElapseTimeSecString();
        final String bulletCount = "" + stat.getBulletDodgedCount();

        String highTime = GameStringUtils.convertElapseTime(preferences.getFloat(PreferenceKeys.HIGH_SCORE_TIME_KEY, 0.0f));

        String highBullet = "" + preferences.getInteger(PreferenceKeys.HIGH_SCORE_BULLET_KEY, 0);


        retryButton = new TextButton("Retry", game.skin, "big_button");

        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, null));
                dispose();
            }
        });

        replayButton = new TextButton("Replay", game.skin, "big_button");

        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game, stat.gameRecorder));
                dispose();
            }
        });

        shareButton = new TextButton("Share", game.skin, "big_button");
        shareButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.shareResolver.share("I am playing Bullet Proof. Survived " + time + "s and dodged " + bulletCount + " bullets. Can you beat me? https://play.google.com/store/apps/details?id=org.liberty.multi.bulletproof");
            }
        });

        mainScreenButton = new TextButton("Main", game.skin, "big_button");
        mainScreenButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        gameOverLabel = new Label("You died", game.skin, "big_red_label");


        timeLabel = new Label("Time: " + time + "s", game.skin, "big_white_label");
        bulletCountLabel = new Label("Bullets: " + bulletCount, game.skin, "big_white_label");
        highTimeLabel = new Label("Best time: " + highTime + "s", game.skin, "med_orange_label");
        highBulletCountLabel = new Label("Best bullets: " + highBullet, game.skin, "med_orange_label");

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(gameOverLabel).height(200);
        table.row();
        table.add(timeLabel).height(80);
        table.row();
        table.add(highTimeLabel).height(60);
        table.row();
        table.add(bulletCountLabel).height(80);
        table.row();
        table.add(highBulletCountLabel).height(60);
        table.row();
        table.add(retryButton).height(80).width(400);
        table.row();
        table.add(replayButton).height(80).width(400);
        table.row();
        table.add(shareButton).height(80).width(400);
        table.row();
        table.add(mainScreenButton).height(80).width(400);

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (Gdx.input.isKeyPressed(Keys.BACK)) {
                    // The stage can not be disposed within the loop
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
        InputMultiplexer multiplexer = new InputMultiplexer(backProcessor, stage);
        Gdx.input.setInputProcessor(multiplexer);
        Gdx.input.setCatchBackKey(true);
    }


    @Override
    public void render(float delta) {
        BackgroundColorGenerator.setBgColor(stat.getCurrentLevel());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        game.batch.end();
    }

    private void saveHighscore() {
        float time = stat.getGameElapseTimeSec();
        float highTime = preferences.getFloat(PreferenceKeys.HIGH_SCORE_TIME_KEY, 0.0f);
        if (time > highTime) {
            preferences.putFloat(PreferenceKeys.HIGH_SCORE_TIME_KEY, time);
        }

        int level = stat.getCurrentLevel();
        int highLevel = preferences.getInteger(PreferenceKeys.HIGH_SCORE_LEVEL_KEY, 1);
        if (level > highLevel) {
            preferences.putInteger(PreferenceKeys.HIGH_SCORE_LEVEL_KEY, level);
        }

        int bullet = stat.getBulletDodgedCount();
        int highBullet = preferences.getInteger(PreferenceKeys.HIGH_SCORE_BULLET_KEY, 0);
        if (bullet > highBullet) {
            preferences.putInteger(PreferenceKeys.HIGH_SCORE_BULLET_KEY, bullet);
        }
        preferences.flush();
    }

    private void submitHighscore() {
        float highTime = preferences.getFloat(PreferenceKeys.HIGH_SCORE_TIME_KEY, 0.0f);
        int highBullet = preferences.getInteger(PreferenceKeys.HIGH_SCORE_BULLET_KEY, 0);

        game.scoreBoardResolver.submitBestTime(highTime);
        game.scoreBoardResolver.submitHighBullet(highBullet);
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

