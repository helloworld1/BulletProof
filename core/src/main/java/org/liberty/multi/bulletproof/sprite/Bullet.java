package org.liberty.multi.bulletproof.sprite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;
import org.liberty.multi.bulletproof.BulletProof;

public class Bullet extends Sprite implements Poolable {

    private float velocityX;

    private float velocityY;

    private BulletProof game;

    public Bullet(BulletProof game) {
        super(game.assetManager.get("images/bullet.png", Texture.class), 8, 8);
        this.game = game;
    }

    public void init(float x, float y, float velocityX, float velocityY) {
        setX(x);
        setY(y);

        this.velocityX = velocityX;
        this.velocityY = velocityY;

        float velocity = (float) Math.sqrt(velocityX * velocityX+ velocityY * velocityY);
        if (velocity > 300) {
            setColor(Color.BLUE);
        } else if (velocity > 250) {
            setColor(Color.GREEN);
        } else if (velocity > 200) {
            setColor(Color.YELLOW);
        } else if (velocity > 150) {
            setColor(Color.CYAN);
        } else {
            setColor(Color.WHITE);
        }
    }

    public void initRandom(float targetX, float targetY, float velocity) {
        int side = MathUtils.random(0, 4);

        float x = 0f, y = 0f, velocityX = 0f, velocityY = 0f;

        // Left side
        if (side == 0) {
            x = 0;
            y = MathUtils.random(0f, game.viewportHeight);
        }

        // Top side
        if (side == 1) {
            x = MathUtils.random(0f, game.viewportWidth);
            y = game.viewportHeight;
        }

        // Right side
        if (side == 2) {
            x = game.viewportWidth;
            y = MathUtils.random(0f, game.viewportHeight);
        }

        // Bottom side
        if (side == 3) {
            x = MathUtils.random(0f, game.viewportWidth);
            y = 0;
        }

        float dx = targetX - x;
        float dy = targetY - y;

        // Add small randomness
        dx = dx + MathUtils.random(0f, dx / 10);
        dy = dy + MathUtils.random(0f, dx / 10);

        velocityX = dx / (float)Math.sqrt(dx * dx + dy * dy) * velocity;
        velocityY = dy / (float)Math.sqrt(dx * dx + dy * dy) * velocity;
        init(x, y, velocityX, velocityY);
    }
    
    public void move(float delta) {
        this.setX(this.getX() + this.velocityX * delta);
        this.setY(this.getY() + this.velocityY * delta);
    }

    public boolean isOutOfBound() {
        return (this.getX() < 0 || this.getX() > game.viewportWidth || this.getY() < 0 || this.getY() > game.viewportHeight);
    }

    /**
     * @return the velocityX
     */
    public float getVelocityX() {
        return velocityX;
    }

    /**
     * @return the velocityY
     */
    public float getVelocityY() {
        return velocityY;
    }

    @Override
    public void reset() {
        // Nothing
    }
}
