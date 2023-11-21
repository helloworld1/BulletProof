package org.liberty.multi.bulletproof.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.liberty.multi.bulletproof.BulletProof;

public class StarFieldBackgroundActor extends Actor {

    private final Sprite backgroundSprite;

    private final ShaderProgram shader;

    private float scrollTimer1 = 0.0f;

    private float scrollTimer2 = 0.0f;

    public StarFieldBackgroundActor(BulletProof game, float viewportWidth, float viewportHeight) {
        Texture starFieldTexture = game.assetManager.get("images/star-field.png", Texture.class);
        starFieldTexture.setWrap(TextureWrap.MirroredRepeat, TextureWrap.MirroredRepeat);

        backgroundSprite = new Sprite(starFieldTexture);

        shader = game.assetManager.get("shaders/texture_alpha", ShaderProgram.class);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setShader(shader);
        shader.setUniformf("textureDimensionScaling1", 0.5f);
        shader.setUniformf("textureDimensionScaling2", 1.5f);
        shader.setUniformf("textureAlphaScaling1", 0.5f);
        shader.setUniformf("textureAlphaScaling2", 1.0f);
        shader.setUniformf("xOffset1", scrollTimer1);
        shader.setUniformf("xOffset2", scrollTimer2);
        backgroundSprite.draw(batch);
        batch.setShader(null);
    }

    @Override
    public void act(float delta) {
        // delta / ? controls the speed of the scrolling
        scrollTimer1 = scrollTimer1 + delta / 50;
        scrollTimer2 = scrollTimer2 + delta / 19;
    }
}

