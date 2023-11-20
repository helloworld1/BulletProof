package org.liberty.multi.bulletproof.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import org.liberty.multi.bulletproof.BulletProof;
import org.liberty.multi.bulletproof.actor.StarFieldBackgroundActor;
import org.liberty.multi.bulletproof.util.BackgroundColorGenerator;
import org.liberty.multi.bulletproof.value.PreferenceKeys;


public class MainMenuScreen implements Screen {
    private final BulletProof game;

    private Stage stage;

    private Table table;

    private Label gameTitleLabel;

    private int highLevel;

    private TextButton playButton;

    private TextButton leaderboardButton;

    private TextButton achievementsButton;

    private TextButton soundButton;

    private TextButton aboutButton;

    private TextButton quitButton;

    private StarFieldBackgroundActor starFieldBackgroundActor;

    private final OrthographicCamera camera;

    private Music mainMusic;
    
    public MainMenuScreen(final BulletProof game) {
        this.game = game;

        final Preferences preferences = Gdx.app.getPreferences(PreferenceKeys.SETTING_NAME);
        highLevel = preferences.getInteger(PreferenceKeys.HIGH_SCORE_LEVEL_KEY, 1);

        camera = new OrthographicCamera();
        this.camera.setToOrtho(false, game.viewportWidth, game.viewportHeight);

        starFieldBackgroundActor = new StarFieldBackgroundActor(game, game.viewportWidth, game.viewportHeight);

        mainMusic = game.assetManager.get("audio/main.mp3", Music.class);
        mainMusic.setLooping(true);

        gameTitleLabel = new Label("Bullet Proof", game.skin, "big_white_label");

        playButton = new TextButton("Play", game.skin, "big_button");
        leaderboardButton = new TextButton("Leaderboard", game.skin, "big_button");
        achievementsButton = new TextButton("Achievements", game.skin, "big_button");

        game.soundEnabled = preferences.getBoolean(PreferenceKeys.SOUND_ENABLED_KEY, true);
        if (game.soundEnabled) {
            soundButton =  new TextButton("Sound on", game.skin, "med_button");
        } else {
            soundButton =  new TextButton("Sound off", game.skin, "med_button");
        }

        if (game.soundEnabled) {
            mainMusic.play();
        }

        aboutButton =  new TextButton("About", game.skin, "med_button");
        aboutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.net.openURI("https://github.com/helloworld1/BulletProof");
            }
        });

        quitButton = new TextButton("Quit", game.skin, "big_button");

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new TutorialScreen(game));
                dispose();
            }
        });


        leaderboardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.scoreBoardResolver.viewLeaderboard();
            }
        });

        soundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundEnabled = !game.soundEnabled;
                preferences.putBoolean(PreferenceKeys.SOUND_ENABLED_KEY, game.soundEnabled);
                preferences.flush();

                if (game.soundEnabled) {
                    soundButton.setText("Sound on");
                    mainMusic.play();
                } else {
                    soundButton.setText("Sound off");
                    mainMusic.stop();
                }
            }
        });

        achievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.scoreBoardResolver.viewAchievements();
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage = new Stage();
        stage.setViewport(new StretchViewport(game.viewportWidth, game.viewportHeight));
        stage.getViewport().setCamera(camera);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(starFieldBackgroundActor);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.row();
        table.add(gameTitleLabel).height(300).width(400);
        table.row();
        table.add(playButton).height(85).width(400);
        table.row();
        table.add(leaderboardButton).height(85);
        table.row();
        table.add(achievementsButton).height(85);
        table.row();
        table.add(quitButton).height(85).width(400);
        table.row();
        Table table2 = new Table();
        //table2.setFillParent(true);
        table2.add(soundButton).height(60).width(200).spaceRight(25);
        table2.add(aboutButton).height(60).width(180).spaceLeft(25);
        table.add(table2);

        // Use default handling for back key
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float delta) {
        BackgroundColorGenerator.setBgColor(highLevel);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        game.batch.begin();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        mainMusic.stop();
    }
}
