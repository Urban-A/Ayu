package com.mygdx.game.hud;

import com.badlogic.gdx.math.Rectangle;

public class GameObjectRestartButton implements MyClickListener {

  public enum STATE {
    START, WIN, LOSE
  }

  public STATE state;
  public STATE newState;
  public boolean changeState4LogicUpdate; //something else can be change in world (simulation, ...)
  Rectangle bounds;


  public Rectangle getBounds() {
    return bounds;
  }

  public GameObjectRestartButton(float x, float y, float w, float h) {
    changeState4LogicUpdate = false;
    bounds = new Rectangle(x,y,w,h);
    setState(STATE.START);
  }

  public void setState(STATE state) {
    this.state = state;
  }


  @Override
  public boolean onClickEvent(float x, float y) {
    if (!changeState4LogicUpdate && bounds.contains(x, y)) {
      newState = STATE.START;
      changeState4LogicUpdate = true; //schedule for future, update method will use it
      return true;
    }
    return false;
  }


}