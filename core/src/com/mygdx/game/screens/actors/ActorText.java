package com.mygdx.game.screens.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorText extends Actor {
    String displayText;
    float posX, posY;
    BitmapFont font;
    GlyphLayout layout;

    public ActorText(String displayText, float posX, float posY, float scale, Color color){
        this.displayText=displayText;
        this.posX=posX;
        this.posY=posY;



        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Oswald-VariableFont_wght.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 64;
        font = generator.generateFont(parameter);
        font.setColor(color);
        font.getData().setScale(scale);

        layout = new GlyphLayout();
        layout.setText(font, this.displayText);

        generator.dispose();
    }

    public float getWidth(){
        return(layout.width);
    }

    public float getHeight(){
        return(layout.height);
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
        layout.setText(font, this.displayText);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.draw(batch, displayText, posX, posY);
    }
}
