package com.mygdx.game.screens.actors;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.game.Ayu;

public class ActorTextBounds extends ActorText{
    Ayu game;
    Rectangle bounds;
    public ActorTextBounds(String displayText, float posX, float posY, float scale, Color color, final Ayu game) {
        super(displayText, posX, posY, scale, color);

        this.game=game;
        //setBounds(posX, posY - layout.height, layout.width, layout.height);
        bounds=new Rectangle(posX, posY - layout.height, layout.width, layout.height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
