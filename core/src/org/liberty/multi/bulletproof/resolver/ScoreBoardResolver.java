package org.liberty.multi.bulletproof.resolver;

public interface ScoreBoardResolver {
    boolean isSignedIn();

    void signIn();

    void signOut();

    void submitBestTime(float time);

    void submitHighBullet(int bullets);
    
    void viewLeaderboard();

    void viewAchievements();
}

