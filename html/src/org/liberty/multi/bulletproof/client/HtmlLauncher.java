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
                // Resizable application, uses available space in browser
                return new GwtApplicationConfiguration(true);
                // Fixed size application:
                //return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new BulletProof(new DummyScoreBoard(), new DummyShareResolver());
        }
}