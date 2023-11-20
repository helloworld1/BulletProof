package org.liberty.multi.bulletproof.resolver;

public interface ScoreBoardResolver {
    void submitBestTime(float time);

    void submitHighBullet(int bullets);
    
    void viewLeaderboard();

    void viewAchievements();
}