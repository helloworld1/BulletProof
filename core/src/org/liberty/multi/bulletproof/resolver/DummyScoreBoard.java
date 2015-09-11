package org.liberty.multi.bulletproof.resolver;

public class DummyScoreBoard implements ScoreBoardResolver {

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public void signIn() {
    }

    @Override
    public void viewLeaderboard() {
    }

    @Override
    public void viewAchievements() {
    }

    @Override
    public void submitBestTime(float time) {

    }

    @Override
    public void submitHighBullet(int bullets) {
    }

    @Override
    public void signOut() {
    }

}

