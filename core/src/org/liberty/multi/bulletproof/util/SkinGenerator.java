package org.liberty.multi.bulletproof.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class SkinGenerator {
    public static Skin getSkin() {
        Skin skin = new Skin();

        BitmapFont defaultBigFont = new BitmapFont();
        defaultBigFont.getData().setScale(5.0f);

        BitmapFont defaultMediumFont = new BitmapFont();
        defaultMediumFont.getData().setScale(3.0f);

        BitmapFont defaultSmallFont = new BitmapFont();


        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("big_font", defaultBigFont);
        skin.add("medium_font", defaultMediumFont);
        skin.add("small_font", defaultSmallFont);

        Color transparent = new Color(Color.WHITE);
        transparent.a = 0f;
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", transparent);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", transparent);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("big_font");
        skin.add("big_button", textButtonStyle);

        TextButtonStyle medTextButtonStyle = new TextButtonStyle();
        medTextButtonStyle.up = skin.newDrawable("white", transparent);
        medTextButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        medTextButtonStyle.checked = skin.newDrawable("white", transparent);
        medTextButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        medTextButtonStyle.font = skin.getFont("medium_font");
        skin.add("med_button", medTextButtonStyle);

        LabelStyle bigRedLabelStyle = new LabelStyle();
        bigRedLabelStyle.font = defaultBigFont;
        bigRedLabelStyle.fontColor = Color.RED;
        skin.add("big_red_label", bigRedLabelStyle);

        LabelStyle bigWhiteLabelStyle = new LabelStyle();
        bigWhiteLabelStyle.font = defaultBigFont;
        bigWhiteLabelStyle.fontColor = Color.WHITE;
        skin.add("big_white_label", bigWhiteLabelStyle);

        LabelStyle medOrangeLabelStyle = new LabelStyle();
        medOrangeLabelStyle.font = defaultMediumFont;
        medOrangeLabelStyle.fontColor = Color.ORANGE;
        skin.add("med_orange_label", medOrangeLabelStyle);
        return skin;
    }

}
