package org.liberty.multi.bulletproof;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Aircraft extends Sprite {

    private BulletProof game;

    /**
     * Use a customized bounding box for collision detection,
     * the bounding box is smaller than the actual sprite to match
     * the aircraft easily.
     */
    private Rectangle customBoundingBox;

    public Aircraft(BulletProof game) {
        super(game.assetManager.get("images/f-15f.png", Texture.class));
        this.game = game;
        customBoundingBox = new Rectangle();
        setBounds(10, 5, 20, 45);
        setSize(40,50);
        setX(game.viewportWidth / 2);
        setY(game.viewportHeight / 2);
    }

    public void move(float deltaX, float deltaY) {
        float resultX = getX() + deltaX;
        float resultY = getY() + deltaY;
        if (resultX >= 0 && resultX <= game.viewportWidth - getWidth()) {
            setX(resultX);
        }

        if (resultY >= 0 && resultY <= game.viewportHeight - getWidth()) {
            setY(resultY);
        }

    }

    /**
     * Return a smaller bounding box for collision detection
     * @return a bounding box
     */
    public Rectangle getCollisionBoundingBox() {
        Rectangle rectangle = getBoundingRectangle();
        customBoundingBox.set(rectangle.x + rectangle.width * 0.25f, rectangle.y - rectangle.height * 0.10f, rectangle.width * 0.5f, rectangle.height * 0.90f);
        return customBoundingBox;

    }

    public float getCenterX() {
        return getX() + getWidth() / 2;
    }

    public float getCenterY() {
        return getY() + getHeight() / 2;
    }
}
