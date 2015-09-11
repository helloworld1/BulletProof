package org.liberty.multi.bulletproof.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.liberty.multi.bulletproof.BulletProof;

public class StarFieldBackgroundActor extends Actor {

    private BulletProof game;

    private Sprite backgroundSprite;

    private Sprite backgroundSprite2;

    private float scrollTimer = 0.0f;

    private float scrollTimer2 = 0.0f;

    private ShaderProgram shader;

    public StarFieldBackgroundActor(BulletProof game, float viewportWidth, float viewportHeight) {
        Texture starFieldTexture = game.assetManager.get("images/star-field.etc1", Texture.class);
        starFieldTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

        // Bigger stars and scrolls faster
        backgroundSprite = new Sprite(starFieldTexture, 0, 0, 240, 400);

        // Smaller stars and scrolls slower
        backgroundSprite2 = new Sprite(starFieldTexture, 0, 0, 614, 1024);

        backgroundSprite.setSize(viewportWidth * 2, viewportHeight);

        backgroundSprite2.setSize(viewportWidth * 2, viewportHeight);

        shader = game.assetManager.get("shaders/texture_alpha", ShaderProgram.class);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setShader(shader);
        shader.setUniformf("textureAlpha", 1.0f);
        backgroundSprite.draw(batch);
        batch.flush();

        shader.setUniformf("textureAlpha", 0.6f);
        backgroundSprite2.draw(batch);
        batch.setShader(null);
    }

    public void act(float delta) {
        // delta / ? controls the speed of the scrolling
        scrollTimer = (scrollTimer + delta / 50) % 1.0f;
        scrollTimer2 = (scrollTimer2 + delta / 19) % 1.0f;

        backgroundSprite.setU(scrollTimer);
        backgroundSprite.setU2(scrollTimer + 0.5f);
        
        backgroundSprite2.setU(scrollTimer);
        backgroundSprite2.setU2(scrollTimer + 1.0f);
    }
}

