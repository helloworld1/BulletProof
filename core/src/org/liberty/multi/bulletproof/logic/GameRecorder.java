package org.liberty.multi.bulletproof.logic;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.liberty.multi.bulletproof.sprite.Bullet;

public class GameRecorder {

    private Array<Vector3> aircraftRecords;

    private Array<BulletData> bulletRecords;

    private int aircraftRecordIndex = 0;

    private int bulletRecordIndex = 0;

    public GameRecorder() {
        aircraftRecords = new Array<Vector3>(1200);
        bulletRecords = new Array<BulletData>(200);
    }

    /**
     * Record the aircraft's position./
     */
    public void recordAircraft(float x, float y, float gameTime) {
        aircraftRecords.add(new Vector3(x, y, gameTime));
    }

    /**
     * Record the bullet's position and velocity.
     */
    public void recordBullet(Bullet bullet, float gameTime) {
        bulletRecords.add(new BulletData(bullet.getX(), bullet.getY(), bullet.getVelocityX(), bullet.getVelocityY(), gameTime));
    }

    /**
     * Pop the location of the aircraft based on the time.
     *
     * Linear interpolation is used to calculate the position based on the recorded time.
     */
    public Vector3 getAircraftPosition(float gameTime) {
        // Last frame
        if (aircraftRecordIndex == aircraftRecords.size - 2) {
            if (aircraftRecords.get(0).z <= gameTime) {
                Vector3 v = aircraftRecords.get(aircraftRecordIndex);
                aircraftRecordIndex++;
                return v;
            }
        }

        while (aircraftRecordIndex <= aircraftRecords.size - 3) {
            if (aircraftRecords.get(aircraftRecordIndex).z < gameTime && aircraftRecords.get(aircraftRecordIndex + 1).z < gameTime) {
                aircraftRecordIndex++;
                continue;
            }
            if (aircraftRecords.get(aircraftRecordIndex).z <= gameTime && aircraftRecords.get(aircraftRecordIndex + 1).z >= gameTime) {
                Vector3 v = new Vector3();
                v.x = aircraftRecords.get(aircraftRecordIndex).x + (gameTime - aircraftRecords.get(aircraftRecordIndex).z) / (aircraftRecords.get(aircraftRecordIndex + 1).z - aircraftRecords.get(aircraftRecordIndex).z) * (aircraftRecords.get(aircraftRecordIndex + 1).x - aircraftRecords.get(aircraftRecordIndex).x);
                v.y = aircraftRecords.get(aircraftRecordIndex).y + (gameTime - aircraftRecords.get(aircraftRecordIndex).z) / (aircraftRecords.get(aircraftRecordIndex + 1).z - aircraftRecords.get(aircraftRecordIndex).z) * (aircraftRecords.get(aircraftRecordIndex + 1).y - aircraftRecords.get(aircraftRecordIndex).y);
                return v;
            }

            if (aircraftRecords.get(aircraftRecordIndex).z > gameTime && aircraftRecords.get(aircraftRecordIndex + 1).z > gameTime) {
                return aircraftRecords.get(aircraftRecordIndex);
            }
        }
        return null;
    }

    public Array<BulletData> getBullet(float gameTime) {
        if (bulletRecordIndex > bulletRecords.size - 1) {
            return null;
        }

        BulletData data = bulletRecords.get(bulletRecordIndex);

        if (data.time <= gameTime) {
            Array<BulletData> bullets = new Array<BulletData>();
            for (int i = bulletRecordIndex; i < bulletRecords.size; i++) {
                BulletData data1 = bulletRecords.get(i);
                if (data1.time <= gameTime) {
                    bulletRecordIndex++;
                    bullets.add(data1);
                } else {
                    break;
                }
            }
            return bullets;
        }
        return null;
    }

    /**
     * Reset the record playing state.
     *
     * Call this method before playing the record.
     */
    public void resetRecordPlayingState() {
        aircraftRecordIndex = 0;
        bulletRecordIndex = 0;
    }


    /**
     * Represent a bullet's position and velocity.
     */
    public static class BulletData {
        private float x;
        private float y;
        private float velocityX;
        private float velocityY;
        private float time;

        public BulletData(float x, float y, float velocityX, float velocityY, float time) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.time = time;
        }

        /**
         * @return the x
         */
        public float getX() {
            return x;
        }

        /**
         * @return the y
         */
        public float getY() {
            return y;
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

        /**
         * @return the time
         */
        public float getTime() {
            return time;
        }
    }
}
