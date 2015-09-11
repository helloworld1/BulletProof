package org.liberty.multi.bulletproof;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BulletProof extends Game {
    public SpriteBatch batch;

    public float viewportWidth;

    public float viewportHeight;

    public ScoreBoardResolver scoreBoardResolver;
    
    public ShareResolver shareResolver;

    public Skin skin;

    public AssetManager assetManager;

    // The value is set on the main menu screen
    public boolean soundEnabled = true;

    public BulletProof(ScoreBoardResolver scoreBoardResolver, ShareResolver shareResolver) {
        this.scoreBoardResolver = scoreBoardResolver;
        this.shareResolver = shareResolver;
    }
    
    @Override
    public void create() {      
        this.viewportWidth = 480;
        this.viewportHeight = 800;

        batch = new SpriteBatch();
        batch.enableBlending();

        assetManager = new AssetManager();
        assetManager.setLoader(ShaderProgram.class, new ShaderAssetLoader(new InternalFileHandleResolver()));

        assetManager.load("images/tut.etc1", Texture.class);
        assetManager.load("images/f-15f.png", Texture.class);
        assetManager.load("images/earth-bg.etc1", Texture.class);
        assetManager.load("images/bullet.png", Texture.class);
        assetManager.load("images/star-field.etc1", Texture.class);
        assetManager.load("effects/explosion.p", ParticleEffect.class);
        assetManager.load("audio/main.mp3", Music.class);
        assetManager.load("audio/explode.mp3", Sound.class);
        assetManager.load("audio/ding.mp3", Sound.class);

        assetManager.load("shaders/texture_alpha", ShaderProgram.class);

        this.setScreen(new LoadingScreen(this));
    }


    @Override
    public void dispose() {
        skin.getFont("big_font").dispose();
        skin.getFont("medium_font").dispose();
        skin.getFont("small_font").dispose();
        skin.dispose();
    }

    @Override
    public void render() {      
        super.render();
    }
}
