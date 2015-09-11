package org.liberty.multi.bulletproof.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import org.liberty.multi.bulletproof.BulletProof;
import org.liberty.multi.bulletproof.resolver.DummyScoreBoard;
import org.liberty.multi.bulletproof.resolver.DummyShareResolver;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new BulletProof(new DummyScoreBoard(), new DummyShareResolver());
        }
}