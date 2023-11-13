package org.liberty.multi.bulletproof.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.AuthenticationResult;
import com.google.android.gms.games.PlayGames;

import org.liberty.multi.bulletproof.BulletProof;
import org.liberty.multi.bulletproof.R;
import org.liberty.multi.bulletproof.resolver.ScoreBoardResolver;

public class MainActivity extends AndroidApplication implements ScoreBoardResolver {
    private static final String TAG = MainActivity.class.getSimpleName();


    private static final int ACTIVITY_RESULT_LEADERBOARD = 1;

    private static final int ACTIVITY_RESULT_ACHIEVEMENTS = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup application
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

        initialize(new BulletProof(this, new AndroidShareResolver(this)), cfg);
    }

    @Override
    public void submitBestTime(float time) {
        long score = (long) (time * 100);
        PlayGames.getLeaderboardsClient(this).submitScore(ScoreBoardId.BEST_TIME_LEADERBOARD_ID, score);
        unlockAchievements(time);
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
        runPlayGamesAuthenticated(() -> PlayGames.getLeaderboardsClient(MainActivity.this)
                .getLeaderboardIntent(ScoreBoardId.BEST_TIME_LEADERBOARD_ID)
                .addOnSuccessListener(intent -> startActivityForResult(intent, ACTIVITY_RESULT_LEADERBOARD)));


    }

    @Override
    public void viewAchievements() {
        runPlayGamesAuthenticated(() -> PlayGames.getAchievementsClient(this)
                .getAchievementsIntent()
                .addOnSuccessListener(intent -> startActivityForResult(intent, ACTIVITY_RESULT_ACHIEVEMENTS)));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Enable immersive mode
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }
    }

    private void unlockAchievements(float time) {
        for (Integer achievementTime: ScoreBoardId.TIME_ACHIEVEMENT_ID_MAP.keySet()) {
            String achievementId = ScoreBoardId.TIME_ACHIEVEMENT_ID_MAP.get(achievementTime);
            if (time >= achievementTime) {
                PlayGames.getAchievementsClient(this).unlock(achievementId);
            }
        }
    }

    private void showAlert(final int resId) {
        Toast.makeText(MainActivity.this, resId, Toast.LENGTH_SHORT).show();
    }

    private void runPlayGamesAuthenticated(final Runnable runnable) {
        PlayGames.getGamesSignInClient(this).isAuthenticated().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                showAlert(R.string.sign_in_failed_text);
                Log.e(TAG, "Error getting Play service authenticated status", task.getException());
                return;
            }

            AuthenticationResult result = task.getResult();
            if (result.isAuthenticated()) {
                runnable.run();
            } else {
                PlayGames.getGamesSignInClient(MainActivity.this).signIn()
                        .addOnCompleteListener(task1 -> {
                            if (!task1.isSuccessful()) {
                                showAlert(R.string.sign_in_failed_text);
                                Log.e(TAG, "Error signing in to Play services", task.getException());
                                return;
                            }

                            showAlert(R.string.sign_in_succeeded_text);

                            runnable.run();
                        });
            }
        });
    }
}
