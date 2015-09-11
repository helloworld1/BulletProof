package org.liberty.multi.bulletproof.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import org.liberty.multi.bulletproof.sprite.Aircraft;

public class AircraftControlGestureListener implements GestureListener {

    private Aircraft aircraft;

    private float viewportWidth;
    private float viewportHeight;


    public AircraftControlGestureListener(Aircraft aircraft, float viewportWidth, float viewportHeight) {
        this.aircraft = aircraft;
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // Negative Y because pan down means moving down instead of moving up
        
		float screenWidth = Gdx.graphics.getWidth();

		float screenHeight = Gdx.graphics.getHeight();

        // The deltaX and deltaY are the screen's pixel not transformed to the viewport
        // We need to transform to the actual delta on the viewport so the aircraft speed
        // is the same as finger panning speed
        aircraft.move(deltaX * (viewportWidth / screenWidth) , -deltaY * (viewportHeight / screenHeight));
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
            Vector2 pointer1, Vector2 pointer2) {
        // TODO Auto-generated method stub
        return false;
    }
}

