package com.mygdx.game.screens.actors;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.game.Ayu;

public class ActorTextClickable extends ActorText{
    Ayu game;


    public ActorTextClickable(String displayText, float posX, float posY, float scale, Color color, final Ayu game, final Screen screen) {
        super(displayText, posX, posY, scale, color);

        this.game=game;

        setBounds(posX, posY - layout.height, layout.width, layout.height);

        setTouchable(Touchable.enabled);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(screen);
                return true;
            }
        });
    }
}
