package com.mygdx.game.screens.actors;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.Ayu;

public class ActorTextClickYesNo extends ActorText {
    //private static final Logger log = new Logger(ActorTextClickYesNo.class.getName(), Logger.DEBUG);

    public ActorTextClickYesNo(final String displayText, final float posX, final float posY, float scale, Color color) {
        super(displayText, posX, posY, scale, color);

        setBounds(posX, posY - layout.height, layout.width, layout.height);

        setTouchable(Touchable.enabled);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(ActorTextClickYesNo.super.displayText.equals("On")){
                    setDisplayText("Off");
                    setBounds(posX, posY - layout.height, layout.width, layout.height);
                }else{
                    setDisplayText("On");
                    setBounds(posX, posY - layout.height, layout.width, layout.height);
                }
                return true;
            }
        });
    }
}
