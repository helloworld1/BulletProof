package org.liberty.multi.bulletproof;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;


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

    private TextButton signInOutButton;

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

        if (preferences.getBoolean(PreferenceKeys.ALWAYS_SIGN_IN_KEY, false)) {
            game.scoreBoardResolver.signIn();
        }

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

        // This value is also used to determine the behaviro of the button
        final boolean alwaysSignIn = preferences.getBoolean(PreferenceKeys.ALWAYS_SIGN_IN_KEY, false);
        if (alwaysSignIn) {
            signInOutButton =  new TextButton("Sign out", game.skin, "med_button");
        } else {
            signInOutButton =  new TextButton("Sign in", game.skin, "med_button");
        }

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

                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        signInOutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (alwaysSignIn) {
                    preferences.putBoolean(PreferenceKeys.ALWAYS_SIGN_IN_KEY, false);
                    preferences.flush();
                    game.scoreBoardResolver.signOut();
                } else {
                    preferences.putBoolean(PreferenceKeys.ALWAYS_SIGN_IN_KEY, true);
                    preferences.flush();
                    game.scoreBoardResolver.signIn();
                }

                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        achievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.scoreBoardResolver.viewAchievements();
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
        table.add(playButton).height(100).width(400);
        table.row();
        table.add(leaderboardButton).height(100);
        table.row();
        table.add(achievementsButton).height(100);

        table.row();
        Table table2 = new Table();
        //table2.setFillParent(true);
        table2.add(soundButton).height(60).width(200).spaceRight(25);
        table2.add(signInOutButton).height(60).width(180).spaceLeft(25);
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
