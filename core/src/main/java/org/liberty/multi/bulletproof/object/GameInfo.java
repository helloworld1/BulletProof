package org.liberty.multi.bulletproof.object;

import org.liberty.multi.bulletproof.logic.GameRecorder;
import org.liberty.multi.bulletproof.util.GameStringUtils;

public class GameInfo {

    private float elapseTime;

    private boolean paused = false;

    private int bulletDodgedCount;

    public GameRecorder gameRecorder;

    public GameInfo() {
        bulletDodgedCount = 0;
        gameRecorder = new GameRecorder();
    }

    /**
     * This method must be called in the render() function.
     */
    public void update(float delta) {
        if (!paused) {
            this.elapseTime += delta;
        }
    }

    public int getCurrentLevel() {
        return (int)(getGameElapseTimeSec() / 5);
    }

    public float getGameElapseTimeSec() {
        return elapseTime;
    }

    public String getGameElapseTimeSecString() {
        return GameStringUtils.convertElapseTime(getGameElapseTimeSec());
    }

    public int getBulletDodgedCount() {
        return bulletDodgedCount;
    }

    public void increaseBulletDodgedCount() {
        this.bulletDodgedCount++;
    }

    public int getMaxBulletCount() {
        return 50 + getCurrentLevel();
    }

    public int getBulletVelocity() {
        return 120 + 5 * getCurrentLevel();
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        // Make sure the time is correct, reset start time so the elapsed time is still correct.
        paused = false;

    }
}
