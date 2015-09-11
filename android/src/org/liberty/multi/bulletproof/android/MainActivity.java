package org.liberty.multi.bulletproof.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import org.liberty.multi.bulletproof.BulletProof;
import org.liberty.multi.bulletproof.value.PreferenceKeys;
import org.liberty.multi.bulletproof.resolver.ScoreBoardResolver;

public class MainActivity extends AndroidApplication implements ScoreBoardResolver {

    private GameHelper gameHelper;

    private static final int ACTIVITY_RESULT_LEADERBOARD = 1;

    private static final int ACTIVITY_RESULT_ACHIEVEMENTS = 2;

    private Preferences preferences;

    private boolean googlePlayGamesEnabled = false;

    private GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {

        @Override
        public void onSignInFailed() {
            showAlert(R.string.sign_in_failed_text);
            googlePlayGamesEnabled = false; 
            preferences.putBoolean(PreferenceKeys.ALWAYS_SIGN_IN_KEY, false);
            preferences.flush();
        }

        @Override
        public void onSignInSucceeded() {
            // showAlert(R.string.sign_in_succeeded_text);
            googlePlayGamesEnabled = true;
            preferences.putBoolean(PreferenceKeys.ALWAYS_SIGN_IN_KEY, true);
            preferences.flush();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        
        initialize(new BulletProof(this, new AndroidShareResolver(this)), cfg);

        this.gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.setup(gameHelperListener);
        preferences = Gdx.app.getPreferences(PreferenceKeys.SETTING_NAME);
    }

    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }

    @Override
    public void signIn() {
        runOnUiThread(new Runnable(){
            public void run() {
                gameHelper.beginUserInitiatedSignIn();
            }
        });
    }

    @Override
    public void submitBestTime(float time) {
        if (isSignedIn()) {
            long score = (long) (time * 100);
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), ScoreBoardId.BEST_TIME_LEADERBOARD_ID, score);
            unlockAchievements(time);
        } else {
            showAlert(R.string.signing_in_text);
            signIn();
        }
    }

    @Override
    public void submitHighBullet(int bullets) {
        // if (isSignedIn()) {
        //     // gameHelper.getGamesClient().submitScore(ScoreBoardId.BEST_TIME_LEADERBOARD_ID, score);
        // } else {
        //     showAlert(R.string.signing_in_text);
        //     login();
        // }
    }

    @Override
    public void viewLeaderboard() {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    ScoreBoardId.BEST_TIME_LEADERBOARD_ID), ACTIVITY_RESULT_LEADERBOARD);
        } else {
            showAlert(R.string.signing_in_text);
            signIn();
        }

    }

    @Override
    public void viewAchievements() {
        if (isSignedIn()) {
            startActivityForResult(
                    Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), ACTIVITY_RESULT_ACHIEVEMENTS);
        } else {
            showAlert(R.string.signing_in_text);
            signIn();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googlePlayGamesEnabled) {
            gameHelper.onStop();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googlePlayGamesEnabled) {
            gameHelper.onStart(this);
        }
    }

    @Override
    public void signOut() {
        googlePlayGamesEnabled = false;
        gameHelper.signOut();
        showAlert(R.string.signing_out_text);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        gameHelper.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void unlockAchievements(float time) {
        for (Integer achievementTime: ScoreBoardId.TIME_ACHIEVEMENT_ID_MAP.keySet()) {
            if (time >= achievementTime) {
                Games.Achievements.unlock(gameHelper.getApiClient(), ScoreBoardId.TIME_ACHIEVEMENT_ID_MAP.get(achievementTime));
            }
        }
    }

    private void showAlert(final int resId) {
        runOnUiThread(new Runnable(){
            public void run() {
                Toast.makeText(MainActivity.this, resId, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
