package org.liberty.multi.bulletproof;

/**
 * Do string manupilation in the game
 */
public class GameStringUtils {

    public static String convertElapseTime(float time) {
        long longTime = (long) (time * 100);
        return "" + longTime / 100 + "." + (longTime / 10) % 10 + "" + longTime % 10;
    }

}

