package org.liberty.multi.bulletproof.logic;

import com.badlogic.gdx.math.MathUtils;
import org.liberty.multi.bulletproof.object.GameInfo;

public class GameEventManager {

    private static final int FAST_BULLET_BASELINE = 10;
    private static final int SUPER_FAST_BULLET_BASELINE = 20;
    private static final int ULTRA_FAST_BULLET_BASELINE = 45;

    private GameInfo gameInfo;

    private float nextFastBulletTime = 0f;

    private float nextSuperFastBulletTime = 0f;

    private float nextUltraFastBulletTime = 0f;

    private int currentLevel;

    public GameEventManager(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        nextFastBulletTime = getInterval(FAST_BULLET_BASELINE);
        nextSuperFastBulletTime = getInterval(SUPER_FAST_BULLET_BASELINE);
        nextUltraFastBulletTime = getInterval(ULTRA_FAST_BULLET_BASELINE);
        currentLevel = 0;
    }

    public EventType retrieveEvent() {
        if (currentLevel != gameInfo.getCurrentLevel()) {
            currentLevel = gameInfo.getCurrentLevel();
            return EventType.LevelUp;
        }

        if (nextFastBulletTime <= gameInfo.getGameElapseTimeSec()) {
            nextFastBulletTime = gameInfo.getGameElapseTimeSec() + getInterval(FAST_BULLET_BASELINE);
            return EventType.FastBullet;
        }

        if (nextSuperFastBulletTime <= gameInfo.getGameElapseTimeSec()) {
            nextSuperFastBulletTime = gameInfo.getGameElapseTimeSec() + getInterval(SUPER_FAST_BULLET_BASELINE);
            return EventType.SuperFastBullet;
        }

        if (nextUltraFastBulletTime <= gameInfo.getGameElapseTimeSec()) {
            nextUltraFastBulletTime = gameInfo.getGameElapseTimeSec() + getInterval(ULTRA_FAST_BULLET_BASELINE);
            return EventType.UltraFastBullet;
        }

        return EventType.None;
    }

    private int getInterval(int baseline) {
        return Math.max(baseline / 4, MathUtils.random(- baseline / 3 - gameInfo.getCurrentLevel() / 2, baseline / 3 + gameInfo.getCurrentLevel() / 2) + baseline);
    }

    public enum EventType {
        None,
        FastBullet,
        SuperFastBullet,
        UltraFastBullet,
        LevelUp
    }
}

